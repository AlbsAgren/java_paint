package se.miun.alag1901.dt062g.jpaint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
* <h1>Rectangle</h1>
* Class representing a circle shape.
*
* @author  Albin Ågren (alag1901)
* @version 1.4
* @since   2020-12-20
*/
@XmlRootElement
public class Circle extends Shape {
	// constant for PI
	@XmlTransient
	public final double PI = 3.14;
	
	/**
	 * Default constructor
	 */
	public Circle() {
		super();
	}

	/**
	 * Constructor initializing Circle using doubles to represent points
	 * @param pX, the x coordinate
	 * @param pY, the y coordinate
	 * @param pColor, the color
	 */
	public Circle(double pX, double pY, String pColor) {
		super(pX, pY, pColor);
	}

	/**
	 * Constructor initializing Circle using a Point object
	 * @param pPoint, a point object
	 * @param pColor, the color
	 */
	public Circle(Point pPoint, String pColor) {
		super(pPoint, pColor);
	}

	@Override
	public void draw() {
		System.out.println(this.toString());
	}

	@Override
	public void draw(Graphics g) {
		Point center = super.points.get(0);

		// get the coordinates for the upper left corner and width to use with drawOval
		int xOrigin = 0, yOrigin = 0, width = 0;
		try {
			xOrigin = (int)(center.getX() - this.getRadius());
			yOrigin = (int)(center.getY() - this.getRadius());
			width = (int)(this.getRadius() * 2);
		} catch(UninitializedPointException ue) {
			System.err.println("UninitializedPointException in Circle->draw(): " + ue.getMessage());
		}
		
		// draw the shape
		Graphics2D g2 = (Graphics2D)g;
		// enable antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.decode("0x" + this.color.substring(1)));
		g2.drawOval(xOrigin, yOrigin, width, width);
		g2.fillOval(xOrigin, yOrigin, width, width);
	}

	/**
	 * Method for getting the radius of circle
	 * @return the radius
	 * @throws UninitializedPointException 
	 */
	public double getRadius() throws UninitializedPointException {
		// return radius, or throw UninitializedPointException if operation cannot be performed
		try {
			// use pythagorean theorem to get radius using the points
			return Math.sqrt(Math.pow(Math.abs(points.get(0).getX() - points.get(1).getX()), 2)
							 + Math.pow(Math.abs(points.get(0).getY() - points.get(1).getY()), 2));
		} catch(Exception e) {
			throw new UninitializedPointException("error in Circle.getRadius()");
		}
	}

	@Override
	public double getCircumference() throws UninitializedPointException {
		try {
			return this.getRadius() * 2 * PI;
		} catch(UninitializedPointException e) {
			// pass the exception up the calling chain
			String message = "error in Circle.getCircumference, from: ";
			throw new UninitializedPointException(message + e.getMessage());
		}
	}

	@Override
	public double getArea() throws UninitializedPointException {
		try {
			return Math.pow(this.getRadius(), 2) * PI;
		} catch(UninitializedPointException e) {
			String message = "error in Circle.getArea, from: ";
			throw new UninitializedPointException(message + e.getMessage());
		}
	}

	@Override
	public String toString() {
		try {
			return "Circle [start=" + points.get(0).toString() + "; end="
				   + points.get(1).toString() + "; radius=" + this.getRadius()
				   + "; color=" + color + "]";
		} catch(Exception e) {
			return "Circle [start=" + points.get(0).toString() + "; end="
				   + "N/A; radius=N/A; color=" + color + "]";
		}
	}
}
