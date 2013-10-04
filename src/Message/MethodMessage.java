package Message;

public class MethodMessage implements RMIMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3150556858524474075L;
	
	private MethodInfo method; 
	
	public MethodMessage(MethodInfo m) {
		method = m;
	}
	
	@Override
	public MethodInfo get() {
		// TODO Auto-generated method stub
		return method;
	}

	@Override
	public void set(Object obj) {
		// TODO Auto-generated method stub
		method = (MethodInfo)obj;
	}

}
