import { createContext, useContext, useReducer } from "react";

const AuthContext = createContext();

const savedUser = JSON.parse(localStorage.getItem("user"));
const initialState = {
  currentUser: savedUser,
  isAuthenticated: savedUser != null,
  authError: "",
};

const FAKE_USER = {
  username: "ddanny165",
  password: "password",
  firstName: "Danylo",
  secondName: "Moskaliuk",
  email: "moskaliuk.daniel@gmail.com",
};

function reducer(state, action) {
  switch (action.type) {
    case "login":
      localStorage.setItem("user", JSON.stringify(action.payload));
      return {
        ...state,
        isAuthenticated: true,
        currentUser: action.payload,
        authError: "",
      };
    case "logout":
      localStorage.removeItem("user");
      return { ...state, isAuthenticated: false, currentUser: null };
    case "failedLogin":
      return { ...state, authError: action.payload };
    default:
      throw new Error("Unknown action type!");
  }
}

function AuthProvider({ children }) {
  const [{ currentUser, isAuthenticated, authError }, dispatch] = useReducer(
    reducer,
    initialState
  );

  function login(email, password) {
    if (email === FAKE_USER.email && password === FAKE_USER.password) {
      dispatch({ type: "login", payload: FAKE_USER });
      console.log("check-logged-in");
    } else {
      dispatch({
        type: "failedLogin",
        payload: "Your email and/or password is incorrect! Try again.",
      });
    }
  }

  function logout() {
    dispatch({ type: "logout" });
  }

  return (
    <AuthContext.Provider
      value={{ currentUser, isAuthenticated, authError, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

function useAuth() {
  const value = useContext(AuthContext);
  if (value === undefined) {
    throw new Error("FakeAuthContext was used outside of FakeAuthProvider!");
  }
  return value;
}

export { AuthProvider, useAuth };
