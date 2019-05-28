/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

/**
 * Class to print simplex tableaus
 *
 * @author gustavo
 */
public class SimplexPrinter implements SimplexSolver.SimplexListener {

    @Override
    public void handle(double[][] tableau, int iteration) {
        printTableau(tableau, iteration);

    }

    /**
     * Prints the specified tableau
     *
     * @param tableau tableau to be printed
     * @param interation iteration number
     */
    private void printTableau(double[][] tableau, int iteration) {
        System.out.println("Iteração " + iteration + ":");
        printHeader(tableau);
        int totalRows = tableau.length;
        for (int i = 0; i < totalRows; i++) {
            printRowSeparator(tableau);
            System.out.print("|");
            printBaseVariableForRow(tableau, i);
            printTotalForRow(tableau[i]);
            printVariablesValues(tableau[i]);
            printLineNumber(i);
            printDivisionResultForRow(tableau[i]);
            System.out.println();
        }
        printRowSeparator(tableau);
        isMultipleSolutionSimplex(tableau);
    }

    /**
     * Prints the tableau header
     *
     * @param tableau
     */
    private void printHeader(double[][] tableau) {
        printRowSeparator(tableau);
        System.out.print("|");
        System.out.print("  Base");
        System.out.print("|");
        System.out.print("Vlr.Co");
        System.out.print("|");
        int variables = SimplexUtils.getNumberOfVariables(tableau);
        for (int i = 0; i < variables; i++) {
            System.out.print("    x" + i);
            System.out.print("|");
        }
        System.out.print(" Linha|");
        System.out.print("Divis.|");
        System.out.println("");
    }

    /**
     * Prints the row separator
     *
     * @param tableau
     */
    private void printRowSeparator(double[][] tableau) {
        int variables = SimplexUtils.getNumberOfVariables(tableau);
        //
        // Total columns = Base variable + variables + total value for row + division row + current line
        //
        int totalColumns = variables + 1 + 1 + 1 + 1;
        for (int i = 0; i < totalColumns; i++) {
            System.out.print("+");
            System.out.print(repeat("-", 6));
        }
        System.out.print("+");
        System.out.println();
    }

    /**
     * Prints the variables values for the specified row
     *
     * @param row
     */
    private void printVariablesValues(double[] row) {
        int variables = SimplexUtils.getNumberOfVariables(row);
        for (int i = 0; i < variables; i++) {
            double currentValue = row[i];
            printValueHandlingDecimals(currentValue);
            System.out.print("|");
        }
    }

    /**
     * Prints the specified value handling decimals if needed
     *
     * @param value
     */
    private void printValueHandlingDecimals(double value) {
        // If double value has no decimal
        String formatted;
        if (value % 1 == 0) {
            formatted = String.format("%6d", (int) value);
        } else {
            formatted = String.format("%6.2f", value);
        }
        System.out.print(formatted);
    }

    /**
     * Prints the total value for the specified row
     *
     * @param row
     */
    private void printTotalForRow(double[] row) {
        int totalIndex = SimplexUtils.getConstraintEqualityIndex(row);
        printValueHandlingDecimals(row[totalIndex]);
        System.out.print("|");
    }

    /**
     * Prints the current line number
     *
     * @param lineNumber
     */
    private void printLineNumber(int lineNumber) {
        System.out.print(String.format("%6d", lineNumber));
        System.out.print("|");
    }

    /**
     * Prints the division result for the specified row
     *
     * @param row
     */
    private void printDivisionResultForRow(double[] row) {
        int divisionIndex = SimplexUtils.getPivotDivisionColumnIndex(row);
        printValueHandlingDecimals(row[divisionIndex]);
        System.out.print("|");
    }

    /**
     * Exibe a variável básica/base para a linha especificada
     *
     * @param tableau
     * @param rowIndex
     */
    private void printBaseVariableForRow(double[][] tableau, int rowIndex) {
        int baseIndex = getBaseVariableIndexForRow(tableau, rowIndex);
        System.out.print("    x" + baseIndex);
        System.out.print("|");
    }
    
    /**
     * Returns {@code true} if this simplex tableau has multiple solutions
     * 
     * @param tableau 
     * @return {@code true} if this simplex tableau has multiple solutions
     */
    public boolean isMultipleSolutionSimplex(double[][] tableau) {
        int[] baseIndexes = getBaseVariablesIndexes(tableau);
        double[] targetFunctionRow = tableau[SimplexSolver.TARGET_FUNCTION_ROW_WITHIN_TABLEAU];
        int variablesNumber = SimplexUtils.getNumberOfVariables(targetFunctionRow);
        for (int i = 0; i < variablesNumber; i++) {
            if (!isBaseVariable(baseIndexes, i)) {
               double currentValue = targetFunctionRow[i];
               if (currentValue == 0) {
                   return true;
               }
            }
        }
        return false;
    }
    
    /**
     * Returns {@code true} if the {@code variableIndex} represents a base
     * variable.
     * 
     * <p> To be considered a base variable, {@code variableIndex} must be
     * present within {@code baseIndexes} array.
     * 
     * @param baseIndexes base variables indexes
     * @param variableIndex target variable to check if is a base variable
     * @return {@code true} if is a base variable
     */
    public boolean isBaseVariable(int[] baseIndexes, int variableIndex) {
        for (int i = 0; i < baseIndexes.length; i++) {
            if (baseIndexes[i] == variableIndex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an array with the indexes of every base variable within tableau
     * 
     * @param tableau
     * @return array with the indexes of every base variable within tableau
     */
    public int[] getBaseVariablesIndexes(double[][] tableau) {
        int[] baseIndexes = new int[tableau.length];
        int arrayIndex = 0;
        for (int i = 0; i < tableau.length; i++) {
            int indexForRow = getBaseVariableIndexForRow(tableau, i);
            baseIndexes[arrayIndex++] = indexForRow;
        }
        return baseIndexes;
    }
    
    /**
     * Retorna o índice da variável básica/base para a linha especificada.
     *
     * <p>
     * Uma variável básica é a única que possui coeficiente 1 positivo e só
     * existe na linha atual.
     *
     * @param tableau
     * @param rowIndex
     * @return int
     */
    public int getBaseVariableIndexForRow(double[][] tableau, int rowIndex) {
        int variables = SimplexUtils.getNumberOfVariables(tableau[rowIndex]);
        // Iterates over the variables of the current row
        for (int i = 0; i < variables; i++) {
            // If this variable possibly represents a base variable
            if (tableau[rowIndex][i] == 1) {
                // Guarantees this variable has zero value on the other rows
                boolean baseVariable = true;
                for (int j = 0; j < tableau.length; j++) {
                    if (j != rowIndex && tableau[j][i] != 0) {
                        baseVariable = false;
                    }
                }
                if (baseVariable) {
                    return i;
                }
            }
        }
        return -1;
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
