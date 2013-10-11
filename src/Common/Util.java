package Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	
	/** 
     * write specific content into a file called filename 
     *
     * @param content   the content we want to write into a file
     * @param filename  the naem of the file that we want to write 
     * @since           1.0
     */
	public static void writeToFile(String content, String filename) {
		File newfile = new File(filename);
		if(!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(newfile);
			out.write(content.getBytes(), 0, content.getBytes().length);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** 
     * read content from a file called filename 
     *
     * @param filename  the naem of the file that we want to read
     * @return			the content of the file 
     * @since           1.0
     */
	public static byte[] readFromFile(String filename) {
		File newfile = new File(filename);
		if(!newfile.exists()) {
			System.out.println("File not exist!");
			return null;
		} 
		FileInputStream in = null;
		byte[] content = new byte[(int) newfile.length()];
		try {
			in = new FileInputStream(newfile);
			in.read(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return content;
	}
	
	public static void writeBinaryToFile(byte[] content, String filename) {
		File newfile = new File(filename);
		if(!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(newfile);
			out.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
