package SoftwareRepository;

import Globals.MyClassLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 */
public class SoftwareRepository implements IDirectoryObserver {
    private IDirectoryPublisher IPublisher;
    private HashMap<String, JarMethodLink> methods = new HashMap<String, JarMethodLink>();
    private String libPath = "";

    public HashMap<String, JarMethodLink> getMethods() {
        return methods;
    }

    private void loadMethods() {
        File folder = new File(libPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                loadMethods(listOfFiles[i].getName());
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
        ArrayList<Method> result = MyClassLoader.methodsFromJar(path, path);
        for (Method method : result) {
            if (methods.get(method.getName()) != null) {
                // update hashmap
                methods.remove(method.getName());
                methods.put(method.getName(), new JarMethodLink(path, path, method));
            }
            else {
                // insert into hashmap
                methods.put(method.getName(), new JarMethodLink(path, path, method));
            }
        }
    }


    public SoftwareRepository() {
        loadMethods();

        DirectoryWatcher dw=new DirectoryWatcher(libPath);
        this.IPublisher = dw;
        this.IPublisher.registerObserver(this);
        dw.start();
    }

    @Override
    public void update(String action, String filename) {
        loadMethods(filename);
    }
}
