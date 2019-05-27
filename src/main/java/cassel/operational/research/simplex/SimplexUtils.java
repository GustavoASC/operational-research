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

}
