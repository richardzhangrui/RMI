package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Remote_Thread implements Runnable{

	private int port;
	private RemoteServerRef ref;
	
	private ServerSocket listener;
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	private boolean isRun = true;
	
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public Remote_Thread(int port, RemoteServerRef ref) {
		super();
		this.port = port;
		this.ref = ref;
	}
	
	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			System.out.printf("Remote Server: cannot start Remote server at port; %d!",getPort());
			e.printStackTrace();
		}
		
		while (isRun) {
			Socket sock;
			try {
				sock = listener.accept();
				
				Remote_Service rt = new Remote_Service(ref, sock);
				executor.execute(rt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				isRun = false;
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
		
	}

}
