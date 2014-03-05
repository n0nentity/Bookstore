package Settings;

/**
 * Created by HeierMi on 04.03.14.
 * Password generation to save new password
 */
public class PasswordGenerator {

    /**
     * new password routine with file save
     * @param args
     */
    public static void main(String[] args){
        String newPassword = "password";
        SettingsHandler.getInstance().getSettings().DatabasePassword(newPassword);
        SettingsHandler.write();
    }
}
