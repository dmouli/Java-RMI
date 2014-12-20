package examples;

import rmi.Remote440;
import rmi.Remote440Exception;

public interface TextManipulator extends Remote440 {
	public String capitalizeText(String input) throws Remote440Exception;

	public String lowerCaseText(String input) throws Remote440Exception;

	public String concatText(String input) throws Remote440Exception;
}
