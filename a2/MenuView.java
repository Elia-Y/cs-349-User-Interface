import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MenuView extends JMenuBar implements Observer {

    private Model model;
    private ToolView toolview;

    /**
     * Create a new View.
     */
    public MenuView(Model model) {
        JMenu menu1 = new JMenu("File");  
        JMenu menu2 = new JMenu("Edit");  
        JMenu menu3 = new JMenu("Format");  
        JMenuBar menubar = new JMenuBar();
        JMenuItem fileNew = new JMenuItem("New");
        JMenuItem fileExit = new JMenuItem("Exit");
        JMenuItem editSelection = new JMenuItem("Selection Mode");
        JMenuItem editDrawing = new JMenuItem("Drawing Mode");
        JMenuItem editDelete = new JMenuItem("Delete Shape");
        JMenuItem editTransform = new JMenuItem("Transform Shape");
        JMenu formatStroke = new JMenu("Stroke width");
        JMenuItem formatFillColor = new JMenuItem("Fill Colour");
        JMenuItem formatStrokeColor = new JMenuItem("Stroke Colour");

        JRadioButton stroke1 = new JRadioButton("1px");
        JRadioButton stroke2 = new JRadioButton("2px");
        JRadioButton stroke3 = new JRadioButton("3px");
        JRadioButton stroke4 = new JRadioButton("4px");
        JRadioButton stroke5 = new JRadioButton("5px");
        JRadioButton stroke6 = new JRadioButton("6px");
        JRadioButton stroke7 = new JRadioButton("7px");
        JRadioButton stroke8 = new JRadioButton("8px");
        JRadioButton stroke9 = new JRadioButton("9px");
        JRadioButton stroke10 = new JRadioButton("10px");
        ButtonGroup group = new ButtonGroup();
        
        menu1.add(fileNew);
        menu1.add(fileExit);
        menu2.add(editSelection);
        menu2.add(editDrawing);
        menu2.add(editDelete);
        menu2.add(editTransform);
        menu3.add(formatStroke);
        menu3.add(formatFillColor);
        menu3.add(formatStrokeColor);
        
        group.add(stroke1);
        group.add(stroke2);
        group.add(stroke3);
        group.add(stroke4);
        group.add(stroke5);
        group.add(stroke6);
        group.add(stroke7);
        group.add(stroke8);
        group.add(stroke9);
        group.add(stroke10);
        
        formatStroke.add(stroke1);
        formatStroke.add(stroke2);
        formatStroke.add(stroke3);
        formatStroke.add(stroke4);
        formatStroke.add(stroke5);
        formatStroke.add(stroke6);
        formatStroke.add(stroke7);
        formatStroke.add(stroke8);
        formatStroke.add(stroke9);
        formatStroke.add(stroke10);

        this.add(menu1);
        this.add(menu2);
        this.add(menu3);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        fileNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.clearCanvas();
                model.notifyObservers();
            }
        });

        fileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        editSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawMode(false);
                model.notifyObservers();
            }
        });

        editDrawing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setDrawMode(true);
                model.notifyObservers();
            }
        });

        editDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (model.checkMultiMode() == false) {
                    if (model.checkDrawMode() == false && model.getSelectItem() != null) {
                        //for ()
                        model.removeItem(model.getSelectItem());
                        model.notifyObservers();
                        return;
                    }
                } else {
                    model.removeAllSelected();
                    model.notifyObservers();
                }

            }
        });

        editTransform.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                if (model.checkDrawMode() == false && model.getSelectItem() != null) {
                    System.out.println("Transforming a shape....");

                    JPanel myDialog = new JPanel();
                    SpinnerNumberModel model1 = new SpinnerNumberModel(0, -600, 600, 1);
                    SpinnerNumberModel model2 = new SpinnerNumberModel(0, -400, 400, 1);
                    SpinnerNumberModel model3 = new SpinnerNumberModel(0, 0, 360, 1);
                    SpinnerNumberModel model4 = new SpinnerNumberModel(1, 0, 10, 0.1);
                    SpinnerNumberModel model5 = new SpinnerNumberModel(1, 0, 10, 0.1);
                    JSpinner translateX = new JSpinner(model1);
                    JSpinner translateY = new JSpinner(model2);
                    JSpinner rotate = new JSpinner(model3);
                    JSpinner scaleX = new JSpinner(model4);
                    JSpinner scaleY = new JSpinner(model5);

                    // The UI of Transform dialog can be improved
                    myDialog.add(new JLabel("Translate (px): "));
                    myDialog.add(new JLabel("x: "));
                    myDialog.add(translateX);
                    myDialog.add(new JLabel("y: "));
                    myDialog.add(translateY);
                    myDialog.add(Box.createHorizontalStrut(15));
                    myDialog.add(new JLabel("Rotate (degrees): "));
                    myDialog.add(rotate);
                    myDialog.add(Box.createHorizontalStrut(15));
                    myDialog.add(new JLabel("Scale (times): "));
                    myDialog.add(new JLabel("x: "));
                    myDialog.add(scaleX);
                    myDialog.add(new JLabel("y: "));
                    myDialog.add(scaleY);

                    JOptionPane.showMessageDialog(null, myDialog, "TransformDialog", JOptionPane.OK_CANCEL_OPTION);
                    if (model.checkMultiMode() == false) {
                        model.getSelectItem().translateX = (int)translateX.getValue();
                        model.getSelectItem().translateY = (int)translateY.getValue();
                        model.getSelectItem().rotate = (int)rotate.getValue();
                        model.getSelectItem().scaleX = (float)(double)scaleX.getValue();
                        model.getSelectItem().scaleY = (float)(double)scaleY.getValue();
                    } else {
                        for (int i = model.getSelectSize() - 1; i >= 0; i--) {
                            model.getSelectItem(i).translateX = (int)translateX.getValue();
                            model.getSelectItem(i).translateY = (int)translateY.getValue();
                            model.getSelectItem(i).rotate = (int)rotate.getValue();
                            model.getSelectItem(i).scaleX = (float)(double)scaleX.getValue();
                            model.getSelectItem(i).scaleY = (float)(double)scaleY.getValue();
                        }
                    }
                    model.notifyObservers();
                }
            }
        });

        formatFillColor.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                Color initialBackground = formatFillColor.getBackground();
                model.setFillColor(JColorChooser.showDialog(null, "Change Stroke Color", initialBackground));
                model.notifyObservers();
            }
        });

        formatStrokeColor.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                Color initialBackground = formatStrokeColor.getBackground();
                model.setDrawingColor(JColorChooser.showDialog(null, "Change Stroke Color", initialBackground));
                model.notifyObservers();
            }
        });

        stroke1.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(1);
                toolview.properties.setSelectedIndex(0); 
                model.notifyObservers();
            }
        });

        stroke2.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(2);
                toolview.properties.setSelectedIndex(1); 
                model.notifyObservers();
            }
        });

        stroke3.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(3);
                toolview.properties.setSelectedIndex(2); 
                model.notifyObservers();
            }
        });

        stroke4.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(4);
                toolview.properties.setSelectedIndex(3); 
                model.notifyObservers();
            }
        });

        stroke5.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(5);
                toolview.properties.setSelectedIndex(4); 
                model.notifyObservers();
            }
        });

        stroke6.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(6);
                toolview.properties.setSelectedIndex(5); 
                model.notifyObservers();
            }
        });

        stroke7.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(7);
                toolview.properties.setSelectedIndex(6); 
                model.notifyObservers();
            }
        });

        stroke8.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(8);
                toolview.properties.setSelectedIndex(7); 
                model.notifyObservers();
            }
        });

        stroke9.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(9);
                toolview.properties.setSelectedIndex(8); 
                model.notifyObservers();
            }
        });

        stroke10.addActionListener(new ActionListener() {
            public  void actionPerformed(ActionEvent event) {
                model.setThickness(10);
                toolview.properties.setSelectedIndex(9); 
                model.notifyObservers();
            }
        });

        fileNew.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        fileExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        editSelection.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        editDrawing.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
        editDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));  // This does not work
        editTransform.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

    }

    public void setToolView(ToolView update) {
        this.toolview = update;
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {

    }
}
