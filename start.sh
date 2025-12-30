#!/bin/bash
./mvnw clean package
java -jar target/sfgame-0.0.1-SNAPSHOT.jar
