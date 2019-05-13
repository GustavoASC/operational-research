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
     * Row where target function variables are found within tableau matrix,
     * starting from zero index
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
     * Returns {@code true} if the specified tableau is optimal
     *
     * @param tableau matrix to test optimality
     * @return {@code true} if the specified tableau is optimal
     */
    public boolean isOptimal(double[][] tableau) {
        for (int i = 0; i < tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU].length; i++) {
            if (tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the column index of largest value in tableau, which is the next
     * pivot column.
     * <p>
     * The row index is related to
     * {@link SimplexSolver.TARGET_FUNCTION_ROW_WITHIN_TABLEAU} within tableau.
     *
     * @param tableau tableau to find the largest value
     * @return largest value index within tableau
     */
    public int findPivotColumnIndex(double[][] tableau) {
        double largestValue = 0.0;
        int bestCollumnIndex = 0;
        for (int i = 0; i < tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU].length; i++) {
            double absoluteValue = Math.abs(tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i]);
            if (absoluteValue >= largestValue) {
                largestValue = absoluteValue;
                bestCollumnIndex = i;
            }
        }
        return bestCollumnIndex;
    }

    /**
     * Finds the most appropriate pivot row considering the specified tableau
     * and pivot column.
     * <p>
     * The rule to choose the row is the one which has the smallest value,
     * dividing the constraint equality by the coefficient of the variable in
     * pivot column index.
     *
     * @param tableau simplex tableau
     * @param pivotColumnIndex pivot column index
     * @return most appropriate pivot row index
     */
    public int findPivotRowIndex(double[][] tableau, int pivotColumnIndex) {
        int bestRowIndex = 0;
        double smallestResult = Double.MAX_VALUE;
        for (int i = 0; i < tableau.length; i++) {
            double divisionResult = calculateDivisionForRow(tableau, i, pivotColumnIndex);
            if (divisionResult <= smallestResult) {
                bestRowIndex = i;
                smallestResult = divisionResult;
            }
        }
        return bestRowIndex;
    }

    /**
     * Calculates the division for the specified row with the specified pivot
     * column index.
     * <p>
     * This method divides the constraint equality by the coefficient of the
     * variable in pivot column index.
     * 
     * @param tableau simplex tableau
     * @param rowIndex row index within tableau
     * @param columnIndex column index within tableau (pivot column)
     * @return division result
     */
    public double calculateDivisionForRow(double[][] tableau, int rowIndex, int columnIndex) {
        double pivotValue = tableau[rowIndex][columnIndex];
        int rowLength = tableau[rowIndex].length;
        double rowResult = tableau[rowIndex][rowLength - 1];
        double divisionResult = rowResult / pivotValue;
        return divisionResult;
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
