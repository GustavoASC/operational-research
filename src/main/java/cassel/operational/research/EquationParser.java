/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import org.mariuszgromada.math.mxparser.Expression;

/**
 * Class to parse mathematic equations
 */
public class EquationParser {

    /**
     * Parses the specified equation
     *
     * @param equation string representing the equation to be parsed
     * @return equation result
     */
    public double parse(String equation) {
        Expression e = new Expression(equation);
        return e.calculate();
    }

}
