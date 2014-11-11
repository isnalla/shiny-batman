package org.optimizer.app;

public class UltimateOptimizer {
	
	public double objective(double... x){
		
		double z = 150*x[0] + 175*x[1];
		
		return z;
	}

	public static void main(String[] args) {
		double[] x_arr = {150, 175};
		double[][] constraints = {
				{7.0, 11.0, 77.0},
				{10.0, 8.0, 80.0},
				{1.0, 0.0, 9.0},
				{0.0, 0.0, 6.0}
		};
		
		double[] solution = Simplex.getInstance().solve(x_arr,constraints);
		Simplex.getInstance().printArray(solution);
	}

}
