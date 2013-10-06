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

public class RemoteServerRef {
	
	private static volatile Hashtable<Integer, Object> objects
		= new Hashtable<Integer, Object>(100);
	private static volatile Hashtable<Integer, Hashtable<Integer, Method>> methods 
		= new Hashtable<Integer, Hashtable<Integer, Method>>(100);
	
	private static final int PORT = 15440;
	private static final int REG_PORT = 15640;
	private static final String REG_HOST = "localhost";
	
	private int port;
	
	private Integer objid = 0;
	
	private volatile boolean isRun = true;
	
	private String reg_host;
	private int reg_port;
	
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
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	void add_Obj(Object obj) {
		Hashtable<Integer, Method> tmp = new Hashtable<Integer, Method>();
		for (Method m: obj.getClass().getMethods()) {
			Integer key = Util.Hash_Method(m);
			tmp.put(key, m);
		}
		
		synchronized(methods) {
			synchronized(objects) {
				synchronized(this.objid) {
					methods.put(this.objid, tmp);
					objects.put(this.objid, obj);
					objid++;
				}
			}
		}
	}
	
	void remove_Obj(int id) {
		synchronized(methods) {
			synchronized(objects) {
				methods.remove(id);
				objects.remove(id);
			}
		}
	}
	
	void cmdline() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("-$->");
		String args[];
		String input;
		try {
			input = reader.readLine();
			args = input.split(" ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Remote Server: Read input error!");
			return;
		}
		
		if(args[0].equals("quit")) {
				isRun = false;
				System.exit(0);
		}
		else if(args[0].equals("print")){
			
		}
		else {
			Class<?> obj;
			try {
				obj = Class.forName(args[0]);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.printf("Remote Server: No such class: %s\n", args[0]);
				return;
			}
			
			boolean flag = false;
			
			for (Class<?> i: obj.getInterfaces()) {
				if (i.getName().equals("Server.Remote")) 
					flag = true;
			}
			
			if (!flag) {
				System.out.printf("Remote Server: %s is not a remote object!\n", args[0]);
				return;
			}
			
			Constructor<?>[] cs = obj.getConstructors();
			Constructor<?> c = cs[0];
			Object newobj;
			
			
			try {
				newobj = c.newInstance();
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				System.out.printf("Remote Server: %s initiation failed\n", args[0]);
				return;
			}
			
			RemoteObjectRef ror;
			String hostip = null;
			try {
				hostip = InetAddress.getLocalHost().getHostAddress().toString();
				ror = new RemoteObjectRef(hostip, this.getPort(), this.objid, obj.getName());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				System.out.printf("Remote Server: Unknow host address: %s\n", hostip);
				return;
			}
			
			RegMessage message = new RegMessage();
			
			message = new RegMessage(message.new regInfo("rebind", args[1], ror));
			
			try {
				CommunicationModule.writeObject(this.reg_host, this.reg_port,message);
			} catch (RemoteException e) {
				System.out.printf("Remote Server: Remote Exception! Cannot connect to: %s: %d\n", this.reg_host, this.reg_port);
				return;
			}
			
			this.add_Obj(newobj);
			System.out.printf("Remote Server: Add new remote object %s with service name %s success!\n", obj.getName(),args[1]);
		}
	}
	
	void start() {
		Remote_Thread rt = new Remote_Thread(port, this);
		Thread t = new Thread(rt);
		t.start();
		
		while(isRun) {
			cmdline();
		}
	}
	
	void do_job(MethodMessage m, String host, int port) {
		MethodInfo info = m.get();
		
		Object obj = objects.get(info.getObjKey());
		if(obj == null) {
			ExMessage message = new ExMessage(new RemoteException("No such object",new Throwable("Caused by no such object")));
			try {
				CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
			}
			System.out.println("Remote Server: Do job finished but find no such object!");
			return;
		}
		Method method = methods.get(info.getObjKey()).get(info.getHashcode());
		
		if(method == null) {
			ExMessage message = new ExMessage(new RemoteException("No such method",new Throwable("Caused by no such method")));
			try {
				CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
			}
			System.out.println("Remote Server: Do job finished but find no such method!");
			return;
		}
		
		RMIMessage message = null;
		
		try {
			if(method.getReturnType().equals(void.class)){
				method.invoke(obj, m.get().getParams());
				System.out.printf("Remote Server: Do job %s.%s finished and with no return value %s:%d!\n",obj.getClass().toString(),method.getName(),host,port);
				return;
			}
			else {
				message = new RVMessage(method.invoke(obj, m.get().getParams()));
				System.out.printf("Remote Server: Do job %s.%s finished and send return value to %s:%d!\n",obj.getClass().toString(),method.getName(),host,port);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Remote Server: Do job finished but invoke failed with Exception!");
			message = new ExMessage(new RemoteException("Remote Exception",new Throwable("Caused by Invoke Method")));
		}  finally {
			try {
				if(message != null)
					CommunicationModule.writeObject(host, port, message);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
			}
		}
			
	}
	
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
