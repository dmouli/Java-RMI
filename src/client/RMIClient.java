package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import examples.PersonList;
import examples.TextManipulator;
import registry.RegistryMessage;
import registry.RegistryRequestType;
import rmi.Remote440Exception;
import rmi.RemoteObjectReference;

/**
 * RMIClient : Class that invokes remote methods on the the server.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RMIClient {
	private static ObjectOutputStream toRegistry;
	private static ObjectInputStream fromRegistry;

	public static void main(String args[]) {

		// Connect to registry
		String registryHost = args[0];
		int registryPort = Integer.parseInt(args[1]);
		Socket registrySocket = null;
		try {
			registrySocket = new Socket(registryHost, registryPort);
			toRegistry = new ObjectOutputStream(
					registrySocket.getOutputStream());
			fromRegistry = new ObjectInputStream(
					registrySocket.getInputStream());
			System.out.println("Client has connected with Registry.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// search for all keys in registry
			RegistryMessage list = new RegistryMessage(
					RegistryRequestType.LIST, null, null);
			toRegistry.writeObject(list);
			System.out.println(fromRegistry.readObject());

			// Lookup unique key in registry to retreive Remote Object Reference
			RegistryMessage lookupText = new RegistryMessage(
					RegistryRequestType.LOOKUP, "ctm1", null);
			toRegistry.writeObject(lookupText);
			RemoteObjectReference textROR = ((RegistryMessage) fromRegistry
					.readObject()).getROR();

			TextManipulator textStub = (TextManipulator) textROR.localize();

			// Test 1 : Invoke a Remote Method, given a Local-instance Parameter
			System.out.println("This should be 'HELLO' : "
					+ textStub.capitalizeText("hello"));

			// Test 2 : Invoke a Remote Method that throws an exception, given a
			// Local-instance Parameter
			System.out.println("This should throw an exception : ");
			System.out.println(textStub.capitalizeText(""));

		} catch (Remote440Exception | IOException | ClassNotFoundException e) {
			System.err.println("TextManipulator exception:");
			e.printStackTrace();
		}

		try {
			// Lookup unique key in registry to retrieve Remote Object Reference
			RegistryMessage person1Lookup = new RegistryMessage(
					RegistryRequestType.LOOKUP, "cpl1", null);
			RegistryMessage person2Lookup = new RegistryMessage(
					RegistryRequestType.LOOKUP, "cpl2", null);

			toRegistry.writeObject(person1Lookup);
			RemoteObjectReference person1ROR = ((RegistryMessage) fromRegistry
					.readObject()).getROR();

			toRegistry.writeObject(person2Lookup);
			RemoteObjectReference person2ROR = ((RegistryMessage) fromRegistry
					.readObject()).getROR();

			PersonList person1Stub = (PersonList) person1ROR.localize();
			PersonList person2Stub = (PersonList) person2ROR.localize();

			// Test 3 : Invoke a Remote Method that returns a Remote Object,
			// given a Local-instance Parameter
			person1Stub = person1Stub.addPerson("Person1", true);
			System.out.println("After Adding Person 1 : "
					+ person1Stub.getList());

			person1Stub = person1Stub.addPerson("Person1", true);
			person1Stub = person1Stub.addPerson("Person1", false);
			person1Stub = person1Stub.addPerson("Person2", false);
			System.out.println("After Adding Person1, Person2 : "
					+ person1Stub.getList());

			person1Stub = person1Stub.removePerson("Person1");
			person1Stub = person1Stub.removePerson("Person2");
			System.out.println("After Removing Person1, Person2 : "
					+ person1Stub.getList());

			// Test 4 : Invoke a Remote Method that returns a Remote Object,
			// given a Remote-Object Parameter
			person2Stub = person2Stub.addPerson("FOREIGNER1", true);
			person2Stub = person2Stub.addPerson("FOREIGNER2", true);
			System.out.println("This is List 2 : " + person2Stub.getList());
			person1Stub = person1Stub.combineLists(person2Stub);
			System.out.println("After combining Lists 1 and 2 : "
					+ person1Stub.getList());

			person1Stub = person1Stub.clearList();
			System.out.println("After clearing list :" + person1Stub.getList());

			// Test 5 : Invoke a Remote Method that returns a Remote Object and
			// throws an exception, given a Local-instance Parameter
			System.out.println("This should throw an exception : ");
			person1Stub = person1Stub.removePerson("Person3");

			RegistryMessage doneRegistering = new RegistryMessage(
					RegistryRequestType.DONE, null, null);
			toRegistry.writeObject(doneRegistering);
			toRegistry.close();
			fromRegistry.close();
			registrySocket.close();

		} catch (Remote440Exception | IOException e) {
			System.err.println("PersonList exception : ");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
