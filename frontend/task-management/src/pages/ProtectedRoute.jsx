import { useNavigate } from "react-router";
import { useAuth } from "../contexts/FakeAuthContext";
import { useEffect } from "react";

let secondsToRedirect = 2;

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(
    function () {
      if (!isAuthenticated) {
        setTimeout(() => navigate("/"), secondsToRedirect * 1000);
      }
    },
    [isAuthenticated, navigate]
  );

  return isAuthenticated ? (
    children
  ) : (
    <h1 style={{ color: "red", textAlign: "center" }}>
      Not authorized! Redirecting in {secondsToRedirect} seconds...
    </h1>
  );
}

export default ProtectedRoute;
