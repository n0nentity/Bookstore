package Globals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @class MyClassLoader.java
 * @author Christian Eichmann, Stefan Schultes
 * @version 0.4
 * Class to unpack contents of Java-archive to usable Classes and methods
 */

public class MyClassLoader extends java.lang.ClassLoader {
    public MyClassLoader(java.lang.ClassLoader parent) {
        super(parent);
    }

    /**
     * Method to read content of JAR-Archive, returns an HashMap of ByteArrayOutputStream. Example: ( "file.class" --> ByteArrayOutputStream )
     * @param jarPath relative or absolute path to JAR-archive
     * @return HashMap<String, ByteArrayOutputStream>
     * @throws java.io.IOException
     */
    private static HashMap<String, ByteArrayOutputStream> unzipJar(String jarPath) throws IOException {
        File file = new File(jarPath); //loads file
        @SuppressWarnings("resource")
        JarFile jar = new JarFile(file); //converts into JAR-file
        HashMap<String, ByteArrayOutputStream> hm=new HashMap<String, ByteArrayOutputStream>();//output-HashMap

        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {//iterate through all jar-entries
            JarEntry entry = enums.nextElement();
            if(entry.toString().endsWith(".class")){ 					//read only .class-files
                InputStream is = jar.getInputStream(entry);				//load in InputStream
                ByteArrayOutputStream bo=new ByteArrayOutputStream();
                while (is.available() > 0) {
                    bo.write(is.read());								//write data in ByteArrayOutputStream
                }
                hm.put(entry.toString(),bo);							//put whole Output-Stream in Hashmap with xxx.class as key-value
            }
        }
        return hm;
    }

    /**
     * Returns a Class from the .jar archive
     * @param className Name of the Class (without .class)
     * @param hmBuffer HashMap of ClassNames,ByteArrayOutputStream
     * @return the Class
     * @throws ClassNotFoundException
     */
    private Class loadClass(String className, HashMap<String, ByteArrayOutputStream> hmBuffer) throws ClassNotFoundException {
        try {
            byte[] classData = null;
            //searching for the class
            for(Map.Entry<String,ByteArrayOutputStream> e : hmBuffer.entrySet()){

                if(e.getKey().contains(className.substring(0, className.indexOf("."))+".class")) {
                    String name = e.getKey().substring(e.getKey().lastIndexOf("/")+1, e.getKey().indexOf("."));
                    className = name + "." + name;

                    classData = e.getValue().toByteArray();
                }
            }
            return defineClass(className,classData,0,classData.length);
        } catch (ClassFormatError e) { e.printStackTrace();}
          catch (NullPointerException e) { e.printStackTrace();}

        throw new ClassNotFoundException();
    }

    private ArrayList<Class> loadClasses(HashMap<String, ByteArrayOutputStream> hmBuffer) throws ClassNotFoundException{
        ArrayList<Class> result = new ArrayList<Class>();
        if (hmBuffer != null) {
            try {
                byte[] classData = null;
                //searching for the class
                for(Map.Entry<String,ByteArrayOutputStream> e : hmBuffer.entrySet()){
                    if(e.getKey().endsWith(".class")) {
                        classData = e.getValue().toByteArray();
                        try {
                            String name = e.getKey().substring(e.getKey().lastIndexOf("/")+1, e.getKey().indexOf("."));
                            name = name + "." + name;
                            /*
                            String name = e.getKey();
                            */
                            /*
                            String name = e.getKey().substring(0, e.getKey().indexOf("."));
                            */
                            Class c = defineClass(name, classData, 0, classData.length);
                            result.add(c);
                        }
                        catch (Exception ex) {

                        }
                    }
                }
            }
            catch (ClassFormatError e) { e.printStackTrace();}
            catch (NullPointerException e) { e.printStackTrace();}
        }
        return result;
    }
      
    public static ArrayList<Class> loadClassesFromJar(String path) {
        ArrayList<Class> result = null;

        HashMap<String,ByteArrayOutputStream> hm = null;
        try{
            //HashMap of classes from .jar
            hm = MyClassLoader.unzipJar(path);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        try {
            //Getting the Class
            java.lang.ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
            MyClassLoader myClassLoader = new MyClassLoader(parentClassLoader);

            result = myClassLoader.loadClasses(hm);
        }
        catch (ClassNotFoundException e)   { e.printStackTrace();}

        return result;
    }

    /**
     * Calls a method from a .jar archive
     * @param path to the .jar (e.g. "C:\\TEST\\test.jar")
     * @param className name of the class (e.g. "Test")
     * @param methodName name of the method (e.g. "magic")
     * @return Object (return value of the called method); null if error occurred
     */
    public static Object methodFromJar(String path, String className, String methodName){
        Method method;
        HashMap<String,ByteArrayOutputStream> hm = null;
        try{
            //HashMap of classes from .jar
            hm = MyClassLoader.unzipJar(path);
        }
        catch (IOException e){ e.printStackTrace(); }

        try {
            //Getting the Class
            java.lang.ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
            MyClassLoader myClassLoader = new MyClassLoader(parentClassLoader);
            Class serviceClass = myClassLoader.loadClass(className,hm);
            //Getting the method
            Object serviceObject = serviceClass.newInstance();
            method = serviceClass.getDeclaredMethod(methodName);
            //Calling the method
            return method.invoke(serviceObject);
        }
        catch (ClassNotFoundException e)   { e.printStackTrace();}
        catch (NoSuchMethodException e)    { e.printStackTrace();}
        catch (IllegalAccessException e)   { e.printStackTrace();}
        catch (InvocationTargetException e){ e.printStackTrace();}
        catch (InstantiationException e)   { e.printStackTrace();}
        return null;
    }

    public static ArrayList<Method> methodsFromJar(String path, String className) {
        ArrayList<Method> result = new ArrayList<Method>();

        HashMap<String,ByteArrayOutputStream> hm = null;
        try{
            //HashMap of classes from .jar
            hm = MyClassLoader.unzipJar(path);
        }
        catch (IOException e){ e.printStackTrace(); }

        try {
            //Getting the Class
            java.lang.ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
            MyClassLoader myClassLoader = new MyClassLoader(parentClassLoader);

            //Class serviceClass = myClassLoader.loadClass(className,hm);
            Class serviceClass = myClassLoader.loadClass(className, hm);

            //Getting the method
            Object serviceObject = serviceClass.newInstance();
            //Parameter types
            for (Method method : serviceClass.getMethods()) {
                result.add(method);
            }
        }
        catch (ClassNotFoundException e)   { e.printStackTrace();}
        catch (IllegalAccessException e)   { e.printStackTrace();}
        catch (InstantiationException e)   { e.printStackTrace();}

        return result;
    }
    /**
     * Calls a method with one parameter from a .jar archive
     * @param path to the .jar (e.g. "C:\\TEST\\test.jar")
     * @param className name of the class
     * @param methodName name off the method
     * @param paraTypes Array of parameter types (e.g. String.class)
     * @param paraValues Array of parameter values (e.g. "Hi")
     * @return Object (return value of the called method); null if error occurred
     */
    public static Object methodFromJar(String path, String className, String methodName,Class[] paraTypes, Object[] paraValues){
        Method method;
        HashMap<String,ByteArrayOutputStream> hm = null;
        try{
            //HashMap of classes from .jar
            hm = MyClassLoader.unzipJar(path);
        }
        catch (IOException e){ e.printStackTrace(); }

        try {
            //Getting the Class
            java.lang.ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
            MyClassLoader myClassLoader = new MyClassLoader(parentClassLoader);
            Class serviceClass = myClassLoader.loadClass(className,hm);
            //Getting the method
            Object serviceObject = serviceClass.newInstance();
            //Parameter types
            method = serviceClass.getDeclaredMethod(methodName,paraTypes);
            //Calling the method with parameter values
            return method.invoke(serviceObject,paraValues);
        }
        catch (ClassNotFoundException e)   { e.printStackTrace();}
        catch (NoSuchMethodException e)    { e.printStackTrace();}
        catch (IllegalAccessException e)   { e.printStackTrace();}
        catch (InvocationTargetException e){ e.printStackTrace();}
        catch (InstantiationException e)   { e.printStackTrace();}

        return null;
    }

    /**
     * Returns a Class from the .jar archive
     * @param path of the archive (e.g. "C:\\TEST\\test.jar")
     * @param className name of the class (e.g. "Test")
     * @return
     */
    public static Class classFromJar(String path, String className) throws ClassNotFoundException{
        Class serviceClass;
        HashMap<String,ByteArrayOutputStream> hm = null;
        try{
            hm = MyClassLoader.unzipJar(path);
        }
        catch (IOException e){e.printStackTrace();
        }
        //Getting the class
        java.lang.ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
        MyClassLoader myClassLoader = new MyClassLoader(parentClassLoader);
        serviceClass = myClassLoader.loadClass(className,hm);     //can throw ClassNotFoundException

        return serviceClass;
    }


}