package Server;

import java.net.Socket;

import Exceptions.RemoteException;
import Message.MethodMessage;

/**
 * Remote_Service is a class responsible of serving a connection to the remote server. It adds 
 * the connection to CommunicationModule, then keeps reading RMI method messages from the 
 * connected client, passing the corresponding message along with client hostname and port number
 * to the remote server do_job method, where the method message will be processed.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class Remote_Service implements Runnable{
	
	private String host;
	private int port;
	private RemoteServerRef ref;
	private Socket sock;
	private boolean isRun = true;
	
	/** 
     * constructor of Remote_Service class
     * 
     * @param ref       the remote server it is working for
     * @para sock       the socket to the connected client
     * @since           1.0
     */
	public Remote_Service(RemoteServerRef ref, Socket sock) {
		super();
		this.ref = ref;
		this.sock = sock;
	}
	
	/** 
     * The main service function of this class. It adds the connection to CommunicationModule, 
     * then keeps reading method message from the client, passing the corresponding message along
     * with client hostname and port number to the remote server do_job method, where the method 
     * message will be processed.
     * 
     * @since           1.0
     */
	@Override
	public void run() {
		host = sock.getInetAddress().toString();
		port = sock.getPort();
		String key = host + port;
		CommunicationModule.addSock_ts(key, sock);
		
		while(isRun) {
			MethodMessage m;
			try {
				m = (MethodMessage)CommunicationModule.readObject(host, port);
				ref.do_job(m, host, port);
			} catch (RemoteException e) {
				System.out.printf("Remote Server: Job Finished at %s:%d\n",host,port);
				CommunicationModule.removeSock_ts(key);
				isRun = false;
			}
		}
	}

}
