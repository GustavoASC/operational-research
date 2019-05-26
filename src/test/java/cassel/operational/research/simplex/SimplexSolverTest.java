/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SimplexSolverTest {

//    @Test
//    public void testSolve() {
//        SimplexSolver solver = new SimplexSolver();
//        double[][] tableau = new double[0][0];
//        double[][] expected = new double[0][0];
//        assertArrayEquals(expected, solver.solve(tableau));
//    }

    @Test
    public void testAddSlackVariablesSingleSmallRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{1.5, 2.0},
        };
        double[][] expected = new double[][]{
            new double[]{1.0, 1.5, 2.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesSingleRowOneElement() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 0.0, 0.0},
        };
        double[][] expected = new double[][]{
            new double[]{1.0, 2.0, 0.0, 0.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesSingleRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 0.0},
        };
        double[][] expected = new double[][]{
            new double[]{1.0, 2.0, 1.0, 1.0, 14.0, 0.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesTwoRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 0.0},
            new double[]{4.0, 2.0, 3.0, 28.0, 0.0},
        };
        double[][] expected = new double[][]{
            new double[]{1.0, 2.0, 1.0, 1.0, 0.0, 14.0, 0.0},
            new double[]{0.0, 4.0, 2.0, 3.0, 1.0, 28.0, 0.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesThreeRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 0.0},
            new double[]{4.0, 2.0, 3.0, 28.0, 0.0},
            new double[]{2.0, 5.0, 5.0, 30.0, 0.0},
        };
        double[][] expected = new double[][]{
            new double[]{1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 14.0, 0.0},
            new double[]{0.0, 4.0, 2.0, 3.0, 1.0, 0.0, 28.0, 0.0},
            new double[]{0.0, 2.0, 5.0, 5.0, 0.0, 1.0, 30.0, 0.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddDivisionResultColumn() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
            new double[]{4.0, 2.0, 3.0, 28.0},
            new double[]{2.0, 5.0, 5.0, 30.0},
        };
        double[][] expected = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 0.0},
            new double[]{4.0, 2.0, 3.0, 28.0, 0.0},
            new double[]{2.0, 5.0, 5.0, 30.0, 0.0},
        };
        assertArrayEquals(expected, solver.addDivisionResultColumn(tableau));
    }

    @Test
    public void testInvertTargetFunctionSignal() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
        };
        double[][] expected = new double[][]{
            new double[]{-2.0, -1.0, -1.0, -14.0},
        };
        assertArrayEquals(expected, solver.invertTargetFunctionSignal(tableau));
    }

    @Test
    public void testIsOptimalFourVariables() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
        };
        assertEquals(true, solver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimalOneVariable() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 0.0},
        };
        assertEquals(true, solver.isOptimal(tableau));
    }

    @Test
    public void testIsOptimalOneVariableTwoRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0},
            new double[]{-5.0},
        };
        assertEquals(true, solver.isOptimal(tableau));
    }

    @Test
    public void testIsNotOptimalFourVariables() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, -1.0, 1.0, 14.0, 0.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testIsNotOptimalOneVariable() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0, 0.0, 0.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testIsNotOptimalOneVariableTwoRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0, 0.0, 0.0},
            new double[]{3.0, 0.0, 0.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testFindPivotColumnIndex() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0, -7.0, -1.5, 0.0},
        };
        assertEquals(1, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnWithTotalResult() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{1.0, -0.98, 0.0, 19.98, 0.0, 0.0, 36.0},
        };
        assertEquals(1, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexAllPositive() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 7.0, 1.5},
        };
        assertEquals(0, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexThirdRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 7.0, -25.5},
        };
        assertEquals(0, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexFirstSingleRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 0.0, 0.0},
        };
        assertEquals(0, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testCalculateDivisionForSingleRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 8.0, 0.0},
        };
        assertEquals(4, solver.calculateDivisionForRow(tableau, 0, 0));
    }

    @Test
    public void testCalculateDivisionForTwoRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 5.0, 16.0, 0.0},
            new double[]{4.0, 20.0, 8.0, 0.0},
        };
        assertEquals(2, solver.calculateDivisionForRow(tableau, 1, 0));
    }

    @Test
    public void testCalculateDivisionForTwoRowFourColumns() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{16.0, 5.0, 2.0, 30.0, 0.0},
            new double[]{8.0, 20.0, 4.0, 90.0, 0.0},
        };
        assertEquals(15, solver.calculateDivisionForRow(tableau, 0, 2));
    }

    @Test
    public void testFindPivotRowIndex() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 5.0, 30.0, 16.0},
            new double[]{4.0, 20.0, 90.0, 8.0},
        };
        assertEquals(1, solver.findPivotRowIndex(tableau, 0));
    }

    @Test
    public void testFindPivotRowIndexInvertedRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{4.0, 20.0, 90.0, 8.0},
            new double[]{2.0, 5.0, 30.0, 16.0},
        };
        assertEquals(1, solver.findPivotRowIndex(tableau, 0));
    }

    @Test
    public void testFindPivotRowIndexThreeRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{8.0, 20.0, 4.0, 90.0, 0.0},
            new double[]{30.0, 5.0, 70.0, 30.0, 5.0},
            new double[]{16.0, 5.0, 2.0, 30.0, 9.0},
        };
        assertEquals(1, solver.findPivotRowIndex(tableau, 2));
    }
    
    @Test
    public void testCreateTableauFromPivot() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{ 1.0,   3.0,  2.0, 1.0,  0.0, 0.0, 10.0},
            new double[]{ 1.0,   5.0,  1.0, 0.0,  1.0, 0.0,  8.0},
            new double[]{-8.0, -10.0, -7.0, 0.0,  0.0, 1.0,  0.0},
        };
        double[][] expected = new double[][]{
            new double[]{ 0.4,   0.0,  1.4, 1.0, -0.6, 0.0,  0.0},
            new double[]{ 0.2,   1.0,  0.2, 0.0,  0.2, 0.0,  0.0},
            new double[]{-6.0,   0.0, -5.0, 0.0,  2.0, 1.0,  0.0},
        };
        assertArrayEquals(expected, solver.createTableauFromPivot(tableau, 1, 1));
    }
    
    @Test
    public void testGetBaseVariableIndexForRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{ 1.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0, 0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0, 0.0},
        };
        assertEquals(0, solver.getBaseVariableIndexForRow(tableau, 0));
        assertEquals(3, solver.getBaseVariableIndexForRow(tableau, 1));
        assertEquals(4, solver.getBaseVariableIndexForRow(tableau, 2));
        assertEquals(5, solver.getBaseVariableIndexForRow(tableau, 3));
    }

    @Test
    public void testGetBaseVariableIndexForRowInvalid() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{ 0.0,  -5.0, -6.0, 0.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.3, 1.0,  0.0, 0.0,  0.0},
            new double[]{ 0.0,   0.2,  0.1, 0.0,  1.0, 0.0,  0.0},
            new double[]{ 0.0,   0.3,  0.2, 0.0,  0.0, 1.0,  0.0},
        };
        assertEquals(-1, solver.getBaseVariableIndexForRow(tableau, 0));
    }

}
