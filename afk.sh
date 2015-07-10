#!/bin/bash

./gradlew build

java -jar -client ./build/libs/AFK-0.0.2.jar script.afk