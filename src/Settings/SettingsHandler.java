package Settings;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by HeierMi on 24.02.14.
 * SettingsHandler with SingleTon to get global settings
 */
public class SettingsHandler {
    private static SettingsHandler instance;
    private static String filePath = "Settings.xml";
    private static Settings settings;

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

    public Settings getSettings() {
        return settings;
    }

    public static String getFilePath() {
        return filePath;
    }

    /**
     * write settings in file
     */
    public static void write() {
        try {
            write(settings, filePath);
        }
        catch (Exception e) {
        }
    }

    /**
     * write specific Settings object f in filename param
     * @param f
     * @param filename
     * @throws Exception
     */
    public static void write(Settings f, String filename) throws Exception{
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(filename)));
        encoder.writeObject(f);
        encoder.close();
    }

    /**
     * read settings from filename param
     * @param filename
     * @return
     * @throws Exception
     */
    public static Settings read(String filename) throws Exception {
        XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(filename)));
        Settings o = (Settings)decoder.readObject();
        decoder.close();
        return o;
    }
}
