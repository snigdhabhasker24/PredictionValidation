This task is implemented in Java. The run commands are specified in the run.sh file. The input and output files are passed as command line arguments along with the folder name.

javac ./src/PredictionValidation.java

java -classpath ./src PredictionValidation ./Input/actual.txt ./Input/prediction.txt ./Input/window.txt ./Output/comparison.txt

Approach:

The actual.txt and prediction.txt files values are read and stored into two arraylists. 

Initially for each hour, the error is calculated between actual and predicted values for each matched stock value. The sum of error values for each hour is stored along the count of error values (sum,n). This is used to calculate the average value directly using the sum and count values of the hours included in each window.

For calculating the average value for sliding windows, for all the hours included in the window, the sum of errors stored previously are added, and the count values are also added up. Then the average is calculated by dividing the total sum by total count.

If sum1, sum2, sum3 and count1, count2, count3 are the sum of error values and count values for hours 1, 2 and 3 and the window size is 3. The first average is calculated as (sum1+sum2+sum3)/(count1+count2+count3).

For each sliding window, the same process is used to calculate the average. Exceptional cases in the input files are handled.
