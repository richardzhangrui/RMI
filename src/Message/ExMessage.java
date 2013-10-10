package Message;

/**
 * ExMessage is a class communicating remote exceptions. It implements RMIMessage interface.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class ExMessage implements RMIMessage{

	private static final long serialVersionUID = 1860845232792116296L;
	private Exception ex;
	
	/** 
     * constructor of ExMessage class, initializing it with an exception
     * 
     * @param e         the exception to be capsuled and transmitted 
     * @since           1.0
     */
	public ExMessage(Exception e) {
		ex = e;
	}
	
	/** 
     * get the exception from message
     * 
     * @return          the exception in the message
     * @since           1.0
     */
	public Exception get() {
		return ex;
	}

    /** 
     * set the exception in message
     * 
     * @param obj       the exception to be capsuled and transmitted 
     * @since           1.0
     */
	public void set(Object obj) {
		ex = (Exception)obj;
	}
	

}
