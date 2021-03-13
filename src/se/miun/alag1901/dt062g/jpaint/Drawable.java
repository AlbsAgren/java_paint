package se.miun.alag1901.dt062g.jpaint;

/**
* <h1>Drawable</h1>
* Interface for managing the "rendering" of drawable objects.
*
* @author  Albin �gren (alag1901)
* @version 1.0
* @since   2020-11-14
*/
public interface Drawable {
	/**
	 * Method for drawing a shape
	 */
	void draw();

	/**
	 * Method for drawing a shape
	 * @param g, a Graphics object
	 */
	void draw(java.awt.Graphics g);
}
