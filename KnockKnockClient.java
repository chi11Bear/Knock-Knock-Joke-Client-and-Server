/**
 * KnockKnockClient.java
 * 
 * Demonstrates how to use client-side sockets.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * @author darren.joyner
 *
 */


public class KnockKnockClient {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// check command line args
		if (args.length != 2 || (Integer.parseInt(args[1]) < 0)) {
			System.err.println("Error: Port parameter must be an integer");
			System.err.println("Usage: java SSClient <hostname> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = -1;
		try {
			portNumber = Integer.parseInt(args[1]);

			//connecting to socket
			Socket socket = new Socket(hostName, portNumber);

			//setting up reader/writer 
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			//sending/recieving messages 
			if (socket.isConnected()) {
				System.out.println();
				System.out.println("Tell me a Joke");
				System.out.println();
				out.println("Tell me a Joke.");
				while (true){
					String ask = in.readLine();
					if (ask.contains("Knock Knock.")) {
						System.out.println("Who's there?");
						System.out.println();
						out.println("Who's there?");
					} else if (!ask.contains(".") && !ask.contains("?") && !ask.contains("!")) {
						System.out.println(ask + " Who?");
						out.println("Who?");
					}
					if (ask.contains("Bye.")) {
						break;		
					}
				}
			} 
		
			//closing connections and terminating
		    in.close();
			out.close();
			socket.close();
			System.exit(0);

		} catch (NumberFormatException nfe) {
			System.err.println("Invalid port number");
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println("" + ioe.getMessage());
			System.exit(1);						
		}
	}
}