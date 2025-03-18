# ubuntu
FROM ubuntu:24.04

RUN apt-get update && \
    apt-get install -y \
    wget \
    curl \
    zip \
    unzip \
    build-essential \
    libssl-dev \
    libffi-dev \
    zlib1g-dev \
    && apt-get clean

# python 3.10
RUN wget https://www.python.org/ftp/python/3.10.16/Python-3.10.16.tgz && \
    tar xzf Python-3.10.16.tgz

RUN cd Python-3.10.16 && ./configure --prefix=/opt/python3.10 && make && make install
RUN rm Python-3.10.16.tgz && rm -r Python-3.10.16/

RUN rm -f /usr/bin/python3 \
    && ln -s /opt/python3.10/bin/python3.10 /usr/bin/python \
    && ln -s /opt/python3.10/bin/python3.10 /usr/bin/python3 \
    && ln -s /opt/python3.10/bin/pip3.10 /usr/bin/pip3 \
    && python3 --version && python --version


# java 8 + unzip
RUN apt-get install -y openjdk-8-jdk
RUN java -version

# kotlin
RUN wget -O kotlin-compiler.zip https://github.com/JetBrains/kotlin/releases/download/v2.1.10/kotlin-compiler-2.1.10.zip

RUN unzip kotlin-compiler.zip -d /opt \
    && ln -s /opt/kotlinc/bin/kotlinc /usr/local/bin/kotlinc \
    && ln -s /opt/kotlinc/bin/kotlin /usr/local/bin/kotlin \
    && rm kotlin-compiler.zip \
    && kotlin -version

# gradle
RUN wget -O gradle.zip https://services.gradle.org/distributions/gradle-8.13-bin.zip

RUN unzip gradle.zip -d /opt/gradle \
    && ln -s /opt/gradle/gradle-8.13/bin/gradle /usr/local/bin/gradle \
    && rm gradle.zip \
    && gradle --version

WORKDIR /app

COPY build.gradle /app/
RUN gradle build

CMD [ "java", "HelloWorld"]