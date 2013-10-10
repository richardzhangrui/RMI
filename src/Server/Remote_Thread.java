package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Remote_Thread is runnable class running in a thread of remote server, responsible of 
 * continuously accepting connections from clients. Upon a new connection is established,
 * it creates a corresponding Remote_Service which will serve the connected client.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class Remote_Thread implements Runnable{

	private int port;
	private RemoteServerRef ref;
	private ServerSocket listener;
	private static ExecutorService executor = Executors.newCachedThreadPool();
	private boolean isRun = true;

    /** 
     * constructor of Remote_Thread class
     * 
     * @param port		the port number of the remote server
     * @param ref       the remote server it is working for
     * @since           1.0
     */	
	public Remote_Thread(int port, RemoteServerRef ref) {
		super();
		this.port = port;
		this.ref = ref;
	}
	
	/** 
     * get running status of remote thread
     * 
     * @return          true if it is running, false otherwise
     * @since           1.0
     */
	public boolean isRun() {
		return isRun;
	}

    /** 
     * set the running status of the remote thread
     * 
     * @param isRun     true if it is running, false otherwise
     * @since           1.0
     */
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	/** 
     * get the port number of the remote server
     * 
     * @return          port number
     * @since           1.0
     */
	public int getPort() {
		return port;
	}

    /** 
     * set the port number of the remote server it is working for
     * 
     * @param port      port number
     * @since           1.0
     */
	public void setPort(int port) {
		this.port = port;
	}

    /** 
     * the run function of the remote thread, creating a listener, then continuously accepting 
     * connections from clients, creating remote services to serve the accepted connections
     * 
     * @since           1.0
     */	
	@Override
	public void run() {
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
				isRun = false;
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
		
	}

}
