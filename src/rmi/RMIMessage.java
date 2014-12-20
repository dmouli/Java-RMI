package rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RMIMessage : Class that encapsulates/marshalls a method invocation and
 * provides functionality to unmarshall, invoke method, marshall return result.
 * 
 * @author Divya Mouli
 * @author Disha Bora
 * 
 */
public class RMIMessage extends Message {
	private static final long serialVersionUID = 2131510874174393140L;

	private String methodName;
	private Object[] args;
	private Class<?>[] argTypes;
	private Object returnValue;
	private Class<?> returnType;
	private RemoteObjectReference ror;
	private Remote440Exception thrownException;

	public RMIMessage(RemoteObjectReference ror, String methodName,
			Object[] args) {
		this.ror = ror;
		this.methodName = methodName;
		getArgRefsAndTypes(args);
	}

	public void unmarshallAndInvokeMethod(Object callee,
			RemoteObjectTable rorToObject) {
		Method method = null;
		System.out.println("Callee: " + callee.toString());
		Class<?> calleeClass = callee.getClass();

		try {
			method = calleeClass.getMethod(methodName, argTypes);

			// check if any of the arguments are Remote Object References,
			// and if so, retrieve their objects
			Object[] methodArgs = new Object[args.length];
			for (int i = 0; i < args.length; i++) {
				if (RemoteObjectReference.class.isAssignableFrom(args[i]
						.getClass())) {
					methodArgs[i] = rorToObject
							.findObj((RemoteObjectReference) args[i]);
				} else {
					methodArgs[i] = args[i];
				}
			}

			Object returnValue = method.invoke(callee, methodArgs);
			this.returnType = method.getReturnType();
			this.returnValue = returnValue;

			// return value is a remote object - need to pass a ROR of this
			// object to the client
			if (Remote440.class.isAssignableFrom(returnType)) {
				this.returnValue = rorToObject.addObject(ror.getRORhost(),
						ror.getRORport(), returnValue);
			}

		} catch (InvocationTargetException e) {
			// the method being called threw an exception
			this.thrownException = (Remote440Exception) e.getCause();
		} catch (NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException e) {
			// other method-invocation related error
			e.printStackTrace();
		}
	}

	public RemoteObjectReference getROR() {
		return ror;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object getRetValue() {
		return returnValue;
	}

	public Class<?> getRetType() {
		return returnType;
	}

	public Remote440Exception getExceptionThrown() {
		return thrownException;
	}

	private void getArgRefsAndTypes(Object[] args) {
		int argNum = 0;
		Object[] refs = new Object[args.length];
		Class<?>[] types = new Class<?>[args.length];

		for (Object arg : args) {
			Class<?> argClass = arg.getClass();

			// argument is of a remote object type
			if (Remote440.class.isAssignableFrom(argClass)) {
				RemoteObjectReference argROR = ((Stub) arg).getROR();
				refs[argNum] = argROR;
				try {
					types[argNum] = Class.forName(argROR.getRORri());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return;
				}

			} else {
				// argument is of a local type
				refs[argNum] = arg;
				types[argNum] = argClass;
			}

			argNum++;
		}

		this.args = refs;
		this.argTypes = types;
	}

}
