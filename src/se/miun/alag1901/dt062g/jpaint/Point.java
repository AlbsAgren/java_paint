package se.miun.alag1901.dt062g.jpaint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* <h1>Point</h1>
* Class for representing a point in a two-dimensional coordinate system.
*
* @author  Albin Ågren (alag1901)
* @version 1.1
* @since   2020-12-05
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Point {
	@XmlElement(name="x")
	private double xCoord;
	@XmlElement(name="y")
	private double yCoord;

	/**
	 * Default constructor
	 */
	public Point() {
		xCoord = 0;
		yCoord = 0;
	}

	/**
	 * Constructor for initializing coordinate variables
	 * @param pX, the x coordinate
	 * @param pY, the y coordinate
	 */
	public Point(double pX, double pY) {
		xCoord = pX;
		yCoord = pY;
	}

	/**
	 * Method for getting the x coordinate
	 * @return the x coordinate
	 */
	public double getX() {
		return xCoord;
	}

	/**
	 * Method for setting the x coordinate
	 * @param pX, the x coordinate
	 */
	public void setX(double pX) {
		xCoord = pX;
	}

	/**
	 * Method for getting the y coordinate
	 * @return the y coordinate
	 */
	public double getY() {
		return yCoord;
	}

	/**
	 * Method for setting the x coordinate
	 * @param pY, the Y coordinate
	 */
	public void setY(double pY) {
		yCoord = pY;
	}

	@Override
	public String toString() {
		return "(" + xCoord + ", " + yCoord + ")";
	}

}
