package Message;
import Server.Remote;


public class RorMessage implements RMIMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -475036571713209175L;
	
	private Remote ref;
	
	public RorMessage(Remote ref) {
		super();
		this.ref = ref;
	}
	
	@Override
	public Remote get() {
		// TODO Auto-generated method stub
		return ref;
	}

	@Override
	public void set(Object obj) {
		// TODO Auto-generated method stub
		ref = (Remote) obj;
	}
	

}
