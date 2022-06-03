#!/bin/bash

./gradlew clean shadowJar

java -jar -client -Xms1m -Xmx1024m  -XX:+OptimizeStringConcat -XX:CompileThreshold=500 -XX:MaxHeapFreeRatio=90 -XX:MinHeapFreeRatio=10 ./build/libs/AFK-0.0.4-all.jar script.afk