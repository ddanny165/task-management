import styles from "./TaskList.module.css";
import Task from "./Task";
import { useTasks } from "../../contexts/TasksContext";
import Button from "../Button";
import { useEffect, useState } from "react";
import Popup from "../Popups/Popup";
import { useAuth } from "../../contexts/FakeAuthContext";
import Form from "../Form";

const taskStatuses = ["TO_DO", "IN_PROGRESS", "DONE"];
const taskPriorities = ["LOW", "MEDIUM", "HIGH"];
const users = ["ddanny165", "ddanny228"]; // TODO: get from API

function TaskList() {
  const { tasks, isLoading, error, statusEmojis, getTasks, createTask } =
    useTasks();
  const [isAddPopupShown, setIsAddPopupShown] = useState(false);

  let { currentUser } = useAuth();
  let currentlyLoggedInUsername = currentUser.username;

  useEffect(() => {
    getTasks(currentlyLoggedInUsername);
  }, [currentlyLoggedInUsername]);

  // add task form -- TODO: refactor in a separate component
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
      {isLoading && !error && <div>Loading...</div>}
      {!isLoading && !error && tasks.length > 0 && (
        <>
          <h1 className={styles["welcome-header"]}>
            Your tasks, {currentlyLoggedInUsername}
          </h1>
          <div className={styles["add-btn"]}>
            <Button onClick={handleAddTask} type={"add-task"}>
              ➕ Add a new task
            </Button>
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
              <div className={styles["popup-content"]}>
                <h2 className={styles["popup-header"]}>Add a new task</h2>
                <Form
                  setIsAddPopupShown={setIsAddPopupShown}
                  taskStatuses={taskStatuses}
                  taskPriorities={taskPriorities}
                  currentlyLoggedInUsername={currentlyLoggedInUsername}
                  users={users}
                  createTask={createTask}
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
