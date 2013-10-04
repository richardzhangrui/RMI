package Examples;

import Registry.LocateRegistry;
import Server.RemoteObjectRef;


public class Hello_Client implements Hello_Interface{
	
	private String host;
	private int port;
	
	private static final String HOST = "localhost";
	private static final int PORT = 15640;
	
	public Hello_Client(){}
	
	@Override
	public String sayHello(String name) {
		RemoteObjectRef ref = (RemoteObjectRef)(LocateRegistry.getRegistry(host, port).lookup("hello"));
		
		Hello_Stub hs = (Hello_Stub) ref.localise();
		
		return hs.sayHello(name);
	}

	@Override
	public String sayHello(int ok) {
		RemoteObjectRef ref = (RemoteObjectRef)(LocateRegistry.getRegistry(host, port).lookup("hello"));
		
		Hello_Stub hs = (Hello_Stub) ref.localise();
		
		return hs.sayHello(ok);
	}
	
	@Override
	public String testException() {
		RemoteObjectRef ref = (RemoteObjectRef)(LocateRegistry.getRegistry(host, port).lookup("hello"));
		
		Hello_Stub hs = (Hello_Stub) ref.localise();
		
		return hs.testException();
	}
	
	public static void main(String[] args) {
		Hello_Client hello = new Hello_Client();
		if(args.length >= 2) {
			hello.host = args[0];
			hello.port = Integer.parseInt(args[1]);
		}
		else {
			hello.host = HOST;
			hello.port = PORT;
		}
		
		System.out.println(hello.sayHello("Richard"));
		System.out.println(hello.sayHello(1));
		hello.testException();
		
	}

	

}
