/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

/**
 * Class to solve Linear Problems with Simplex algorithm.
 *
 * @author gustavo
 */
public class SimplexSolver {

    /**
     * Row where target function variables are found within tableau matrix, starting from zero index
     */
    private static final int TARGET_FUNCTION_ROW_WITHIN_TABLEAU = 0;

    /**
     *
     * @param tableau
     * @return resolved simplex tableau
     */
    public double[][] solve(double[][] tableau) {
        printTableau(tableau);
        return tableau;
    }

    /**
     * Adds slack variables to the specified tableau
     *
     * @param tableau without slack variables
     * @return tableau with slack variables
     */
    public double[][] addSlackVariables(double[][] tableau) {
        double[][] tableauWithSlacks = new double[tableau.length][];
        int totalSlackVariables = tableau.length;
        // Adds slack variables for each row
        for (int i = 0; i < tableau.length; i++) {
            // References the current tableau row
            double[] currentOriginalTableauRow = tableau[i];
            // Creates a new array, with original row size + total slack variables to be added
            double[] rowWithSlack = new double[currentOriginalTableauRow.length + totalSlackVariables];
            // Clones the original tableau variables to the new array
            // For now the value of slack variables are ignored and keep zero
            System.arraycopy(currentOriginalTableauRow, 0, rowWithSlack, 0, currentOriginalTableauRow.length);
            // Specifies the value of the slack variable for the current row
            rowWithSlack[currentOriginalTableauRow.length + i] = 1.0;
            // Adds the new row to the new tableau matrix
            tableauWithSlacks[i] = rowWithSlack;
        }
        return tableauWithSlacks;
    }

    /**
     * Inverts the signal of every target function variable
     *
     * @param tableau 
     * @return
     */
    public double[][] invertTargetFunctionSignal(double[][] tableau) {
        for (int i = 0; i < tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU].length; i++) {
            tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i] = tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i] * -1;
        }
        return tableau;
    }

    /**
     *
     * @param tableau
     * @return
     */
    public double[][] solveNextStep(double[][] tableau) {
        printTableau(tableau);
        return tableau;
    }

    private void printTableau(double[][] tableau) {
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.print(tableau[i][j] + " | ");
            }
            System.out.println();
        }
    }

}
