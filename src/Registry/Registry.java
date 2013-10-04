package Registry;

import java.io.Serializable;

import Server.Remote;

public interface Registry extends Remote, Serializable {
	public String[] list();
	public Remote lookup(String name);
	public void rebind(String name, Remote ror);
}
