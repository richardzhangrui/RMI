package Server;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Common.Util;
import Exceptions.RemoteException;
import Message.*;


/**
 * RemoteObjectRef class is reference of remote object. It contains necessary information about 
 * remote object, like ip address, port number, object key and remote interface name. It can 
 * localise on a remote client from the original object, and create a stub there. Providing
 * remote object reference, method on it, and parameters, the invoke method will pass the information
 * via method message, invoke the remote method on a remote server, and return the return value
 * from the remote method.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RemoteObjectRef implements Serializable, Remote{

	private static final long serialVersionUID = 8621606705581312196L;
    private String IP_adr;
    private int Port;
    private int Obj_Key;
    private String Remote_Interface_Name;
	
	/** 
     * constructor of RemoteObjectRef class, initializing ip address, port number, object key 
     * and remote interface name of the remote object
     * 
     * @param ip        the ip address (hostname)
     * @param port      the port number
     * @param obj_key   the object key
     * @param riname    the remote interface name
     * @since           1.0
     */
    public RemoteObjectRef(String ip, int port, int obj_key, String riname) 
    {
		IP_adr=ip;
		Port=port;
		Obj_Key=obj_key;
		setRemote_Interface_Name(riname);
    }
    
    /** 
     * localise the remote object reference and locate a stub with the name of remote
     * interface plus suffix "_Stub"
     * 
     * @return          the remote stub object
     * @since           1.0
     */
    public Object localise()
    {
        
    	String stub_name = Remote_Interface_Name + "_Stub";
    	Remote remote_stub = null;
    	
    	Class<?> c;
		try {
			c = Class.forName(stub_name);
	    	Constructor<?> o = c.getConstructor(this.getClass());
	    	remote_stub = (Remote)o.newInstance(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return remote_stub;
    }
    
    /** 
     * invoke a remote method on a referenced remote object, with the parameters needed.
     * this method will capsule all information in a method message and communicate with the
     * destination server, and then read the return value from the remote server.
     * 
     * @param obj           the remote object reference containing info about remote object
     * @param method        the method to be invoked
     * @param params        the parameter of the remote method
     * @return              the remote return value if there is one, null otherwise
     * @since           1.0
     */
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
    
    /** 
     * get the remote interface name from remote object reference
     * 
     * @return          the remote interface name string
     * @since           1.0
     */
	public String getRemote_Interface_Name() {
		return Remote_Interface_Name;
	}

    /** 
     * set the remote interface name in the remote object reference
     * 
     * @param remote_interface_name     the remote interface name string 
     * @since                           1.0
     */
	public void setRemote_Interface_Name(String remote_Interface_Name) {
		Remote_Interface_Name = remote_Interface_Name;
	}
	
	/** 
     * get the ip address (hostname) from the remote object reference
     * 
     * @return          the ip adrress string
     * @since           1.0
     */
	public String getIP_adr() {
		return IP_adr;
	}

    /** 
     * set the ip address (hostname) in the remote object reference
     * 
     * @param iP_adr    the ip address string 
     * @since           1.0
     */
	public void setIP_adr(String iP_adr) {
		IP_adr = iP_adr;
	}

    /** 
     * get the port number from the remote object reference
     * 
     * @return          the port number
     * @since           1.0
     */
	public int getPort() {
		return Port;
	}

    /** 
     * set the port number in the remote object reference
     * 
     * @param port      the port number 
     * @since           1.0
     */
	public void setPort(int port) {
		Port = port;
	}

    /** 
     * get the object key from the remote object reference
     * 
     * @return          the integer object key
     * @since           1.0
     */
	public int getObj_Key() {
		return Obj_Key;
	}

    /** 
     * set the object key in the remote object reference
     * 
     * @param obj_Key   the integer remote object key 
     * @since           1.0
     */
	public void setObj_Key(int obj_Key) {
		Obj_Key = obj_Key;
	}
	
}
