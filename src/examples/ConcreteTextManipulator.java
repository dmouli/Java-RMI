package examples;

import rmi.Remote440Exception;

public class ConcreteTextManipulator implements TextManipulator {

	@Override
	public String capitalizeText(String input) throws Remote440Exception {
		if (input.equals("")) {
			throw new Remote440Exception("No input provided");
		}
		return input.toUpperCase();
	}

	@Override
	public String lowerCaseText(String input) throws Remote440Exception {
		return input.toLowerCase();
	}

	@Override
	public String concatText(String input) throws Remote440Exception {
		return input.replaceAll("\\s+", "");
	}

}
