package Exceptions;


/**
 * RemoteException class is responsible of showing
 * the cause and detailed messages of an exception happening remotely.
 * 
 * @author      Jing Gao
 * @author		Rui Zhang
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class RemoteException extends java.io.IOException {

	private static final long serialVersionUID = -6737045103510223320L;
    public Throwable detail;

    /**
     * constructor of RemoteException class
     *
     * @param s         the string of detailed information
     * @param cause     the throwable cause of the remote exception
     */
    public RemoteException(String s, Throwable cause) {
        super(s);
        initCause(null);
        detail = cause;
    }


    /**
     * get the throwable cause of the remote exception
     *
     * @return          the cause
     */
    public Throwable getCause() {
        return detail;
    }
}
