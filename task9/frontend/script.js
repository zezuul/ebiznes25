async function sendMessage() {
    const userInput = document.getElementById("userInput").value;
    const responseArea = document.getElementById("responseArea");

    const res = await fetch("http://localhost:8000/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ message: userInput })
    });

    const data = await res.json();
    responseArea.textContent = data.response || data.error;
}