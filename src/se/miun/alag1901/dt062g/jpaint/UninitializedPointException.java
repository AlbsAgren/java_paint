package se.miun.alag1901.dt062g.jpaint;

/**
* <h1>UninitializedPointException</h1>
* Class for handling exceptions arising with the calculation of shape dimensions.
*
* @author  Albin �gren (alag1901)
* @version 1.0
* @since   2020-11-22
*/
public class UninitializedPointException extends Exception {
	private static final long serialVersionUID = 1;
	
	/**
	 * Default constructor
	 */
	public UninitializedPointException() {
		// blank
	}

	/**
	 * Constructor taking a string describing the nature of the exception
	 * @param pMessage, a string describing the nature of the exception
	 */
	public UninitializedPointException(String pMessage) {
		super(pMessage);
	}

	/**
	 * Constructor taking another exception object to pass up the calling chain
	 * @param pCause, an exception object with the cause of the exception
	 */
	public UninitializedPointException(Throwable pCause) {
		super(pCause);
	}
}
