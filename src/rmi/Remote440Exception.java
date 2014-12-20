package rmi;

/**
 * Remote440Exception : All methods of a remote class should throw
 * Remote440Exceptions
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class Remote440Exception extends Exception {
	private static final long serialVersionUID = -104462347971460671L;

	String exceptionMsg;

	public Remote440Exception(String msg) {
		this.exceptionMsg = msg;
	}

	public String getMessage() {
		return this.exceptionMsg;
	}

}
