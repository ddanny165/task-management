import { useState } from "react";
import styles from "./Form.module.css";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

// TODO: Make Reusable
function Form({
  setIsAddPopupShown,
  taskStatuses,
  taskPriorities,
  currentlyLoggedInUsername,
  users,
  createTask,
}) {
  const [newTaskTitle, setNewTaskTitle] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [newTaskStatus, setNewTaskStatus] = useState(taskStatuses[0]);
  const [newTaskPriority, setNewTaskPriority] = useState(taskPriorities[0]);
  const [newTaskDeadline, setNewTaskDeadline] = useState(new Date());
  const [newTaskAssigneeID, setNewTaskAssigneeID] = useState(
    currentlyLoggedInUsername
  );
  const [newTaskCreationError, setNewTaskCreationError] = useState("");

  function handleClosePopup(e) {
    e.preventDefault();
    setIsAddPopupShown(false);
    setNewTaskCreationError("");
  }

  function handleTaskCreation(e) {
    e.preventDefault();
    if (newTaskTitle === "") {
      setNewTaskCreationError("The title can not be empty 🥲");
      return;
    }

    let newTask = {
      title: newTaskTitle,
      description: newTaskDescription,
      status: newTaskStatus,
      createdAt: new Date().toISOString().replace("Z", ""),
      priority: newTaskPriority,
      toBeDoneUntil: newTaskDeadline.toISOString().replace("Z", ""),
      creatorUsername: currentlyLoggedInUsername,
      assignedEmployeeUsername: newTaskAssigneeID,
    };

    createTask(newTask);
    setIsAddPopupShown(false);
  }

  return (
    <form className={styles.form} onSubmit={() => {}}>
      {newTaskCreationError && (
        <div style={{ color: "red", textAlign: "center" }}>
          {newTaskCreationError}
        </div>
      )}
      <div>
        <label htmlFor="title">Title</label>
        <input
          id="title"
          type="text"
          onChange={(e) => {
            if (newTaskTitle !== "") {
              setNewTaskCreationError("");
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
        <button onClick={handleTaskCreation}>Add</button>
      </div>
    </form>
  );
}

export default Form;
