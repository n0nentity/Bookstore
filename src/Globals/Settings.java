package Globals;

import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 * Created by HeierMi on 24.02.14.
 * Konfigurationsdatei mit den
 Parametern port, databaseType, databaseUser
 und (AES-verschlüsseltem) databasePassword.
 */
public class Settings {
    private String databaseHost = "localhost";
    private String databaseName = "bookstore";
    private int databasePort;
    private String databaseType;
    private String databaseUser;
    private String databasePassword = null;
    private String databasePasswordEncrypted;

    public String getDatabasePasswordEncrypted() {
        return databasePasswordEncrypted;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        // decrypt
        if (databasePassword == null)
            databasePassword = "";
        return databasePassword;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabasePasswordEncrypted(String databasePasswordEncrypted) {
        this.databasePasswordEncrypted = databasePasswordEncrypted;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
