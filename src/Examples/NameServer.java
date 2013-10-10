package Examples;

import Exceptions.RemoteException;
import Registry.LocateRegistry;
import Server.RemoteObjectRef;
import Server.Remote;


public interface NameServer // extends YourRemote 
{
    public RemoteObjectRef match(String name);
    public NameServer add(String s, RemoteObjectRef r, NameServer n);
    public NameServer next();   
}

