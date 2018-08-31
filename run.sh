#!/bin/bash
javac ./src/PredictionValidation.java
java -classpath ./src PredictionValidation ./input/actual.txt ./input/prediction.txt ./input/window.txt ./output/comparison.txt
