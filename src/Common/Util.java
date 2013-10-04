package Common;

import java.lang.reflect.Method;

public final class Util {
	private Util() {}
	
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
