import { useParams } from "react-router";
import { useTasks } from "../../contexts/TasksContext";
import { useEffect } from "react";
import styles from "./TaskDetails.module.css";
import BackButton from "../BackButton";

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

function TaskDetails() {
  const { id } = useParams();
  const { isLoading, currentTask, error, getTask, statusEmojis } = useTasks();

  useEffect(
    function () {
      getTask(id);
      console.log(currentTask);
    },
    [id]
  );

  return (
    <>
      <BackButton />
      {error && <div>{error}</div>}
      {isLoading && !error && <div> Loading... </div>}
      {!isLoading && !error && currentTask !== null && (
        <div className={styles["task-details"]}>
          <h2>{currentTask.title}</h2>

          <div>
            <div className={styles.enums}>
              <p>
                <strong>Priority:</strong>{" "}
                <span
                  className={`${styles.priority} ${
                    styles[currentTask.priority.toLowerCase()]
                  }`}
                >
                  {currentTask.priority}
                </span>
              </p>

              <div>
                <strong>Status:</strong>{" "}
                <span
                  className={`${styles.status} ${
                    styles[currentTask.status.toLowerCase()]
                  }`}
                >{`${statusEmojis[currentTask.status]} ${
                  currentTask.status
                }`}</span>
              </div>
            </div>

            <div>
              <strong>Description:</strong> {currentTask.description}
            </div>

            <div className={styles.dates}>
              <div>
                <strong>Created At:</strong> {formatDate(currentTask.createdAt)}
              </div>

              <div>
                <strong>Deadline:</strong>{" "}
                {formatDate(currentTask.toBeDoneUntil)}
              </div>
            </div>

            <div className={styles.users}>
              <div>
                <strong>Assignee:</strong>{" "}
                {currentTask.assignedEmployeeUsername}
              </div>

              <div>
                <strong>Creator:</strong> {currentTask.creatorUsername}
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default TaskDetails;
