from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from openai import OpenAI
import os, random

client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

app = FastAPI()

# Middleware CORS — umożliwia połączenie z frontendem
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Modele wiadomości
class ChatRequest(BaseModel):
    message: str

# Otwarcia i zamknięcia rozmowy
greetings = [
    "Cześć! ",
    "Dzień dobry! ",
    "Hej! ",
    "Witaj! ",
    "Miło Cię widzieć! "
]

closings = [
    " Dzięki za rozmowę!",
    " Miłego dnia!",
    " Trzymaj się!",
    " Do zobaczenia!",
    " W razie czego — wróć śmiało."
]

# Endpoint czatu
@app.post("/chat")
async def chat(request: ChatRequest):
    try:
        greeting = random.choice(greetings)
        closing = random.choice(closings)
        full_message = f"{greeting}{request.message}{closing}"

        response = client.chat.completions.create(
            model="gpt-3.5-turbo",
            messages=[{"role": "user", "content": full_message}]
        )

        return {"response": response.choices[0].message.content.strip()}
    except Exception as e:
        return {"error": str(e)}