package registry;

import rmi.Message;
import rmi.RemoteObjectReference;

/**
 * RegistryMessage : Message Object sent by server or client to the registry to
 * make requests.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RegistryMessage extends Message {
	private static final long serialVersionUID = -8391054558675964245L;
	private RegistryRequestType requestType;
	private RemoteObjectReference ror;
	private String objectKey;

	public RegistryMessage(RegistryRequestType requestType, String objectKey,
			RemoteObjectReference ror) {
		this.requestType = requestType;
		this.objectKey = objectKey;
		this.ror = ror;
	}

	public RegistryRequestType getType() {
		return requestType;
	}

	public RemoteObjectReference getROR() {
		return ror;
	}

	public void setRemoteObjectReference(RemoteObjectReference ror) {
		this.ror = ror;
	}

	public String getObjectKey() {
		return objectKey;
	}

}
