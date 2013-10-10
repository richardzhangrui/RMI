package Registry;

/**
 * LocateRegistry is a class responsible of locating RMI registry on remote registry server.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public final class LocateRegistry {
	
    /** 
     * constructor of LocateRegistry class
     *
     * @since           1.0
     */
	private LocateRegistry() {}
	
	/** 
     * get RMI registry on remote registry server
     * 
     * @param host      host name string
     * @param           port number
     * @since           1.0
     */
	public static Registry_Client getRegistry(String host, int port)
    {
        return new Registry_Client(host, port);
    }
}
