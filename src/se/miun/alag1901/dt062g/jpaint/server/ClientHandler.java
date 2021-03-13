package se.miun.alag1901.dt062g.jpaint.server;

import java.net.*;
import java.io.*;

/**
* <h1>ClientHandler</h1>
* Class for managing client requests in a separate thread from the main server thread
*
* @author  Albin Ågren (alag1901)
* @version 1.1
* @since   2021-01-10
*/
public class ClientHandler extends Thread {
	private Socket socket;	
	private String address;
	
	/**
	 * Constructor
	 * @param socket, a socket object with an established connection
	 */
	public ClientHandler(Socket socket) {
		this.socket = socket;
		this.address = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}
	
	@Override
	public void run() {
		DataOutputStream out = null;
		DataInputStream in = null;
		
		System.out.println("New client connected from: " + this.address);
	
		// open streams
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {
			System.out.println("Failed to open streams for connection to " +
					this.address + "err: " + e.getMessage());
			e.printStackTrace();
			
			// close streams and socket and return
			closeConnection(out, in);
			return;
		}
		
		String command;
		try {
			command = in.readUTF();
			System.out.println("Command '" + command + "' recieved from client at " + this.address);
		} catch(IOException e) {
			System.out.println("Failed to read command from from client at " +
					this.address + "err: " + e.getMessage());
			e.printStackTrace();
			
			closeConnection(out, in);
			return;
		}
		
		// perform requested action
		switch(command) {
			case "list": {
				try {
					listFiles(out, in);
				} catch(IOException e) {
					System.out.println("Failed to list files for client at " +
							this.address + " err: " + e.getMessage());
					e.printStackTrace();
				}
				break;
				
			}
			case "load": {
				try {
					loadFile(out, in);
				} catch(IOException e) {
					System.out.println("Failed to load file for client at " +
							this.address + " err: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "save": {
				try {
					saveFile(out, in);
				} catch(IOException e) {
					System.out.println("Failed to save file for client at " +
							this.address + " err: " + e.getMessage());
					e.printStackTrace();
				}
				break;				
			}
			default: {
				System.out.println("Invalid command: " + command + " from client at " + 
						this.address);
			}
		}
		// close connection after each command has been executed
		closeConnection(out, in);
	}
	
	/**
	 * Method for providing client with a list of available files
	 * @param out, an output stream object to the client
	 * @param in, an input stream object from the client
	 * @throws IOException
	 */
	private void listFiles(DataOutputStream out, DataInputStream in) throws IOException {
		File directory = new File("xml");
		
		// use list to filter out non-xml files
		String[] xmlFiles = directory.list((dir, name) -> {
			return name.endsWith(".xml");
		});
		
		// send length of array to client before sending actual data
		int len = xmlFiles == null ? 0 : xmlFiles.length;
		out.writeInt(len);
		
		System.out.println("Sedning list of files to client at " + this.address);
		
		// send the matching filenames
		for(int i = 0; i < len; ++i) {
			out.writeUTF(xmlFiles[i]);
		}
	}

	/**
	 * Method for providing client with a requested file
	 * @param out, an output stream object to the client
	 * @param in, an input stream object from the client
	 * @throws IOException
	 */
	private void loadFile(DataOutputStream out, DataInputStream in) throws IOException {
		String fileName;
		FileInputStream fileInput;
		
		// client first sends name of desired file
		fileName = in.readUTF();
		
		// try to open requested file, inform client if successful
		try {
			fileInput = new FileInputStream(new File("./xml/" + fileName));
			out.writeUTF("OK");
		} catch(FileNotFoundException e) {
			System.out.println("File " + fileName + " requested by client at " + this.address + " not found");
			out.writeUTF("NOT FOUND");
			return;
		}
		
		// pattern adapted from: https://stackoverflow.com/a/9526674
		byte[] buffer = new byte[8192];	// use bytearray as buffer

		int count;	
		// read from fileInput to buffer, and then write from buffer to DataOutputStream
		System.out.println("Sending file " + fileName + " to client at " + this.address);
		while((count = fileInput.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		
		fileInput.close();
	}

	/**
	 * Method for saving client-supplied file to server
	 * @param out, an output stream object to the client
	 * @param in, an input stream object from the client
	 * @throws IOException
	 */
	public void saveFile(DataOutputStream out, DataInputStream in) throws IOException {
		String fileName = in.readUTF();
		FileOutputStream fileOutput = new FileOutputStream("./xml/" + fileName);
		
		// if outputstream sucessfully opened (i.e. no exception thrown), send go-ahead to server
		out.writeUTF("OK");
		
		System.out.println("Saving file " + fileName + " for client at " + this.address);
		
		// get file from InputStream and save locally, same pattern as Client.getFileFromServer()
		byte[] buffer = new byte[8192];
		int count;
	
		while((count = in.read(buffer)) > 0) {
			fileOutput.write(buffer, 0, count);
		}
		fileOutput.close();
	}
	
	/**
	 * Method for terminating connection to client and closing associated streams
	 * @param out, an output stream object to the client
	 * @param in, an input stream object from the client
	 */
	private void closeConnection(DataOutputStream out, DataInputStream in) {
		try {
			System.out.println("Client from " + this.address + " has disconnected");			
			
			if(out != null) { out.close(); }
			if(in != null) { in.close(); }
			this.socket.close();
		} catch(IOException e) {
			System.err.println("Failed to close connection from client at " + 
					this.address + " err: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
