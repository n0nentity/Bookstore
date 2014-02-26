package SoftwareRepository;

import Globals.MyClassLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HeierMi on 24.02.14.
 */
public class SoftwareRepository implements IDirectoryObserver {
    private IDirectoryPublisher IPublisher;

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    private void loadMethods() {
        // WHICH FILES
    }

    private void loadMethods(String path) {
        ArrayList<Method> result = MyClassLoader.methodsFromJar(path, path);
        for (Method method : result) {
            if (methods.get(method.getName()) != null) {
                // update hashmap
                methods.remove(method.getName());
                methods.put(method.getName(), method);
            }
            else {
                // insert into hashmap
                methods.put(method.getName(), method);
            }
        }
    }

    private HashMap<String, Method> methods = new HashMap<String, Method>();


    public SoftwareRepository() {
        loadMethods();

        DirectoryWatcher dw=new DirectoryWatcher("\\lib");
        this.IPublisher = dw;
        this.IPublisher.registerObserver(this);
        dw.start();
    }

    @Override
    public void update(String action, String filename) {
        loadMethods(filename);
    }
}
