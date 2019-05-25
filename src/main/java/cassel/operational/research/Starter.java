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
            new double[]{-5.0, -6.0, 0.0},
            new double[]{ 0.2,  0.3, 1.8},
            new double[]{ 0.2,  0.1, 1.2},
            new double[]{ 0.3,  0.3, 2.4},
        };
        solver.solve(tableau);
    }

}
