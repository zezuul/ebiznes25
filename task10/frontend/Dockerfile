FROM python:3.9-slim

WORKDIR /app

# Kopiuj pliki statyczne
COPY index.html .
COPY script.js .

EXPOSE 5500
CMD ["python", "-m", "http.server", "5500"]