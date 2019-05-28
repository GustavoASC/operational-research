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
    public void testGetBaseVariableIndexForRow() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0, 0.0},
        };
        assertEquals(0, printer.getBaseVariableIndexForRow(tableau, 0));
        assertEquals(3, printer.getBaseVariableIndexForRow(tableau, 1));
        assertEquals(4, printer.getBaseVariableIndexForRow(tableau, 2));
        assertEquals(5, printer.getBaseVariableIndexForRow(tableau, 3));
    }

    @Test
    public void testGetBaseVariableIndexForRowInvalid() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 0.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0},
        };
        assertEquals(-1, printer.getBaseVariableIndexForRow(tableau, 0));
    }

    @Test
    public void testGetBaseVariablesIndexes() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0, -5.0, -6.0, 0.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 1.0, 0.0},
        };
        int[] expected = new int[] {0, 3, 4, 5};
        assertArrayEquals(expected, printer.getBaseVariablesIndexes(tableau));
    }

    @Test
    public void testGetBaseVariablesIndexesWithInvalid() {
        SimplexPrinter printer = new SimplexPrinter();
        double[][] tableau = new double[][]{
            new double[]{ 1.0, -5.0, -6.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 0.0},
        };
        int[] expected = new int[] {0, 3, 4, -1};
        assertArrayEquals(expected, printer.getBaseVariablesIndexes(tableau));
    }

    @Test
    public void testIsBaseVariable() {
        SimplexPrinter printer = new SimplexPrinter();
        int[] baseIndexes = new int[] {0, 3, 4, 5};
        assertTrue(printer.isBaseVariable(baseIndexes, 0));
        assertTrue(printer.isBaseVariable(baseIndexes, 3));
        assertTrue(printer.isBaseVariable(baseIndexes, 4));
        assertTrue(printer.isBaseVariable(baseIndexes, 5));
        assertFalse(printer.isBaseVariable(baseIndexes, 6));
    }

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
