import PageNav from "../components/PageNav";
import styles from "./PageNotFound.module.css";

function PageNotFound() {
  return (
    <div className={styles.container}>
      <PageNav />
      <h1>Page not found 😢</h1>
    </div>
  );
}

export default PageNotFound;
