package SoftwareRepository;

import Globals.MyClassLoader;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 */
public class SoftwareRepository implements IDirectoryObserver {
    public SoftwareRepository() {
        loadMethods();

        DirectoryWatcher dw=new DirectoryWatcher(libPath);
        this.IPublisher = dw;
        this.IPublisher.registerObserver(this);
        dw.start();
    }

    private IDirectoryPublisher IPublisher;
    private HashMap<String, JarMethodLink> methods = new HashMap<String, JarMethodLink>();
    private String libPath = "lib\\";

    public HashMap<String, JarMethodLink> getMethods() {
        return methods;
    }

    private void loadMethods() {
        File folder = new File(libPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".jar") && !listOfFiles[i].getName().startsWith("guava") && !listOfFiles[i].getName().startsWith("mysql")) {
                    loadMethods(listOfFiles[i].getAbsolutePath());
                }
            }
        }
    }

    private void loadMethods(String path) {
        ArrayList<Class> classes = MyClassLoader.loadClassesFromJar(path);
        if (classes != null) {
            for (Class c : classes) {
                loadMethods(path, c.getName());
            }
        }
    }

    private void loadMethods(String path, String className) {
        ArrayList<Method> result = MyClassLoader.methodsFromJar(path, className);
        for (Method method : result) {
            if (methods.get(method.getName()) != null) {
                // update hashmap
                methods.remove(method.getName());
                methods.put(method.getName(), new JarMethodLink(path, className, method));
            }
            else {
                // insert into hashmap
                methods.put(method.getName(), new JarMethodLink(path, className, method));
            }
        }
    }

    @Override
    public void update(String action, String filename) {
        loadMethods(filename);
    }
}
