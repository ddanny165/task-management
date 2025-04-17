import { useState } from "react";
import PageNav from "../../components/PageNav";
import styles from "./Login.module.css";
import Button from "../../components/Button";
import { useNavigate } from "react-router";

function Login() {
  const [email, setEmail] = useState("moskaliuk.daniel@gmail.com");
  const [password, setPassword] = useState("password");
  const navigate = useNavigate();

  function handleSubmit(e) {
    e.preventDefault();
    navigate("/app", { replace: true });
  }

  return (
    <div className={styles.login}>
      <PageNav />

      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.row}>
          <label htmlFor="email">Email address</label>
          <input
            type="email"
            id="email"
            onChange={(e) => setEmail(e.target.value)}
            value={email}
          />
        </div>

        <div className={styles.row}>
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            onChange={(e) => setPassword(e.target.value)}
            value={password}
          />
        </div>

        <Button type={"primary"}> Log in </Button>
      </form>
    </div>
  );
}

export default Login;
