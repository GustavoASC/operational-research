/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

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
    public static final int TARGET_FUNCTION_ROW_WITHIN_TABLEAU = 0;
    /* List with listeners to simplex tableau completions */
    private final List<SimplexListener> listeners;

    /**
     * Creates a new Simplex solver
     */
    public SimplexSolver() {
        this.listeners = new LinkedList<>();
    }
    
    /**
     * Adds a new Simplex listener
     * 
     * @param l listener to be added
     * @return this
     */
    public SimplexSolver addSimplexListener(SimplexListener l) {
        listeners.add(l);
        return this;
    }

    /**
     * Solves the specified tableau
     *
     * @param tableau
     */
    public void solve(double[][] tableau) {
        int iteration = 0;
        tableau = addDivisionResultColumn(tableau);
        tableau = addSlackVariables(tableau);
        while (!isOptimal(tableau)) {
            iteration++;
            int pivotColumn = findPivotColumnIndex(tableau);
            calculateDivisionForTableau(tableau, pivotColumn);
            fireTableauIterationSolved(tableau, iteration, false);
            int pivotRow = findPivotRowIndex(tableau, pivotColumn);
            tableau = createTableauFromPivot(tableau, pivotRow, pivotColumn);
        }
        iteration++;
        fireTableauIterationSolved(tableau, iteration, true);
    }
    
    /**
     * Fires a tableau iteration solved listener
     * 
     * @param tableau
     * @param iteration 
     */
    private void fireTableauIterationSolved(double[][] tableau, int iteration, boolean finalIteration) {
        listeners.forEach((l) -> {
            l.handle(tableau, iteration, finalIteration);
        });
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
                if (j != SimplexUtils.getConstraintEqualityIndex(currentOriginalTableauRow)) {
                    rowWithSlack[j + 1] = currentOriginalTableauRow[j];
                }
            }
            // Specifies the value of the slack variable for the current row
            int slackVariableIndex = calculateSlackIndex(currentOriginalTableauRow, i);
            rowWithSlack[slackVariableIndex] = 1.0;
            // Reinserts the constraint equality value
            rowWithSlack[SimplexUtils.getConstraintEqualityIndex(rowWithSlack)] = getConstraintEqualityValue(currentOriginalTableauRow);
            // Adds the new row to the new tableau matrix
            tableauWithSlacks[i] = rowWithSlack;
        }
        return tableauWithSlacks;
    }

    /**
     * Adds a columns to each row repesenting the division between the last
     * result and the pivot column
     *
     * @param tableau current tableau
     * @return tableau with division result column
     */
    public double[][] addDivisionResultColumn(double[][] tableau) {
        double[][] tableauWithDivisionResult = new double[tableau.length][];
        for (int i = 0; i < tableau.length; i++) {
            double[] rowWithDivisionResult = new double[tableau[i].length + 1];
            System.arraycopy(tableau[i], 0, rowWithDivisionResult, 0, tableau[i].length);
            tableauWithDivisionResult[i] = rowWithDivisionResult;
        }
        return tableauWithDivisionResult;
    }

    /**
     * Calculates the index in which the slack variable should be inserted
     *
     * @param row row array
     * @param slackNumber number of the slack variable to be inserted (1, 2,
     * 3...)
     * @return the slack index
     */
    private int calculateSlackIndex(double[] row, int slackNumber) {
        if (slackNumber == 0) {
            return 0;
        }
        int constraintIndex = SimplexUtils.getConstraintEqualityIndex(row);
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
        int variables = SimplexUtils.getNumberOfVariables(tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU]);
        for (int i = 0; i < variables; i++) {
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
        int variables = SimplexUtils.getNumberOfVariables(tableau);
        for (int i = 0; i < variables; i++) {
            double currentValue = tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i];
            if (currentValue < largestValue) {
                largestValue = currentValue;
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
        for (int i = 1; i < tableau.length; i++) {
            double[] currentRow = tableau[i];
            double divisionResult = tableau[i][SimplexUtils.getPivotDivisionColumnIndex(currentRow)];
            if (divisionResult <= smallestResult) {
                bestRowIndex = i;
                smallestResult = divisionResult;
            }
        }
        return bestRowIndex;
    }

    /**
     * Calculates the pivot division for every tableau row
     *
     * @param tableau
     * @param pivotColumnIndex
     */
    private void calculateDivisionForTableau(double[][] tableau, int pivotColumnIndex) {
        for (int i = 1; i < tableau.length; i++) {
            double[] currentRow = tableau[i];
            int divisionIndex = SimplexUtils.getPivotDivisionColumnIndex(currentRow);
            double divisionResult = calculateDivisionForRow(tableau, i, pivotColumnIndex);
            currentRow[divisionIndex] = divisionResult;
        }
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
        int index = SimplexUtils.getConstraintEqualityIndex(row);
        return row[index];
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
        double[] pivotRow = tableau[pivotRowIndex];
        double pivotValue = pivotRow[pivotColumnIndex];
        for (int i = 0; i < (SimplexUtils.getNumberOfVariables(pivotRow) + 1); i++) {
            double currentPivotRowValue = pivotRow[i];
            newTableau[pivotRowIndex][i] = currentPivotRowValue / pivotValue;
        }
        //
        // Now, calculates the value of remaining tableau variables 
        //
        // The rule is:
        //    - New tableau value = (Negative value in old tableau pivot column) * (value in new tableau pivot row) + (Old tableau value)
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < (SimplexUtils.getNumberOfVariables(tableau[i]) + 1); j++) {
                // Checks if variable hasn't been modified yet by the
                // operations above
                if (i != pivotRowIndex && j != pivotColumnIndex) {
                    BigDecimal negativeValueOldTableauPivotColumn = new BigDecimal(tableau[i][pivotColumnIndex] * -1).setScale(15, RoundingMode.HALF_EVEN);
                    BigDecimal valueNewTableauPivotRow = new BigDecimal(newTableau[pivotRowIndex][j]).setScale(15, RoundingMode.HALF_EVEN);
                    BigDecimal oldTableauValue = new BigDecimal(tableau[i][j]).setScale(15, RoundingMode.HALF_EVEN);
                    BigDecimal firstResult = negativeValueOldTableauPivotColumn.multiply(valueNewTableauPivotRow).setScale(15, RoundingMode.HALF_EVEN);
                    BigDecimal newValue = firstResult.add(oldTableauValue).setScale(15, RoundingMode.HALF_EVEN);
                    double doubleValue = newValue.doubleValue();
                    newTableau[i][j] = doubleValue;
                }
            }
        }
        return newTableau;
    }

    /**
     * Listener to simplex tablea iterations
     */
    public interface SimplexListener {

        /**
         * Handles the tableau after its iteration
         *
         * @param tableau
         * @param iteration
         * @param finalIteration 
         */
        public void handle(double[][] tableau, int iteration, boolean finalIteration);
    }
}
