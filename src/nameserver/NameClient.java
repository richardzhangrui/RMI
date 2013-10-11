package nameserver;

import java.io.FileNotFoundException;

import Registry.LocateRegistry;
import Registry.Registry_Client;
import Server.RemoteObjectRef;

public class NameClient {
	public static void main(String[] args) throws FileNotFoundException {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String serviceName = args[2];
	
		Registry_Client sr = 
			    LocateRegistry.getRegistry(host, port);
			RemoteObjectRef ror = (RemoteObjectRef)sr.lookup(serviceName);
			
		NameServer s = (NameServer) ror.localise();
		
		s = s.add("server123", ror, s);
		
		RemoteObjectRef se = s.match("server123");
		
		if(se == null){
			System.out.println("match error 1");
			return;
		}
		
		se.setObj_Key(se.getObj_Key() + 1);
		
		s = (NameServer) se.localise();
		
		se = s.next().match("server");
		
		if(se == null){
			System.out.println("match error 2");
			return;
		}
		
		System.out.println("Test Sucess!");
	}
}
