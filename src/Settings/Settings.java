package Settings;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.xml.bind.annotation.XmlTransient;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.*;

/**
 * Created by HeierMi on 24.02.14.
 * Configuration with port, databaseType, databaseUser and AES databasePassword
 */
public class Settings implements Serializable {
    private String databaseHost = "localhost";
    private String databaseName = "bookstore";
    private int databasePort;
    private String databaseType;
    private String databaseUser;
    private String databasePasswordEncrypted;
    @XmlTransient
    private AES aes = new AES();
    @XmlTransient
    private String internalPassword = "DHBWSE2istGenial";

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

    @XmlTransient
    public String DatabasePassword() {
        String result = null;
        try {
            // Decoding String with AES
            byte[] decode = new BASE64Decoder().decodeBuffer(databasePasswordEncrypted);
            InputStream is = new ByteArrayInputStream( decode );
            result = new String( aes.decode(is, internalPassword) );
        }
        catch (Exception e) {

        }
        return result;
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

    @XmlTransient
    public void DatabasePassword(String databasePassword) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            aes.exEncode(databasePassword, out, internalPassword);
            databasePasswordEncrypted = new BASE64Encoder().encode( out.toByteArray() );
        }
        catch (Exception e) {

        }
    }
}
