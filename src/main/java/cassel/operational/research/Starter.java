/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import cassel.operational.research.simplex.EqualityType;
import cassel.operational.research.simplex.SimplexPrinter;
import cassel.operational.research.simplex.SimplexRow;
import cassel.operational.research.simplex.SimplexSolver;
import java.io.IOException;

/**
 * Starts the operational research application
 *
 * @author gustavo
 */
public class Starter {

    public static void main(String[] args) {
        SimplexRow[] tableau = new SimplexRow[]{
            new SimplexRow(new double[]{-5.0, -6.0, 0.0}, EqualityType.LESS_OR_EQUAL),
            new SimplexRow(new double[]{ 0.2,  0.3, 1.8}, EqualityType.LESS_OR_EQUAL),
            new SimplexRow(new double[]{ 0.2,  0.1, 1.2}, EqualityType.LESS_OR_EQUAL),
            new SimplexRow(new double[]{ 0.3,  0.3, 2.4}, EqualityType.LESS_OR_EQUAL),
        };
        if (args.length > 0) {
            String filename = args[0];
            try {
                tableau = new TableauFileReader().readFromFile(filename);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        new MainWindow().setVisible(true);
        SimplexSolver solver = new SimplexSolver().addSimplexListener(new SimplexPrinter());
        solver.maximize(tableau);
        System.out.println("SIMPLEX finalizado.");
    }

}
