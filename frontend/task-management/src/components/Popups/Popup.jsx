import styles from "./Popup.module.css";

function Popup({ children }) {
  return (
    <div className={styles["popup-overlay"]}>
      <div className={styles["popup-content"]}>{children}</div>
    </div>
  );
}

export default Popup;
