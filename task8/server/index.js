const express = require("express");
const session = require("express-session");
const cors = require("cors");
const passport = require("passport");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");

require("dotenv").config();

require("./auth/google");

const { findOrCreateUser } = require("./db/users");

const app = express();
const PORT = 5000;

const SECRET = "verysecretkey";

const users = [];

app.use(cors({ origin: process.env.FRONTEND_URL, credentials: true }));
app.use(express.json());
app.use(
    session({
        secret: "secret_session_key",
        resave: false,
        saveUninitialized: true,
    })
);
app.use(passport.initialize());
app.use(passport.session());

// Register
app.post("/auth/register", async (req, res) => {
    const { email, password } = req.body;
  
    if (!email || !password) {
      return res.status(400).json({ error: "Email and password required" });
    }
  
    const existingUser = users.find(user => user.email === email);
    if (existingUser) {
      return res.status(400).json({ error: "User already exists" });
    }
  
    try {
      const hashedPassword = await bcrypt.hash(password, 10);
      const newUser = { email, password: hashedPassword };
      users.push(newUser);
  
      const token = jwt.sign({ email }, SECRET, { expiresIn: "1h" });
      return res.status(201).json({ token });
    } catch (err) {
      console.error("Registration error:", err);
      return res.status(500).json({ error: "Internal server error" });
    }
  });
  
  
  // Login
  app.post("/auth/login", async (req, res) => {
    const { email, password } = req.body;
    const user = users.find(user => user.email === email);
    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ error: "Invalid credentials" });
    }
  
    const token = jwt.sign({ email: user.email }, SECRET, { expiresIn: "1h" });
    res.json({ token });
  });
  
  // Get user
  app.get("/api/user", (req, res) => {
    const authHeader = req.headers.authorization;
    if (!authHeader?.startsWith("Bearer ")) return res.sendStatus(401);
  
    const token = authHeader.split(" ")[1];
    try {
      const user = jwt.verify(token, SECRET);
      res.json({ userId: user.email });
    } catch {
      res.sendStatus(403);
    }
  });


app.get("/auth/google", passport.authenticate("google", { scope: ["profile", "email"] }));

app.get(
    "/auth/google/callback",
    passport.authenticate("google", { failureRedirect: "/" }),
    async (req, res) => {
        const user = await findOrCreateUser(req.user);
        const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: "1h" });

        res.redirect(`${process.env.FRONTEND_URL}/oauth-callback?token=${token}`);
    }
);

app.get("/me", (req, res) => {
    const authHeader = req.headers.authorization;
    const token = authHeader && authHeader.split(" ")[1];
    if (!token) return res.sendStatus(401);

    jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) return res.sendStatus(403);
        res.json({ userId: user.id });
    });
});

app.get('/logout', (req, res) => {
    req.logout(() => {
      req.session.destroy(() => {
        res.redirect('http://localhost:3000'); // Redirect back to frontend
      });
    });
  });
  

app.listen(PORT, () => console.log(`Server running on http://localhost:${PORT}`));