package Server;

import java.net.Socket;

import Exceptions.RemoteException;
import Message.MethodMessage;

public class Remote_Service implements Runnable{
	
	private String host;
	private int port;
	private RemoteServerRef ref;
	private Socket sock;
	
	private boolean isRun = true;
	
	public Remote_Service(RemoteServerRef ref, Socket sock) {
		super();
		this.ref = ref;
		this.sock = sock;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
