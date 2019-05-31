/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

/**
 * Utility methods for Simplex
 *
 * @author gustavo
 */
public class SimplexUtils {

    /**
     * Returns the index of the constraint for the specified row
     *
     * @param row row values
     * @return constraint equality value
     */
    public static int getConstraintEqualityIndex(double[] row) {
        return row.length - 2;
    }

    /**
     * Returns the number of variables in tableau
     *
     * @param tableau
     * @return number of variables
     */
    public static int getNumberOfVariables(double[][] tableau) {
        return getNumberOfVariables(tableau[0]);
    }

    /**
     * Returns the number of variables for row in tableau
     *
     * @param row
     * @return number of variables
     */
    public static int getNumberOfVariables(double[] row) {
        int constraintEqualityIndex = getConstraintEqualityIndex(row);
        return constraintEqualityIndex;
    }

    /**
     * Returns the index of the pivot division
     *
     * @param row row values
     * @return pivot division column index
     */
    public static int getPivotDivisionColumnIndex(double[] row) {
        return row.length - 1;
    }
    
    /**
     * Returns an array with the indexes of every base variable within tableau
     * 
     * @param tableau
     * @return array with the indexes of every base variable within tableau
     */
    public static int[] getBaseVariablesIndexes(double[][] tableau) {
        int[] baseIndexes = new int[tableau.length];
        int arrayIndex = 0;
        for (int i = 0; i < tableau.length; i++) {
            int indexForRow = SimplexUtils.getBaseVariableIndexForRow(tableau, i);
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
    public static int getBaseVariableIndexForRow(double[][] tableau, int rowIndex) {
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
    public static boolean isBaseVariable(int[] baseIndexes, int variableIndex) {
        for (int i = 0; i < baseIndexes.length; i++) {
            if (baseIndexes[i] == variableIndex) {
                return true;
            }
        }
        return false;
    }
    

}
