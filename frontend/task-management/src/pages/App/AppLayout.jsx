import { Outlet } from "react-router";
import styles from "./AppLayout.module.css";
import PageNav from "../../components/PageNav";

function AppLayout() {
  return (
    <div className={styles.app}>
      <PageNav />
      <Outlet />
    </div>
  );
}

export default AppLayout;
