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

public class Registry_Server implements Registry{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7472958118304045615L;

	private static Hashtable<String, Remote> bindings = new Hashtable<String, Remote>(100);
	
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	public ServerSocket listener;
	
	private boolean isRun = true;

	private static final int PORT = 15640;
	
	private int port;
	
	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Registry_Server(int port) {
		super();
		if (port <= 0) {
			port = PORT;
		}
		else {
			this.port = port;
		}
	}
	
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
	
	@Override
	public void rebind(String name, Remote ror) {
		synchronized (bindings) {
			bindings.put(name, ror);
		}
	}
	
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
	
	public static void main(String[] args) {
		Registry_Server reg;
		if(args.length >= 2)
			 reg = new Registry_Server(Integer.parseInt(args[1]));
		else
			 reg = new Registry_Server(PORT);
		
		try {
			reg.listener = new ServerSocket(reg.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.printf("Registry Server: cannot start registry server at port; %d!",reg.getPort());
			e.printStackTrace();
		}
		
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
