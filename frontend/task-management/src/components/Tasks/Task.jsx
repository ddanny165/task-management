import { useState } from "react";
import styles from "./Task.module.css";
import { useNavigate } from "react-router";
import Popup from "../Popups/Popup";
import { useTasks } from "../../contexts/TasksContext";

function formatDate(dateString) {
  const dateObj = new Date(dateString);
  const options = {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  };

  return dateObj.toLocaleDateString("en-US", options);
}

function Task({ task }) {
  const navigate = useNavigate();
  const [isDeletePopupShown, setIsDeletePopupShown] = useState(false);
  const { deleteTask } = useTasks();

  function handleRemoveBtnCLick(e) {
    e.stopPropagation();
    e.preventDefault();

    setIsDeletePopupShown(true);
  }

  function handleClosePopup() {
    setIsDeletePopupShown(false);
  }

  function handleTaskDeletion(id) {
    setIsDeletePopupShown(false);
    deleteTask(id);
  }

  return (
    <>
      <li className={styles.task} onClick={() => navigate(`${task.id}`)}>
        <button className={styles["remove-btn"]} onClick={handleRemoveBtnCLick}>
          x
        </button>
        <h2>{task.title}</h2>

        <div>
          <p>
            <strong>Priority:</strong>{" "}
            <span
              className={`${styles.priority} ${
                styles[task.priority.toLowerCase()]
              }`}
            >
              {task.priority}
            </span>
          </p>

          <div>
            <strong>Deadline:</strong> {formatDate(task.toBeDoneUntil)}
          </div>

          <div>
            <strong>Assignee:</strong> {task.assignedEmployeeUsername}
          </div>
        </div>
      </li>
      {isDeletePopupShown && (
        <Popup>
          <div className={styles["popup-content"]}>
            <h2>Confirm Deletion</h2>
            <p>
              Are you sure that you want to delete the task{" "}
              <strong>{task.title}</strong>?
            </p>
            <div className={styles["popup-buttons"]}>
              <button onClick={() => handleTaskDeletion(task.id)}>
                Delete
              </button>
              <button onClick={handleClosePopup}>Close</button>
            </div>
          </div>
        </Popup>
      )}
    </>
  );
}

export default Task;
