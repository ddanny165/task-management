import { useEffect, useState } from "react";
import styles from "./TaskList.module.css";

const currentlyLoggedInUsername = "ddanny165";
const BASE_URL = "http://localhost:8080/api/users";

function TaskList() {
  const [tasks, setTasks] = useState([]);
  const [isLoadingTasks, setisLoadingTasks] = useState(false);
  const [error, setError] = useState("");

  const toDoTasks = tasks.filter((task) => task.status === "TO_DO");
  const inProgressTasks = tasks.filter((task) => task.status === "IN_PROGRESS");
  const doneTasks = tasks.filter((task) => task.status === "DONE");

  useEffect(function () {
    async function getTasks() {
      setisLoadingTasks(true);
      setError("");
      try {
        const res = await fetch(
          `${BASE_URL}/${currentlyLoggedInUsername}/tasks`
        );
        const data = await res.json();
        setTasks(data);
      } catch (err) {
        console.log(err);
        setError("There was an error loading data...");
      } finally {
        setisLoadingTasks(false);
      }
    }

    getTasks();
  }, []);

  return (
    <>
      {error && <div>{error}</div>}
      {isLoadingTasks && !error && <div>Loading...</div>}
      {!isLoadingTasks && !error && tasks.length > 0 && (
        <div className={styles.container}>
          <ul className={styles.row}>
            {toDoTasks.map((task) => (
              <li>
                <div>
                  <h2>Task Details:</h2>
                  <p>
                    <strong>Title:</strong> {task.title}
                  </p>
                  <p>
                    <strong>Description:</strong> {task.description}
                  </p>
                  <p>
                    <strong>Status:</strong> {task.status}
                  </p>
                  <p>
                    <strong>Priority:</strong> {task.priority}
                  </p>
                  <p>
                    <strong>Created At:</strong> {task.createdAt}
                  </p>
                  <p>
                    <strong>To Be Done Until:</strong> {task.toBeDoneUntil}
                  </p>
                  <p>
                    <strong>Assigned Employee:</strong>{" "}
                    {task.assignedEmployeeUsername}
                  </p>
                  <p>
                    <strong>Creator:</strong> {task.creatorUsername}
                  </p>
                </div>
              </li>
            ))}
          </ul>
          <ul className={styles.row}>
            {inProgressTasks.map((task) => (
              <li>
                <div>
                  <h2>Task Details:</h2>
                  <p>
                    <strong>Title:</strong> {task.title}
                  </p>
                  <p>
                    <strong>Description:</strong> {task.description}
                  </p>
                  <p>
                    <strong>Status:</strong> {task.status}
                  </p>
                  <p>
                    <strong>Priority:</strong> {task.priority}
                  </p>
                  <p>
                    <strong>Created At:</strong> {task.createdAt}
                  </p>
                  <p>
                    <strong>To Be Done Until:</strong> {task.toBeDoneUntil}
                  </p>
                  <p>
                    <strong>Assigned Employee:</strong>{" "}
                    {task.assignedEmployeeUsername}
                  </p>
                  <p>
                    <strong>Creator:</strong> {task.creatorUsername}
                  </p>
                </div>
              </li>
            ))}
          </ul>
          <ul className={styles.row}>
            {doneTasks.map((task) => (
              <li>
                <div>
                  <h2>Task Details:</h2>
                  <p>
                    <strong>Title:</strong> {task.title}
                  </p>
                  <p>
                    <strong>Description:</strong> {task.description}
                  </p>
                  <p>
                    <strong>Status:</strong> {task.status}
                  </p>
                  <p>
                    <strong>Priority:</strong> {task.priority}
                  </p>
                  <p>
                    <strong>Created At:</strong> {task.createdAt}
                  </p>
                  <p>
                    <strong>To Be Done Until:</strong> {task.toBeDoneUntil}
                  </p>
                  <p>
                    <strong>Assigned Employee:</strong>{" "}
                    {task.assignedEmployeeUsername}
                  </p>
                  <p>
                    <strong>Creator:</strong> {task.creatorUsername}
                  </p>
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </>
  );
}

export default TaskList;
