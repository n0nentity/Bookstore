import Globals.Settings;
import Globals.SettingsHandler;

/**
 * Created by HeierMi on 24.02.14.
 */
public class Application {

    public static void main(String[] args){
        String s = SettingsHandler.getInstance().getSettings().getDatabaseHost();
    }
}
