#!/bin/bash

./gradlew clean shadowJar

# java -jar -client -Xms1m -Xmx1024m  -XX:+OptimizeStringConcat -XX:CompileThreshold=500 -XX:MaxHeapFreeRatio=90 -XX:MinHeapFreeRatio=10 ./build/libs/afk.jar $1
time java -jar -client -Xms1m -Xmx1024m  -XX:+OptimizeStringConcat -XX:CompileThreshold=500 -XX:MaxHeapFreeRatio=90 -XX:MinHeapFreeRatio=10 -Dpolyglot.engine.WarnInterpreterOnly=false ./build/libs/afk.jar $1