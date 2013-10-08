package Examples;

import java.io.Serializable;

public class ZipCodeList implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3666890187129184632L;
	String city;
    String ZipCode;
    ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n)
    {
        city=c;
        ZipCode=z;
        next=n;
    }
}
