import { BrowserRouter, Navigate, Route, Routes } from "react-router";
import "./App.css";
import AppLayout from "./pages/App/AppLayout";
import Homepage from "./pages/Home/HomePage";
import Login from "./pages/Login/Login";
import TaskList from "./components/Tasks/TaskList";
import TaskDetails from "./components/Tasks/TaskDetails";
import { TasksProvider } from "./contexts/TasksContext";
import { AuthProvider } from "./contexts/FakeAuthContext";
import PageNotFound from "./pages/PageNotFound";
import ProtectedRoute from "./pages/ProtectedRoute";

function App() {
  return (
    <AuthProvider>
      <TasksProvider>
        <BrowserRouter>
          <Routes>
            <Route index element={<Homepage />} />
            <Route path="login" element={<Login />} />
            <Route
              path="app"
              element={
                <ProtectedRoute>
                  <AppLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<Navigate replace to="tasks" />}></Route>
              <Route path="tasks" element={<TaskList />}></Route>
              <Route path="tasks/:id" element={<TaskDetails />}></Route>
            </Route>
            <Route path="*" element={<PageNotFound />}></Route>
          </Routes>
        </BrowserRouter>
      </TasksProvider>
    </AuthProvider>
  );
}

export default App;
