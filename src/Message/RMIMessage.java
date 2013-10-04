package Message;

import java.io.Serializable;

import Server.Remote;

public interface RMIMessage extends Remote, Serializable{
	
	public Object get();
	public void set(Object obj);
	
}
