package SoftwareRepository;

import Globals.MyClassLoader;
import sun.tools.jar.resources.jar;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 * Software repository including directory watcher
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

    /**
     * load methods from all jar archives in lib path
     */
    private void loadMethods() {
        File folder = new File(libPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {

                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".jar")) {
                    loadMethods(listOfFiles[i].getAbsolutePath());
                }
            }
        }
    }

    /**
     * load methods of single jar archive
     * @param path
     */
    private void loadMethods(String path) {
        ArrayList<Class> classes = MyClassLoader.loadClassesFromJar(path);
        if (classes != null) {
            for (Class c : classes) {
                loadMethods(path, c.getName());
            }
        }
    }

    /**
     * load methods of a single class in a single jar archive
     * @param path
     * @param className
     */
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
    /**
     * directory watch
     */
    public void update(String action, String filename) {
        ArrayList<JarMethodLink> arrayList = new ArrayList<>(methods.values());
        for (JarMethodLink jarMethodLink : arrayList) {
            if (new File(jarMethodLink.getPath()).getName().equals(filename))
                methods.remove(jarMethodLink.getMethod().getName());
        }
        loadMethods(libPath + filename);
    }
}
