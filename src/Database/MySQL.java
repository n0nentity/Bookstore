package Database;

import Settings.SettingsHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by HeierMi on 24.02.14.
 */
public class MySQL {
    private Connection databaseConnection = null;

    private static MySQL instance = null;

    private Connection createConnection() {
        try {
            /*
            String databaseConnectionURL = "jdbc:mysql://" + SettingsHandler.getInstance().getSettings().getDatabaseHost()
                    + ":" + SettingsHandler.getInstance().getSettings().getDatabasePort()
                    + ";databaseName=" + SettingsHandler.getInstance().getSettings().getDatabaseName()
                    + ";user=" + SettingsHandler.getInstance().getSettings().getDatabaseUser()
                    + ";password=" + SettingsHandler.getInstance().getSettings().getDatabasePassword();
                    */
            String databaseConnectionURL = "jdbc:mysql://" + SettingsHandler.getInstance().getSettings().getDatabaseHost()
                    + ":" + SettingsHandler.getInstance().getSettings().getDatabasePort()
                    + "/" + SettingsHandler.getInstance().getSettings().getDatabaseName()
                    + "?user=" + SettingsHandler.getInstance().getSettings().getDatabaseUser()
                    + "&password=" + SettingsHandler.getInstance().getSettings().DatabasePassword()
                    + "&useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
            this.databaseConnection = DriverManager.getConnection(databaseConnectionURL);
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
        }
        return databaseConnection;
    }

    public static MySQL getInstance() {
        if (instance == null) {
            synchronized(MySQL.class) {
                instance = new MySQL();
                instance.createConnection();
            }
        }
        return instance;
    }

    public Connection getDatabaseConnection() {

        return this.databaseConnection;
    }
}
