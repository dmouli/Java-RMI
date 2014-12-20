package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CommunicationModule : Class that handles network communication between server
 * and client (from server side)
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class CommunicationModule {
	private ServerSocket serverSocket;
	private Socket clientSocket;

	public CommunicationModule(int port) {
		try {
			serverSocket = new ServerSocket(port);
			clientSocket = null;
			System.out.println("Server is open on port " + port);
		} catch (IOException e) {
			System.out.println("Could not open server socket on port " + port
					+ " - " + e);
			return;
		}
	}

	public void sendToClient(Message methodMsg) throws Remote440Exception {
		try {
			ObjectOutputStream toClient = new ObjectOutputStream(
					clientSocket.getOutputStream());
			toClient.writeObject(methodMsg);
			toClient.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Remote440Exception("Message not sent");
		}
	}

	public Message receiveFromClient() throws Remote440Exception {
		try {
			clientSocket = serverSocket.accept();
			ObjectInputStream fromClient = new ObjectInputStream(
					clientSocket.getInputStream());
			return (Message) fromClient.readObject();
		} catch (Exception e) {
			throw new Remote440Exception("Message not received");
		}
	}
}
