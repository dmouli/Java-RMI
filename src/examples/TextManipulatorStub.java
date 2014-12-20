package examples;

import rmi.RMIMessage;
import rmi.Remote440Exception;
import rmi.Stub;

/**
 * TextManipulatorStub : Manually created stub to invoke TextManipulator
 * methods.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class TextManipulatorStub extends Stub implements TextManipulator {

	@Override
	public String capitalizeText(String input) throws Remote440Exception {
		Object[] arguments = new Object[1];
		arguments[0] = input;
		RMIMessage msg = new RMIMessage(getROR(), "capitalizeText", arguments);
		executeRMIMessage(msg);
		if (getReturnedMessage().getExceptionThrown() != null) {
			throw getReturnedMessage().getExceptionThrown();
		}
		return (String) getReturnedMessage().getRetValue();
	}

	@Override
	public String lowerCaseText(String input) {
		Object[] arguments = new Object[1];
		arguments[0] = input;
		RMIMessage msg = new RMIMessage(getROR(), "lowerCaseText", arguments);
		executeRMIMessage(msg);
		return (String) getReturnedMessage().getRetValue();
	}

	@Override
	public String concatText(String input) {
		Object[] arguments = new Object[1];
		arguments[0] = input;
		RMIMessage msg = new RMIMessage(getROR(), "concatText", arguments);
		executeRMIMessage(msg);
		return (String) getReturnedMessage().getRetValue();
	}

}
