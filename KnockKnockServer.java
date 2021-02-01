
/**
 * KnockKnockServer.java
 * 
 * Demonstrates how to use server-side sockets.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * @author darren.joyner
 * programming assignment #1
 */


public class KnockKnockServer {
	
	/**
	* @param args
	*/
	
	
	private static ServerSocket serverSocket;
	static Random random;


	public static void main(String[] args) {
		try{
			int portNumber = -1;
		
			// check command line args
			if (args.length != 3 ||  (Integer.parseInt(args[0]) < 0)) {
				System.err.println("Error: incorrect number of arguments");
				System.err.println("Error: Port parameter must be an integer");
				System.err.println("Usage: java KnockKnock <port number> <text file name> <seed value>");
				System.exit(1);
			}

			portNumber = Integer.parseInt(args[0]);
			
			//read txt file to list
			String strLine;
			List<String> list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(args[1]));
			while ((strLine = br.readLine()) != null)
			{
				list.add(strLine);
			}
			br.close();

			serverSocket = new ServerSocket(portNumber);
			
			//selecting a random line and seed
			random = new Random();
			random.setSeed(Long.parseLong(args[2]));
			while (true) {
				int index = random.nextInt(list.size() + 1);
				//random.setSeed(Long.parseLong(args[2]));

				//preparing message to be send
				String joke = list.get(index);  
				String[] arrOfJoke = joke.split("#", 2);
				String setUpLine = arrOfJoke[0].replaceAll("\\s", "");
				
				//setting up socket ann listening
				System.out.println("The server is listening at: " + 
				serverSocket.getInetAddress() + " on port " + 
				serverSocket.getLocalPort());
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client accepted");
		
				//setting up reader/writer 
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				
				//sending/recieving messages 
				while(true) {
					String request = in.readLine();
					if (request.contains("Tell me a Joke.")) {
						System.out.println();
						System.out.println("Knock Knock");
						out.println("Knock Knock.");
					} else if (request.contains("Who's there?")) {
						System.out.println();
						System.out.println(arrOfJoke[0]);
						out.println(setUpLine);
					} else if (request.contains("Who?")) {
						System.out.println();
						System.out.println(arrOfJoke[1].trim());
						System.out.println("Bye");
						out.println("Bye.");
						System.out.println();
						break;
					}
				}
	
				//closing current connection
				in.close();
				out.close();
				clientSocket.close();	
				
				//break socket connection, 
				//and terminate program
				serverSocket.close();
				System.exit(0);
			}	
		} catch (IOException ioe) {
			System.err.println("" + ioe.getMessage());
			System.exit(1);			
		} catch(NumberFormatException nfe){
			System.err.println("Error: Port parameter must be an integer");
			System.err.println("Usage: ensure port number and/or seed values are positive numbers");
			System.exit(1);
      	} 
	}
}