/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

/**
 * Available equality types
 */
public enum EqualityType {

    /* Available types */
    EQUAL("="), LESS_OR_EQUAL("<="), GREATER_OR_EQUAL(">=");
    /* Symbol associated to each type */
    private final String symbol;

    /**
     * Creates a new equality type with the associated symbol
     *
     * @param symbol
     */
    private EqualityType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the apropriate type from it's symbol
     *
     * @param symbol
     * @return EqualityType
     */
    public static EqualityType fromSymbol(String symbol) {
        for (EqualityType type : EqualityType.values()) {
            if (symbol.equals(type.symbol)) {
                return type;
            }
        }
        return null;
    }

}
