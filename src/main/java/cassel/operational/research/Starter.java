/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import cassel.operational.research.simplex.SimplexSolver;

/**
 * Starts the operational research application
 *
 * @author gustavo
 */
public class Starter {

    public static void main(String[] args) {
//        new MainWindow().setVisible(true);
        SimplexSolver solver = new SimplexSolver();
        double[][] tableau = new double[][]{
            new double[]{-8.0, -10.0, -7.0,  0.0},
            new double[]{ 1.0,   3.0,  2.0, 10.0},
            new double[]{ 1.0,   5.0,  1.0,  8.0},
        };
        solver.solve(tableau);
    }

}
