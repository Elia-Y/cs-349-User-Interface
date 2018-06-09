import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

public class EllipseModel extends ShapeModel {
    public EllipseModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        this.a = startPoint;
        this.b = endPoint;
        Rectangle rect = new java.awt.Rectangle(startPoint);
        rect.add(endPoint);
        this.shape = new Ellipse2D.Double(rect.x, rect.y, rect.width, rect.height);

        // Create the handler
        minX = rect.x;
        maxX = rect.x + rect.width;
      	minY = rect.y;
        maxY = rect.y + rect.height;
        center = new Point2D.Double(0.5 * (minX + maxX), 0.5 * (minY + maxY));
        this.scaleHandler = new Rectangle2D.Double(maxX-4, maxY-4, 8, 8);
        this.rotateHandler = new Ellipse2D.Double(0.5*(minX+maxX)-4, minY-14, 8, 8);
    }
}
