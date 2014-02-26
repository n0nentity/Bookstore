package Mediator;

import Globals.MyClassLoader;
import MethodLinkRepository.MethodLink;
import MethodLinkRepository.MethodLinkRepository;
import SoftwareRepository.SoftwareRepository;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by HeierMi on 26.02.14.
 */
public class Mediator {
    private MethodLinkRepository methodLinkRepository = new MethodLinkRepository();
    private SoftwareRepository softwareRepository = new SoftwareRepository();
    private Parser parser = new Parser();

    public String request(String request) {
        String response = null;

        HashMap<String,MethodLink> searchResults = methodLinkRepository.getMethodLinkSearch().search(request);

        if (searchResults != null && searchResults.size() == 1) {
            // parse and interpret

            Collection<MethodLink> methodLinks = searchResults.values();
            for (MethodLink methodLink : methodLinks) {
                if (parser.parse(request)) {
                    String[] functionParams = parser.getFunctionParams(request);

                    Method method = softwareRepository.getMethods().get(methodLink.getLinkMethodName());
                    if (method != null) {
                        MyClassLoader.methodFromJar("", method.getClass().toString(), method.getName());
                    }
                }
            }
        }
        else {
            response = request + " not found";
        }
        return response;
    }
}
