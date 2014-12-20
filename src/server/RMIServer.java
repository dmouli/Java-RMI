package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import examples.ConcretePersonList;
import examples.ConcreteTextManipulator;
import registry.RegistryMessage;
import registry.RegistryRequestType;
import rmi.CommunicationModule;
import rmi.RMIMessage;
import rmi.Remote440Exception;
import rmi.RemoteObjectReference;
import rmi.RemoteObjectTable;

/**
 * RMIServer : Class that creates remote objects, registers them in registry,
 * and responds to remote method invocations made by client
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RMIServer extends Thread {
	private String host;
	private int port;
	private RemoteObjectTable rorToObject;
	private CommunicationModule communication;
	private Socket registrySocket;
	private ObjectOutputStream toRegistry;
	private ObjectInputStream fromRegistry;

	public RMIServer(String host, int port, String registryHost,
			int registryPort) {
		this.host = host;
		this.port = port;
		this.rorToObject = new RemoteObjectTable();
		this.communication = new CommunicationModule(port);

		// connect to registry
		try {
			registrySocket = new Socket(registryHost, registryPort);
			System.out.println("Server connected to Registry");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// Create some objects identified as remote objects
		ConcreteTextManipulator ctm = new ConcreteTextManipulator();
		ConcretePersonList cpl = new ConcretePersonList(new ArrayList<String>());
		ConcretePersonList cpl2 = new ConcretePersonList(
				new ArrayList<String>());

		// Generate ROR for objects
		RemoteObjectReference ctmROR = rorToObject.addObject(host, port, ctm);
		RemoteObjectReference cplROR = rorToObject.addObject(host, port, cpl);
		RemoteObjectReference cpl2ROR = rorToObject.addObject(host, port, cpl2);

		// Register objects with registry
		RegistryMessage ctmRegister = new RegistryMessage(
				RegistryRequestType.BIND, "ctm1", ctmROR);
		RegistryMessage cpl1Register = new RegistryMessage(
				RegistryRequestType.BIND, "cpl1", cplROR);
		RegistryMessage cpl2Register = new RegistryMessage(
				RegistryRequestType.BIND, "cpl2", cpl2ROR);
		try {
			toRegistry = new ObjectOutputStream(
					registrySocket.getOutputStream());

			toRegistry.writeObject(ctmRegister);
			// wait for confirmation message from registry
			fromRegistry = new ObjectInputStream(
					registrySocket.getInputStream());
			fromRegistry.readObject();

			toRegistry.writeObject(cpl1Register);
			fromRegistry.readObject();

			toRegistry.writeObject(cpl2Register);
			fromRegistry.readObject();

			RegistryMessage doneRegistering = new RegistryMessage(
					RegistryRequestType.DONE, null, null);
			toRegistry.writeObject(doneRegistering);
			System.out
					.println("Server has completed registering remote objects with registry.");

		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		while (true) {

			try {
				// accept incoming RMIClient socket and listen for method
				// invocation request from client
				RMIMessage methodMsg = (RMIMessage) communication
						.receiveFromClient();
				System.out.println("Server has received an RMIClient.");

				// unmarshall and execute method
				RemoteObjectReference clientMethodROR = methodMsg.getROR();
				Object localObject = rorToObject.findObj(clientMethodROR);
				methodMsg.unmarshallAndInvokeMethod(localObject, rorToObject);

				// Send RMIMessage, now with marshalled return value, back to
				// client
				communication.sendToClient(methodMsg);

			} catch (Remote440Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**************************************************************************/
	/****************************** MAIN METHOD *******************************/
	/**************************************************************************/
	public static void main(String args[]) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String registryHost = args[2];
		int registryPort = Integer.parseInt(args[3]);
		RMIServer server = new RMIServer(host, port, registryHost, registryPort);
		server.start();
	}

}
