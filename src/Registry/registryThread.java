package Registry;

import java.net.Socket;

import Exceptions.RemoteException;
import Message.ExMessage;
import Message.RVMessage;
import Message.RegMessage;
import Message.RorMessage;
import Server.CommunicationModule;
import Server.Remote;

public class registryThread implements Runnable{
	private String host;
	private int port;
	private Registry_Server server;
	private Socket sock;
	
	private boolean isRun = true;
	
	public registryThread(Socket sock, Registry_Server server) {
		super();
		this.sock = sock;
		this.server = server;
	}
	
	@Override
	public void run() {
		host = sock.getInetAddress().toString();
		port = sock.getPort();
		String key = host + port;
		CommunicationModule.addSock_ts(key, sock);
		// TODO Auto-generated method stub
		while(isRun) {
			RegMessage m;
			try {
				m = (RegMessage)CommunicationModule.readObject(host, port);
			} catch (RemoteException e) {
				System.out.printf("Registry Server: Job Finished at %s:%d\n",host,port);
				isRun = false;
				break;
			}
			switch(m.get().cmd){
				case "lookup":
					Remote obj = server.lookup(m.get().service_name);
					if(obj instanceof ExMessage)
						try {
							CommunicationModule.writeObject(host, port, (ExMessage)obj);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							System.out.printf("Registry Server: Write remote exception at %s:%d\n",host,port);
							isRun = false;
						}
					else {
						RorMessage message = new RorMessage(obj);
						try {
							CommunicationModule.writeObject(host, port, message);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							System.out.printf("Registry Server: Write remote exception at %s:%d\n",host,port);
							isRun = false;
						}
					}
					break;
				case "rebind":
					server.rebind(m.get().service_name, m.get().ror);
					break;
				case "list":
					String[] names = server.list();
					RVMessage message = new RVMessage(names);
					try {
						CommunicationModule.writeObject(host, port, message);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
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
