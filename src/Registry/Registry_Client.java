package Registry;

import Exceptions.RemoteException;
import Message.RMIMessage;
import Message.RegMessage;
import Server.CommunicationModule;
import Server.Remote;

/**
 * Registry_Client is a class responsible for remote visits to the RMI registry server from
 * its clients (either RMI clients or RMI servers). It implements Registry interface. From the
 * clients, operations on remote RMI registry is like local operations. 
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class Registry_Client implements Registry{

	private static final long serialVersionUID = 8626103234166801911L;
	private String host;
	private int port;
	
	/** 
     * constructor of Registry_Client class
     * 
     * @param host      the hostname of the RMI registry server
     * @param port		the port number of the RMI registry server 
     * @since           1.0
     */
	public Registry_Client(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}


	/**
	 * get the list of service names registered with RMI registry server
	 *
	 * @since           1.0
	 */
	@Override
	public String[] list(){
		RegMessage m = new RegMessage();
		RegMessage.regInfo info = m.new regInfo("list", null, null);

		m.set(info);
		try {
			CommunicationModule.writeObject(host, port, m);
		} catch (RemoteException e) {
			System.out.printf("Registry Client: Rmote Exception(%s)!\n",e.getCause().toString());
		}
		
		RMIMessage obj = null;
		try {
			obj = CommunicationModule.readObject(host, port);
		} catch (RemoteException e) {
			System.out.printf("Registry Client: Rmote Exception(%s)!\n",e.getCause().toString());
		}
		
		return (String[]) obj.get();
	}


   	/**
	 * look up by service name a remote object reference on RMI registry server, 
	 * return the result object that is returned from the RMI registry server.
	 *
	 * @param serviceName    the string of service name
	 * @since               1.0
	 */
	@Override
	public Remote lookup(String name){
		RegMessage m = new RegMessage();
		RegMessage.regInfo info = m.new regInfo("lookup", name, null);

		m.set(info);
		try {
			CommunicationModule.writeObject(host, port, m);
		} catch (RemoteException e) {
			System.out.printf("Registry Client: Rmote Exception(%s)!\n",e.getCause().toString());
		}
		
		Remote obj = null;
		try {
			obj = (Remote)CommunicationModule.readObject(host, port).get();
		} catch (RemoteException e) {
			System.out.printf("Registry Client: Rmote Exception(%s)!\n",e.getCause().toString());
		}
		
		return obj;
	}

    /**
	 * bind a service with remote object reference on the RMI registry server
	 *
	 * @param name       the string of service name
	 * @param ror        the remote object reference to be registered
	 * @since           1.0
	 */
	@Override
	public void rebind(String name, Remote ror){

		RegMessage m = new RegMessage();
		RegMessage.regInfo info = m.new regInfo("rebind", name, ror);

		m.set(info);
		try {
			CommunicationModule.writeObject(host, port, m);
		} catch (RemoteException e) {
			System.out.printf("Registry Client: Rmote Exception(%s)!\n",e.getCause().toString());
		}
	}
	

}
