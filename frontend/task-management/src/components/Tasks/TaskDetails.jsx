import { useParams } from "react-router";
import { useTasks } from "../../contexts/TasksContext";
import { useEffect, useState } from "react";
import styles from "./TaskDetails.module.css";
import BackButton from "../BackButton";
import Popup from "../Popups/Popup";
import { useAuth } from "../../contexts/FakeAuthContext";
import Loading from "../Loading";
import TaskForm from "../TaskForm";

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
  const { currentUser } = useAuth();
  const { isLoading, currentTask, error, getTask, updateTask, statusEmojis } =
    useTasks();
  const [isEditPopupShown, setIsEditPopupShown] = useState(false);

  let [isAuthorizedToView, setIsAuthorizedToView] = useState(false);

  useEffect(
    function () {
      getTask(id);
    },
    [id]
  );

  useEffect(
    function () {
      // TODO: change the isauthorized logic, currently only the asignee is able to look at task details
      if (currentTask) {
        setIsAuthorizedToView(
          currentUser.username === currentTask.assignedEmployeeUsername
        );
      }
    },
    [currentUser, currentTask]
  );

  function handleEditTask(e) {
    e.preventDefault();
    setIsEditPopupShown(true);
  }

  return (
    <>
      <BackButton />
      {isLoading && <Loading />}
      {!isLoading && error && <div>{error}</div>}
      {!isLoading && !error && currentTask && !isAuthorizedToView && (
        <h1 style={{ color: "red", textAlign: "center" }}>Not authorized!</h1>
      )}
      {!isLoading && !error && currentTask && isAuthorizedToView && (
        <>
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
                  >
                    {`${statusEmojis[currentTask.status]} ${
                      currentTask.status
                    }`}
                  </span>
                </div>
              </div>
              <div>
                <strong>Description:</strong> {currentTask.description}
              </div>
              <div className={styles.dates}>
                <div>
                  <strong>Created At:</strong>{" "}
                  {formatDate(currentTask.createdAt)}
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

          <button className={styles["edit-button"]} onClick={handleEditTask}>
            ✍️ Edit Task
          </button>

          {isEditPopupShown && (
            <Popup>
              <div>
                <h2 className={styles["popup-header"]}>Update a task</h2>
                <TaskForm
                  setIsAPopupShown={setIsEditPopupShown}
                  currentlyLoggedInUsername={currentUser.username}
                  actOnTask={updateTask}
                  taskId={id}
                  taskToUpdate={currentTask}
                  actionType="updateTask"
                />
              </div>
            </Popup>
          )}
        </>
      )}
    </>
  );
}

export default TaskDetails;
