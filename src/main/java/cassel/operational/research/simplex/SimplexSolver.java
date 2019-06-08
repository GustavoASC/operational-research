/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to maximize Linear Problems with Simplex algorithm.
 *
 * @author gustavo
 */
public class SimplexSolver {

    /**
     * Row where target function variables are found within tableau matrix,
     * starting from zero index
     */
    public static final int TARGET_FUNCTION_ROW_WITHIN_TABLEAU = 0;
    /** Value to multiply the Big M for */
    public static final int BIG_M_MULTIPLIER = 5;
    /** Value indicating that the row does not contain any artificial variable */
    public static final int NO_ARTIFICIAL_VARIABLE = -1;
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
     * Solves the specified tableau with minimization
     * 
     * @param tableau 
     */
    public void minimize(double[][] tableau) {

    }
    
    /**
     * Solves the specified tableau with maximization
     *
     * @param tableau
     */
    public void maximize(SimplexRow[] tableau) {
        SimplexRow[] normalizedRows;
        normalizedRows = addDivisionResultColumnToRow(tableau);
        normalizedRows = normalizeEquations(normalizedRows);
        double[][] result;
        int[] artificialIndexes = new int[tableau.length];
        result = convertRowsToMatrix(normalizedRows);
        result = addArtificialVariables(result, artificialIndexes);
        if (containsArtificialVariable(artificialIndexes)) {
            result = applyBigMMethod(result, artificialIndexes);
        }
        doMaximize(result);
    }
    
    /**
     * Returns {@code true} if the artificial indexes array contains any 
     * artificial variable index
     * 
     * @param artificialIndexes
     * @return 
     */
    private boolean containsArtificialVariable(int[] artificialIndexes) {
        for (int i = 0; i < artificialIndexes.length; i++) {
            int currentIndex = artificialIndexes[i];
            if (currentIndex != NO_ARTIFICIAL_VARIABLE) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Converts the specified rows to a double matrix
     * 
     * @param rows
     * @return tableu matrix
     */
    protected double[][] convertRowsToMatrix(SimplexRow[] rows) {
        // Initializes the tableau matrix
        double[][] tableau = new double[rows.length][];
        // Inserts the rows values into the matrix
        for (int i = 0; i < rows.length; i++) {
            // Acquires the row reference
            double[] originalRow = rows[i].getVariables();
            // Creates a new array where values will be inserted within matrix
            double[] doubleRow = new double[originalRow.length];
            // Clones the values of SimplexRow to the new structure
            for (int j = 0; j < originalRow.length; j++) {
                doubleRow[j] = originalRow[j];
            }
            // Inserts the new array into the matrix
            tableau[i] = doubleRow;
        }
        return tableau;
    }
    
    /**
     * Maximizes the specified tableau which contains only equations and not 
     * inequations
     * 
     * @param tableau 
     */
    private void doMaximize(double[][] tableau) {
        int iteration = 0;
        while (!isOptimal(tableau)) {
            iteration++;
            int pivotColumn = findPivotColumnIndex(tableau);
            calculateDivisionForTableau(tableau, pivotColumn);
            int pivotRow = findPivotRowIndex(tableau, pivotColumn);
            if (pivotRow == -1) {
                fireTableauIterationSolved(tableau, iteration, true, true);
                return;
            }
            fireTableauIterationSolved(tableau, iteration, false, false);
            tableau = createTableauFromPivot(tableau, pivotRow, pivotColumn);
        }
        iteration++;
        fireTableauIterationSolved(tableau, iteration, true, false);
    }
    
    /**
     * Normalizes the specified tableau transforming inequation constraints into
     * equations
     * 
     * @param tableau
     * @return normalized tableau
     */
    protected SimplexRow[] normalizeEquations(SimplexRow[] tableau) {
        SimplexRow[] normalized = new SimplexRow[tableau.length];
        int totalSlackVariables = tableau.length;
        for (int i = 0; i < tableau.length; i++) {
            // Acquires the current row object
            SimplexRow row = tableau[i];
            // References the current tableau row variables
            double[] currentOriginalTableauRow = row.getVariables();
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
            // If the current row represents a less or equal inequation
            if (row.getEqualityType() != EqualityType.GREATER_OR_EQUAL ) {
                rowWithSlack[slackVariableIndex] = 1.0;
            } else {
                rowWithSlack[slackVariableIndex] = -1.0;
            }
            // Reinserts the constraint equality value
            int constraintEqualityIndex = SimplexUtils.getConstraintEqualityIndex(rowWithSlack);
            double constraintEqualityValue = getConstraintEqualityValue(currentOriginalTableauRow);
            rowWithSlack[constraintEqualityIndex] = constraintEqualityValue;
            // Adds the new row to the new tableau matrix
            normalized[i] = new SimplexRow(rowWithSlack, EqualityType.EQUAL, row.getEqualityValue(), row.getDivisionResult());
        }
        return normalized;
    }
    
    /**
     * Fires a tableau iteration solved listener
     * 
     * @param tableau
     * @param iteration 
     * @param finalIteration
     * @param infiniteSolution
     */
    private void fireTableauIterationSolved(double[][] tableau, int iteration, boolean finalIteration, boolean infiniteSolution) {
        listeners.forEach((l) -> {
            l.handle(tableau, iteration, finalIteration, infiniteSolution);
        });
    }

    /**
     * Adds artificial variables for the rows thar does not contain any basic 
     * variable
     *
     * @param tableau without without artificial variablesslack variables
     * @return tableau with artificial variables added
     */
    protected double[][] addArtificialVariables(double[][] tableau) {
        return addArtificialVariables(tableau, new int[tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU].length]);
    }

    /**
     * Adds artificial variables for the rows thar does not contain any basic 
     * variable
     *
     * @param tableau without without artificial variablesslack variables
     * @param artificialIndexes in-out array used to retain artificial indexes.
     * This array will be populated inside this method and contains the column
     * index where artificial variable has been added
     * 
     * @return tableau with artificial variables added
     */
    protected double[][] addArtificialVariables(double[][] tableau, int[] artificialIndexes) {
        // Calculates the new total number of slack variables
        int totalNewVariables = 0;
        for (int i = 0; i < tableau.length; i++) {
            int baseIndex = SimplexUtils.getBaseVariableIndexForRow(tableau, i);
            if (baseIndex == -1) {
                totalNewVariables++;
            }
        }
        double[][] tableauWithSlacks = new double[tableau.length][];
        int currentSlackIndex = 0;
        // Adds slack variables for each row
        for (int i = 0; i < tableau.length; i++) {
            // References the current tableau row
            double[] currentOriginalTableauRow = tableau[i];
            // Creates a new array, with original row size + total slack variables to be added
            double[] rowWithSlack = new double[currentOriginalTableauRow.length + totalNewVariables];
            // Clones the original tableau variables to the new array
            // For now the value of slack variables are ignored and keep zero
            for (int j = 0; j < currentOriginalTableauRow.length; j++) {
                // The constraint equality is not copied yet because it will 
                // be copied after the slack variable is inserted
                if (j != SimplexUtils.getConstraintEqualityIndex(currentOriginalTableauRow)) {
                    rowWithSlack[j] = currentOriginalTableauRow[j];
                }
            }
            // Checks if the current row already have base variable
            int baseIndex = SimplexUtils.getBaseVariableIndexForRow(tableau, i);
            if (baseIndex == -1) {
                // Specifies the value of the slack variable for the current row
                int slackVariableIndex = calculateArtificialIndex(currentOriginalTableauRow, currentSlackIndex);
                rowWithSlack[slackVariableIndex] = 1.0;
                artificialIndexes[i] = slackVariableIndex;
                currentSlackIndex++;
            } else {
                // This row has no artificial index
                artificialIndexes[i] = NO_ARTIFICIAL_VARIABLE;
            }
            // Reinserts the constraint equality value
            rowWithSlack[SimplexUtils.getConstraintEqualityIndex(rowWithSlack)] = getConstraintEqualityValue(currentOriginalTableauRow);
            // Adds the new row to the new tableau matrix
            tableauWithSlacks[i] = rowWithSlack;
        }
        return tableauWithSlacks;
    }
    
    /**
     * Applies the Big M Method for the specified tableau which should already
     * contain artificial variables
     * 
     * @param tableau tableau with artificial variables, to apply the Big M
     * method
     * @param artificialIndexes indexes for each row representing the column
     * index where artificial variable is placed
     * @return tableau with the Big M method applied
     */
    protected double[][] applyBigMMethod(double[][] tableau, int[] artificialIndexes) {
        double bigValue = generateBigMValue(tableau);
        double[] originalFunction = tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU];
        double[] newObjectiveFunction = new double[originalFunction.length];
        double[] rowsSum              = new double[originalFunction.length];
        // Modifies the original function putting the Big M value at columns
        // where artificial variables have been added in other rows
        for (int j = 0; j < originalFunction.length; j++) {
            if (isArtificialIndex(artificialIndexes, j)) {
                originalFunction[j] = bigValue;
            }
        }
        // Starts from index 1 because we want to ignore the target function
        for (int i = 1; i < tableau.length; i++) {
            if (artificialIndexes[i] != NO_ARTIFICIAL_VARIABLE) {
                double[] row = tableau[i];
                // Does not use SimplexUtils.getNumberOfVariables(row) method 
                // because we want to sum every column, even the constraint
                // equality value, and not only variables
                for (int j = 0; j < row.length; j++) {
                    double currentRowValue = row[j];
                    rowsSum[j] += currentRowValue;
                }
            }
        }
        // Iterates over the new objective function row multiplying by the
        // Big M number
        for (int j = 0; j < newObjectiveFunction.length; j++) {
            double currentRowSum = rowsSum[j];
            double originalFunctionValue = originalFunction[j];
            double sumMultipliedByBigBalue = currentRowSum * (bigValue * -1);
            newObjectiveFunction[j] = sumMultipliedByBigBalue + originalFunctionValue;
        }
        // Switches the old tableau objective function within tableau
        tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU] = newObjectiveFunction;
        return tableau;
    }
    
    /**
     * Returns {@code true} if the specified {@code targetIndex} represents an
     * artificial variable within {@code artificialIndexes}
     * 
     * @param artificialIndexes
     * @param targetIndex
     * @return 
     */
    private boolean isArtificialIndex(int[] artificialIndexes, int targetIndex) {
        for (int i = 0; i < artificialIndexes.length; i++) {
            int currentIndex = artificialIndexes[i];
            if (currentIndex == targetIndex) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates the Big M value for the specified tableau.
     * 
     * <p> The rule used is find the biggest value within the objective function
     * row and multiply it by 
     * 
     * @param tableau tableau to generate the Big M value
     * @return Big M value
     */
    private double generateBigMValue(double[][] tableau) {
        double biggestValue = 0.0;
        int variables = SimplexUtils.getNumberOfVariables(tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU]);
        for (int i = 0; i < variables; i++) {
            if (tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i] > biggestValue) {
                biggestValue = tableau[TARGET_FUNCTION_ROW_WITHIN_TABLEAU][i];
            }
        }
        double value = biggestValue * BIG_M_MULTIPLIER;
        return value;
    }
    
    /**
     * Adds a columns to each row repesenting the division between the last
     * result and the pivot column
     *
     * @param tableau current tableau
     * @return tableau with division result column
     */
    protected double[][] addDivisionResultColumn(double[][] tableau) {
        double[][] tableauWithDivisionResult = new double[tableau.length][];
        for (int i = 0; i < tableau.length; i++) {
            double[] rowWithDivisionResult = new double[tableau[i].length + 1];
            System.arraycopy(tableau[i], 0, rowWithDivisionResult, 0, tableau[i].length);
            tableauWithDivisionResult[i] = rowWithDivisionResult;
        }
        return tableauWithDivisionResult;
    }

    /**
     * Adds a columns to each row repesenting the division between the last
     * result and the pivot column
     *
     * @param tableau current tableau
     * @return tableau with division result column
     */
    protected SimplexRow[] addDivisionResultColumnToRow(SimplexRow[] tableau) {
        SimplexRow[] tableauWithDivisionResult = new SimplexRow[tableau.length];
        for (int i = 0; i < tableau.length; i++) {
            SimplexRow currentRow = tableau[i];
            double[] currentRowVariables = currentRow.getVariables();
            double[] rowWithDivisionResult = new double[currentRowVariables.length + 1];
            System.arraycopy(currentRowVariables, 0, rowWithDivisionResult, 0, currentRowVariables.length);
            tableauWithDivisionResult[i] = new SimplexRow(rowWithDivisionResult, currentRow.getEqualityType(), currentRow.getEqualityValue(), currentRow.getDivisionResult());
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
     * Calculates the index in which the slack variable should be inserted
     *
     * @param row row array
     * @param slackNumber number of the slack variable to be inserted (1, 2,
     * 3...)
     * @return the slack index
     */
    private int calculateArtificialIndex(double[] row, int slackNumber) {
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
    protected double[][] invertTargetFunctionSignal(double[][] tableau) {
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
    protected boolean isOptimal(double[][] tableau) {
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
    protected int findPivotColumnIndex(double[][] tableau) {
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
    protected int findPivotRowIndex(double[][] tableau, int pivotColumnIndex) {
        int bestRowIndex = -1;
        double smallestResult = Double.MAX_VALUE;
        for (int i = 1; i < tableau.length; i++) {
            double[] currentRow = tableau[i];
            double divisionResult = tableau[i][SimplexUtils.getPivotDivisionColumnIndex(currentRow)];
            if (divisionResult > 0 && divisionResult <= smallestResult) {
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
    protected double calculateDivisionForRow(double[][] tableau, int rowIndex, int columnIndex) {
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
    protected double[][] createTableauFromPivot(double[][] tableau, int pivotRowIndex, int pivotColumnIndex) {
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
         * @param infiniteSolution
         */
        public void handle(double[][] tableau, int iteration, boolean finalIteration, boolean infiniteSolution);
    }
}
