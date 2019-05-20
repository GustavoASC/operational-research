/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
     */
    public void solve(double[][] tableau) {
        int iteration = 0;
        tableau = addSlackVariables(tableau);
        while (!isOptimal(tableau)) {
            iteration++;
            printTableau(tableau, iteration);
            int pivotColumn = findPivotColumnIndex(tableau);
            int pivotRow = findPivotRowIndex(tableau, pivotColumn);
            tableau = createTableauFromPivot(tableau, pivotRow, pivotColumn);
        }
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
            for (int j = 0; j < currentOriginalTableauRow.length; j++) {
                // The constraint equality is not copied yet because it will 
                // be copied after the slack variable is inserted
                if (j != getConstraintEqualityIndex(currentOriginalTableauRow)) {
                    rowWithSlack[j] = currentOriginalTableauRow[j];
                }
            }
            // Specifies the value of the slack variable for the current row
            int slackVariableIndex = calculateSlackIndex(currentOriginalTableauRow, i);
            rowWithSlack[slackVariableIndex] = 1.0;
            // Reinserts the constraint equality value
            rowWithSlack[getConstraintEqualityIndex(rowWithSlack)] = getConstraintEqualityValue(currentOriginalTableauRow);
            // Adds the new row to the new tableau matrix
            tableauWithSlacks[i] = rowWithSlack;
        }
        return tableauWithSlacks;
    }
    
    /**
     * Calculates the index in which the slack variable should be inserted
     * 
     * @param row row array
     * @param slackNumber number of the slack variable to be inserted
     * (1, 2, 3...)
     * @return the slack index
     */
    private int calculateSlackIndex(double[] row, int slackNumber) {
        int constraintIndex = getConstraintEqualityIndex(row);
        int constraintFromEnd = row.length - constraintIndex;
        int slackIndexWithinRow = row.length + slackNumber - constraintFromEnd;
        return slackIndexWithinRow;
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
        double rowResult = getConstraintEqualityValue(tableau[rowIndex]);
        double divisionResult = rowResult / pivotValue;
        return divisionResult;
    }
    
    /**
     * Returns the value of the constraint for the specified row
     * 
     * @param row row values
     * @return constraint equality value
     */
    private double getConstraintEqualityValue(double[] row) {
        int index = getConstraintEqualityIndex(row);
        return row[index];
    }
    
    /**
     * Returns the index of the constraint for the specified row
     * 
     * @param row row values
     * @return constraint equality value
     */
    private int getConstraintEqualityIndex(double[] row) {
        return row.length - 1;
    }

    /**
     * Creates a new tableau from the specified pivot column and row
     *
     * @param tableau
     * @param pivotRowIndex
     * @param pivotColumnIndex
     * @return
     */
    public double[][] createTableauFromPivot(double[][] tableau, int pivotRowIndex, int pivotColumnIndex) {
        // Creates the new and empty tableau
        double[][] newTableau = new double[tableau.length][];
        for (int i = 0; i < tableau.length; i++) {
            newTableau[i] = new double[tableau[i].length];
        }
        // Divides every value of the pivot row from the pivot value
        double[] pivotRow   = tableau[pivotRowIndex];
        double   pivotValue = pivotRow[pivotColumnIndex];
        for (int i = 0; i < pivotRow.length; i++) {
            double currentPivotRowValue = pivotRow[i];
            newTableau[pivotRowIndex][i] = currentPivotRowValue / pivotValue;
        }
        //
        // Now, calculates the value of remaining tableau variables 
        //
        // The rule is:
        //    - New tableau value = (Negative value in old tableau pivot column) * (value in new tableau pivot row) + (Old tableau value)
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                // Checks if variable hasn't been modified yet by the
                // operations above
                if (i != pivotRowIndex && j != pivotColumnIndex) {
                    BigDecimal negativeValueOldTableauPivotColumn = new BigDecimal(tableau[i][pivotColumnIndex] * -1).setScale(2, RoundingMode.HALF_EVEN);
                    BigDecimal valueNewTableauPivotRow = new BigDecimal(newTableau[pivotRowIndex][j]).setScale(2, RoundingMode.HALF_EVEN);
                    BigDecimal oldTableauValue = new BigDecimal(tableau[i][j]).setScale(2, RoundingMode.HALF_EVEN);
                    BigDecimal firstResult = negativeValueOldTableauPivotColumn.multiply(valueNewTableauPivotRow);
                    BigDecimal newValue = firstResult.add(oldTableauValue);
                    newTableau[i][j] = newValue.doubleValue();
                }
            }
        }
        return newTableau;
    }

    /**
     * Prints the specified tableau
     * 
     * @param tableau tableau to be printed
     * @param interation iteration number 
     */
    private void printTableau(double[][] tableau, int iteration) {
        System.out.println("Tableau in iteration " + iteration + ":");
        for (int i = 0; i < tableau.length; i++) {
            System.out.print("+");
            System.out.print(repeat("-", (8 * (tableau[0].length - 1))));
            System.out.print("+");
            System.out.println();
            System.out.print("|");
            for (int j = 0; j < tableau[i].length; j++) {
                String formatted = String.format("%6.2f", tableau[i][j]);
                System.out.print(formatted + "|");
            }
            System.out.println();
        }
        System.out.print("+");
        System.out.print(repeat("-", (8 * (tableau[0].length - 1))));
        System.out.print("+");
        System.out.println();
    }
    
    
    /**
     * Repeats the specified {@code string} {@code repeatTimes} times
     * 
     * @param target string to be repeated
     * @param repeatTimes number of repetitions
     * @return repeated String
     */
    private String repeat(String target, int repeatTimes) {
        StringBuilder repeated = new StringBuilder();
        for (int i = 0; i < repeatTimes; i++) {
            repeated.append(target);
        }
        return repeated.toString();
    }
}
