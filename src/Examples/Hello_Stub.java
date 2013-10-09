package Examples;

import java.lang.reflect.Method;

import Common.Util;
import Exceptions.RemoteException;
import Message.ExMessage;
import Message.MethodInfo;
import Message.MethodMessage;
import Message.RVMessage;
import Server.CommunicationModule;
import Server.Remote;
import Server.RemoteObjectRef;

public class Hello_Stub implements Remote,Hello_Interface{
	RemoteObjectRef ref;
	
	public Hello_Stub(RemoteObjectRef r) {
		this.ref = r;
	}
	
	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		
		Class<?>[] types = new Class<?>[1];
		types[0] = String.class;
		Method method;
		try {
			method = this.getClass().getMethod("sayHello", types);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("No such method!");
			return null;
		}
		int key = Util.Hash_Method(method);
		Object[] params = new Object[1];
		params[0] = name;
		MethodInfo info = new MethodInfo(ref.getObj_Key(), key, params);
		MethodMessage message = new MethodMessage(info);
		
		Object obj = null;
		
		try {
			CommunicationModule.writeObject(ref.getIP_adr(), ref.getPort(), message);
			obj = CommunicationModule.readObject(ref.getIP_adr(), ref.getPort());
			if(obj instanceof ExMessage)
				throw (RemoteException)((ExMessage)obj).get();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.printf("Remote Exception %s caused by %s!\n",e.detail, e.getCause().toString());
			return null;
		}
		
		return (String)((RVMessage)obj).get();
	}

	@Override
	public String sayHello(int ok) {
		Class<?>[] types = new Class<?>[1];
		types[0] = int.class;
		Method method;
		try {
			method = this.getClass().getMethod("sayHello", types);
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("No such method!");
			return null;
		}
		int key = Util.Hash_Method(method);
		Object[] params = new Object[1];
		params[0] = ok;
		MethodInfo info = new MethodInfo(ref.getObj_Key(), key, params);
		MethodMessage message = new MethodMessage(info);
		
		Object obj = null;
		
		try {
			CommunicationModule.writeObject(ref.getIP_adr(), ref.getPort(), message);
			obj = CommunicationModule.readObject(ref.getIP_adr(), ref.getPort());
			if(obj instanceof ExMessage)
				throw (RemoteException)((ExMessage)obj).get();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.printf("Remote Exception %s caused by %s!\n",e.detail, e.getCause().toString());
			return null;
		}
		
		return (String)((RVMessage)obj).get();
	}

	@Override
	public String testException() {
		Method method;
		try {
			method = this.getClass().getMethod("testException");
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("No such method!");
			return null;
		}
		int key = Util.Hash_Method(method);

		MethodInfo info = new MethodInfo(ref.getObj_Key(), key, null);
		MethodMessage message = new MethodMessage(info);
		
		Object obj = null;
		
		try {
			CommunicationModule.writeObject(ref.getIP_adr(), ref.getPort(), message);
			obj = CommunicationModule.readObject(ref.getIP_adr(), ref.getPort());
			if(obj instanceof ExMessage)
				throw (RemoteException)((ExMessage)obj).get();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.printf("Remote Exception %s caused by %s!\n",e.detail, e.getCause().toString());
			return null;
		}
		
		return (String)((RVMessage)obj).get();
	}

	@Override
	public void testException2() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
}
