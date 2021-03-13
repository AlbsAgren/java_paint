package se.miun.alag1901.dt062g.jpaint.server;

import java.net.*;

/**
* <h1>Server</h1>
* Class for listening to client requests on a specified port
*
* @author  Albin Ågren (alag1901)
* @version 1.0
* @since   2020-12-30
*/
public class Server {
	private static int port = 10000;

	public static void main(String[] args) {
		// use other port if provided as argument at program start
		if(args.length != 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				System.out.println("Invalid port argument, using default (10000)");
			}
		}

		try {
			listen();
		} catch(Exception e) {
			System.out.println("Error in server: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method for listening for incoming connections
	 * @throws Exception
	 */
	private static void listen() throws Exception {
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Server listening on port: " + port);
		
		// start a new ClientHandler thread on client connections
		while(true) {
			Socket socket = ss.accept();
			new ClientHandler(socket).start();
		}
	}
}
