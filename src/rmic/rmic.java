package rmic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;


public class rmic {
	public static void main(String[] args) {
		
		if(args.length < 1){
			System.out.println("Usage:java rmic [classname]");
			System.exit(0);
		}
		
		String classname = args[0];
		
		int pos = classname.lastIndexOf(".");
		
		String classname2 = classname.substring(pos+1, classname.length());
		
		Class<?> obj;
		try {
			obj = Class.forName(classname);
		} catch (ClassNotFoundException e) {
			System.out.printf("Rmic: There's no such class: %s!\n", classname);
			return;
		}
		
		String buffer = "";
		buffer += "package Examples;\n" +
				"import java.lang.reflect.Method;\n" +
				"import Common.Util;\n" +
				"import Exceptions.RemoteException;\n" +
				"import Message.ExMessage;\n" +
				"import Message.MethodInfo;\n" +
				"import Message.MethodMessage;\n" + 
				"import Message.RVMessage;\n" +
				"import Server.CommunicationModule;\n" +
				"import Server.Remote;\n" +
				"import Server.RemoteObjectRef;\n";
		
		buffer += "public class "+ classname2 +"_Stub implements ";
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
			
			if(flag) {
				buffer += "obj = CommunicationModule.readObject(ref.getIP_adr(), ref.getPort());\n" +
						"if(obj instanceof ExMessage){\n" +
						"throw (RemoteException)((ExMessage)obj).get();\n";
			}
			
			buffer += "}} catch (RemoteException e) {\n" +
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
		
		File newfile = new File(classname2+"_Stub.java");
		if(!newfile.exists()) {
			try {
				newfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		try {
			
			FileOutputStream out = new FileOutputStream(newfile);
			out.write(buffer.getBytes(), 0, buffer.getBytes().length);
//			ObjectOutputStream o = new ObjectOutputStream(out);
//			o.writeObject(buffer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Runtime.getRuntime().exec("javac "+classname2+"_Stub.java");
			Thread.sleep(1000);
			Runtime.getRuntime().exec("rm "+classname2+"_Stub.java");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
