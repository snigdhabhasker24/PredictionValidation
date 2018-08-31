#!/bin/bash
javac ./src/PredictionValidation.java
java -classpath ./src PredictionValidation ./Input/actual.txt ./Input/prediction.txt ./Input/window.txt ./Output/comparison.txt

javac .\src\PredictionValidation.java
java -classpath .\src PredictionValidation .\Input\actual.txt .\Input\prediction.txt .\Input\window.txt .\Output\comparison.txt