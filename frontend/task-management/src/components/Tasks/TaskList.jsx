import styles from "./TaskList.module.css";
import { useTasks } from "../../contexts/TasksContext";
import { useEffect, useState } from "react";
import { useAuth } from "../../contexts/FakeAuthContext";
import Task from "./Task";
import Popup from "../Popups/Popup";
import TaskForm from "../TaskForm";
import Loading from "../Loading";

function TaskList() {
  const { tasks, isLoading, error, statusEmojis, getTasks, createTask } =
    useTasks();
  const [isAddPopupShown, setIsAddPopupShown] = useState(false);

  let { currentUser } = useAuth();
  let currentlyLoggedInUsername = currentUser.username;

  useEffect(() => {
    getTasks(currentlyLoggedInUsername);
  }, [currentlyLoggedInUsername]);

  const toDoTasks = tasks.filter((task) => task.status === "TO_DO");
  const inProgressTasks = tasks.filter((task) => task.status === "IN_PROGRESS");
  const doneTasks = tasks.filter((task) => task.status === "DONE");

  function handleAddTask(e) {
    e.preventDefault();
    setIsAddPopupShown(true);
  }

  return (
    <>
      {error && <div>{error}</div>}
      {isLoading && !error && <Loading />}
      {!isLoading && !error && (
        <>
          <h1 className={styles["welcome-header"]}>
            Your tasks, {currentlyLoggedInUsername}
          </h1>
          <div className={styles["add-btn"]}>
            <button onClick={handleAddTask}>➕ Add a new task</button>
          </div>
          <div className={styles.container}>
            <div>
              <h2
                className={styles["status-header"]}
              >{`${statusEmojis["TO_DO"]} TO DO`}</h2>
              <ul className={styles.row}>
                {toDoTasks.map((task) => (
                  <Task task={task} key={task.id} />
                ))}
              </ul>
            </div>
            <div>
              <h2 className={styles["status-header"]}>
                {" "}
                {`${statusEmojis["IN_PROGRESS"]} IN PROGRESS`}
              </h2>
              <ul className={styles.row}>
                {inProgressTasks.map((task) => (
                  <Task task={task} key={task.id} />
                ))}
              </ul>
            </div>
            <div>
              <h2
                className={styles["status-header"]}
              >{`${statusEmojis["DONE"]} DONE`}</h2>
              <ul className={styles.row}>
                {doneTasks.map((task) => (
                  <Task task={task} key={task.id} />
                ))}
              </ul>
            </div>
          </div>

          {isAddPopupShown && (
            <Popup>
              <div>
                <h2 className={styles["popup-header"]}>Add a new task</h2>
                <TaskForm
                  setIsAPopupShown={setIsAddPopupShown}
                  currentlyLoggedInUsername={currentlyLoggedInUsername}
                  actOnTask={createTask}
                  actionType="createTask"
                />
              </div>
            </Popup>
          )}
        </>
      )}
    </>
  );
}

export default TaskList;
