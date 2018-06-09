import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.*;

public class LineModel extends ShapeModel {
    public LineModel(Point startPoint, Point endPoint) {
        super(startPoint, endPoint);
        this.a = startPoint;
        this.b = endPoint;

        Path2D path = new Path2D.Double();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        this.shape = path;

        // Create the handler
        minX = Math.min(a.x, b.x);
        maxX = Math.max(a.x, b.x);
        minY = Math.min(a.y, b.y);
        maxY = Math.max(a.y, b.y);
        center = new Point2D.Double(0.5 * (minX + maxX), 0.5 * (minY + maxY));
        this.scaleHandler = new Rectangle2D.Double(maxX-4, maxY-4, 8, 8);
        this.rotateHandler = new Ellipse2D.Double(0.5*(minX+maxX)-4, minY-14, 8, 8);
    }

    @Override
    public boolean hitTest(Point2D p) {
        
        Point M;
        try {
            Point2D origin = new Point2D.Double(p.getX(),p.getY());
            Point2D after = new Point2D.Double();
            latest.inverseTransform(origin, after);
            //M = new Point2D.Double(after.getX(), after.getY());
            M = new Point((int)after.getX(), (int)after.getY());
        } catch (NoninvertibleTransformException ex) {
            System.out.println("NoninvertibleTransformException happens");
            //M = new Point2D.Double(p.getX(),p.getY());
            M = (Point) p;
        }     

        return pointToLineDistance(a,b, M) < 10;
    }

    public double pointToLineDistance(Point A, Point B, Point P) {
        double normalLength = Math.sqrt((B.x-A.x)*(B.x-A.x)+(B.y-A.y)*(B.y-A.y));
        return Math.abs((P.x-A.x)*(B.y-A.y)-(P.y-A.y)*(B.x-A.x))/normalLength;
    }
}