
// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	private Socket client = null;
	private ServerSocket server = null;

	public Server(int port) {

		HashMap<String, String> users = new HashMap<String, String>();
		users.put("danny", "dre@margh_shelled");
		users.put("matty", "win&win99");

		ArrayList<ClientObject> clients = new ArrayList<>();

		Boolean gameInSession = false;

		try {
			server = new ServerSocket(port);
			System.out.println("Waiting for a client ...");

			while (!gameInSession) {

				if (clients.size() == 2) {
					gameInSession = true;
					for (int i = 0; i < 5; i++) {

						this.setRandomSpotter(clients);
						clients.get(0).startGame();
						clients.get(1).startGame();

						int choiceOne = clients.get(0).getChoice();
						int choiceTwo = clients.get(1).getChoice();

					}

				} else {

					client = server.accept();
					System.out.println("Connection established");

					BufferedReader read = new BufferedReader(new InputStreamReader(client.getInputStream()));

					String username = read.readLine();
					String password = read.readLine();

					if (users.containsKey(username)) {
						if (password.equals(users.get(username))) {
							System.out.println("Login Successful");

							ClientObject newClient = this.createNewClientObject(client, username);
							newClient.start();
							
							clients.add(newClient);

						}

					} else {
						System.out.println("Login Failed");
					}
				}

			}

		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public ClientObject createNewClientObject(Socket client, String username) {
		ClientObject newThread = new ClientObject(client, null, null, username);

		try {
			DataInputStream inputStream = new DataInputStream(client.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

			newThread = new ClientObject(client, inputStream, outputStream, username);
		} catch (IOException i) {
			System.out.println(i);
		}

		return newThread;
	}

	public void setRandomSpotter(ArrayList<ClientObject> clients) {
		double randomDouble = Math.random();
		int randomIndex = (int) Math.round(randomDouble);
		clients.get(randomIndex).makePlayerTheSpotter();
	}

	public static void main(String args[]) {
		Server server = new Server(7621);
	}
}

class ClientObject extends Thread {

	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String username;
	int count = 0;
	boolean spotter = false;
	boolean winner = false;

	public ClientObject(Socket socket, DataInputStream input, DataOutputStream output, String username) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.username = username;
	}

	public void start() {
		System.out.println(this.username + " is connected");
	}

	public void incrementCount() {
		this.count++;
	}

	public int getCount() {
		return this.count;
	}

	public void makePlayerTheSpotter() {
		this.spotter = true;
	}

	public void makePlayerTheDealer() {
		this.spotter = false;
	}

	public void setWinner() {
		this.winner = true;
	}

	public void startGame() {
		try {	 
			this.output.writeBoolean(true);
		} catch (IOException i) {
			System.out.println(i);
		}
	}

	public int getChoice(){
		int choice = 0;
		try {
			choice = this.input.readInt();
		} catch (IOException i) {
			System.out.println(i);
		}
		return choice;

	}

}