package nameserver;

import Server.RemoteObjectRef;

public class NameServerImpl 
    implements NameServer
{
    String serviceName;
    RemoteObjectRef ro;
    NameServer next;

    public NameServerImpl()
    {
	serviceName="server";
	ro=new RemoteObjectRef("localhost", 15440, -1, "NameServerImpl");
	next=null;
    }

    public NameServerImpl(String s, RemoteObjectRef r, NameServer n)
    {
	serviceName=s;
	ro=r;
	next=n;
    }
    
    public NameServer add(String s, RemoteObjectRef r, NameServer n)
    {
	return new NameServerImpl(s, r, this);
    }

    public RemoteObjectRef match(String name)
    {
    	System.out.println(serviceName+" "+name);
	if (name.equals(serviceName))
	    return ro;
	else
	    return null;
    }
    
    public  NameServer next()
    {
	return next;
    }

}

