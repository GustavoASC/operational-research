/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import java.util.Arrays;
import java.util.Objects;

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
     */
    public SimplexRow(double[] variables) {
        this(variables, EqualityType.LESS_OR_EQUAL, 0.0, 0.0);
    }

    /**
     * Creates a Simplex Row instance with general row information
     * 
     * @param variables
     * @param equalityType
     */
    public SimplexRow(double[] variables, EqualityType equalityType) {
        this(variables, equalityType, 0.0, 0.0);
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(this.variables);
        hash = 97 * hash + Objects.hashCode(this.equalityType);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.equalityValue) ^ (Double.doubleToLongBits(this.equalityValue) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.divisionResult) ^ (Double.doubleToLongBits(this.divisionResult) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimplexRow other = (SimplexRow) obj;
        if (Double.doubleToLongBits(this.equalityValue) != Double.doubleToLongBits(other.equalityValue)) {
            return false;
        }
        if (Double.doubleToLongBits(this.divisionResult) != Double.doubleToLongBits(other.divisionResult)) {
            return false;
        }
        if (!Arrays.equals(this.variables, other.variables)) {
            return false;
        }
        if (this.equalityType != other.equalityType) {
            return false;
        }
        return true;
    }
    
    

}
