package se.miun.alag1901.dt062g.jpaint.client;

import java.net.*;
import java.io.*;

/**
* <h1>Client</h1>
* Class for interacting with a server managing xml files
*
* @author  Albin Ågren (alag1901)
* @version 1.0
* @since   2020-12-30
*/
public class Client {
	public static final String DEFAULT_ADDRESS = "localhost";
	public static final int DEFAULT_PORT = 10000;
	
	private String serverAddress;
	private int serverPort;
	
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	
	/**
	 * Constructor
	 * @param serverAddress, the address of the server
	 * @param serverPort, the port of the server
	 */
	public Client(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;

		this.socket = null;
		this.out = null;
		this.in = null;
	}
	
	/**
	 * Default constructor
	 */
	public Client() {
		this.serverAddress = DEFAULT_ADDRESS;
		this.serverPort = DEFAULT_PORT;

		this.socket = null;
		this.out = null;
		this.in = null;
	}
	
	/**
	 * Method for establishing a connection with the server
	 * @return a boolean indicating if connection was successfull
	 */
	public boolean connect() {
		if(this.socket != null) {
			return false;
		}
		
		// try to open socket and streams, return false on failure
		try {
			this.socket = new Socket(serverAddress, serverPort);
			this.out = new DataOutputStream(socket.getOutputStream());
			this.in = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Method for disconnecting from server
	 */
	public void disconnect() {
		try {
			if(this.out != null) { 
				this.out.close();
				this.out = null; 
			}
			if(this.in != null) { 
				this.in.close();
				this.in = null;
			}			
			this.socket.close();
			this.socket = null;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method for getting list of available files on server
	 * @return an array of the names of the available files
	 */
	public String[] getFilenamesFromServer() {
		// return null on failure to connect
		if(!this.connect()) {
			return null;
		}
		
		String[] fileNames;
		try {
			// send command to server
			this.out.writeUTF("list");
			
			// create array of appropriate length
			fileNames = new String[this.in.readInt()];

			// read fileNames sent from server
			for(int i = 0; i < fileNames.length; ++i) {
				fileNames[i] = this.in.readUTF();
			}
			
			// disconnect after reading data and return the retrieved file names
			this.disconnect();
			return fileNames;

		} catch(IOException e) {
			// print stack trace for debugging and return null if read fails
			e.printStackTrace();;
			return null;
		}
	}
	
	/**
	 * Method for retrieving a file from the server
	 * @param fileName, the name of the desired file
	 * @return the absolute path to the retrieved file
	 */
	public String getFileFromServer(String fileName) {
		if(!this.connect()) {
			return null;
		}
		
		try {
			this.out.writeUTF("load");
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			this.out.writeUTF(fileName);

			// return empty string if file not found
			if(!this.in.readUTF().equals("OK")) {
				return "";
			}			
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}

		// save to user downloads folder (might only work on linux)
		String filePath = System.getProperty("user.home") + "/Downloads/" + fileName;
		FileOutputStream file;
		try {
			file = new FileOutputStream(filePath);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		// get file from server using bytearray as buffer 
		// pattern adapted from https://stackoverflow.com/a/9526674
		byte[] buffer = new byte[8192];
		int count;
		try {
			while((count = in.read(buffer)) > 0) {
				file.write(buffer, 0, count);
			}
			file.close();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		this.disconnect();
		return filePath;
	}
	
	/**
	 * Method for saving file to server
	 * @param fileName, the name of the file to be saved
	 * @param saveAs, the desired name of the file on the server
	 * @return a boolean indicating if the operation was successful
	 */
	public boolean saveAsFileToServer(String fileName, String saveAs) {
		if(!this.connect()) {
			return false;
		}
		
		try {
			this.out.writeUTF("save");
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// try to open requested file, inform client if successful
		FileInputStream file;
		try {
			file = new FileInputStream(new File("./" + fileName));
			out.writeUTF(saveAs);	// send desired filename to server
			
			// return false if server cant process request
			if(!in.readUTF().equals("OK")) {
				file.close();
				return false;
			}
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// same basic pattern as ClientHandler.loadFile()
		byte[] buffer = new byte[8192];	
		int count;
		try {
			while((count = file.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			file.close();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		// if successful, return true
		this.disconnect();
		return true;
	}
	
	/**
	 * Method for saving a file to server
	 * @param fileName, the name of the file to be saved
	 * @return a boolean indicating if the operation was successful
	 */
	public boolean saveFileToServer(String fileName) {
		return saveAsFileToServer(fileName, fileName);
	}
}