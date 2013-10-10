package Message;

import java.io.Serializable;
import Server.Remote;

/**
 * RMIMessage is the interface for all types of messages among RMI clients and servers, including
 * ROR messages, exception messages, etc.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public interface RMIMessage extends Remote, Serializable{
	
	/** 
     * get the exception from message
     * 
     * @return          the content object in the message
     * @since           1.0
     */
	public Object get();
	
	/** 
     * set the exception in message
     * 
     * @param obj       the object to be capsuled and transmitted 
     * @since           1.0
     */
	public void set(Object obj);
	
}
