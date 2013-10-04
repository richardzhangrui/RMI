package Message;

import java.io.Serializable;

import Server.Remote;

public class RegMessage implements RMIMessage{

	public RegMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegMessage(regInfo reg) {
		super();
		this.reg = reg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7715655264625017766L;

	@Override
	public regInfo get() {
		// TODO Auto-generated method stub
		return reg;
	}

	@Override
	public void set(Object obj) {
		// TODO Auto-generated method stub
		reg = (regInfo)obj;
	}
	
	private regInfo reg;
	
	public class regInfo implements Serializable{
		public regInfo(String cmd, String service_name, Remote ror) {
			super();
			this.cmd = cmd;
			this.service_name = service_name;
			this.ror = ror;
		}
		/**
		 * 
		 */
		private static final long serialVersionUID = 8230230872595558427L;
		public String cmd;
		public String service_name;
		public Remote ror;
	}
}
