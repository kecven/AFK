#!/bin/bash

./gradlew build

# -Xmx1024m
java -jar -client -Xms1m -Xmx1024m  -XX:+OptimizeStringConcat -XX:CompileThreshold=500 -XX:MaxHeapFreeRatio=90 -XX:MinHeapFreeRatio=10 ./build/libs/AFK-0.0.3.jar script.afk