package registry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

import rmi.CommunicationModule;
import rmi.Remote440Exception;
import rmi.RemoteObjectReference;

/**
 * RMIRegistry : Registry Server that accepts requests from server and client.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RMIRegistry extends Thread {
	private String registryHost;
	private int registryPort;
	private static ConcurrentHashMap<String, RemoteObjectReference> registry = new ConcurrentHashMap<String, RemoteObjectReference>();

	public RMIRegistry(int port) {
		this.registryPort = port;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		try {
			serverSocket = new ServerSocket(registryPort);
			System.out.println("Registry is open on port " + registryPort);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Could not open registry on port " + registryPort
					+ " - " + e1);
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				
				// listen for incoming Message
				ObjectInputStream fromClient = new ObjectInputStream(
						clientSocket.getInputStream());
				ObjectOutputStream toClient = new ObjectOutputStream(
						clientSocket.getOutputStream());
				System.out.println("REGISTRY RECIEVED A CLIENT");

				while (true) {
					RegistryMessage request = (RegistryMessage) fromClient
							.readObject();
					if (request.getType() == RegistryRequestType.DONE) {
						break;
					}

					if (request.getType() == RegistryRequestType.LOOKUP) {
						System.out.println("RECEVIED LOOKUP REQUEST");
						String lookupKey = request.getObjectKey();
						RemoteObjectReference ror = lookup(lookupKey);
						request.setRemoteObjectReference(ror);
						toClient.writeObject(request);

					} else if (request.getType() == RegistryRequestType.BIND) {
						System.out.println("RECEVIED BIND REQUEST");
						String bindKey = request.getObjectKey();
						RemoteObjectReference bindROR = request.getROR();
						bind(bindKey, bindROR);

						// just send client a dummy value so they know request
						// was successful
						System.out.println("Sending confirmation");
						toClient.writeObject("DONE");

						System.out.println("Sent confirmation");
					} else if (request.getType() == RegistryRequestType.UNBIND) {
						String unbindKey = request.getObjectKey();
						unbind(unbindKey);
						
						// just send client a dummy value so they know request
						// was successful
						System.out.println("Sending confirmation");
						toClient.writeObject("DONE");

					} else if (request.getType() == RegistryRequestType.REBIND) {
						String rebindKey = request.getObjectKey();
						RemoteObjectReference rebindROR = request.getROR();
						rebind(rebindKey, rebindROR);
						
						// just send client a dummy value so they know request
						// was successful
						System.out.println("Sending confirmation");
						toClient.writeObject("DONE");

					} else if (request.getType() == RegistryRequestType.LIST) {
						String list = listKeys();
						toClient.writeObject(list);
					}
				}
			} catch (IOException | ClassNotFoundException | Remote440Exception e) {
				e.printStackTrace();
			}
		}
	}

	private RemoteObjectReference lookup(String objectKey)
			throws Remote440Exception {
		if (registry.containsKey(objectKey)) {
			return registry.get(objectKey);
		} else {
			throw new Remote440Exception(
					"Object specified by key is not in registry");
		}
	}

	private void bind(String objectKey, RemoteObjectReference ror) {
		if (registry.contains(objectKey)) {
			return;
		}
		registry.put(objectKey, ror);
	}

	private static void unbind(String objectKey) {
		registry.remove(objectKey);
	}

	private static void rebind(String objectKey, RemoteObjectReference ror) {
		registry.put(objectKey, ror);
	}

	private String listKeys() {
		String list = "[";
		for (String key : registry.keySet()) {
			list = list + key + ", \n";
		}
		list = list + "]";
		return list;
	}

	public String getRegistryHost() {
		return registryHost;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	/**************************************************************************/
	/****************************** MAIN METHOD *******************************/
	/**************************************************************************/
	public static void main(String args[]) {
		int port = Integer.parseInt(args[0]);
		RMIRegistry reg = new RMIRegistry(port);
		reg.start();
	}

}
