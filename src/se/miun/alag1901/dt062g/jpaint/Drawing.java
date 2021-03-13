package se.miun.alag1901.dt062g.jpaint;

import java.awt.Graphics;

import java.util.LinkedList;
import java.util.stream.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* <h1>Drawing</h1>
* Class representing a drawable collection of shapes.
*
* @author  Albin Ågren (alag1901)
* @version 1.3
* @since   2021-01-10
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Drawing implements Drawable {
	@XmlElement
	private String name;
	@XmlElement
	private String author;
	@XmlElement(name="shape")
	private LinkedList<Shape> shapes;

	/**
	 * Default constructor
	 */
	public Drawing() {
		shapes = new LinkedList<Shape>();
		name = "";
		author = "";
	}
	
	/**
	 * Constructor for initializing name and author
	 * @param pName, the name of the drawing
	 * @param pAuthor, the author of the drawing
	 */
	public Drawing(String pName, String pAuthor) {
		name = pName;
		author = pAuthor;
		shapes = new LinkedList<Shape>();
	}
	
	/**
	 * Method for getting drawing name
	 * @return, the name of the drawing
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method for setting drawing name
	 * @param pName, the name of the drawing
	 */
	public void setName(String pName) {
		name = pName;
	}
	
	/**
	 * Method for getting drawing author
	 * @return, the name of the author
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Method for setting drawing author
	 * @param pAuthor, the name of the author
	 */
	public void setAuthor(String pAuthor) {
		author = pAuthor;
	}

	/**
	 * Method for adding new shape to drawing, null shapes will be ignored
	 * @param pShape, the shape object to be added
	 */
	public void addShape(Shape pShape) {
		if(pShape != null) {
			shapes.add(pShape);
		}
	}
	
	/**
	 * Method for getting number of drawings in shape
	 * @return, the number of drawings
	 */
	public int getSize() {
		return shapes.size();
	}
	
	/**
	 * Method for getting total circumference of all shapes
	 * @return, the total circumference
	 */
	public double getTotalCircumference() {
		// use mapToDouble to convert stream to DoubleStream, then use sum to get total circumference
		return shapes.stream().mapToDouble(shape -> {
			try {
				return shape.getCircumference();
			} catch(UninitializedPointException e) {
				return 0;
			}
		}).sum();
	}
	
	/**
	 * Method for getting total area of all shapes
	 * @return, the total area
	 */
	public double getTotalArea() {
		return shapes.stream().mapToDouble(shape -> {
			try {
				return shape.getArea();
			} catch(UninitializedPointException e) {
				return 0;
			}
		}).sum();
	}
	
	/**
	 * Method for clearing all drawing data
	 */
	public void clear() {
		shapes.clear();
		name = "";
		author = "";
	}
	
	/**
	 * Method for getting painting default filename on the form [name] by [author] 
	 * @return a string with the suggested filename 
	 */
	public String getByline() {
		if(!name.isBlank()) {
			if(!author.isBlank()) {
				return name + " by " + author;
			}
			return name;
		} else if(!author.isBlank()) {
			return "[unnamed drawing]" + "by" + author;
		}
		return "";
	}
	
	/**
	 * Method for getting the list of shapes that compose the drawing
	 * @return the list of shapes
	 */
	public LinkedList<Shape> getShapes() {
		return this.shapes;
	}
	
	/**
	 * Method for deleting the most recently added shapes of the drawing
	 * @param noToDelete, the number of shapes to delete
	 */
	public void deleteLast(int nrToDelete) {
		if(!this.shapes.isEmpty()) {
			for(int i = 0; i < nrToDelete; ++i) {
				this.shapes.removeLast();
			}
		}
	}
	
	/**
	 * Method for checking if drawing is empty, drawing is considered empty if
	 * it has no shapes, no author and no name
	 * @return true if drawing is empty, false if not
	 */
	public boolean isEmpty() {
		return this.shapes.isEmpty() && this.getByline().isBlank();
	}
	
	@Override
	public void draw() {
		System.out.println("A drawing by " + author + " called " + name);
		
		for(Shape shape : shapes) {
			System.out.println(shape.toString());
		}
	}

	@Override
	public void draw(Graphics g) {
		for(Shape shape : shapes) {
			shape.draw(g);
		}
	}

	@Override
	public String toString() {
		return "Drawing[name=" + name + "; author=" + author + "; size="
			   + this.getSize() + "; circumference=" 
			   + this.getTotalCircumference() + "; area=" + this.getTotalArea()
			   + "]";
	}
}
