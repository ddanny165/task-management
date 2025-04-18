import { BrowserRouter, Navigate, Route, Routes } from "react-router";
import "./App.css";
import AppLayout from "./pages/App/AppLayout";
import Homepage from "./pages/Home/HomePage";
import Login from "./pages/Login/Login";
import TaskList from "./components/Tasks/TaskList";
import TaskDetails from "./components/Tasks/TaskDetails";
import { TasksProvider } from "./contexts/TasksContext";

function App() {
  return (
    <TasksProvider>
      <BrowserRouter>
        <Routes>
          <Route index element={<Homepage />} />
          <Route path="login" element={<Login />} />
          <Route path="app" element={<AppLayout />}>
            <Route index element={<Navigate replace to="tasks" />}></Route>
            <Route path="tasks" element={<TaskList />}></Route>
            <Route path="tasks/:id" element={<TaskDetails />}></Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </TasksProvider>
  );
}

export default App;
