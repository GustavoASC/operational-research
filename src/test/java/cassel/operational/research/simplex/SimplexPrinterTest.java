/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SImplexPrinter class
 * @author gustavo
 */
public class SimplexPrinterTest {

    @Test
    public void testIsMultipleSolutionSimplex() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0, -5.0, -6.0, 0.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 1.0, 0.0},
        };
        assertFalse(printer.isMultipleSolutionSimplex(tableau));
    }

    @Test
    public void testIsNotMultipleSolutionSimplex() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0,  0.0, -6.0, 0.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 1.0, 0.0},
        };
        assertTrue(printer.isMultipleSolutionSimplex(tableau));
    }
    
    @Test
    public void testIsNotMultipleSolutionSimplexTwoZeroColums() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0,  0.0,  0.0, 0.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 1.0, 0.0},
        };
        assertTrue(printer.isMultipleSolutionSimplex(tableau));
    }
    
}
