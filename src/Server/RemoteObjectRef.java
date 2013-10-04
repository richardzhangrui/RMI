package Server;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Common.Util;
import Exceptions.RemoteException;
import Message.*;

public class RemoteObjectRef implements Serializable, Remote{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8621606705581312196L;
	
	private String IP_adr;
    private int Port;
    private int Obj_Key;
    private String Remote_Interface_Name;
	
    public RemoteObjectRef(String ip, int port, int obj_key, String riname) 
    {
		IP_adr=ip;
		Port=port;
		Obj_Key=obj_key;
		setRemote_Interface_Name(riname);
    }
    
    // this method is important, since it is a stub creator.
    // 
    public Object localise()
    {
		// Implement this as you like: essentially you should 
		// create a new stub object and returns it.
		// Assume the stub class has the name e.g.
		//
		//       Remote_Interface_Name + "_stub".
		//
		// Then you can create a new stub as follows:
		// 
		//       Class c = Class.forName(Remote_Interface_Name + "_stub");
		//       Object o = c.newinstance()
		//
		// For this to work, your stub should have a constructor without arguments.
		// You know what it does when it is called: it gives communication module
		// all what it got (use CM's static methods), including its method name, 
		// arguments etc., in a marshalled form, and CM (yourRMI) sends it out to 
		// another place. 
		// Here let it return null.
    	
    	String stub_name = Remote_Interface_Name + "_Stub";
    	Remote remote_stub = null;
    	
    	Class<?> c;
		try {
			c = Class.forName(stub_name);
	    	Constructor<?> o = c.getConstructor(this.getClass());
	    	remote_stub = (Remote)o.newInstance(this);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return remote_stub;
    }
    
    public Object invoke(Remote obj, Method method, Object[] params) throws RemoteException {
    	RemoteObjectRef ror = (RemoteObjectRef)obj;
    	MethodInfo minfo = new MethodInfo(ror.Obj_Key, Util.Hash_Method(method), params);
    	MethodMessage m = new MethodMessage(minfo);
    	
    	CommunicationModule.writeObject(ror.IP_adr, ror.Port, m);
    	
    	if(method.getReturnType() == void.class) {
    		return null;
    	}
    	
    	RVMessage returnValue = (RVMessage)CommunicationModule.readObject(ror.IP_adr, ror.Port);
    	
    	if(returnValue == null){
    		throw new RemoteException("Remote Exception",new Throwable("Caused by I/O error"));
    	}
    	
		return returnValue.get();
    }
    
    
	public String getRemote_Interface_Name() {
		return Remote_Interface_Name;
	}

	public void setRemote_Interface_Name(String remote_Interface_Name) {
		Remote_Interface_Name = remote_Interface_Name;
	}
	
	public String getIP_adr() {
		return IP_adr;
	}

	public void setIP_adr(String iP_adr) {
		IP_adr = iP_adr;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public int getObj_Key() {
		return Obj_Key;
	}

	public void setObj_Key(int obj_Key) {
		Obj_Key = obj_Key;
	}
	
}
