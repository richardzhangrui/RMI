package ZipCodeR;

import java.io.Serializable;

import Server.Remote;

public interface ZipCodeRList  extends Remote,Serializable
{
    public String find(String city);
    public ZipCodeRList add(String city, String zipcode);
    public ZipCodeRList next();   
}
