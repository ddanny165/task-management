import styles from "./PageNav.module.css";

function PageNav() {
  return (
    <nav className={styles.nav}>
      <p> TASKONAUT ✅ </p>
      <ul>
        <li>About</li>
        <li>Pricing</li>
        <li>Login</li>
      </ul>
    </nav>
  );
}

export default PageNav;
