import styles from "./TaskList.module.css";
import Task from "./Task";
import { useTasks } from "../../contexts/TasksContext";
import Button from "../Button";
import { useEffect, useState } from "react";
import Popup from "../Popups/Popup";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { useAuth } from "../../contexts/FakeAuthContext";

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
  }, []);

  // add task form -- TODO: refactor in a separate component
  const [newTaskTitle, setNewTaskTitle] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [newTaskStatus, setNewTaskStatus] = useState(taskStatuses[0]);
  const [newTaskPriority, setNewTaskPriority] = useState(taskPriorities[0]);
  const [newTaskDeadline, setNewTaskDeadline] = useState(new Date());
  const [newTaskAssigneeID, setNewTaskAssigneeID] = useState(
    currentlyLoggedInUsername
  );
  const [newTaskCreationError, setNewTaskCreationError] = useState("");

  const toDoTasks = tasks.filter((task) => task.status === "TO_DO");
  const inProgressTasks = tasks.filter((task) => task.status === "IN_PROGRESS");
  const doneTasks = tasks.filter((task) => task.status === "DONE");

  function handleAddTask(e) {
    e.preventDefault();
    setIsAddPopupShown(true);
  }

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
              </div>
            </Popup>
          )}
        </>
      )}
    </>
  );
}

export default TaskList;
