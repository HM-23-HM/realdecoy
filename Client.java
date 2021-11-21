
// A Java program for a Client
import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream gameReader;
	private DataOutputStream gameWriter;
	PrintWriter loginWriter;
	BufferedReader loginReader;

	// constructor to put ip address and port
	public Client(String address, int port) {

		Boolean gameInSession = false;
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");

			loginWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			// loginReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String username = JOptionPane.showInputDialog(null, "Enter Name");

			loginWriter.println(username);

			Box passwordBox = Box.createHorizontalBox();
			JLabel label = new JLabel("Password: ");
			passwordBox.add(label);

			JPasswordField passwordField = new JPasswordField(24);
			passwordBox.add(passwordField);

			int passwordPane = JOptionPane.showConfirmDialog(null, passwordBox, "Enter your password",
					JOptionPane.OK_CANCEL_OPTION);

			if (passwordPane == JOptionPane.OK_OPTION) {
				String passText = new String(passwordField.getPassword());
				loginWriter.println(passText);
			}

			loginWriter.flush();

		} catch (UnknownHostException u) {
			System.out.println(u);
		} catch (IOException i) {
			System.out.println("In the first block");
			i.printStackTrace();
		}

		try {

			gameReader = new DataInputStream(socket.getInputStream());

			Boolean check = gameReader.readBoolean();
			gameInSession = check;
			gameReader.close();

			if (gameInSession) {
				int choice = chooseOption();
	
				gameWriter = new DataOutputStream(socket.getOutputStream());
				gameWriter.writeInt(choice);
				gameWriter.flush();
			}

		} catch (IOException i) {
			System.out.println("Another one " + i);
		}



	}

	public int chooseOption() {
		Object[] options = { "1", "2", "3" };
		int choice = JOptionPane.showOptionDialog(null, "Choose a number", "Find the Queen",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

		System.out.println("The option chosen is: " + choice);
		return choice;
	}

	public static void main(String args[]) {
		Client client = new Client("127.0.0.1", 7621);
	}
}
