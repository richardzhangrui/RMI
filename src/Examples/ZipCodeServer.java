package Examples;

import Server.Remote;

public interface ZipCodeServer extends Remote
{
    public void initialise(ZipCodeList newlist);
    public String find(String city);
    public ZipCodeList findAll();
    public void printAll();
}
