package Exceptions;

public class RemoteException extends java.io.IOException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6737045103510223320L;

    /**
     * The cause of the remote exception.
     *
     * <p>This field predates the general-purpose exception chaining facility.
     * The {@link Throwable#getCause()} method is now the preferred means of
     * obtaining this information.
     *
     * @serial
     */
    public Throwable detail;

    /**
     * Constructs a <code>RemoteException</code>.
     */
    public RemoteException() {
        initCause(null);  // Disallow subsequent initCause
    }

    /**
     * Constructs a <code>RemoteException</code> with the specified
     * detail message.
     *
     * @param s the detail message
     */
    public RemoteException(String s) {
        super(s);
        initCause(null);  // Disallow subsequent initCause
    }

    /**
     * Constructs a <code>RemoteException</code> with the specified detail
     * message and cause.  This constructor sets the {@link #detail}
     * field to the specified <code>Throwable</code>.
     *
     * @param s the detail message
     * @param cause the cause
     */
    public RemoteException(String s, Throwable cause) {
        super(s);
        initCause(null);  // Disallow subsequent initCause
        detail = cause;
    }

    /**
     * Returns the detail message, including the message from the cause, if
     * any, of this exception.
     *
     * @return the detail message
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
     * Returns the cause of this exception.  This method returns the value
     * of the {@link #detail} field.
     *
     * @return  the cause, which may be <tt>null</tt>.
     * @since   1.4
     */
    public Throwable getCause() {
        return detail;
    }
}