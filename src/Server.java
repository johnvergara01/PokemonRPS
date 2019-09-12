import java.net.*;
import java.io.*;
/**
 * server class, takes in data from client, determines move to make and sends to client
 * @author John
 *
 */
public class Server extends Thread {
	/**
	 * server socket of server
	 */
	private ServerSocket server;
	
	/**
	 * socket of server
	 */
	private Socket sock;
	
	/**
	 * reads in data from client
	 */
	private BufferedReader read;
	
	/**
	 * sends data to client
	 */
	private PrintStream write;
	
	/**
	 * computer class, determines move to make
	 */
	private Computer comp;
	
	/**
	 * computer's move as a string
	 */
	private String enemyInput;
	
	/**
	 * file to read in "computer.dat", saves hashmap
	 */
	private File f;
	
	/**
	 * default constructor
	 */
	public Server()
	{
		// creates server socket and waits for connection
		try
		{
			server = new ServerSocket(1235);
			System.out.println("Waiting");
			sock = server.accept();
			read = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			write = new PrintStream(sock.getOutputStream());
			System.out.println("Connected");
		}
		catch(IOException e){}
		
		// load in previous hashmap if it exists
		f = new File("computer.dat");
		if(f.exists())
		{
			try
			{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
				comp = (Computer) in.readObject();
				in.close();
			}
			catch(IOException e)
			{
				System.out.println("Error processing file");
			}
			catch(ClassNotFoundException e){}
		}
		else
		{
			comp = new Computer();
		}
	}
	
	/**
	 * main thread of server, makes a prediction and sends data to client
	 */
	public void run()
	{
		try
		{
			while(true)
			{
				// makes prediction
				enemyInput = comp.makePrediction();
				// reads in client data
				String playerInput = read.readLine();
				// stores data
				comp.storeHistory(playerInput);
				comp.storePattern();
				// send prediction to client
				write.println(enemyInput);
				// saves file to computer
				try
				{
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
					out.writeObject(comp);
					out.close();
				}
				catch(IOException e){}
			}
		}
		catch (IOException e) {}
	}

	/**
	 * main of program, creates and runs the server
	 * @param args needed for main
	 */
	public static void main(String[] args) 
	{
		Server serv = new Server();
		serv.start();
	}
}
