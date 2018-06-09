import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import java.awt.geom.AffineTransform;
import javax.swing.undo.*;
import java.awt.geom.*;

public class CanvasView extends JPanel implements Observer {
    DrawingModel model;
    private Point2D lastMouse;
    private Point2D startMouse;
    private String mode;
    private Point2D currentCenter; // this shouldn't change in one dragging motion
    double startScaleX;
    double startScaleY;
    double startRotate;

    public CanvasView(DrawingModel model) {
        super();
        this.model = model;

        MouseAdapter mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                ShapeModel select = checkSelect(e.getPoint());
                if (select == null) {
                    model.selected = false;
                } else {
                    ShapeModel oldSelect = model.selectedShape;
                    ShapeModel newSelect = select;
                    //boolean oldSelected = model.selected;

                    UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                        public void redo() throws CannotRedoException {
                            super.redo();
                            model.selectedShape = newSelect;
                            model.selected = true;
                            model.updateViews();
                        }

                        public void undo() throws CannotUndoException {
                            super.undo();
                            model.selectedShape = oldSelect; // lastSelect can be null
                            model.selected = false;
                            model.updateViews();
                        }
                    };              

                    model.undoManager.addEdit(undoableEdit);
                    model.selectedShape = newSelect;
                    model.selected = true;
                    model.updateViews();
                }

                repaint();
            }
            
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastMouse = e.getPoint();
                startMouse = e.getPoint();  // This shouldn't be changed once it's settled
                if (model.selected == true)
                    currentCenter = transformCenter();

                if (model.selected == true) {
                    if (model.selectedShape.rotateHandler.contains(startMouse)) {
                        mode = new String("Rotate");
                        startRotate = model.selectedShape.rotate;
                    } else if (model.selectedShape.scaleHandler.contains(startMouse)) {
                        mode = new String("Scale");
                        startScaleX = model.selectedShape.scaleX;
                        startScaleY = model.selectedShape.scaleY;
                    } else if (model.selectedShape.hitTest(startMouse)){
                        mode = new String("Translate");
                    } else {
                        mode = null;
                        model.selected = false;
                    }
                } else {
                    mode = null;
                }
            }

            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                double tempX = lastMouse.getX();
                double tempY = lastMouse.getY();
                lastMouse = e.getPoint();
                
                if (model.selected == true && mode != null) {

                    if (mode.equals("Rotate")) {
                    
                        model.selectedShape.rotate = - Math.toDegrees(Math.atan2(lastMouse.getX() - currentCenter.getX(), lastMouse.getY() - currentCenter.getY()))+180;

                    } else if (mode.equals("Scale")) {

                        lastMouse = transformClick(lastMouse);
                        double scaleX = (lastMouse.getX()- model.selectedShape.center.getX()) / (model.selectedShape.maxX - model.selectedShape.center.getX());
                        double scaleY = (lastMouse.getY()- model.selectedShape.center.getY()) / (model.selectedShape.maxY - model.selectedShape.center.getY());
                        model.selectedShape.scaleX = scaleX;
                        model.selectedShape.scaleY = scaleY;

                    } else if (mode.equals("Translate")) {

                        double newTranslateX = lastMouse.getX() - tempX;
                        double newTranslateY = lastMouse.getY() - tempY;
                        model.selectedShape.translateX += newTranslateX;
                        model.selectedShape.translateY += newTranslateY;

                    } else {

                    }

                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (model.selected == false || mode == null) {

                    ShapeModel lastSelect = model.selectedShape;
                    ShapeModel shape = new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse);
                    
                    UndoableEdit undoableEdit = new AbstractUndoableEdit() {
                        public void redo() throws CannotRedoException {
                            super.redo();
                            model.addShape(shape);
                            model.selectedShape = shape;
                            model.selected = true;
                            model.updateViews();
                        }

                        public void undo() throws CannotUndoException {
                            super.undo();
                            model.removeShape(model.selectedShape);
                            model.selectedShape = lastSelect; // lastSelect can be null
                            model.selected = true;
                            if (lastSelect == null) {
                                model.selected = false;
                            }
                            model.updateViews();
                        }
                    };              

                    model.undoManager.addEdit(undoableEdit);

                    model.addShape(shape);
                    model.selectedShape = shape;
                    model.selected = true;

                } else {
                    double tempX = lastMouse.getX();
                    double tempY = lastMouse.getY();
                    lastMouse = e.getPoint();

                    if (mode.equals("Rotate")) {

                        model.selectedShape.rotate = - Math.toDegrees(Math.atan2(lastMouse.getX() - currentCenter.getX(), lastMouse.getY() - currentCenter.getY()))+180;
                        
                        d_rotate();
                    } else if (mode.equals("Scale")) {
                        lastMouse = transformClick(lastMouse);
                        double scaleX = (lastMouse.getX()- model.selectedShape.center.getX()) / (model.selectedShape.maxX - model.selectedShape.center.getX());
                        double scaleY = (lastMouse.getY()- model.selectedShape.center.getY()) / (model.selectedShape.maxY - model.selectedShape.center.getY());
                        model.selectedShape.scaleX = scaleX;
                        model.selectedShape.scaleY = scaleY;

                        d_scale();

                    } else if (mode.equals("Translate")) {
                        double newTranslateX = lastMouse.getX() - tempX;
                        double newTranslateY = lastMouse.getY() - tempY;
                        model.selectedShape.translateX += newTranslateX;
                        model.selectedShape.translateY += newTranslateY;

                        d_translate(lastMouse.getX() - startMouse.getX(),lastMouse.getY() - startMouse.getY());
                    } else {
                        // We should never arrive here
                        System.out.println("Invalid motion");
                    }
                }

                startMouse = null;
                lastMouse = null;
                model.updateViews();
            }
        };

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);

        model.addObserver(this);
    }

    /*
     inverseTransform the mouse click for use of scaling the shape
     */
    public Point2D transformClick(Point2D p) {
        Point2D M;
        try {
            Point2D origin = new Point2D.Double(p.getX(),p.getY());
            Point2D after = new Point2D.Double();
            model.selectedShape.getTransformNoScale().inverseTransform(origin, after);
            M = new Point2D.Double(after.getX(), after.getY());
        } catch (NoninvertibleTransformException ex) {
            System.out.println("NoninvertibleTransformException happens");
            M = p;
        }

        return M;
    }

    /*
     Transform the center point of the current selected shape
     This function is for use of rotating the shape
     */
    public Point2D transformCenter() {
        Point2D origin = new Point2D.Double(model.selectedShape.center.getX(),model.selectedShape.center.getY());
        Point2D M;
        
        Point2D after = new Point2D.Double();
        model.selectedShape.latest.transform(origin, after);
        M = new Point2D.Double(after.getX(), after.getY());
       
        return M;
    }


    public void d_translate(double x, double y) {
        double newTranslateX = model.selectedShape.translateX;
        double newTranslateY = model.selectedShape.translateY;
        double oldTranslateX = model.selectedShape.translateX - x;
        double oldTranslateY = model.selectedShape.translateY - y;

        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            public void redo() throws CannotRedoException {
                super.redo();
                model.selectedShape.translateX = newTranslateX;
                model.selectedShape.translateY = newTranslateY;
                model.updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                model.selectedShape.translateX = oldTranslateX;
                model.selectedShape.translateY = oldTranslateY;
                model.updateViews();
            }
        };

        model.undoManager.addEdit(undoableEdit);

        model.selectedShape.translateX = newTranslateX;
        model.selectedShape.translateY = newTranslateY;

        model.updateViews();
    }

    public void d_rotate() {
        double newRotate = model.selectedShape.rotate;
        //double oldRotate = - Math.toDegrees(Math.atan2(startMouse.getX() - currentCenter.getX(), startMouse.getY() - currentCenter.getY())) + 180;
        double oldRotate = startRotate;

        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            public void redo() throws CannotRedoException {
                super.redo();
                model.selectedShape.rotate = newRotate;
                model.updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                model.selectedShape.rotate = oldRotate;
                model.updateViews();
            }
        };

        model.undoManager.addEdit(undoableEdit);
        model.selectedShape.rotate = newRotate;
        model.updateViews();
    }

    public void d_scale() {
        double newScaleX = model.selectedShape.scaleX;
        double newScaleY = model.selectedShape.scaleY;
        double oldScaleX = startScaleX;
        double oldScaleY = startScaleY;

        UndoableEdit undoableEdit = new AbstractUndoableEdit() {
            public void redo() throws CannotRedoException {
                super.redo();
                model.selectedShape.scaleX = newScaleX;
                model.selectedShape.scaleY = newScaleY;
                model.updateViews();
            }

            public void undo() throws CannotUndoException {
                super.undo();
                model.selectedShape.scaleX = oldScaleX;
                model.selectedShape.scaleY = oldScaleY;
                model.updateViews();
            }
        };

        model.undoManager.addEdit(undoableEdit);
        // actually we dont need this
        model.selectedShape.scaleX = newScaleX;
        model.selectedShape.scaleY = newScaleY;

        model.updateViews();
    }


    public void update(Observable o, Object arg) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        drawAllShapes(g2);

        if (model.selected == false) {
            drawCurrentShape(g2);
        } else {
            drawSelected(g2);
        }

    }

    private void drawAllShapes(Graphics2D g2) {
        g2.setColor(new Color(66,66,66));
        //g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setStroke(new BasicStroke(2));

        for(ShapeModel shape : model.getShapes()) {
            AffineTransform M = g2.getTransform();
            g2.translate(shape.translateX, shape.translateY);
            g2.translate(shape.center.getX(), shape.center.getY());

            g2.rotate(Math.toRadians(shape.rotate));
            g2.scale(shape.scaleX, shape.scaleY);
            g2.translate(shape.center.getX()*(-1), shape.center.getY()*(-1));

            shape.latest = new AffineTransform(g2.getTransform());
            g2.draw(shape.getShape());

            g2.setTransform(M);
        }
    }

    private void drawSelected(Graphics2D g2) {
        if (model.selected == true && model.selectedShape != null) {
            model.selectedShape.drawSelect(g2);
        }
    }

    private void drawCurrentShape(Graphics2D g2) {
        if (model.selected == true) {
            return;
        }

        if (startMouse == null) {
            return;
        }

        g2.setColor(new Color(66,66,66));
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.draw(new ShapeModel.ShapeFactory().getShape(model.getShape(), (Point) startMouse, (Point) lastMouse).getShape());
    }

    /*
      Check if the mouse is inside some shape when a click happens
    */
    private ShapeModel checkSelect(Point2D m) {  // m is the point of mouse
        for(ShapeModel shape : model.getShapes()) {
            if (shape.hitTest(m)) {
                return shape;
                //model.selected = true;
                //return true;
            }
        }
        return null;
    }
}
