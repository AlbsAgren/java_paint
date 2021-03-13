package se.miun.alag1901.dt062g.jpaint;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
* <h1>File Handler</h1>
* Class for saving Drawing objects to file using XML format.
*
* @author  Albin Ågren (alag1901)
* @version 1.1
* @since   2020-12-20
*/
public class FileHandler {

	/**
	 * Method for saving Drawing object to file
	 * @param drawing, the drawing to be saved
	 * @param fileName, the name of the savefile
	 */
	public static void saveToXML(Drawing drawing, String fileName) {
		// append .xml if nessecary
		if(!fileName.endsWith(".xml")) {
			fileName = fileName + ".xml";
		}
		
		// handle JAXBExceptions
		try {
			// create new context and marshaller for Drawing class
			JAXBContext context = JAXBContext.newInstance(Drawing.class);		
			Marshaller marshaller = context.createMarshaller();
	
			// use indentation for greater readability
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	
			// output to file
			marshaller.marshal(drawing, new File(fileName));
		} catch(JAXBException je) {
			System.out.println("failed to save, exception in FileHandler: " + je.getMessage());
		}

	}
	
	/**
	 * Method for saving drawing object to file, generates a filename from drawing data
	 * @param drawing, the drawing to be saved
	 */
	public static void saveToXML(Drawing drawing) {
		// generate filename and use other saveToXML method to save drawing
		String fileName = drawing.getName() + " by " + drawing.getAuthor() + ".xml";
		saveToXML(drawing, fileName);
	}
	
	/**
	 * Method for loading a drawing from an XML file
	 * @param fileName, the name of the XML file
	 * @return, a Drawing object
	 */
	public static Drawing loadFromXML(String fileName) {
		Drawing drawing = null;		// empty drawing to save to
		
		// handle exceptions while attempting to read file
		try {
			// create new context and unmarshaller for Drawing class
			JAXBContext context = JAXBContext.newInstance(Drawing.class);		
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			// read drawing data from XML file
			drawing = (Drawing)unmarshaller.unmarshal(new File(fileName));
		} catch(JAXBException je) {
			System.out.println("file not read, exception in FileHandler: " + je.getMessage());
		} catch (IllegalArgumentException ie) {
			System.out.println("file not read, exception in FileHandler: " + ie.getMessage());	
		}
		return drawing;
	}
}
