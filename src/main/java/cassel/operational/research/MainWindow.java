/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cassel.operational.research;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.jfree.chart.ChartPanel;

/**
 * Application's main window
 *
 * @author gustavo
 */
public class MainWindow extends JFrame {

    /* Target function equation */
    private JTextField targetFunctionField;
    /* Panel where chart is rendered */
    private JPanel chartPanel;

    /**
     * Creates a new invisible application's main window
     */
    public MainWindow() {
        this.buildGui();
    }

    /**
     * Builds the application's main window components
     */
    private void buildGui() {
        setTitle("Operational Research Equation Renderer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(buildMainPanel());
        setSize(800, 600);
    }

    /**
     * Builds the main window panel where all of the other components will be
     * built
     *
     * @return main panel
     */
    private JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buildConfigsPanel(), BorderLayout.WEST);
        mainPanel.add(buildChartPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Builds the configuration panel
     *
     * @return JPanel
     */
    private JPanel buildConfigsPanel() {
        JPanel configsPanel = new JPanel(new BorderLayout());
        configsPanel.add(buildConfigsFieldsPanel(), BorderLayout.CENTER);
        configsPanel.add(buildConfigsFooterPanel(), BorderLayout.SOUTH);
        return configsPanel;
    }

    /**
     * Builds the configuration fields panel
     *
     * @return JPanel
     */
    private JPanel buildConfigsFieldsPanel() {
        JPanel configsFieldsPanel = new JPanel();
        configsFieldsPanel.setLayout(new GridLayout(3, 0));
        configsFieldsPanel.add(buildConfigsVariablePanel());
        configsFieldsPanel.add(buildConfigsFunctionPanel());
        configsFieldsPanel.add(buildConfigsRestrictionsPanel());
        return configsFieldsPanel;
    }

    /**
     * Builds the configuration variables panel
     *
     * @return JPanel
     */
    private JPanel buildConfigsVariablePanel() {
        JPanel variablesPanel = new JPanel(new BorderLayout());
        variablesPanel.add(new JLabel("Variables"), BorderLayout.NORTH);
        variablesPanel.add(buildVariablesTable(), BorderLayout.CENTER);
        return variablesPanel;
    }

    /**
     * Builds the variables table
     *
     * @return JComponent
     */
    private JComponent buildVariablesTable() {
        String[][] data = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
        String[] cols = {"Name", "Description", "Value"};
        JTable table = new JTable(data, cols);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setPreferredSize(new Dimension(300, 86));
        return sp;
    }

    /**
     * Builds configuration function panel
     *
     * @return JComponent
     */
    private JComponent buildConfigsFunctionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Function: "));
        panel.add(buildFunctionTypeCombo());
        panel.add(buildTargetFunctionField());
        return panel;
    }

    /**
     * Builds a panel to configure function restrictions
     *
     * @return JComponent
     */
    private JComponent buildConfigsRestrictionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Restriction: "), BorderLayout.NORTH);
        return panel;
    }

    /**
     * Builds the configs footer panel
     */
    private JPanel buildConfigsFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(buildChartRenderingButton(), BorderLayout.CENTER);
        return footer;
    }

    /**
     * Builds button the fire chart rendering
     *
     * @return JButton
     */
    private JButton buildChartRenderingButton() {
        JButton render = new JButton("Render chart");
        render.addActionListener((ActionEvent event) -> {
            JPanel renderedChart = new ChartPanel(new OperationalResearchRenderer().render());
            chartPanel.removeAll();
            chartPanel.add(renderedChart, BorderLayout.CENTER);
            repaint();
            revalidate();
        });
        return render;
    }

    /**
     * Builds the function type combo
     *
     * @return JComboBox
     */
    private JComboBox buildFunctionTypeCombo() {
        JComboBox functionType = new JComboBox<OperationalResearchRenderer.FunctionType>();
        functionType.addItem(OperationalResearchRenderer.FunctionType.MAX);
        functionType.addItem(OperationalResearchRenderer.FunctionType.MIN);
        return functionType;
    }

    /**
     * Builds the target function field
     *
     * @return JTextField
     */
    private JTextField buildTargetFunctionField() {
        targetFunctionField = new JTextField();
        targetFunctionField.setPreferredSize(new Dimension(100, 25));
        return targetFunctionField;
    }

    /**
     * Builds the panel where the chart will be rendered
     *
     * @return JPanel
     */
    private JPanel buildChartPanel() {
        chartPanel = new JPanel(new BorderLayout());
        return chartPanel;
    }

}
