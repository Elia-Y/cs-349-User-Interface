import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.geom.*;
import javax.vecmath.*;
import java.lang.Math.*;
import java.lang.String;


public class Canvas extends JPanel implements Observer {

    private Model model;
    private Point2d M = new Point2d(0,0); // position of mouse
 //   private boolean click = true; 

    /**
     * Create a new View.
     */
    public Canvas(Model model) {
        // Hook up this observer so that it will be notified when the model
        // changes.
        super();
        this.model = model;
        model.addObserver(this);


        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (model.checkDrawMode()) {
                    model.addItem(new Item());
                    model.getItem(model.getSize()-1).setShape(new String(model.getDrawingShape()));
                    model.getItem(model.getSize()-1).setColor(model.getDrawingColor());
                    model.getItem(model.getSize()-1).fillColor = model.getFillColor();
                    model.getItem(model.getSize()-1).strokeThickness = model.getThickness();

                    model.getItem(model.getSize()-1).addPoint(event.getX(), event.getY());
                    model.getItem(model.getSize()-1).xmin = event.getX();
                    model.getItem(model.getSize()-1).xmax = event.getX();
                    model.getItem(model.getSize()-1).ymin = event.getY();
                    model.getItem(model.getSize()-1).ymax = event.getY();                    
                }
                model.notifyObservers();
            }

            public void mouseReleased(MouseEvent event) {
                if (model.checkDrawMode()) {
                    if (model.getItem(model.getSize()-1).getPointsSize() < 2) {
                          model.removeItem(model.getSize()-1);
                          //model.notifyObservers();
                          return;
                    // what if end point has x < points[0].getX and similar to y???
                    }

                    if (model.getItem(model.getSize()-1).shape.equals("Freedom")) {

                        model.getItem(model.getSize()-1).addPoint(event.getX(), event.getY());

                    } else {

                        model.getItem(model.getSize()-1).setPoint(event.getX(), event.getY());
                        int x1 = (int)model.getItem(model.getSize()-1).getPoint(1).getX();
                        int y1 = (int)model.getItem(model.getSize()-1).getPoint(1).getY();
                        int x0 = (int)model.getItem(model.getSize()-1).getPoint(0).getX();
                        int y0 = (int)model.getItem(model.getSize()-1).getPoint(0).getY();

                        model.getItem(model.getSize()-1).xmin = (int)Math.min(x0, x1);
                        model.getItem(model.getSize()-1).xmax = (int)Math.max(x0, x1);

                        model.getItem(model.getSize()-1).ymin = (int)Math.min(y0, y1);
                        model.getItem(model.getSize()-1).ymax = (int)Math.max(y0, y1);

                    }

                    double center_x = (double)(model.getItem(model.getSize()-1).xmax + model.getItem(model.getSize()-1).xmin);
                    double center_y = (double)(model.getItem(model.getSize()-1).ymax + model.getItem(model.getSize()-1).ymin);
                    center_x = center_x / 2;
                    center_y = center_y / 2;
                    model.getItem(model.getSize()-1).center = new Point2d(center_x, center_y);
                    model.notifyObservers();
                }
                //model.notifyObservers();
            }

            public void mouseClicked(MouseEvent event) {

                if (model.checkDrawMode() == false) {
                    for (int i = model.getSize() - 1; i >= 0; i--) {
                        boolean result = model.getItem(i).hittest(event.getX(), event.getY());

                        if (result) {
                            if (model.getItem(i).selected == true) {
                                model.setUnselectItem(model.getItem(i));
                            } else {

                                model.setSelectItem(model.getItem(i));
                            }

                            model.notifyObservers();
                            return;
                        }
                    }

                    System.out.println("No item is selected");

                    if (model.getSelectItem() != null && model.checkMultiMode() == false) {
                        model.getSelectItem().selected = false;
                        model.clearSelectItem();
                        System.out.println("Clear Selecting");
                        model.notifyObservers();
                    }

                    
                }
            }
    
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent event) {


                if (model.checkDrawMode() == true) { 
                    //System.out.println("Size in dragged: "+model.getSize());
                    if (model.getItem(model.getSize()-1) == null) return;  // this should never be true

                    if (model.getDrawingShape().equals("Freedom")) {
                        model.getItem(model.getSize()-1).addPoint(event.getX(), event.getY());
                        if (model.getItem(model.getSize()-1).xmin > (int)event.getX()) {
                            model.getItem(model.getSize()-1).xmin = (int)event.getX();
                        }
                        if (model.getItem(model.getSize()-1).xmax < (int)event.getX()) {
                            model.getItem(model.getSize()-1).xmax = (int)event.getX();
                        }
                        if (model.getItem(model.getSize()-1).ymin > (int)event.getY()) {
                            model.getItem(model.getSize()-1).ymin = (int)event.getY();
                        }
                        if (model.getItem(model.getSize()-1).ymax < (int)event.getY()) {
                            model.getItem(model.getSize()-1).ymax = (int)event.getY();
                        }

                    } else {

                        if (model.getItem(model.getSize()-1).getPointsSize() == 1) {
                            model.getItem(model.getSize()-1).addPoint(event.getX(), event.getY());
                        }
                        model.getItem(model.getSize()-1).setPoint(event.getX(), event.getY());
                    }
                } else {
                    // what is this used for?
                    M = new Point2d(event.getX(), event.getY());
                }

                model.notifyObservers();
            }
        });

        setBackground(Color.WHITE);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < this.model.getSize(); ++i) {
            //System.out.println("Debug: in the loop to draw each item.");
            if (this.model.getItem(i) != null) {
                this.model.getItem(i).draw(g2);
            }
        }
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        //System.out.println("Canvas changed!");
        repaint();
    }
}
