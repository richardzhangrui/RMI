package Message;

import java.io.Serializable;

public class MethodInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6722844168862307032L;
	
	private Integer ObjKey;
	
	private int hashcode;
	//private String name;
	private Object[] params;
	//private Class<?>[] parameterTypes;
	
	public MethodInfo(Integer key, int hash, Object[] params) {
		super();
		this.ObjKey = key;
		this.hashcode = hash;
		this.params = params;
	}
	
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
//	public Class<?>[] getParameterTypes() {
//		return parameterTypes;
//	}
//	public void setParameterTypes(Class<?>[] parameterTypes) {
//		this.parameterTypes = parameterTypes;
//	}

	public Integer getObjKey() {
		return ObjKey;
	}

	public void setObjKey(Integer objKey) {
		ObjKey = objKey;
	}

	public int getHashcode() {
		return hashcode;
	}

	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}
}
