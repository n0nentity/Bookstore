package MethodLinkRepository;

import Mediator.FunctionLibrary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by root on 24.02.14.
 * parser class
 */
public class Parser {
    private HashMap<String,Method> funcs;

    public Parser(){
        funcs = new HashMap<String, Method>();
        // Lade alle Methoden aus FunctionLibrary
        for(Method m : FunctionLibrary.class.getMethods()){
            funcs.put(m.getName(),m);
        }
    }

    public String getFunctionName(String cmd) {
        return cmd.substring(0,cmd.indexOf('('));
    }

    public String[] getFunctionParams(String cmd) {
        return cmd.substring(cmd.indexOf('(')+1,cmd.length()-1).split(",");
    }

    public boolean parse(String cmd){
        if(cmd.contains("(")  && cmd.contains(")")){
            String f = cmd.substring(0,cmd.indexOf('('));
            String[] p = cmd.substring(cmd.indexOf('(')+1,cmd.length()-1).split(",");
            Method func = funcs.get(f);
            if(func != null){
                try {
                    Class[] parameterTypes = null;
                    Object[] parameter = null;
                    if (p.length == 1 && p[0].equals("")) {

                    }
                    else {
                        parameterTypes = func.getParameterTypes();
                        parameter = new Object[p.length];
                        for (int i = 0; i < p.length; i++) {
                            if (i < parameterTypes.length) {
                                if (parameterTypes[i] == int.class) {
                                    parameter[i] = new Integer(p[i].trim());
                                }
                                else {
                                    parameter[i] = p[i].trim();
                                }
                            }
                            else {
                                parameter[i] = p[i].trim();
                            }
                        }
                    }
                    func.invoke(null, parameter);
                    return true;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
            //return "Unbekannte Funktion \"" + f + "\"";
        }else{
            //return "Syntaxfehler";
            return false;
        }
    }
}
