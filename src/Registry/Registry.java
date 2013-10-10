package Registry;

import java.io.Serializable;
import Server.Remote;

/**
 * Registry is the interface for RMI registry operations, which include lookup, rebind
 * and list methods. It extends Remote and Serializable interfaces.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public interface Registry extends Remote, Serializable {
    
    /** 
     * list all the service names registered with RMI registry
     * 
     * @since           1.0
     */
	public String[] list();
	
	/**
	 * look up by service name a remote object reference that is registered with RMI registry, 
	 * return it if found, return an exception message if not found
	 *
	 * @para serviceName    the string of service name
	 * @since               1.0
	 */
	public Remote lookup(String name);

	/**
	 * bind a service with remote object reference on the RMI registry
	 *
	 * @para name       the string of service name
	 * @para ror        the remote object reference to be registered
	 * @since           1.0
	 */
	public void rebind(String name, Remote ror);
}
