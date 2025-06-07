import React, { useEffect, useState } from "react";
import {  login, register, getUser, getUserNormal } from "./api";

function App() {
  const [user, setUser] = useState(null);
  const [form, setForm] = useState({ email: "", password: "" });

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");
    if (token) {
      localStorage.setItem("token", token);
      window.history.replaceState(null, "", "/");
    }

    const savedToken = localStorage.getItem("token");
    if (savedToken) {
      getUser(savedToken)
          .then((res) => setUser(res))
          .catch(() => localStorage.removeItem("token"));
    }
  }, []);



  const handleGoogleLogin = () => {
    window.location.href = "http://localhost:5000/auth/google";
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
    window.location.href = "http://localhost:5000/logout";
  };

  const handleLogin = async () => {
    try {
      const token = await login(form.email, form.password);
      localStorage.setItem("token", token);
      const user = await getUserNormal(token);
      setUser(user);
    } catch (err) {
      alert("Login failed");
    }
  };

  const handleRegister = async () => {
    try {
      await register(form.email, form.password);
      alert("Registered successfully! Now log in.");
    } catch {
      alert("Register failed");
    }
  };
  
  return (
      <div>
        <h1>Hello! This is Ebiznes task8 app.</h1>
        {user ? (
            <div>
              <p>User logged in: {user.userId}</p>
              <button onClick={logout}>Logout</button>
            </div>
        ) : (
            <div>
          <input
            placeholder="Email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />
          <input
            type="password"
            placeholder="Password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
          />
          <button onClick={handleLogin}>Login</button>
          <button onClick={handleRegister}>Register</button>
            <button onClick={handleGoogleLogin}>Log in with Google</button>
            </div>
        )}
      </div>

  );
}

export default App;