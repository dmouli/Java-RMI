package rmi;

import java.io.Serializable;

/**
 * RemoteObjectReference - reference to remote object that is stored in registry
 * and passed to/accessed by client to create their local proxy stub
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RemoteObjectReference implements Serializable {
	private static final long serialVersionUID = 2738705851812663981L;
	private String hostName;
	private int portNumber;
	private int objectKey;
	private String remoteInterface;

	public RemoteObjectReference(String host, int portNum, int objRORid,
			String remInt) {
		this.hostName = host;
		this.portNumber = portNum;
		this.objectKey = objRORid;
		this.remoteInterface = remInt;
	}

	/* Method used to create local proxy stub by client */
	public Object localize() {
		Class<?> stubClass = null;
		Stub stubInstance = null;
		String stubName = remoteInterface + "Stub";

		try {
			stubClass = Class.forName(stubName);
			stubInstance = (Stub) stubClass.newInstance();
			stubInstance.setROR(this);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return stubInstance;
	}

	public String getRORhost() {
		return hostName;
	}

	public int getRORport() {
		return portNumber;
	}

	public int getRORkey() {
		return objectKey;
	}

	public String getRORri() {
		return remoteInterface;
	}
}
