import { useNavigate } from "react-router";
import { useAuth } from "../contexts/FakeAuthContext";
import { useEffect } from "react";

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(
    function () {
      if (!isAuthenticated) {
        navigate("/");
      }
    },
    [isAuthenticated, navigate]
  );

  return isAuthenticated ? (
    children
  ) : (
    <h1 style={{ color: "red", textAlign: "center" }}>Not authorized!</h1>
  );
}

export default ProtectedRoute;
