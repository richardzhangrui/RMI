package Message;

/**
 * RVMessage is a class communicating remote method return value. It implements RMIMessage interface.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RVMessage implements RMIMessage{

	private static final long serialVersionUID = 6234099765470677434L;
	private Object returnValue;
	
	/** 
     * constructor of RVMessage class, initializing it with a return value object
     * 
     * @param e         the return value object to be capsuled and transmitted 
     * @since           1.0
     */
	public RVMessage(Object retV){
		returnValue = retV;
	}
	
	/** 
     * get the return value from message
     *
     * @return          the return value object in the message
     * @since           1.0
     */
	@Override
	public Object get() {
		return returnValue;
	}

    /** 
     * set the return value object in message
     * 
     * @param obj       the return value object to be capsuled and transmitted 
     * @since           1.0
     */
	@Override
	public void set(Object obj) {
		returnValue = obj;
	}

}
