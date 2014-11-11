package org.optimizer.app;

import java.util.HashMap;
import java.util.Map;

/**
 * 	Tableau used for the simplex method
 */
public class Tableau {
	protected double[][] data;
	protected int num_of_constraints;
	protected int num_of_unknowns;
	protected int num_cols;
	protected int num_rows;
	protected int slack_start_index;
	protected Map<String, Double> basicSolution;
	
	public Tableau(double[] x_arr, double[][] constraints){

		num_of_unknowns = x_arr.length;
		num_of_constraints = constraints.length;
		slack_start_index = num_of_unknowns;
		
		//columns : x's, slacks per constraint, z, and answer 
		num_cols = num_of_unknowns + num_of_constraints + 1 + 1; 
		//rows : constrains, objective
		num_rows = num_of_constraints + 1;
		
		data = new double[num_rows][num_cols];
		
		for(int i = 0; i < data.length; i++){
			//fill x[n] columns
			int j;
			for(j = 0; j < num_of_unknowns; j++){
				if(i < num_of_constraints){
					data[i][j] = constraints[i][j];
				}else{
					data[i][j] = x_arr[j]*-1; //negate because of transposition
				}
			}
			//fill slack variables
			for(; j < num_of_unknowns + num_of_constraints; j++){
				if(j == i + slack_start_index){
					data[i][j] = 1;
				}else{
					data[i][j] = 0;
				}
			}
			//fill Z and answer
			if(i < num_of_constraints){
				data[i][j++] = 0;
				data[i][j] = constraints[i][constraints[i].length - 1];
			}else{
				data[i][j++] = 1;
				data[i][j] = 0;
			}
		}
		
		basicSolution = new HashMap<String, Double>();
	}
	
	public int getPivotColumn(){
		int pivot_column = -1;
		
		double most_negative_element = 0;
		//Look at all the numbers in the bottom row, excluding the answer column
		for(int i = 0; i < num_cols; i++){
			
			double element = data[data.length - 1][i];
			if(element < 0){
				//choose the negative number with the largest magnitude
				if(most_negative_element == 0 || element < most_negative_element){
					most_negative_element = element;
					//its column is the pivot column
					pivot_column = i;
				}
			}

		}
		
		//if all the numbers in the bottom row are zero or positive,
		//then the computation is done, pivot_column == -1 signals that
		
		return pivot_column;
	}
	
	public int getPivotElementRow(int pivot_column){
		int pivot_element_row = -1;
		Double minTestRatio = null;
		
		for(int i = 0; i < data.length; i++){
			double b = data[i][pivot_column];
			//The pivot must always be a positive number
 			if(b > 0){
				double a = getRowAnswer(i);
				//for each positive entry b in the pivot column,
				//compute the ratio a/b, where a is the number in the
				//rightmost column in that row
				double test_ratio = a/b;
				
				//among these test ratios, choose the smallest one
				if(minTestRatio == null || test_ratio < minTestRatio){
					minTestRatio = test_ratio;
					//the corresponding number b is the pivot element
					pivot_element_row = i;
				}
			}
		}
		
		return pivot_element_row;
	}
	
	public double[][] getData(){
		return data;
	}
	
	/**
	 * 
	 * @param index column index
	 * @return
	 */
	public String getColumnLabel(int index){
		String label;
		if(index < num_of_unknowns){
			label = "x"+index;
		}else if(index < num_of_constraints){
			label = "S"+(slack_start_index + index);
		}else if (index == num_of_unknowns + num_of_constraints){
			label = "Z";
		}else label = "answer";
		
		return label;
	}
	
	public int getClearedColumnRow(int col_index){
		int cleared_column_row = -1;
		
		for(int i = 0; i < data.length; i++){
			if(cleared_column_row == -1 && data[i][col_index] == 1){
				cleared_column_row = i;
			}else if(cleared_column_row != -1 && data[i][col_index] != 0){
				cleared_column_row = -1;
				break;
			}
		}
		
		return cleared_column_row;
	}
	
	public double getRowAnswer(int row_index){
		return data[row_index][data[row_index].length-1];
	}

	public void clearPivotColumn(int pivot_column, int pivot_element_row) {
		//clear the pivot column using gauss-jordan (one iteration ; without backward substitution) 
		
		//GaussJordan.pivotMatrix(m,i); //not sure if there is pivoting
		double pivot_element = data[pivot_element_row][pivot_column];

		//normalize pivot_element_row
		if(pivot_element != 1){
			double normalizer = pivot_element;
			for(int i = 0; i < data[pivot_element_row].length; i++){
				data[pivot_element_row][i] /= normalizer;
			}
		}

 		for(int i = 0 ; i < data.length; i++){
 			if(i == pivot_element_row) continue;
 			double elementToBeMadeZero = data[i][pivot_column];
			for(int j = 0; j < data[i].length; j++){
   				data[i][j] -= elementToBeMadeZero*data[pivot_element_row][j];
			}
		}
		
	}
	
}
