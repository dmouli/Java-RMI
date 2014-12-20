package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Stub : Class representing basis of a local proxy object used by client.
 * Provides functionality to send RMIMessage to server and retrieve RMIMessage
 * containing result value.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class Stub {
	private RemoteObjectReference ror;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private RMIMessage returnedMessage;

	protected void executeRMIMessage(RMIMessage rmiMsg) {
		Socket socket = null;
		try {
			socket = new Socket(ror.getRORhost(), ror.getRORport());
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(rmiMsg);
			inStream = new ObjectInputStream(socket.getInputStream());
			setReturnedMessage((RMIMessage) inStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public RMIMessage getReturnedMessage() {
		return returnedMessage;
	}

	public void setReturnedMessage(RMIMessage returnedMessage) {
		this.returnedMessage = returnedMessage;
	}

	public void setROR(RemoteObjectReference ror) {
		this.ror = ror;
	}

	public RemoteObjectReference getROR() {
		return ror;
	}

}
