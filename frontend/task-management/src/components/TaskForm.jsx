import { useState } from "react";
import DatePicker from "react-datepicker";
import styles from "./TaskForm.module.css";

import "react-datepicker/dist/react-datepicker.css";

const taskStatuses = ["TO_DO", "IN_PROGRESS", "DONE"];
const taskPriorities = ["LOW", "MEDIUM", "HIGH"];
const users = ["ddanny165", "ddanny228"]; // TODO: get from API

function TaskForm({
  setIsAPopupShown,
  currentlyLoggedInUsername,
  actOnTask,
  actionType,
  taskId,
  taskToUpdate,
}) {
  const [newTaskTitle, setNewTaskTitle] = useState(
    taskToUpdate ? taskToUpdate.title : ""
  );
  const [newTaskDescription, setNewTaskDescription] = useState(
    taskToUpdate ? taskToUpdate.description : ""
  );
  const [newTaskStatus, setNewTaskStatus] = useState(
    taskToUpdate ? taskToUpdate.status : taskStatuses[0]
  );
  const [newTaskPriority, setNewTaskPriority] = useState(
    taskToUpdate ? taskToUpdate.priority : taskPriorities[0]
  );
  const [newTaskDeadline, setNewTaskDeadline] = useState(
    taskToUpdate ? new Date(taskToUpdate.toBeDoneUntil) : new Date()
  );
  const [newTaskAssigneeID, setNewTaskAssigneeID] = useState(
    taskToUpdate
      ? taskToUpdate.assignedEmployeeUsername
      : currentlyLoggedInUsername
  );
  const [taskActionError, setTaskActionError] = useState("");

  function handleClosePopup(e) {
    e.preventDefault();
    setIsAPopupShown(false);
    setTaskActionError("");
  }

  function handleTaskAction(e) {
    e.preventDefault();
    if (newTaskTitle === "") {
      setTaskActionError("The title can not be empty 🥲");
      return;
    }

    let newTaskBody = {
      title: newTaskTitle,
      description: newTaskDescription,
      status: newTaskStatus,
      createdAt: new Date().toISOString().replace("Z", ""),
      priority: newTaskPriority,
      toBeDoneUntil: newTaskDeadline.toISOString().replace("Z", ""),
      creatorUsername: currentlyLoggedInUsername,
      assignedEmployeeUsername: newTaskAssigneeID,
    };

    switch (actionType) {
      case "createTask":
        actOnTask(newTaskBody, currentlyLoggedInUsername);
        break;
      case "updateTask":
        actOnTask(newTaskBody, taskId);
        break;
      default:
        throw new Error("Unknown task form action type!");
    }

    setIsAPopupShown(false);
  }

  return (
    <form className={styles.form} onSubmit={() => {}}>
      {taskActionError && (
        <div style={{ color: "red", textAlign: "center" }}>
          {taskActionError}
        </div>
      )}
      <div>
        <label htmlFor="title">Title</label>
        <input
          id="title"
          type="text"
          onChange={(e) => {
            if (newTaskTitle !== "") {
              setTaskActionError("");
            }
            setNewTaskTitle(e.target.value);
          }}
          value={newTaskTitle}
        />
      </div>
      <div>
        <label htmlFor="description">Description</label>
        <input
          id="description"
          type="text"
          onChange={(e) => setNewTaskDescription(e.target.value)}
          value={newTaskDescription}
        />
      </div>
      <div>
        <label htmlFor="deadline">Deadline</label>
        <div>
          <DatePicker
            id="deadline"
            selected={newTaskDeadline}
            onChange={(date) => {
              setNewTaskDeadline(date);
            }}
            dateFormat="dd/MM/yyyy"
          />
        </div>
      </div>
      <div className={styles["select-container"]}>
        <label htmlFor="status">Status</label>
        <select
          name="status"
          id="status"
          className={styles.select}
          onChange={(e) => setNewTaskStatus(e.target.value)}
          value={newTaskStatus}
        >
          {taskStatuses.map((status) => (
            <option value={status} key={status}>
              {status}
            </option>
          ))}
        </select>
      </div>
      <div className={styles["select-container"]}>
        <label htmlFor="priority">Priority</label>
        <select
          name="priority"
          id="priority"
          className={styles.select}
          onChange={(e) => setNewTaskPriority(e.target.value)}
          value={newTaskPriority}
        >
          {taskPriorities.map((prioriry) => (
            <option value={prioriry} key={prioriry}>
              {prioriry}
            </option>
          ))}
        </select>
      </div>
      <div className={styles["select-container"]}>
        <label htmlFor="asignee">Asignee</label>
        <select
          name="asignee"
          id="asignee"
          className={styles.select}
          onChange={(e) => setNewTaskAssigneeID(e.target.value)}
          value={newTaskAssigneeID}
        >
          {users.map((username) => (
            <option value={username} key={username}>
              {username}
            </option>
          ))}
        </select>
      </div>
      <div className={styles["popup-buttons"]}>
        <button onClick={handleClosePopup}>Close</button>
        <button onClick={handleTaskAction}>
          {actionType === "createTask" ? "Add" : "Update"}
        </button>
      </div>
    </form>
  );
}

export default TaskForm;
