import Button from "../Button";
import styles from "./Task.module.css";
import { Link, useNavigate } from "react-router";

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

  function handleCLick(e) {
    e.stopPropagation();
    e.preventDefault();
    // show popup
    // deleteTask
  }

  return (
    <li className={styles.task} onClick={() => navigate(`${task.id}`)}>
      <button className={styles["remove-btn"]} onClick={handleCLick}>
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
  );
}

export default Task;
