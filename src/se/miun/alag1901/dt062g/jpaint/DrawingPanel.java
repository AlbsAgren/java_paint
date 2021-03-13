package se.miun.alag1901.dt062g.jpaint;

import javax.swing.JPanel;

import java.awt.Graphics;

/**
* <h1>DrawingPanel</h1>
* Class extending JPanel to allow user to display and create drawing based on 
* basic geometric shapes
*
* @author  Albin Ågren (alag1901)
* @version 1.0
* @since   2020-12-20
*/
public class DrawingPanel extends JPanel {
	private static final long serialVersionUID = 1;
	private Drawing drawing;
	
	/**
	 * Default constructor
	 */
	DrawingPanel() {
		drawing = new Drawing();
	}
	
	/**
	 * Constructor for initiating panel with preexisting drawing
	 * @param drawing, the drawing to be displayed
	 */
	DrawingPanel(Drawing drawing) {
		// clear previous drawings
		super.paintComponent(null);
		
		this.drawing = drawing;
		
		this.repaint();
	}
	
	/**
	 * Method for getting current drawing
	 * @return, the current drawing
	 */
	public Drawing getDrawing() {
		return drawing;
	}
	
	/**
	 * Method for setting the current drawing
	 * @param drawing, the drawing to be set
	 */
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
		
		// repaint based on new drawing
		this.repaint();
	}
	
	/**
	 * Method for adding a drawing to the panel, keeps the name and author of the current
	 * drawing but includes the shapes of both
	 * @param drawing, the drawing to be added
	 */
	public void addDrawing(Drawing drawing) {
		for(Shape shape : drawing.getShapes()) {
			this.drawing.addShape(shape);
		}
		
		this.repaint();
	}
	
	/**
	 * Method for undoing the last added shapes
	 * @param noToUndo, the number of shapes to undo
	 */
	public void undo(int nrToUndo) {
		this.drawing.deleteLast(nrToUndo);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(this.drawing != null) {
			this.drawing.draw(g);
		}
	}
}
