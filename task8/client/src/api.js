export async function getUser(token) {
    const res = await fetch("http://localhost:5000/me", {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!res.ok) throw new Error("Unauthorized");
    return await res.json();
}

export const register = async (email, password) => {
    const res = await fetch("http://localhost:5000/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
  
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || "Registration failed");
    return data.token;
  };  
  
export const login = async (email, password) => {
    const res = await fetch("http://localhost:5000/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
    if (!res.ok) throw new Error("Login failed");
    const data = await res.json();
    return data.token;
  };
  
export const getUserNormal = async (token) => {
    const res = await fetch("http://localhost:5000/api/user", {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!res.ok) throw new Error("Unauthorized");
    return res.json();
  };
  