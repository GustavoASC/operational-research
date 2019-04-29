/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

/**
 * Bean representing an operational research variable
 *
 * @author gustavo
 */
public class Variable {

    /* Variable name */
    private final String name;
    /* Variable description */
    private final String description;

    /**
     * Creates a new bean representing an operational research variable, without
     * description
     *
     * @param name variable name
     */
    public Variable(String name) {
        this(name, "");
    }

    /**
     * Creates a new bean representing an operational research variable
     *
     * @param name variable name
     * @param description variable description
     */
    public Variable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the variable name
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the variable description
     * 
     * @return String
     */
    public String getDescription() {
        return description;
    }

}
