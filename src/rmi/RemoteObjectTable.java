package rmi;

import java.util.Hashtable;

/**
 * RemoteObjectTable - table maintained by server that maps
 * RemoteObjectReference to their local Objects.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 */
public class RemoteObjectTable {
	public Hashtable<RemoteObjectReference, Object> map;
	private int objRORid;

	public RemoteObjectTable() {
		map = new Hashtable<RemoteObjectReference, Object>();
		objRORid = 0;
	}

	public RemoteObjectReference addObject(String host, int port, Object obj) {
		String remoteInterface = obj.getClass().getInterfaces()[0].getName();
		RemoteObjectReference objROR = new RemoteObjectReference(host, port,
				objRORid, remoteInterface);
		map.put(objROR, obj);
		objRORid++;
		return objROR;
	}

	public Object findObj(RemoteObjectReference ror) {
		for (RemoteObjectReference tabROR : map.keySet()) {
			if (ror.getRORkey() == tabROR.getRORkey()) {
				return map.get(tabROR);
			}
		}
		return ror;
	}

}
