/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import java.util.LinkedList;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Class to render operational research
 *
 * @author gustavo
 */
public class OperationalResearchRenderer {

    /* Target/objective function */
    private String objectiveFunction;
    /* Type of the Target/objective function */
    private FunctionType objectFunctionType;
    /* Variables */
    private final List<Variable> variables;

    public OperationalResearchRenderer() {
        this.variables = new LinkedList<>();
        this.objectiveFunction = "";
        this.objectFunctionType = FunctionType.MIN;
    }

    /**
     * Sets the target function equation
     * 
     * @param objectiveFunction target function equation
     */
    public void setObjectiveFunction(String objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    /**
     * Sets the target function type (MIN or MAX)
     * 
     * @param objectFunctionType target type
     */
    public void setObjectFunctionType(FunctionType objectFunctionType) {
        this.objectFunctionType = objectFunctionType;
    }
    
    /**
     * Adds a variable to the list
     *
     * @param v variable to be added
     */
    public void addVariable(Variable v) {
        this.variables.add(v);
    }
    
    /**
     * Renders a chart for the specified operational research target function
     * 
     * @return rendered chart
     */
    public JFreeChart render() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15, "Schools", "1970");
        dataset.addValue(30, "Schools", "1980");
        return ChartFactory.createLineChart("Sample Operational Research", "my categoryAxisLabel", "my valueAxisLabel", dataset);
    }

    /**
     * Available function types
     */
    public enum FunctionType {
        MAX,
        MIN
    }
}
