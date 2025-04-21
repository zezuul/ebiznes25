FROM openjdk:11

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj pliki do obrazu
COPY . /app

# Zainstaluj sbt i przygotuj aplikację
RUN curl -L -o sbt.tgz https://github.com/sbt/sbt/releases/download/v1.9.7/sbt-1.9.7.tgz && \
    tar -xzf sbt.tgz && \
    mv sbt /usr/local/sbt && \
    ln -s /usr/local/sbt/bin/sbt /usr/local/bin/sbt

# Buduj aplikację
RUN sbt clean compile stage

# Uruchom aplikację (port 9000 domyślnie)
CMD ["target/universal/stage/bin/play-crud-app", "-Dplay.http.secret.key=super-secret-key"]
