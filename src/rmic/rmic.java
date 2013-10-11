package rmic;

import java.io.IOException;
import java.lang.reflect.Method;

import Common.Util;


/**
 * rmic is the RMI stub compiler class. It creates a _Stub.java code file and fitting in all
 * necessary arguments, method names and parameters. Then the compiler compiles the source 
 * code to generate _Stub.class file, which will act as the stub.
 * 
 * @author      Rui Zhang
 * @author      Jing Gao
 * @version     1.0, 10/08/2013
 * @since       1.0
 */
public class rmic {

    /**
	 * the main function of RMI stub compiler class, responsible of generating the source code 
	 * of the stub and compiling it to generate the stub class.
	 *
	 * @param args      the name of class for which it will generate the stub
	 * @since           1.0
	 */	
	public static void main(String[] args) {
		
		/*
		 * parse the arguments and locate the requested class
		 */
		if(args.length < 1){
			System.out.println("Usage:java rmic [classname]");
			System.exit(0);
		}
		String classname = args[0];
		int pos = classname.lastIndexOf(".");
		String classname1 = classname.substring(0, pos);
		String classname2 = classname.substring(pos+1, classname.length());
		Class<?> obj;
		try {
			obj = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			System.out.printf("Rmic: There's no such class: %s!\n", classname);
			return;
		}
		
		/*
		 * generate source code of the stub
		 */
		String buffer = "";
		buffer += "package " +classname1+";\n" +
				"import java.lang.reflect.Method;\n" +
				"import Common.Util;\n" +
				"import Exceptions.RemoteException;\n" +
				"import Message.ExMessage;\n" +
				"import Message.MethodInfo;\n" +
				"import Message.MethodMessage;\n" + 
				"import Message.RVMessage;\n" +
				"import Server.CommunicationModule;\n" +
				"import Server.Remote;\n" +
				"import Server.RemoteObjectRef;\n" +
				"import Server.RemoteStub;\n";
		
		buffer += "public class "+ classname2 +"_Stub implements RemoteStub,";
		for(Class<?> i : obj.getInterfaces()) {
			buffer += i.getName() + ",";
		}
		
		if(buffer.endsWith(","))
			buffer = buffer.substring(0, buffer.length()-1);
		
		buffer += "{\n" +
				"RemoteObjectRef ref;\n" +
				"public "+classname2+"_Stub(RemoteObjectRef r) {\n" +
				"	this.ref = r;\n" +
				"}\n";
		
		buffer += "public RemoteObjectRef getRef(){ return ref;}\n";
		
		
		/*
		 * create entry for each of the methods
		 */
		for(Method m : obj.getMethods()) {
			if(m.getDeclaringClass() == Object.class) 
				continue;
			buffer += "public "+m.getReturnType().getName()+" "+m.getName()+"(";
			int counter = 0;
			for(Class<?> c : m.getParameterTypes()) {
				buffer += c.getName()+" a"+counter+",";
				counter++;
			}
			if(buffer.endsWith(","))
				buffer = buffer.substring(0, buffer.length()-1);
			
			buffer += ") {\n";
			
			int length = m.getParameterTypes().length;
			if((length = m.getParameterTypes().length) > 0) {
				buffer += "Class<?>[] types = new Class<?>["+length+"];\n";
				counter = 0;
				for(Class<?> type:m.getParameterTypes()){
					buffer += "types["+counter+"] = "+type.getName()+".class;\n";
					counter++;
				}
			}
			
			boolean flag = true;
			if(m.getReturnType().getName().equals("void"))
				flag = false;
			
			buffer += "Method method;\n" +
					"try {\n" +
					"method = this.getClass().getMethod(\""+m.getName()+"\"";
			if(length > 0)
				buffer +=", types);\n";
			else 
				buffer += ");\n";
			buffer += "} catch (NoSuchMethodException | SecurityException e) {\n" +
					"System.out.println(\"No such method!\");\n" +
					"return";
			if(flag) {
				buffer += " null;\n}";
			}
			else 
				buffer += ";\n}";
			
			buffer += "int key = Util.Hash_Method(method);\n";
			
			if(length > 0){
					buffer += "Object[] params = new Object["+length+"];\n";

					for(counter = 0;counter < length; counter++){
						buffer += "params["+counter+"] = a"+counter+";\n";
					}
			}
			buffer += "MethodInfo info = new MethodInfo(ref.getObj_Key(), key, ";
			
			if(length > 0)
				buffer += "params);";
			else 
				buffer += "null);";
				
			buffer += "\nMethodMessage message = new MethodMessage(info);\n" +
					"Object obj = null;\n" +
					"try {\n" +
					"CommunicationModule.writeObject(ref.getIP_adr(), ref.getPort(), message);\n";
			

			buffer += "obj = CommunicationModule.readObject(ref.getIP_adr(), ref.getPort());\n" +
						"if(obj instanceof ExMessage){\n" +
						"throw (RemoteException)((ExMessage)obj).get();\n}";
			
			
			buffer += "} catch (RemoteException e) {\n" +
					"System.out.printf(\"Remote Exception %s caused by %s!\\n\",e.detail, e.getCause().toString());\n";
			
			if(flag)
				buffer += "return null;\n";
			else 
				buffer += "return;\n";
			
			buffer += "}\n";
			
			if(flag)
				buffer += "return " +
						"("+m.getReturnType().getName()+")((RVMessage)obj).get();\n";
			
			buffer += "}\n";
			
		}
		
		buffer += "}\n";
		
		/*
		 * output the buffer to generate the source code file
		 */
		Util.writeToFile(buffer, classname1+"/"+classname2+"_Stub.java");
		
		/*
		 * compile the source code file to generate the stub
		 * remove the java source code
		 */
		try {
			Process pro = Runtime.getRuntime().exec("javac "+classname1+"/"+classname2+"_Stub.java");
			pro.waitFor();
			pro = Runtime.getRuntime().exec("rm "+classname1+"/"+classname2+"_Stub.java");
			pro.waitFor();
			Runtime.getRuntime().exec("mv "+classname2+"_Stub.class "+classname1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
