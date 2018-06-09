
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ToolView extends JToolBar implements Observer {

    private Model model;
    private JToggleButton multiSelect;
    private JToggleButton select;   // used to change the interface of the toolbar after choosing
    private JToggleButton draw;
    JComboBox<String> properties;

    /**
     * Create a new View.
     */
    public ToolView(Model model) {
        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        this.multiSelect = new JToggleButton("MultiSelect");
        this.select = new JToggleButton("Select");
        this.draw = new JToggleButton("Draw");
        this.add(this.multiSelect);
        this.add(this.select);
        this.add(this.draw);
        draw.setSelected(true);

        String[] shapeName = new String[] {"Freedom", "Straight","Rectangle", "Ecllipse"};
        JComboBox<String> shapes = new JComboBox<>(shapeName);
        this.add(shapes);

        String[] propName = new String[] {"1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px"};
        properties = new JComboBox<>(propName);
        this.add(properties);

        JButton fillColor = new JButton("Fill Colour");
        JButton strokeColor = new JButton("Stroke Colour");
        this.add(fillColor);
        this.add(strokeColor);

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setFloatable(false);

        shapes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawingShape((String)shapes.getSelectedItem());
                model.notifyObservers();
            }

        });

        properties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String result = (String)properties.getSelectedItem();
                if (result.equals("1px")) {
                    model.setThickness(1);
                } else if (result.equals("2px")) {
                    model.setThickness(2);
                } else if (result.equals("3px")) {
                    model.setThickness(3);
                } else if (result.equals("4px")) {
                    model.setThickness(4);
                } else if (result.equals("5px")) {
                    model.setThickness(5);
                } else if (result.equals("6px")) {
                    model.setThickness(6);
                } else if (result.equals("7px")) {
                    model.setThickness(7);
                } else if (result.equals("8px")) {
                    model.setThickness(8);
                } else if (result.equals("9px")) {
                    model.setThickness(9);
                } else {
                    model.setThickness(10);
                }
                model.notifyObservers();
            }
        });

        multiSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawMode(false);
                model.setMultiMode(true);
                model.notifyObservers();
            }
        });

        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawMode(false);
                model.notifyObservers();
            }
        });

        draw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawMode(true);
                model.notifyObservers();
            }
        });

        strokeColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Color initialBackground = strokeColor.getBackground();
                model.setDrawingColor(JColorChooser.showDialog(null, "Change Stroke Color", initialBackground));
                model.notifyObservers();
            }
        });

        fillColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Color initialBackground = fillColor.getBackground();
                model.setFillColor(JColorChooser.showDialog(null, "Change Fill Color", initialBackground));
                model.notifyObservers();
            }
        });
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {

        if (this.model.checkMultiMode()) {
            this.select.setSelected(false);
        } else {
            this.select.setSelected(!this.model.checkDrawMode());
        }

        this.draw.setSelected(this.model.checkDrawMode());
        this.multiSelect.setSelected(this.model.checkMultiMode());
    }
}
