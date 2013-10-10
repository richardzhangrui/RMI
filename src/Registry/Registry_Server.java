package Registry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Exceptions.RemoteException;
import Message.ExMessage;
import Server.Remote;

/**
 * Registry_Server is the RMI registry server. It keeps a <serviceName, remoteObjectReference>
 * hashtable. It contains the main() method running on the RMI registry server, which listen on
 * a port number and forward a new connection to a new registryThread that will parse the request
 * and pass corresponding jobs like lookup, rebind and list on the registry hashtable. The above
 * mentioned jobs are realized by implementing Registry interface.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class Registry_Server implements Registry{

	private static final long serialVersionUID = 7472958118304045615L;
	private static Hashtable<String, Remote> bindings = new Hashtable<String, Remote>(100);
	private static ExecutorService executor = Executors.newCachedThreadPool();
	public ServerSocket listener;
	private boolean isRun = true;
	private static final int PORT = 15640;
	private int port;
	
	/** 
     * constructor of Registry_Server class, initializing port number
     * 
     * @param port		the port number the RMI registry server will run on 
     * @since           1.0
     */
	public Registry_Server(int port) {
		super();
		if (port <= 0) {
			port = PORT;
		}
		else {
			this.port = port;
		}
	}
	
	/**
	 * check if the RMI registry server is running
	 *
	 * @return  true if it is running, faulse otherwise
	 * @since   1.0
	 */
	public boolean isRun() {
		return isRun;
	}

    /**
	 * set the running status of RMI registry server
	 *
	 * @param isRun      the running status of the RMI registry server
	 * @since           1.0
	 */
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	/**
	 * get RMI registry server port number
	 *
	 * @return  the port number
	 * @since   1.0
	 */
	public int getPort() {
		return port;
	}

    /**
	 * set RMI registry server port number
	 *
	 * @param port       the port number the RMI registry server runs on
	 * @since           1.0
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * look up by service name a remote object reference that is registered with RMI registry, 
	 * return it if found, return an exception message if not found
	 *
	 * @param serviceName   the string of service name
	 * @return              the remote object reference if found, the exception message otherwise
	 * @since               1.0
	 */
	@Override
	public Remote lookup(String serviceName) {
		synchronized (bindings) {
            Remote obj = bindings.get(serviceName);
            if (obj == null) {
                ExMessage message = new ExMessage(new RemoteException("Remote Exception",new Throwable("No such service with this name")));
                return (Remote) message;
            }
            return obj;
        }
	}
	
	/**
	 * bind a service with remote object reference on the RMI registry
	 *
	 * @param name       the string of service name
	 * @param ror        the remote object reference to be registered
	 * @since           1.0
	 */
	@Override
	public void rebind(String name, Remote ror) {
		synchronized (bindings) {
			bindings.put(name, ror);
		}
	}
	
	/**
	 * get the list of service names registered with RMI registry
	 *
	 * @return          the list of service names
	 * @since           1.0
	 */
	@Override
	public String[] list() {
		String[] names;
        synchronized (bindings) {
            int i = bindings.size();
            names = new String[i];
            Enumeration<String> enum_ = bindings.keys();
            while ((--i) >= 0)
                names[i] = (String)enum_.nextElement();
        }
        
        return names;
	}
	
	/**
	 * This is the main function of RMI registry server, responsible of listening to connections 
	 * and forward a connection to a registryThread and execute it.
	 *
	 * @param args       port number
	 * @since           1.0
	 */
	public static void main(String[] args) {
        
		Registry_Server reg;
		
		/**
		 * get port number or use default port number, then create socket to listen on
		 */
		if(args.length >= 1)
			 reg = new Registry_Server(Integer.parseInt(args[0]));
		else
			 reg = new Registry_Server(PORT);
		
		try {
			reg.listener = new ServerSocket(reg.getPort());
		} catch (IOException e) {
			System.out.printf("Registry Server: cannot start registry server at port; %d!",reg.getPort());
			e.printStackTrace();
		}
		
		
		/**
		 * keep accepting connections, forward each of them to a new registryThread
		 */
		while(reg.isRun){
			try {
				Socket sock = reg.listener.accept();
				
				registryThread rt = new registryThread(sock, reg);
				executor.execute(rt);
				
            } catch (IOException e) {
            	reg.isRun = false;
                System.err.println("managerServer: Create new thread error!");
            }
		}
		
		executor.shutdown();
		
	}

	
	
	
	
}
