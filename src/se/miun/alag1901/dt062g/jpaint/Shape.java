package se.miun.alag1901.dt062g.jpaint;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
* <h1>Shape</h1>
* Abstract super class for representing various geometric figures.
*
* @author  Albin Ågren (alag1901)
* @version 1.3
* @since   2020-12-05
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlSeeAlso({ Circle.class, Rectangle.class })
public abstract class Shape implements Drawable {
	@XmlElement
	protected String color;
	@XmlElement(name="point")
	protected ArrayList<Point> points;

	/**
	 * Default constructor
	 */
	public Shape() {
		points = new ArrayList<Point>();
		color = "";
	}
	
	/**
	 * Constructor for initializing shape using doubles to represent points
	 * @param pX, an x coordinate
	 * @param pY, a y coordinate
	 * @param pColor, the color of the shape
	 */
	public Shape(double pX, double pY, String pColor) {
		// create array and assigning new point with specified coordinates
		points = new ArrayList<Point>();
		points.add(new Point(pX, pY));
		
		color = pColor;
	}

	/**
	 * Constructor for initializing shape using a Point object
	 * @param pPoint, a Point object
	 * @param pColor, the color of the shape
	 */
	public Shape(Point pPoint, String pColor) {
		points = new ArrayList<Point>();
		points.add(pPoint);
		
		color = pColor;
	}

	/**
	 * Method for getting shape color
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Method for setting shape color
	 * @param pColor, the color
	 */
	public void setColor(String pColor) {
		color = pColor;
	}

	/**
	 * Method for getting shape circumference
	 * @return the circumference
	 * @throws UninitializedPointException
	 */
	public abstract double getCircumference() throws UninitializedPointException;

	/**
	 * Method for getting shape area
	 * @return the area
	 * @throws UninitializedPointException
	 */
	public abstract double getArea() throws UninitializedPointException;

	/**
	 * Method for setting the second shape point using a Point object
	 * @param pPoint, a point object
	 */
	public void addPoint(Point pPoint) {
		if(points.size() == 1) {
			points.add(pPoint);
		} else {
			points.set(1, pPoint);
		}
	}

	/**
	 * Method for setting the second shape point using two doubles
	 * @param pX, the x coordinate
	 * @param pY, the y coordinate
	 */
	public void addPoint(double pX, double pY) {
		if(points.size() == 1) {
			points.add(new Point(pX, pY));
		} else {
			points.set(1, new Point(pX, pY));
		}
	}
}
