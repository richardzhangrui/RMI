package nameserver;



import java.io.Serializable;

import Server.Remote;
import Server.RemoteObjectRef;

public interface NameServer  extends Remote,Serializable
{
    public RemoteObjectRef match(String name);
    public NameServer add(String s, RemoteObjectRef r, NameServer n);
    public NameServer next();   
}

