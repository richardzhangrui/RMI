package Message;

public class ExMessage implements RMIMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1860845232792116296L;
	
	private Exception ex;
	
	public ExMessage(Exception e) {
		ex = e;
	}
	
	public Exception get() {
		// TODO Auto-generated method stub
		return ex;
	}

	public void set(Object obj) {
		// TODO Auto-generated method stub
		ex = (Exception)obj;
	}
	

}
