import { Link } from "react-router";
import styles from "./PageNav.module.css";
import { useAuth } from "../contexts/FakeAuthContext";

function PageNav() {
  let { isAuthenticated, logout } = useAuth();

  return (
    <nav className={styles.nav}>
      <p>
        {" "}
        <Link to="/"> TASKONAUT ✅</Link>
      </p>
      <ul>
        <li>About</li>
        <li>Pricing</li>
        <li>
          {!isAuthenticated ? (
            <Link to="/login">Login</Link>
          ) : (
            <Link to="/" onClick={() => logout()}>
              Logout
            </Link>
          )}
        </li>
      </ul>
    </nav>
  );
}

export default PageNav;
