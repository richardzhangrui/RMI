package Examples;

import Exceptions.RemoteException;
import Server.Remote;

public class Hello implements Hello_Interface,Remote{
	
	public Hello(){}
	
	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		return "Hello, "+name;
	}

	@Override
	public String sayHello(int ok) {
		// TODO Auto-generated method stub
		return "Hello, number "+ok;
	}

	@Override
	public String testException() throws RemoteException {
		// TODO Auto-generated method stub
		throw new RemoteException("Test",new Throwable("For test"));
	}
	
}
