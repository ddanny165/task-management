import { createContext, useContext, useReducer } from "react";

const BASE_URL = import.meta.env.VITE_BASE_URL;
const TasksContext = createContext();

const initialState = {
  tasks: [],
  isLoading: false,
  currentTask: null,
  error: "",
};

const statusEmojis = {
  TO_DO: "📝",
  IN_PROGRESS: "🔄",
  DONE: "✅",
};

function reducer(state, action) {
  switch (action.type) {
    case "loading":
      return { ...state, isLoading: true, error: "" };
    case "tasks/loaded":
      return { ...state, isLoading: false, tasks: action.payload };
    case "task/created":
      return {
        ...state,
        isLoading: false,
        tasks:
          action.payload.currentUsername ===
          action.payload.assignedEmployeeUsername
            ? [...state.tasks, { ...action.payload, currentUsername: null }]
            : [...state.tasks],
      };
    case "task/loaded":
      return { ...state, isLoading: false, currentTask: action.payload };
    case "task/updated":
      return {
        ...state,
        isLoading: false,
        currentTask: action.payload,
        tasks: [...state.tasks, action.payload],
      };
    case "task/deleted":
      return {
        ...state,
        isLoading: false,
        tasks: state.tasks.filter((task) => task.id !== action.payload),
      };
    case "rejected":
      return { ...state, isLoading: false, error: action.payload };
    default:
      throw new Error("Unknown action type!");
  }
}

function TasksProvider({ children }) {
  const [{ tasks, isLoading, currentTask, error }, dispatch] = useReducer(
    reducer,
    initialState
  );

  // TODO: Memoize functions to avoid a loop of triggering rerenders upon TasksProvider rerender
  async function getTasks(username) {
    dispatch({ type: "loading" });
    try {
      const res = await fetch(`${BASE_URL}/users/${username}/tasks`);
      if (!res.ok) {
        throw new Error(`Failed to get a task. Status: ${res.status}`);
      }

      const data = await res.json();
      dispatch({ type: "tasks/loaded", payload: data });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error loading tasks...",
      });
    }
  }

  async function createTask(newTaskBody, creatorUsername) {
    dispatch({ type: "loading" });
    try {
      const res = await fetch(`${BASE_URL}/tasks`, {
        method: "POST",
        body: JSON.stringify(newTaskBody),
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!res.ok) {
        throw new Error(`Failed to create a task. Status: ${res.status}`);
      }

      const data = await res.json();
      data.currentUsername = creatorUsername;
      dispatch({ type: "task/created", payload: data });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error creating a task!",
      });
    }
  }

  async function getTask(id) {
    dispatch({ type: "loading" });
    try {
      const res = await fetch(`${BASE_URL}/tasks/${id}`);
      if (!res.ok) {
        throw new Error(`Failed to get a task. Status: ${res.status}`);
      }

      const data = await res.json();
      dispatch({ type: "task/loaded", payload: data });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error loading tasks...",
      });
    }
  }

  async function updateTask(newTaskBody, id) {
    console.log(newTaskBody);
    dispatch({ type: "loading" });
    try {
      const res = await fetch(`${BASE_URL}/tasks/${id}`, {
        method: "PUT",
        body: JSON.stringify(newTaskBody),
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!res.ok) {
        throw new Error(`Failed to update a task. Status: ${res.status}`);
      }

      const data = await res.json();
      dispatch({ type: "task/updated", payload: data });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error updating a task...",
      });
    }
  }

  async function deleteTask(id) {
    dispatch({ type: "loading" });
    try {
      await fetch(`${BASE_URL}/tasks/${id}`, {
        method: "DELETE",
      });
      dispatch({ type: "task/deleted", payload: id });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error deleting tasks...",
      });
    }
  }

  return (
    <TasksContext.Provider
      value={{
        tasks,
        isLoading,
        currentTask,
        error,
        statusEmojis,
        getTasks,
        createTask,
        getTask,
        updateTask,
        deleteTask,
      }}
    >
      {children}
    </TasksContext.Provider>
  );
}

function useTasks() {
  const value = useContext(TasksContext);
  if (value === undefined) {
    throw new Error("TasksContext was used outside of TasksProvider");
  }
  return value;
}

export { TasksProvider, useTasks };
