package Common;

import java.lang.reflect.Method;

/**
 * Util is a utility class for RMI program. It compute the hash code of a givin method.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/8/2013
 * @since       1.0
 */
public final class Util {

    /** 
     * constructor of Util class
     *
     * @since           1.0
     */
	private Util() {}
	
	/** 
     * compute the hash code of a given method
     *
     * @param method    the method we need compute a hash code for
     * @return          the integer hash code of the method
     * @since           1.0
     */
	public static int  Hash_Method(Method method){
		int hash = method.getReturnType().getName().hashCode() ^ method.getName().hashCode();
		
		int i = 0;
		
		for(Class<?> c : method.getParameterTypes()) {
			hash ^= c.getName().hashCode();
			hash += i;
			i++;
		}
		
		return hash;
	}
	
}
