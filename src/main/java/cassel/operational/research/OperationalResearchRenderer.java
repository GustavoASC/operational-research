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
    private String targetFunction;
    /* Type of the Target/objective function */
    private FunctionType targetFunctionType;
    /* Variables */
    private final List<Variable> variables;

    public OperationalResearchRenderer() {
        this.variables = new LinkedList<>();
        this.targetFunction = "";
        this.targetFunctionType = FunctionType.MIN;
    }

    /**
     * Sets the target function equation
     * 
     * @param targetFunction target function equation
     */
    public void setTargetFunction(String targetFunction) {
        this.targetFunction = targetFunction;
    }

    /**
     * Sets the target function type (MIN or MAX)
     * 
     * @param targetFunctionType target type
     */
    public void setTargetFunctionType(FunctionType targetFunctionType) {
        this.targetFunctionType = targetFunctionType;
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
