package Globals;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by HeierMi on 24.02.14.
 */
public class SettingsHandler {
    private static SettingsHandler instance;

    public static Settings getSettings() {
        return settings;
    }

    private static Settings settings;

    private static String filePath = "Settings.xml";

    private SettingsHandler() {}

    public static synchronized SettingsHandler getInstance() {
        if (instance == null) {
            instance = new SettingsHandler();
            try {
                settings = read(filePath);
            }
            catch (Exception ex1) {
                settings = new Settings();
            }
        }

        return instance;
    }

    public static void write(Settings f, String filename) throws Exception{
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(filename)));
        encoder.writeObject(f);
        encoder.close();
    }

    public static Settings read(String filename) throws Exception {
        XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(filename)));
        Settings o = (Settings)decoder.readObject();
        decoder.close();
        return o;
    }
}
