import { createContext, useContext, useEffect, useReducer } from "react";

const currentlyLoggedInUsername = "ddanny165";
const BASE_URL = "http://localhost:8080/api";
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
    case "task/loaded":
      return { ...state, isLoading: false, currentTask: action.payload };
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

  useEffect(function () {
    async function getTasks() {
      dispatch({ type: "loading" });
      try {
        const res = await fetch(
          `${BASE_URL}/users/${currentlyLoggedInUsername}/tasks`
        );
        const data = await res.json();
        dispatch({ type: "tasks/loaded", payload: data });
      } catch (err) {
        console.log(err);
        dispatch({
          type: "rejected",
          payload: "There was an error loading data...",
        });
      }
    }

    getTasks();
  }, []);

  async function getTask(id) {
    dispatch({ type: "loading" });
    try {
      const res = await fetch(`${BASE_URL}/tasks/${id}`);
      const data = await res.json();
      console.log(data);
      dispatch({ type: "task/loaded", payload: data });
    } catch (err) {
      console.log(err);
      dispatch({
        type: "rejected",
        payload: "There was an error loading data...",
      });
    }
  }

  return (
    <TasksContext.Provider
      value={{ tasks, isLoading, currentTask, error, getTask, statusEmojis }}
    >
      {children}
    </TasksContext.Provider>
  );
}

function useTasks() {
  let value = useContext(TasksContext);
  if (value === undefined) {
    throw new Error("TasksContext was used outside of TasksProvider");
  }
  return value;
}

export { TasksProvider, useTasks };
