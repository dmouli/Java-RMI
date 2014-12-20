package examples;

import rmi.RMIMessage;
import rmi.Remote440Exception;
import rmi.RemoteObjectReference;
import rmi.Stub;

/**
 * PersonListStub : Manually created stub to invoke PersonList methods.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class PersonListStub extends Stub implements PersonList {

	@Override
	public PersonList addPerson(String person, Boolean allowMultiple)
			throws Remote440Exception {
		Object[] arguments = new Object[2];
		arguments[0] = person;
		arguments[1] = allowMultiple;
		RMIMessage msg = new RMIMessage(getROR(), "addPerson", arguments);
		executeRMIMessage(msg);

		// Method returned a Remote Object Reference
		// Generate the stub for that ROR and return to client
		if (getReturnedMessage().getRetType().isInterface()) {
			RemoteObjectReference retROR = (RemoteObjectReference) getReturnedMessage()
					.getRetValue();
			PersonListStub newStub = (PersonListStub) retROR.localize();
			return newStub;
		}

		// Method returned an object value
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		return (PersonList) getReturnedMessage().getRetValue();
	}

	@Override
	public PersonList removePerson(String person) throws Remote440Exception {
		Object[] arguments = new Object[1];
		arguments[0] = person;
		RMIMessage msg = new RMIMessage(getROR(), "removePerson", arguments);
		executeRMIMessage(msg);

		// Method returned an object value
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		// Method returned a Remote Object Reference
		// Generate the stub for that ROR and return to client
		if (getReturnedMessage().getRetType().isInterface()) {
			RemoteObjectReference retROR = (RemoteObjectReference) getReturnedMessage()
					.getRetValue();
			PersonListStub newStub = (PersonListStub) retROR.localize();
			return newStub;
		}
		return (PersonList) getReturnedMessage().getRetValue();
	}

	@Override
	public PersonList clearList() throws Remote440Exception {
		Object[] arguments = new Object[0];
		RMIMessage msg = new RMIMessage(getROR(), "clearList", arguments);
		executeRMIMessage(msg);

		// Method returned a Remote Object Reference
		// Generate the stub for that ROR and return to client
		if (getReturnedMessage().getRetType().isInterface()) {
			RemoteObjectReference retROR = (RemoteObjectReference) getReturnedMessage()
					.getRetValue();
			PersonListStub newStub = (PersonListStub) retROR.localize();
			return newStub;
		}

		// Method returned an object value
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		return (PersonList) getReturnedMessage().getRetValue();
	}

	@Override
	public String getList() throws Remote440Exception {
		Object[] arguments = new Object[0];
		RMIMessage msg = new RMIMessage(getROR(), "getList", arguments);
		executeRMIMessage(msg);
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		return (String) getReturnedMessage().getRetValue();
	}

	@Override
	public PersonList combineLists(PersonList otherList)
			throws Remote440Exception {
		Object[] arguments = new Object[1];
		arguments[0] = otherList;
		RMIMessage msg = new RMIMessage(getROR(), "combineLists", arguments);
		executeRMIMessage(msg);

		// Method returned a Remote Object Reference
		// Generate the stub for that ROR and return to client
		Class<?> msgRetType = getReturnedMessage().getRetType();
		if (msgRetType != null && msgRetType.isInterface()) {
			RemoteObjectReference retROR = (RemoteObjectReference) getReturnedMessage()
					.getRetValue();
			return (PersonList) retROR.localize();
		}

		// Method returned an object value
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		return (PersonList) getReturnedMessage().getRetValue();
	}

}
