import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class RectangleModel extends ShapeModel {
    public RectangleModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        this.a = startPoint;
        this.b = endPoint;
        Rectangle2D rect = new Rectangle2D.Double(a.x,a.y, b.x - a.x, b.y - a.y);

        this.shape = rect;

        // Create the handler
        minX = Math.min(a.x, b.x);
        maxX = Math.max(a.x, b.x);
      	minY = Math.min(a.y, b.y);
        maxY = Math.max(a.y, b.y);
        center = new Point2D.Double(0.5 * (minX + maxX), 0.5 * (minY + maxY));
        this.scaleHandler = new Rectangle2D.Double(maxX-4, maxY-4, 8, 8);
        this.rotateHandler = new Ellipse2D.Double(0.5*(minX+maxX)-4, minY-14, 8, 8);
    }
}
