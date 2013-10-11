package Server;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import Common.Util;
import Message.RVMessage;
import Message.RorMessage;


/**
 * RemoteObjectRef class is reference of remote object. It contains necessary information about 
 * remote object, like ip address, port number, object key and remote interface name. It can 
 * localise on a remote client from the original object, and locate a stub there. Providing
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
			try {
				CommunicationModule.writeObject(this.IP_adr, this.Port, new RorMessage(this));
				byte[] objclass = (byte[])((RVMessage)CommunicationModule.readObject(this.IP_adr, this.Port)).get();
				
				if(objclass == null) {
					System.out.println("This stub file does not exist.");
					return null;
				}
				
				int pos = stub_name.lastIndexOf(".");
				String packagename = stub_name.substring(0, pos);
				String filename = stub_name.substring(pos+1);
				
				Util.writeBinaryToFile(objclass, packagename+"/"+filename+".class");
				c = Class.forName(stub_name);
		    	Constructor<?> o = c.getConstructor(this.getClass());
		    	remote_stub = (Remote)o.newInstance(this);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Exception happend when creating stub");
				return null;
			} 
		} catch (Exception e) {
			System.out.println("Exception happend when creating stub");
			return null;
		}
		
		return remote_stub;
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
