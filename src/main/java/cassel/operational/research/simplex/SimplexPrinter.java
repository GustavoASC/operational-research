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
    public void handle(double[][] tableau, int iteration, boolean finalIteration, boolean infiniteSolution) {
        printTableau(tableau, iteration, finalIteration, infiniteSolution);
    }

    /**
     * Prints the specified tableau
     *
     * @param tableau tableau to be printed
     * @param interation iteration number
     * @param finalIteration boolean indicating if the current iteration is the
     * @param infiniteSolution boolean indicating if the simplex if infinite
     * last one
     */
    private void printTableau(double[][] tableau, int iteration, boolean finalIteration, boolean infiniteSolution) {
        System.out.println("Iteracao " + iteration + ":");
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
        if (finalIteration) {
            if (infiniteSolution) {
                    System.out.println("SIMPLEX possui solucao infinita.");
            } else {
                if (isMultipleSolutionSimplex(tableau)) {
                    System.out.println("SIMPLEX possui multiplas solucoes.");
                }
            }
        }
    }

    /**
     * Prints the tableau header
     *
     * @param tableau
     */
    private void printHeader(double[][] tableau) {
        printRowSeparator(tableau);
        System.out.print("|");
        System.out.print("     Base");
        System.out.print("|");
        System.out.print("Valor.Cor");
        System.out.print("|");
        int variables = SimplexUtils.getNumberOfVariables(tableau);
        for (int i = 0; i < variables; i++) {
            printVariableName(i);
        }
        System.out.print("    Linha|");
        System.out.print("   Divis.|");
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
            System.out.print(repeat("-", 9));
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
            formatted = String.format("%9d", (int) value);
        } else {
            formatted = String.format("%9.2f", value);
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
        System.out.print(String.format("%9d", lineNumber));
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
        int baseIndex = SimplexUtils.getBaseVariableIndexForRow(tableau, rowIndex);
        printVariableName(baseIndex);
    }

    /**
     * Prints the variable name from it's index
     * 
     * @param variableIndex variable index
     */
    private void printVariableName(int variableIndex) {
        String variableName = "x" + Integer.toString(variableIndex);
        String spaces = repeat(" ", 9 - variableName.length());
        System.out.print(spaces + variableName);
        System.out.print("|");
    }
    
    /**
     * Returns {@code true} if this simplex tableau has multiple solutions.
     * 
     * <p> To be considered a Multiple Solution Simplex, the objective function
     * row needs to have a non-basic variable with coefficient zero at final
     * iteration.
     * 
     * @param tableau 
     * @return {@code true} if this simplex tableau has multiple solutions
     */
    public boolean isMultipleSolutionSimplex(double[][] tableau) {
        int[] baseIndexes = SimplexUtils.getBaseVariablesIndexes(tableau);
        double[] targetFunctionRow = tableau[SimplexSolver.TARGET_FUNCTION_ROW_WITHIN_TABLEAU];
        int variablesNumber = SimplexUtils.getNumberOfVariables(targetFunctionRow);
        for (int i = 0; i < variablesNumber; i++) {
            if (!SimplexUtils.isBaseVariable(baseIndexes, i)) {
               double currentValue = targetFunctionRow[i];
               if (currentValue == 0) {
                   return true;
               }
            }
        }
        return false;
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
