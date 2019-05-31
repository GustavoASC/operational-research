/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes for Simplex Utilities methods
*/
public class SimplexUtilsTest {
    
    @Test
    public void testGetBaseVariableIndexForRow() {
        double[][] tableau = new double[][]{
            new double[]{ 1.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0, 0.0},
        };
        assertEquals(0, SimplexUtils.getBaseVariableIndexForRow(tableau, 0));
        assertEquals(3, SimplexUtils.getBaseVariableIndexForRow(tableau, 1));
        assertEquals(4, SimplexUtils.getBaseVariableIndexForRow(tableau, 2));
        assertEquals(5, SimplexUtils.getBaseVariableIndexForRow(tableau, 3));
    }

    @Test
    public void testGetBaseVariableIndexForRowInvalid() {
        double[][] tableau = new double[][]{
            new double[]{ 0.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0},
        };
        assertEquals(-1, SimplexUtils.getBaseVariableIndexForRow(tableau, 0));
    }
    
    @Test
    public void testGetBaseVariablesIndexes() {
        double[][] tableau = new double[][]{
            new double[]{ 1.0, -5.0, -6.0, 0.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 1.0, 0.0},
        };
        int[] expected = new int[] {0, 3, 4, 5};
        assertArrayEquals(expected, SimplexUtils.getBaseVariablesIndexes(tableau));
    }

    @Test
    public void testGetBaseVariablesIndexesWithInvalid() {
        double[][] tableau = new double[][]{
            new double[]{ 1.0, -5.0, -6.0, 0.0, 0.0, 0.0, 1.0},
            new double[]{ 0.0,  0.2,  0.3, 1.0, 0.0, 0.0, 0.0},
            new double[]{ 0.0,  0.2,  0.1, 0.0, 1.0, 0.0, 0.0},
            new double[]{ 0.0,  0.3,  0.2, 0.0, 0.0, 1.0, 0.0},
        };
        int[] expected = new int[] {0, 3, 4, -1};
        assertArrayEquals(expected, SimplexUtils.getBaseVariablesIndexes(tableau));
    }

    @Test
    public void testIsBaseVariable() {
        int[] baseIndexes = new int[] {0, 3, 4, 5};
        assertTrue(SimplexUtils.isBaseVariable(baseIndexes, 0));
        assertTrue(SimplexUtils.isBaseVariable(baseIndexes, 3));
        assertTrue(SimplexUtils.isBaseVariable(baseIndexes, 4));
        assertTrue(SimplexUtils.isBaseVariable(baseIndexes, 5));
        assertFalse(SimplexUtils.isBaseVariable(baseIndexes, 6));
    }
    
}
