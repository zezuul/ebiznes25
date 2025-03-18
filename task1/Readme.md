Task 1: Docker

Link to image: [![Docker Hub](https://img.shields.io/badge/Docker%20Hub-View-blue?logo=docker)](https://hub.docker.com/r/zezuul/task1)

Create a Docker image:

- based on Ubuntu 24.04
- with Python version 3.10
- with Java 8 and Kotlin
- add the latest version of Gradle
- include the JDBC SQLite package in the project using Gradle (build.gradle file)
- create a HelloWorld example
- run the application using both CMD and Gradle
- add a Docker Compose configuration

The image should be pushed to Docker Hub, and the link to the image should be added in the README on GitHub.

######

docker build -t zezuul/task1 .
docker push zezuul/task1:latest
