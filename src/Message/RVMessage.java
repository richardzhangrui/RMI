package Message;

public class RVMessage implements RMIMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6234099765470677434L;
	
	private Object returnValue;
	
	public RVMessage(Object retV){
		returnValue = retV;
	}
	
	@Override
	public Object get() {
		// TODO Auto-generated method stub
		return returnValue;
	}

	@Override
	public void set(Object obj) {
		// TODO Auto-generated method stub
		returnValue = obj;
	}

}
