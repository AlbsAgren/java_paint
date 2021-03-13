package se.miun.alag1901.dt062g.jpaint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.xml.bind.annotation.XmlRootElement;

/**
* <h1>Rectangle</h1>
* Class representing a rectangle shape.
*
* @author  Albin Ågren (alag1901)
* @version 1.3
* @since   2020-12-05
*/
@XmlRootElement
public class Rectangle extends Shape {
	/**
	 * Default constructor
	 */
	public Rectangle() {
		super();
	}
	
	/**
	 * Constructor initializing a Rectangle object using doubles to represent
	 * points.
	 * @param pX, the x coordinate
	 * @param pY, the y coordinate
	 * @param pColor, the color
	 */
	public Rectangle(double pX, double pY, String pColor) {
		// call super class constructor
		super(pX, pY, pColor);
	}

	/**
	 * Constructor initializing Rectangle using a Point object
	 * @param pPoint, a point object
	 * @param pColor, the color
	 */
	public Rectangle(Point pPoint, String pColor) {
		super(pPoint, pColor);
	}

	/**
	 * Method for getting width of rectangle
	 * @return the width
	 * @throws UninitializedPointException
	 */
	public double getWidth() throws UninitializedPointException {
		// return height (distance between the points in the x dimension),
		// or -1 if operation failed
		try {
			return Math.abs(points.get(0).getX() - points.get(1).getX());
		} catch(Exception e) {
			throw new UninitializedPointException("error in Rectangle.getWidth()");
		}
	}

	/**
	 * Method for getting height of rectangle
	 * @return the height
	 * @throws UninitializedPointException
	 */
	public double getHeight() throws UninitializedPointException {
		try {
			return Math.abs(points.get(0).getY() - points.get(1).getY());
		} catch(Exception e) {
			throw new UninitializedPointException("error in Rectangle.getHeight()");
		}
	}

	@Override
	public void draw() {
		System.out.println(this.toString());
	}

	@Override
	public void draw(Graphics g) {
		Point p1 = super.points.get(0);
		Point p2;
		
		// in rare cases user may begin painting out of bounds, set p2 to upper left corner by default
		try {
			p2 = super.points.get(1);
		} catch(IndexOutOfBoundsException ie) {
			p2 = new Point(0, 0);
		}
		
		// set upper left corner to the lowest coordinate in either dimension
		Point upperLeftCorner = new Point();
		upperLeftCorner.setX(p1.getX() < p2.getX() ? p1.getX() : p2.getX());
		upperLeftCorner.setY(p1.getY() < p2.getY() ? p1.getY() : p2.getY());

		// get width
		int width = 0, height = 0;
		try {
			width = (int)(this.getWidth());
			height = (int)(this.getHeight());
		} catch(UninitializedPointException ue) {
			System.err.println("UninitializedPointException in Rectangle.draw(): " + ue.getMessage());
		}
		
		// draw the shape
		Graphics2D g2 = (Graphics2D)g;
		g2.setPaint(Color.decode("0x" + this.color.substring(1)));
		g2.drawRect((int)(upperLeftCorner.getX()), (int)(upperLeftCorner.getY()), width, height);
		g2.fillRect((int)(upperLeftCorner.getX()), (int)(upperLeftCorner.getY()), width, height);
	}

	@Override
	public double getCircumference() throws UninitializedPointException {
		// check if operation can be performed, throw exception if not
		try {
			return 2 * this.getHeight() + 2 * this.getWidth();
		} catch(UninitializedPointException e) {
			String message = "error in Rectangle.getCircumference, from: ";
			throw new UninitializedPointException(message + e.getMessage());
		}
	}

	@Override
	public double getArea() throws UninitializedPointException {
		try {
			return this.getHeight() * this.getWidth();
		} catch(UninitializedPointException e) {
			String message = "error in Rectangle.getArea, from: ";
			throw new UninitializedPointException(message + e.getMessage());
		}
	}

	@Override
	public String toString() {
		// return a string with data about the rectangle 
		try {
			return "Rectangle [start=" + points.get(0).toString() 
				   + "; end=" + points.get(1).toString()
				   + "; width=" + this.getWidth()
				   + "; height=" + this.getHeight()
				   + "; color=" + color + "]";
		} catch(Exception e) {
			return "Rectangle [start=" + points.get(0).toString() + "; end="
					   + "N/A; width=N/A; height=N/A; color=" + color + "]";
		}
	}
}
