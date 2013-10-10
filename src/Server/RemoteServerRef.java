package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import Common.Util;
import Exceptions.RemoteException;
import Message.ExMessage;
import Message.MethodInfo;
import Message.MethodMessage;
import Message.RMIMessage;
import Message.RVMessage;
import Message.RegMessage;

/**
 * RemoteServerRef is the remote server where remote objects and methods reside on. It keeps a 
 * <objKey, Object> hashtable, a <objKey, objMethods> hashtable, and a <Object, objKey>
 * hashtable. The main() function runs on the remote server, which creates a remote thread to 
 * listen to connections and then scan for user command to add object and its methods to its 
 * hashtables  * and send corresponding rebind request to RMI registry server in order to register
 * new services there. At the same time, do_job function deals with method messages passed from 
 * remote services, performing the core functionality of invoking remote methods relative to the
 * clients, and returning the return values of the remote methods.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RemoteServerRef {
	
	private static volatile Hashtable<Integer, Object> objects
		= new Hashtable<Integer, Object>(100);
	private static volatile Hashtable<Integer, Hashtable<Integer, Method>> methods 
		= new Hashtable<Integer, Hashtable<Integer, Method>>(100);
	private static volatile Hashtable<Object, Integer> keys
		= new Hashtable<Object, Integer>(100);
	
	private static final int PORT = 15440;
	private static final int REG_PORT = 15640;
	private static final String REG_HOST = "localhost";
	
	private int port;
	private Integer objid = 0;
	private volatile boolean isRun = true;
	private String reg_host;
	private int reg_port;
	
	/** 
     * constructor of RemoteServerRef class
     * 
     * @param port		the port number of the remote server
     * @param regport   the port number of RMI registry server
     * @param reghost   the hostname of RMI registry server
     * @since           1.0
     */
	public RemoteServerRef(int port, int regport, String reghost) {
		super();
		if(port <= 0) {
			this.port = PORT;
		}
		else {
			this.port = port;
		}
		if(regport <= 0) {
			this.reg_port = REG_PORT;
		}
		else {
			this.reg_port = regport;
		}
		this.reg_host = reghost;
	}
	
	/**
	 * get port number of the server
	 *
	 * @return  the port number of the server
	 * @since   1.0
	 */
	public int getPort() {
		return port;
	}

    /**
	 * set the port number of the server
	 *
	 * @param port  the port number of the server
	 * @since       1.0
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * add an object to the <objKey, Object> hashtable, add its methods to <objKey,objMethods>
	 * hash table, and add its objKey to <Object, objKey> hashtable
	 *
	 * @param obj   the object to be added
	 * @since       1.0
	 */
	void add_Obj(Object obj) {
		Hashtable<Integer, Method> tmp = new Hashtable<Integer, Method>();
		for (Method m: obj.getClass().getMethods()) {
			Integer key = Util.Hash_Method(m);
			tmp.put(key, m);
		}
		
		synchronized(methods) {
			synchronized(objects) {
				synchronized(keys) {
					synchronized(this.objid) {
						methods.put(this.objid, tmp);
						objects.put(this.objid, obj);
						keys.put(obj, this.objid);
						objid++;
					}
				}
			}
		}
	}
	
	/**
	 * remove an object (specified by objKey) from the <objKey, Object> hashtable, remove its 
	 * methods from <objKey,objMethods> hashtable, and remove its objKey from <Object, objKey> 
	 * hashtable.
	 *
	 * @param id    the object id of the object to be removed
	 * @since       1.0
	 */
	void remove_Obj(int id) {
		synchronized(methods) {
			synchronized(objects) {
				synchronized(keys){
					methods.remove(id);
					keys.remove(objects.get(id));
					objects.remove(id);
				}
			}
		}
	}
	
	/**
	 * After necessary initializations, the server shows a prompt, reads command from stdin, if the 
	 * user specifies a object and a service name, then the server creates a corresponding remote 
	 * object reference and sends a RegMessage (with rebind command) to RMI registry server in order
	 * to register this remote service to RMI registry.
	 *
	 * @since           1.0
	 */
	void cmdline() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		/*
		 * read and parse user command
		 */
		System.out.print("-$->");
		String args[];
		String input;
		try {
			input = reader.readLine();
			args = input.split(" ");
		} catch (IOException e) {
			System.err.println("Remote Server: Read input error!");
			return;
		}
		
		/*
		 * quit if specified to do so, print info is user types print command
		 */
		if(args[0].equals("quit")) {
				isRun = false;
				System.exit(0);
		}
		else if(args[0].equals("print")){
			
		}
		else {
		
		    /*
		     * User wants to register a remote service to RMI registry, the remote server get the 
		     * specified object, and validate that it qualifies RMI.
		     */
			Class<?> obj;
			try {
				obj = Class.forName(args[0]);
			} catch (ClassNotFoundException e) {
				System.out.printf("Remote Server: No such class: %s\n", args[0]);
				return;
			}
			
			boolean flag = false;
			if(Remote.class.isAssignableFrom(obj)) {
				flag = true;
			}
			if (!flag) {
				System.out.printf("Remote Server: %s is not a remote object!\n", args[0]);
				return;
			}
			
			/*
			 * If the specified object qualifies RMI, the server initializes the object and
			 * create the corresponding remote object reference.
			 */
			Constructor<?>[] cs = obj.getConstructors();
			Constructor<?> c = cs[0];
			Object newobj;
			try {
				newobj = c.newInstance();
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e1) {
				System.out.printf("Remote Server: %s initiation failed\n", args[0]);
				return;
			}
			
			RemoteObjectRef ror;
			String hostip = null;
			try {
				hostip = InetAddress.getLocalHost().getHostAddress().toString();
				ror = new RemoteObjectRef(hostip, this.getPort(), this.objid, obj.getName());
			} catch (UnknownHostException e) {
				System.out.printf("Remote Server: Unknow host address: %s\n", hostip);
				return;
			}
			
			/*
			 * The server creates a RegMessage and send a rebind request to RMI registry server
			 */
			RegMessage message = new RegMessage();
			message = new RegMessage(message.new regInfo("rebind", args[1], ror));
			try {
				CommunicationModule.writeObject(this.reg_host, this.reg_port,message);
			} catch (RemoteException e) {
				System.out.printf("Remote Server: Remote Exception! Cannot connect to: %s: %d\n", this.reg_host, this.reg_port);
				return;
			}
			
			/*
			 * Finally, the server adds the object and associated remote methods to its hashtables.
			 */
			this.add_Obj(newobj);
			System.out.printf("Remote Server: Add new remote object %s with service name %s success!\n", obj.getName(),args[1]);
		}
	}
	
	/**
	 * The server starts a thread to listen to connections, and then keeps reading user commands
	 * from stdin.
	 *
	 * @since           1.0
	 */
	void start() {
		Remote_Thread rt = new Remote_Thread(port, this);
		Thread t = new Thread(rt);
		t.start();
		
		while(isRun) {
			cmdline();
		}
	}
	
	/**
	 * process the method messages passed from remote services, performing the core functionality
	 * of invoking remote methods relative to the clients, and returning the return values of the 
	 * remote methods to the client requesting the RMI.
	 *
	 * @param m     the method message to be processed
	 * @param host  the hostname of the client where the message was sent
	 * @param port  the port number of the client
	 * @since       1.0
	 */
	void do_job(MethodMessage m, String host, int port) {
		MethodInfo info = m.get();

        /*
         * respond an exception message to the client if the requested remote object does not exist
         */		
		Object obj = objects.get(info.getObjKey());
		if(obj == null) {
			ExMessage message = new ExMessage(new RemoteException("No such object",new Throwable("Caused by no such object")));
			try {
				CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e) {
			}
			System.out.println("Remote Server: Do job finished but find no such object!");
			return;
		}
		
		/*
		 * respond an exception message to the client if the requested remote method does not exit 
		 * on the remote object
		 */
		Method method = methods.get(info.getObjKey()).get(info.getHashcode());
		if(method == null) {
			ExMessage message = new ExMessage(new RemoteException("No such method",new Throwable("Caused by no such method")));
			try {
				CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e) {
			}
			System.out.println("Remote Server: Do job finished but find no such method!");
			return;
		}
		
		/*
		 * invoke the specified remote method, return the return value (or exception if any) 
		 * to the client requesting the RMI
		 */
		RMIMessage message = null;
		try {
			Object[] params = m.get().getParams();
			for(Object p : params){
				if(p.getClass().isAssignableFrom(obj.getClass())) {
					p = objects.get(((RemoteStub)p).ref.getObj_Key());
				}
			}
			
			if(method.getReturnType().equals(void.class)){
				method.invoke(obj, params);
				message = new RVMessage(null);
				System.out.printf("Remote Server: Do job %s.%s finished and with no return value %s:%d!\n",obj.getClass().toString(),method.getName(),host,port);
			}
			else {
				Object r = method.invoke(obj, params);
				Class<?> c = obj.getClass();
				
				/*
				 * the situations where the stub need be returned, localise the corresponding remote
				 * object reference and get the stub and return it
				 */
				if(r != null && r.getClass().equals(c)){
					String hostip = InetAddress.getLocalHost().getHostAddress().toString();
					
					add_Obj(r);
					RemoteObjectRef ror = new RemoteObjectRef(hostip, this.getPort(),keys.get(r) , c.getName());
					r = ror.localise();
				}
				
				message = new RVMessage(r);
				System.out.printf("Remote Server: Do job %s.%s finished and send return value to %s:%d!\n",obj.getClass().toString(),method.getName(),host,port);
			}
		} catch (Exception e) {
			System.out.println("Remote Server: Do job finished but invoke failed with Exception!");
			message = new ExMessage(new RemoteException("Remote Exception",new Throwable("Caused by Invoke Method")));
		}  finally {
		
		    /*
		     * respond to the client return value message if RMI succeeded, respond exception
		     * message if there is exceprions.
		     */
			try {
				if(message != null)
					CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e1) {
			}
		}
			
	}

    /**
	 * the main function of remote server, responsible of parsing the arguments, and initializing the 
	 * remote server with the arguments. Then the remote server start working.
	 *
	 * @param args      self port number, hostname and port number of RMI registry server
	 * @since           1.0
	 */	
	public static void main(String[] args) {
		RemoteServerRef server;
		
		if(args.length < 1)
			server = new RemoteServerRef(PORT, REG_PORT, REG_HOST);
		else if(args.length < 2)
			server = new RemoteServerRef(Integer.parseInt(args[0]), REG_PORT, REG_HOST);
		else if(args.length < 3) 
			server = new RemoteServerRef(Integer.parseInt(args[0]), REG_PORT, args[1]);
		else 
			server = new RemoteServerRef(Integer.parseInt(args[0]), Integer.parseInt(args[2]), args[1]);		
		server.start();
	}
}
