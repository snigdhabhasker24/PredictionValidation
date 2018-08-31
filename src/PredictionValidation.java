
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.*;

public class PredictionValidation {
	private ArrayList<String[]> actualValues = new ArrayList<String[]>();
	private ArrayList<String[]> predictedValues = new ArrayList<String[]>();
//Hashmap to store sum of error values and their count for each	hour
	private HashMap<Integer,HourValues> sumByHour=new HashMap<Integer,HourValues>();
	private ArrayList<OutputValues> outputValuesList = new ArrayList<OutputValues>();
	int window;
	public static void main(String[] args){
		PredictionValidation predictionValidation=new PredictionValidation();
		predictionValidation.readFile(args);
		predictionValidation.calculateHourlyValues();
		predictionValidation.calculateAverage();
		predictionValidation.writeOutput(args[3]);
	}
//Read the input files and store the values in arraylists	
	public void readFile(String[] files){
		String line = "";
		try(BufferedReader br= new BufferedReader(new FileReader(files[0]))) {	
			while((line=br.readLine())!=null){				
				actualValues.add(line.split("\\|"));
			}
		}catch (IOException e){
			e.printStackTrace();
		}	
		try(BufferedReader br= new BufferedReader(new FileReader(files[1]))) {			
			while((line=br.readLine())!=null){				
				predictedValues.add(line.split("\\|"));
			}
		}catch (IOException e){
			e.printStackTrace();
		}		
		try(BufferedReader br= new BufferedReader(new FileReader(files[2]))) {			
			window=Integer.parseInt(br.readLine());
		}catch (IOException e){
			e.printStackTrace();
		}
	}
//Calculating sum of errors and their count for each hour, to directly find the average based on window size using these
//values, to reduce computation
	public void calculateHourlyValues(){
		int j=0;
		int flag;  //Flag will be zero until actual hour and stock values are matched with predicted ones
		int prevHr=1; //Prev hour is used to enter values into HashMap once the hour value is changed
		double sum=0;
		int n=0;
		double error;
		for(int i=0;i<predictedValues.size();i++){  //Iterating through predicted values
			flag=0;
			String[] predicted=predictedValues.get(i);
			int predHr=Integer.parseInt(predicted[0]);
			String predStock=predicted[1];
			double predPrice=Double.parseDouble(predicted[2]);
			if(prevHr!=predHr){   
				sumByHour.put(prevHr, new HourValues(sum,n));
				sum=0;
				n=0;
			}
			while(flag==0)
			{
				String[] actual=actualValues.get(j);
				int actHr=Integer.parseInt(actual[0]);
				String actStock=actual[1];
				double actPrice=Double.parseDouble(actual[2]);
				if(predHr==actHr&&predStock.equalsIgnoreCase(actStock)){ //Checking if the hour and stock values are same
					error=Math.abs(actPrice-predPrice);
					j++;
					flag=1;
					sum+=error;
					n+=1;
				}
				else j++;
			}
			prevHr=predHr;
		}
		sumByHour.put(prevHr, new HourValues(sum,n));
	}

//Calculate average for hours in the window using the sum and n (count) values stored by hour
	public void calculateAverage(){
		int lastKeyInt=Integer.parseInt(actualValues.get(actualValues.size()-1)[0]);
		int i,j,n=0;
		double sum,avg;
		for(int k=1;k<=lastKeyInt-(window-1);k++){
			j=k+window-1;
			sum=0;
			n=0;
			for(i=k;i<=j;i++){
				if(sumByHour.containsKey(i)){
					sum+=sumByHour.get(i).getSum();
					n+=sumByHour.get(i).getN();
				}
			}
			if(n==0){
				outputValuesList.add(new OutputValues(k,j,"NA"));
			}else{
				avg=Math.round((sum/n)*100.0)/100.0;
				String formatted = String.format("%.2f", avg); //For two digits after the decimal
				outputValuesList.add(new OutputValues(k,j,formatted));	
			}			
		}
	}
	
//Method to write output to a file	
	public void writeOutput(String path){
			FileWriter fw=null;
			File file= new File(path);
			try{
				if(file.exists()){
				   fw = new FileWriter(file,false);
				}else{
				   file.createNewFile();
				   fw = new FileWriter(file);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				for(int i=0;i<outputValuesList.size();i++){
					OutputValues out = outputValuesList.get(i);
					bw.write(out.getStart()+"|"+out.getEnd()+"|"+out.getAvg());
					bw.newLine();
				}
				System.out.println("The result is stored in the comparison.txt file");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}

//Class HourValues which is used to store sum and count values for each hour
class HourValues{
	double sum;
	int n;
	public HourValues(double sum,int n){
		this.sum=sum;
		this.n=n;
	}
	public double getSum(){
		return sum;
	}
	public int getN(){
		return n;
	}
	public void setSum(double sum){
		this.sum=sum;
	}
	public void setN(int n){
		this.n=n;
	}
}

class OutputValues{
	int start,end;
	String avg;
	public OutputValues(int start, int end, String avg) {
		this.start = start;
		this.end = end;
		this.avg = avg;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
}
