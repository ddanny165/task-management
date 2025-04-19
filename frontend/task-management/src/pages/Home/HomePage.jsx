import { Link } from "react-router";
import HomeNav from "../../components/PageNav";
import styles from "./Homepage.module.css";

export default function Homepage() {
  return (
    <main className={styles.homepage}>
      <HomeNav />
      <section>
        <h1>
          You complete your tasks.
          <br />
          Taskonaut keeps track of your progress.
        </h1>
        <h2>
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Voluptate
          porro sed eveniet vero impedit similique quas illum. Quo harum
          accusantium labore hic voluptas nihil, voluptatibus quaerat,
          cupiditate nostrum laborum deserunt.
        </h2>
        <button className={styles["start-button"]}>
          <Link to={"login"}>Start your journey now</Link>
        </button>
      </section>
    </main>
  );
}
