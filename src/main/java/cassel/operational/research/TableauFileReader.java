/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import cassel.operational.research.simplex.SimplexRow;
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
    public SimplexRow[] readFromFile(String filename) throws IOException {
        List<String[]> fileLines = readFileIntoList(filename);
        SimplexRow[] tableau = new SimplexRow[fileLines.size()];
        for (int i = 0; i < tableau.length; i++) {
            String[] currentLine = fileLines.get(i);
            double[] rowVariables = new double[currentLine.length - 1];
            int currentVariableIndex = 0;
            for (int j = 0; j < currentLine.length; j++) {
                if (j == currentLine.length - 2) {
                    
                } else {
                    rowVariables[currentVariableIndex] = Double.parseDouble(currentLine[j]);
                    currentVariableIndex++;
                }
            }
            tableau[i] = new SimplexRow(rowVariables);
        }
        return tableau;
    }
    
    /**
     * Reads the specified file into a list
     * 
     * @param filename file to be read
     * @return list with file lines
     * @throws IOException if an IO error occurs
     */
    private List<String[]> readFileIntoList(String filename) throws IOException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        List<String[]> fileLines = new LinkedList<>();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().replaceAll(" +", " ").split(" ");
            fileLines.add(line);
        }
        return fileLines;
    }

}
