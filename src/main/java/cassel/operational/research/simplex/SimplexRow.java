/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

/**
 * Class representing a Simplex tableau row
 *
 * @author gustavo
 */
public class SimplexRow {

    /* Row variables */
    private final double[] variables;
    /* Equality type */
    private final EqualityType equalityType;
    /* Row equality value */
    private final double equalityValue;
    /* Row division result */
    private final double divisionResult;

    /**
     * Creates a Simplex Row instance with general row information
     * 
     * @param variables
     * @param equalityType
     * @param equalityValue
     * @param divisionResult 
     */
    public SimplexRow(double[] variables, EqualityType equalityType, double equalityValue, double divisionResult) {
        this.variables = variables;
        this.equalityType = equalityType;
        this.equalityValue = equalityValue;
        this.divisionResult = divisionResult;
    }

    /**
     * Return row variables
     *
     * @return double[]
     */
    public double[] getVariables() {
        return variables;
    }

    /**
     * Return equality type
     *
     * @return EqualityType
     */
    public EqualityType getEqualityType() {
        return equalityType;
    }

    /**
     * Return equality value
     *
     * @return double
     */
    public double getEqualityValue() {
        return equalityValue;
    }

    /**
     * Return division result
     *
     * @return double
     */
    public double getDivisionResult() {
        return divisionResult;
    }

    /**
     * Available equality types
     */
    public enum EqualityType {
        EQUAL, LESS_THAN, GREATER_THAN
    }

}
