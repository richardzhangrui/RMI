package Exceptions;


/**
 * RemoteException class is original from java rmi package. It is responsible of showing
 * the cause and detailed messages of an exception happening remotely.
 * 
 * @author      Oracle
 */
public class RemoteException extends java.io.IOException {

	private static final long serialVersionUID = -6737045103510223320L;
    public Throwable detail;

    /**
     * constructor of RemoteException class
     */
    public RemoteException() {
        initCause(null);
    }

    /**
     * constructor of RemoteException class
     *
     * @param s         the string of detailed information
     */
    public RemoteException(String s) {
        super(s);
        initCause(null);
    }

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
     * get the detailed info message of the exception, along with the throwable cause
     *
     * @return          the detailed info message
     */
    public String getMessage() {
        if (detail == null) {
            return super.getMessage();
        } else {
            return super.getMessage() + "; nested exception is: \n\t" +
                detail.toString();
        }
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
