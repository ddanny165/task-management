import styles from "./TaskList.module.css";
import Task from "./Task";
import { useTasks } from "../../contexts/TasksContext";

const currentlyLoggedInUsername = "ddanny165";

function TaskList() {
  const { tasks, isLoading, error, statusEmojis } = useTasks();

  const toDoTasks = tasks.filter((task) => task.status === "TO_DO");
  const inProgressTasks = tasks.filter((task) => task.status === "IN_PROGRESS");
  const doneTasks = tasks.filter((task) => task.status === "DONE");

  return (
    <>
      {error && <div>{error}</div>}
      {isLoading && !error && <div>Loading...</div>}
      {!isLoading && !error && tasks.length > 0 && (
        <>
          <h1 className={styles["welcome-header"]}>
            Your tasks, {currentlyLoggedInUsername}
          </h1>
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
        </>
      )}
    </>
  );
}

export default TaskList;
