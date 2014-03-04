package Settings;

/**
 * Created by HeierMi on 04.03.14.
 */
public class PasswordGenerator {
    public static void main(String[] args){
        String newPassword = "hyuq16";
        SettingsHandler.getInstance().getSettings().DatabasePassword(newPassword);
        SettingsHandler.write();
    }
}
