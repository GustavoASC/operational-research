/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research.simplex;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SimplexSolverTest {

    @Test
    public void testSolve() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[0][0];
        double[][] expected = new double[0][0];
        assertArrayEquals(expected, solver.solve(tableau));
    }

    @Test
    public void testAddSlackVariablesSingleSmallRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0},
        };
        double[][] expected = new double[][]{
            new double[]{2.0, 1.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesSingleRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
        };
        double[][] expected = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 1.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesTwoRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
            new double[]{4.0, 2.0, 3.0, 28.0},
        };
        double[][] expected = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 1.0, 0.0},
            new double[]{4.0, 2.0, 3.0, 28.0, 0.0, 1.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
    }

    @Test
    public void testAddSlackVariablesThreeRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0},
            new double[]{4.0, 2.0, 3.0, 28.0},
            new double[]{2.0, 5.0, 5.0, 30.0},
        };
        double[][] expected = new double[][]{
            new double[]{2.0, 1.0, 1.0, 14.0, 1.0, 0.0, 0.0},
            new double[]{4.0, 2.0, 3.0, 28.0, 0.0, 1.0, 0.0},
            new double[]{2.0, 5.0, 5.0, 30.0, 0.0, 0.0, 1.0},
        };
        assertArrayEquals(expected, solver.addSlackVariables(tableau));
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
            new double[]{2.0},
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
            new double[]{2.0, -1.0, 1.0, 14.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testIsNotOptimalOneVariable() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testIsNotOptimalOneVariableTwoRows() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0},
            new double[]{3.0},
        };
        assertEquals(false, solver.isOptimal(tableau));
    }

    @Test
    public void testFindPivotColumnIndex() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-2.0, -7.0, -1.5},
        };
        assertEquals(1, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexAllPositive() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 7.0, 1.5},
        };
        assertEquals(1, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexThirdRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0, 7.0, -25.5},
        };
        assertEquals(2, solver.findPivotColumnIndex(tableau));
    }

    @Test
    public void testFindPivotColumnIndexFirstSingleRow() {
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{2.0},
        };
        assertEquals(0, solver.findPivotColumnIndex(tableau));
    }

}
