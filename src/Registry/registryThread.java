package Registry;

import java.net.Socket;

import Exceptions.RemoteException;
import Message.ExMessage;
import Message.RVMessage;
import Message.RegMessage;
import Message.RorMessage;
import Server.CommunicationModule;
import Server.Remote;


/**
 * registryThread is a class responsible of serving a connection to the RMI registry server.
 * It adds the connection to CommunicationModule, then keeps reading requests (commands) from
 * the connection, passing the corresponding operations on RMI registry to RMI registry server.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class registryThread implements Runnable{
	private String host;
	private int port;
	private Registry_Server server;
	private Socket sock;
	
	private boolean isRun = true;
	
	/** 
     * constructor of registryThread class
     * 
     * @param sock      the connection socket
     * @para server     the RMI registry server
     * @since           1.0
     */
	public registryThread(Socket sock, Registry_Server server) {
		super();
		this.sock = sock;
		this.server = server;
	}
	
	/** 
     * The main service function of this class. It adds the connection to CommunicationModule, 
     * then keeps reading requests (commands) from the connection, passing the corresponding 
     * operations on RMI registry to RMI registry server.
     * 
     * @since           1.0
     */
	@Override
	public void run() {
	
	    /**
	     * add the connection to CommunicationModul's hashmap
	     */
		host = sock.getInetAddress().toString();
		port = sock.getPort();
		String key = host + port;
		CommunicationModule.addSock_ts(key, sock);
		
		while(isRun) {
			RegMessage m;
			
			/**
			 * read request (command) from connection
			 */
			try {
				m = (RegMessage)CommunicationModule.readObject(host, port);
			} catch (RemoteException e) {
				System.out.printf("Registry Server: Job Finished at %s:%d\n",host,port);
				isRun = false;
				break;
			}
			
			/**
			 * parse command and pass corresponding registry method to RMI registry server to deal with
			 */
			switch(m.get().cmd){
				case LOOKUP:
					Remote obj = server.lookup(m.get().service_name);
					if(obj instanceof ExMessage)
						try {
							CommunicationModule.writeObject(host, port, (ExMessage)obj);
						} catch (RemoteException e) {
							System.out.printf("Registry Server: Write remote exception at %s:%d\n",host,port);
							isRun = false;
						}
					else {
						RorMessage message = new RorMessage(obj);
						try {
							CommunicationModule.writeObject(host, port, message);
						} catch (RemoteException e) {
							System.out.printf("Registry Server: Write remote exception at %s:%d\n",host,port);
							isRun = false;
						}
					}
					break;
				case REBIND:
					server.rebind(m.get().service_name, m.get().ror);
					break;
				case LIST:
					String[] names = server.list();
					RVMessage message = new RVMessage(names);
					try {
						CommunicationModule.writeObject(host, port, message);
					} catch (RemoteException e) {
						System.out.printf("Registry: Write remote exception at %s:%d\n",host,port);
						isRun = false;
					}
					break;
				default:
					break;	
			}
		}
		
	}
	
}
