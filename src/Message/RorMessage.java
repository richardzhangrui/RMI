package Message;
import Server.Remote;

/**
 * RorMessage is a class communicating remote object reference. It implements RMIMessage interface.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RorMessage implements RMIMessage{

	private static final long serialVersionUID = -475036571713209175L;
	private Remote ref;
	
	/** 
     * constructor of RorMessage class, initializing it with a remote object reference
     * 
     * @param ref       the remote object reference to be capsuled and transmitted 
     * @since           1.0
     */
	public RorMessage(Remote ref) {
		super();
		this.ref = ref;
	}
	
	/** 
     * get the remote object reference from message
     * 
     * @return          the remote object reference in the message
     * @since           1.0
     */
	@Override
	public Remote get() {
		return ref;
	}

    /** 
     * set the remote object reference in message
     * 
     * @param obj        the remote object reference to be capsuled and transmitted
     * @since           1.0
     */
	@Override
	public void set(Object obj) {
		ref = (Remote) obj;
	}
	

}
