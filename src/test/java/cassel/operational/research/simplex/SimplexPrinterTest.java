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
    
}
