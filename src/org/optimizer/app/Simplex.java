package org.optimizer.app;

public class Simplex {
	
	private int num_of_unknowns;
	private int num_of_constraints;
	private static Simplex INSTANCE;
	
	public static Simplex getInstance(){
		if(INSTANCE == null){
			INSTANCE = new Simplex();
		}
		return INSTANCE;
	}
	
	
	public double[] solve(double[] x_arr, double[][] constraints){ //put args here
		num_of_unknowns = x_arr.length;
		num_of_constraints = constraints.length;
		
		double[] basicSolution = null;
		Tableau tableau;
		//Step 1: Using slack variables, convert LP problem to 
		//	a system of linear equations.
		//Step 2: Set up initial tableau. Derive basic solution
		tableau = setupInitialTableau(x_arr, constraints);
		basicSolution = deriveBasicSolution(tableau);
		System.out.print("initial basic solution: ");
		printArray(basicSolution);

		do{
			//Step 3: select pivot column
			int pivot_column = selectPivotColumn(tableau);
				//pivot_column == -1 -> there no negative numbers in the bottom row
				if(pivot_column == -1){break;}
			//Step 4: select pivot element
			int pivot_element_row = selectPivotElement(pivot_column, tableau);
			//Step 5: Use the pivot to clear pivot column using Gauss-Jordan
			clearPivotColumn(pivot_column, pivot_element_row, tableau);
			//Step 6: Repeat steps 3-5 until there are no more negative numbers
			//	in the bottom row (stop after 3)
		}while(true);
		//The solution for the LP problem is the basic solution from
		//the final tableau
		basicSolution = deriveBasicSolution(tableau);
		System.out.print("solution: ");
		printArray(basicSolution);
		return basicSolution;
	}
	
	public void printArray(double[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.print(arr[i]+" ");
		}
		System.out.println();
	}
	

	private static Tableau setupInitialTableau(double[] x_arr, double[][] constraints){
		return new Tableau(x_arr, constraints);
	}
	
	private double[] deriveBasicSolution(Tableau tableau){
		double[] basicSolution = new double[num_of_constraints + num_of_unknowns + 1];
		
		//Look for the columns that are cleared (all zeroes except for one and value is 1)
		for(int i = 0; i < basicSolution.length; i++){
			int cleared_column_row = tableau.getClearedColumnRow(i);
			if(cleared_column_row != -1){
				basicSolution[i] = tableau.getRowAnswer(cleared_column_row);
			}else{
				basicSolution[i] = 0;
			}
		}
		
		return basicSolution;
	}
	
	private static int selectPivotColumn(Tableau tableau){		
		return tableau.getPivotColumn();
	}
	
	private static int selectPivotElement(int pivot_column, Tableau tableau){
		return tableau.getPivotElementRow(pivot_column);
	}
	
	private static void clearPivotColumn(int pivot_column, int pivot_element_row, Tableau tableau){
		tableau.clearPivotColumn(pivot_column, pivot_element_row);
	}
		
}
