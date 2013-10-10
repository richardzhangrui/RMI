package Message;

import java.io.Serializable;

/**
 * MethodInfo is a class containing the information about a method that need be invoked remotely.
 * It will be carry by a MethodMessage to reach remote server.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class MethodInfo implements Serializable{

	private static final long serialVersionUID = -6722844168862307032L;
    private Integer ObjKey;
	private int hashcode;
	private Object[] params;
	
	/** 
     * constructor of MethodInfo class
     * 
     * @param key       the key of object on which the remote method will be invoked
     * @param hash      the hashcode of method computed from Util class
     * @param params    the parameters of the method
     * @since           1.0
     */
	public MethodInfo(Integer key, int hash, Object[] params) {
		super();
		this.ObjKey = key;
		this.hashcode = hash;
		this.params = params;
	}

    /** 
     * get the parameters of the method
     * 
     * @return          the array of parameter objects
     * @since           1.0
     */
	public Object[] getParams() {
		return params;
	}
	
	/** 
     * set the parameters of the method
     * 
     * @param params    the parameter objects to be set to the method
     * @since           1.0
     */
	public void setParams(Object[] params) {
		this.params = params;
	}

    /** 
     * get the object key of the method
     * 
     * @return          the integer object key
     * @since           1.0
     */
	public Integer getObjKey() {
		return ObjKey;
	}

    /** 
     * set the object key of the method
     * 
     * @param objKey    the integer object key
     * @since           1.0
     */
	public void setObjKey(Integer objKey) {
		ObjKey = objKey;
	}

    /** 
     * get the hashcode of the method
     * 
     * @return          the hashcode of the method
     * @since           1.0
     */
	public int getHashcode() {
		return hashcode;
	}

    /** 
     * set the hashcode of the method
     * 
     * @param hashcode  the hashcode of the method
     * @since           1.0
     */
	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}
}
