package Message;

import java.io.Serializable;
import Server.Remote;

/**
 * RegMessage is a class responsible for the messages sent to RMI registry server. It implements 
 * RMIMessage interface.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RegMessage implements RMIMessage{

    /**
     * regInfo is a class inside of RegMessage. It contains the command string, service name and 
     * remote object reference that will be sent to RMI registry server via RegMessage.
     * 
     * @author      Rui Zhang
     * @author      Jing Gao
     * @version     1.0, 10/08/2013
     * @since       1.0
     */
	public class regInfo implements Serializable{
		private static final long serialVersionUID = 8230230872595558427L;
		public String cmd;
		public String service_name;
		public Remote ror;
		
		/** 
         * constructor of regInfo class
         * 
         * @param cmd           command string, needed in all request
         * @param service_name  service name string, needed when initializing a lookup or rebind request
         * @param ror           remote object reference, needed when initializing a rebind request
         * @since               1.0
         */
		public regInfo(String cmd, String service_name, Remote ror) {
			super();
			this.cmd = cmd;
			this.service_name = service_name;
			this.ror = ror;
		}
	}

	private static final long serialVersionUID = 7715655264625017766L;
	private regInfo reg;
	
	/** 
     * constructor of RegMessage class
     * 
     * @since       1.0
     */
	public RegMessage() {
		super();
	}

	/** 
     * constructor of regInfo class
     * 
     * @param reg           the regInfo instance to be contained in the RegMessage
     * @since       1.0
     */
	public RegMessage(regInfo reg) {
		super();
		this.reg = reg;
	}

	/** 
     * get regInfo from the message
     * 
     * @since       1.0
     */
	@Override
	public regInfo get() {
		return reg;
	}

	/** 
     * set regInfo of the message
     * 
     * @param obj   the regInfo object to be contained in the message
     * @since       1.0
     */
	@Override
	public void set(Object obj) {
		reg = (regInfo)obj;
	}
	
}

