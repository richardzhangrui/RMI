package Registry;

public final class LocateRegistry {
	/* disable public constructor */
	private LocateRegistry() {}
	
	public static Registry_Client getRegistry(String host, int port)
    {
        return new Registry_Client(host, port);
    }
}
