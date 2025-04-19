import { useNavigate } from "react-router";
import { useAuth } from "../contexts/FakeAuthContext";
import { useEffect } from "react";

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(
    function () {
      if (!isAuthenticated) {
        setTimeout(() => navigate("/"), 2000);
      }
    },
    [isAuthenticated, navigate]
  );

  return isAuthenticated ? (
    children
  ) : (
    <h1 style={{ color: "red", textAlign: "center" }}>
      Not authorized! Redirecting in 2 seconds...
    </h1>
  );
}

export default ProtectedRoute;
