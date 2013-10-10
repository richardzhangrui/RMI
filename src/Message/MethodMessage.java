package Message;

public class MethodMessage implements RMIMessage{

    /**
     * MethodMessage is a class responsible for message containing RMI method information, which
     * include method object key, method hash code and method parameters.
     * 
     * @author      Rui Zhang
     * @author      Jing Gao
     * @version     1.0, 10/08/2013
     * @since       1.0
     */
	private static final long serialVersionUID = -3150556858524474075L;
	
	private MethodInfo method; 
	
	/** 
     * constructor of MethodMessage class, initializing it with method info
     * 
     * @param m         the method information
     * @since           1.0
     */
	public MethodMessage(MethodInfo m) {
		method = m;
	}
	
	/** 
     * get the method information from message
     * 
     * @return          the method information in the message
     * @since           1.0
     */
	@Override
	public MethodInfo get() {
		return method;
	}

    /** 
     * set the method information in the message
     * 
     * @param obj       the method information in the message
     * @since           1.0
     */
	@Override
	public void set(Object obj) {
		method = (MethodInfo)obj;
	}

}
