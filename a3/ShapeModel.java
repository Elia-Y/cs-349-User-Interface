import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class ShapeModel {
    Shape shape;
    Ellipse2D rotateHandler;
    Rectangle2D scaleHandler;

    Point a;
    Point b;
    Point2D center;
    double minX;
    double maxX;
    double minY;
    double maxY;
    
    double rotate = 0;
    double scaleX = 1;
    double scaleY = 1;
    double translateX = 0;
    double translateY = 0;

    AffineTransform latest;

    public ShapeModel(Point startPoint, Point endPoint) {    }

    public Shape getShape() {
        
        // This is to get the origin shape (without transformation)
        return shape;
    }

    public boolean hitTest(Point2D p) {
        if (latest == null)
            return this.getShape().contains(p);
        return this.latest.createTransformedShape(shape).contains(p);
    }

    /**
     * Given a ShapeType and the start and end point of the shape, ShapeFactory constructs a new ShapeModel
     * using the class reference in the ShapeType enum and returns it.
     */
    public static class ShapeFactory {
        public ShapeModel getShape(ShapeType shapeType, Point startPoint, Point endPoint) {
            try {
                Class<? extends ShapeModel> clazz = shapeType.shape;
                Constructor<? extends ShapeModel> constructor = clazz.getConstructor(Point.class, Point.class);

                return constructor.newInstance(startPoint, endPoint);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public enum ShapeType {
        Ellipse(EllipseModel.class),
        Rectangle(RectangleModel.class),
        Line(LineModel.class);

        public final Class<? extends ShapeModel> shape;
        ShapeType(Class<? extends ShapeModel> shape) {
            this.shape = shape;
        }
    }

    public void drawSelect(Graphics2D g2) {
        // we only need to update handles when we want to use them
        this.updateHandles();

        g2.setColor(Color.BLUE);
        g2.translate(0, 0);
        g2.rotate(0);
        g2.scale(1, 1);
        g2.fill(this.scaleHandler);
        g2.fill(this.rotateHandler);
        g2.setColor(Color.BLACK);
    }

    public void updateHandles() {
        Point2D origin = new Point2D.Double(0.5*(minX+maxX), minY-12+4);
        Point2D after = new Point2D.Double();
        after = this.latest.transform(origin, after);
        this.rotateHandler = new Ellipse2D.Double(after.getX()-4, after.getY()-4, 8, 8);

        origin = new Point2D.Double(maxX, maxY);
        this.latest.transform(origin, after);
        this.scaleHandler = new Rectangle2D.Double(after.getX()-4, after.getY()-4, 8, 8);
    }

    public AffineTransform getTransformNoScale() {
        AffineTransform at = new AffineTransform();
        at.translate(translateX, translateY);
        at.translate(center.getX(), center.getY());
        at.rotate(Math.toRadians(rotate));
        at.translate(center.getX()*(-1), center.getY()*(-1));
        return at;
    }

}
