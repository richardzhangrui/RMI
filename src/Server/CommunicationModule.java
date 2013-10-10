package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import Exceptions.RemoteException;
import Message.ExMessage;
import Message.RMIMessage;

/**
 * CommunicationModule is a class responsible for communication with remote clients/servers.
 * On each client/server, it keeps a HashMap of hostname/port info and corresponding socket.
 * Givin remote host name and port, it can read from or write to the corresponding socket.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public final class CommunicationModule {
	private static volatile HashMap<String, Socket> socks = new HashMap<String, Socket>();
	
	/** 
     * constructor of CommunicationModule class
     * 
     * @since           1.0
     */
	private CommunicationModule() {}
	
	/** 
     * add host/port info and socket to hashmap
     * 
     * @param key       the key string containing hostname and port
     * @param sock      the socked to be recorded in hashmap
     * @since           1.0
     */
	public static void addSock_ts(String key, Socket sock) {
		synchronized(socks) {
			socks.put(key, sock);
		}
	}
	
	/** 
     * remove hostname/port info and corresponding socket from hashmap
     * 
     * @param key       the key string containing host and port
     * @since           1.0
     */
	public static void removeSock_ts(String key) {
		synchronized(socks) {
			socks.remove(key);
		}
	}
	
	/** 
     * write RMI message to a socket specified by hostname/port
     * 
     * @param host      the hostname
     * @param port      the port number
     * @param m         the RMI message to be sent to the destination remote server/client
     * @since           1.0
     */
	public static void writeObject(String host, int port, RMIMessage m) throws RemoteException {
		String key = host + port;
		Socket sock;
		try {
			if (!socks.containsKey(key)) {
					sock = new Socket(host, port);
					addSock_ts(key, sock);
			}
			else {
				sock = socks.get(key);
			}
			
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(m);
		} catch (IOException e) {
			removeSock_ts(key);
			System.out.printf("IOException: WriteObject fail with address:%s, port: %d\n", host, port);
			throw new RemoteException("WriteObject Fail",new Throwable("Caused By I/O Exception"));
		}
		
	}
	
	/** 
     * read RMI message from a socket specified by hostname/port
     * 
     * @param host      the hostname
     * @param port      the port number
     * @return          the RMI message read from the remote server/client
     * @since           1.0
     */
	public static RMIMessage readObject(String host, int port) throws RemoteException {
		String key = host + port;
		Socket sock = null;
		RMIMessage m = null;
		try {
			if (!socks.containsKey(key)) {
				
				sock = new Socket(host, port);
				addSock_ts(key, sock);
			}
			else {
				sock = socks.get(key);
			}
			
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			
			m = (RMIMessage)in.readObject();
		} catch (IOException e) {
			removeSock_ts(key);
			throw new RemoteException("ReadObject Fail",new Throwable("Caused By I/O Exception"));
		} catch (ClassNotFoundException e) {
			System.out.printf("ClassNotFoundException: ReadObject fail with address:%s, port: %d\n", host, port);
		}
		
		if(m instanceof ExMessage) {
			throw (RemoteException)((ExMessage)m).get();
		}
		
		return m;
	}
}
