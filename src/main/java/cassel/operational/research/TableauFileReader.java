/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to read/import a tableau from text file
 *
 * @author gustavo
 */
public class TableauFileReader {

    /**
     * Reads the tableau from the specified file
     *
     * @param filename file to be read
     * @return tableau as a double array
     * @throws IOException if an IO error occurs
     */
    public double[][] readFromFile(String filename) throws IOException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        List<String[]> fileLines = new LinkedList<>();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().split(" ");
            fileLines.add(line);
        }
        double[][] tableau = new double[fileLines.size()][];
        for (int i = 0; i < fileLines.size(); i++) {
            String[] currentLine = fileLines.get(i);
            tableau[i] = new double[currentLine.length];
            for (int j = 0; j < currentLine.length; j++) {
                tableau[i][j] = Double.parseDouble(currentLine[j]);
            }
        }
        return tableau;
    }

}
