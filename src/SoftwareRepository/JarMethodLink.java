package SoftwareRepository;

import java.lang.reflect.Method;

/**
 * Created by HeierMi on 26.02.14.
 * Class to storage a method in software repository
 */
public class JarMethodLink {
    private String path;
    private String className;
    private Method method;

    public JarMethodLink() {
    }

    public JarMethodLink(String path, String className, Method method) {
        this.path = path;
        this.className = className;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
