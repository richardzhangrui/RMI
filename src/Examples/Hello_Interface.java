package Examples;

import Exceptions.RemoteException;

public interface Hello_Interface {
	public String sayHello(String name);
	public String sayHello(int ok);
	public String testException() throws RemoteException;
	public void testException2() throws RemoteException;
}
