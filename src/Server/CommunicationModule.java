package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import Exceptions.RemoteException;
import Message.ExMessage;
import Message.RMIMessage;

public final class CommunicationModule {
	private static volatile HashMap<String, Socket> socks = new HashMap<String, Socket>();
	
	/* disable public constructor */
	private CommunicationModule() {}
	
	public static void addSock_ts(String key, Socket sock) {
		synchronized(socks) {
			socks.put(key, sock);
		}
	}
	
	public static void removeSock_ts(String key) {
		synchronized(socks) {
			socks.remove(key);
		}
	}
	
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
			//System.out.printf("Communication Module:Job Finished with address:%s, port: %d\n", host, port);
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
