import java.util.ArrayList;
import java.lang.Math.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.vecmath.*;
import java.lang.String;

public class Item {
	private ArrayList<Point2d> points;
	String shape = new String("Freedom");   // selected drawing shape

	boolean selected = false;   // only one item can be selected at a time?
	int xmin = 0;
	int xmax = 0;
	int ymin = 0;
	int ymax = 0;

	Point2d center = null;
	int rotate = 0;
	float scaleX = 1;
	float scaleY = 1;
	int translateX = 0;
	int translateY = 0;

	float strokeThickness;
	Color strokeColor;
	Color fillColor;
	AffineTransform latest;    // is this for double buffer?

	public void addPoint(Point2d p) {

		if (points == null) {
			points = new ArrayList<Point2d>();
		}

		points.add(p);
	}

	public void addPoint(double x, double y) {
		addPoint(new Point2d(x, y));
	}

	public void setPoint(double x, double y) {
		points.get(1).setX(x);
		points.get(1).setY(y);
	}

	public Point2d getPoint(int i) {
		return points.get(i);
	}

	public int getPointsSize() {
		return points.size();
	}

	public void setShape(String s) {
		shape = s;
	}

	public void setColor(Color c) {
		strokeColor = c;
	}

	public void draw(Graphics2D g2) {
		if (points == null) {
			return;
		}
		//System.out.println(points.get(0).getX()+" "+points.get(0).getY());
		int[] xpoints, ypoints;
		int p_num = 0;
		xpoints = new int[points.size()];
		ypoints = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			xpoints[i] = (int) points.get(i).getX();
			ypoints[i] = (int) points.get(i).getY();
		}
			
		p_num = points.size();

		if (p_num == 1) return;

		AffineTransform M = g2.getTransform();

		if (center != null) {
			g2.translate(translateX, translateY);
			g2.translate(center.getX(), center.getY());
			g2.rotate(Math.toRadians(rotate));
			g2.scale(scaleX, scaleY);
			g2.translate(center.getX()*(-1), center.getY()*(-1));
			latest = g2.getTransform();
		}

		//latest = g2.getTransform();

		g2.setColor(strokeColor);
		g2.setStroke(new BasicStroke(strokeThickness));

		if (shape.equals("Rectangle")) {

			int w = xpoints[1]-xpoints[0];  // TODO: get the absolute value here
			int h = ypoints[1]-ypoints[0];
			g2.drawRect(xpoints[0], ypoints[0], w, h);			
			g2.setColor(fillColor);
			g2.fillRect(xpoints[0]+1, ypoints[0]+1, w-1, h-1);
			
		} else if (shape.equals("Ecllipse")) {

			int w = xpoints[1]-xpoints[0];  // TODO: get the absolute value here
			int h = ypoints[1]-ypoints[0];
			g2.drawOval(xpoints[0], ypoints[0], w, h);
			g2.setColor(fillColor);
			g2.fillOval(xpoints[0]+1, ypoints[0]+1, w-1, h-1);

		} else if (shape.equals("Straight")) {

			g2.drawLine(xpoints[0], ypoints[0], xpoints[1], ypoints[1]);

		} else {
			g2.drawPolyline(xpoints, ypoints, p_num);
		}

		
		if (selected) {
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(2));
			g2.drawRect((int)(xmin-3-strokeThickness), (int)(ymin-3-strokeThickness), (int)(xmax-xmin+6+2*strokeThickness), (int)(ymax-ymin+6+2*strokeThickness));
		}

		g2.setTransform(M);
	}

	// This is copied from course notes
	static Point2d closestPoint(Point2d M, Point2d P0, Point2d P1) {
		Vector2d v = new Vector2d();
    	v.sub(P1,P0); // v = P1 - P0
    	
    	// early out if line is less than 1 pixel long
    	if (v.lengthSquared() < 0.5)
    		return P0;
    	
    	Vector2d u = new Vector2d();
    	u.sub(M,P0); // u = M - P1
    	// scalar of vector projection ...
    	double s = u.dot(v) / v.dot(v); 
    	
    	// find point for constrained line segment
    	if (s < 0) 
    		return P0;
    	else if (s > 1)
    		return P1;
    	else {
    		Point2d I = P0;
        	Vector2d w = new Vector2d();
        	w.scale(s, v); // w = s * v
    		I.add(w); // I = P0 + w
    		return I;
    	}
	}

	public boolean hittest(double x, double y) {
		Point2d closest;
		Point2d M = new Point2d(x,y); // position of mouse

		if (latest != null) {
			try {
				Point2D origin = new Point2D.Double(x,y);
				Point2D after = new Point2D.Double(x,y);
				latest.inverseTransform(origin, after);
			M = new Point2d(after.getX(), after.getY());
			} catch (NoninvertibleTransformException ex) {
				System.out.println("NoninvertibleTransformException happens");
				M = new Point2d(x, y);
			}
		}

		if (shape.equals("Rectangle") || shape.equals("Ecllipse")) {

			if (x < points.get(1).getX() && x > points.get(0).getX() && y < points.get(1).getY() && y > points.get(0).getY()) {
				return true;
			}
		} else if (shape.equals("Freedom")) {
			for (int i = 0; i < points.size() - 1; ++i) {
				Point2d temp1 = new Point2d(points.get(i));
				Point2d temp2 = new Point2d(points.get(i+1));
				closest = closestPoint(M, temp1, temp2);
				if (M.distance(closest) <= 5) {
					return true;
				}
			}
		} else {
			//System.out.println(points.get(0).getX()+" "+points.get(0).getY());	
			Point2d temp1 = new Point2d(points.get(0));
			Point2d temp2 = new Point2d(points.get(1));
			closest = closestPoint(M, temp1, temp2);
			if (M.distance(closest) <= 5) {
					return true;
			}
			//System.out.println(points.get(0).getX()+" "+points.get(0).getY());
		}
		
		return false;
	}

}