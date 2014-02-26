package Settings;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jan-Hendrik and Philipp Weber on 24.02.14.
 */
public class AES {
    private static Cipher cipher;
    private static Key key;

    //Main decoding method, piping the InputStream is through a CipherStream to decode it.
    private static void pushDecodingInputStreamThroughCipher(InputStream is, ByteArrayOutputStream bos) throws IOException {
        CipherInputStream cis = new CipherInputStream( is, cipher );
        int b = cis.read();
        while(b != -1)
        {
            bos.write( b );
            b= cis.read();
        }
        cis.close();
    }

    //Main encoding method, piping the bytes through the CipherStream to encode them.
    private static void pushEncodeStreamThroughCipher(byte[] bytes, OutputStream out) throws IOException {
        OutputStream cos = new CipherOutputStream( out, cipher );
        cos.write( bytes );
        cos.close();
    }

    //Initiates Cipher and Key for use with AES and the password
    private static void initCipherKeyPair(String pass) throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance("AES");
        key = new SecretKeySpec( pass.getBytes(), "AES" );
    }

    /* Method to decode given InputStream with AES and the given Password pass.
     * Password needs to be 16 Bytes else throws Error to remind you!
     */
    public static byte[] decode( InputStream is, String pass ) throws Exception{
        initCipherKeyPair(pass);
        cipher.init( Cipher.DECRYPT_MODE, key );
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pushDecodingInputStreamThroughCipher(is, bos);
        return bos.toByteArray();
    }

    //encode to ByteArray. Needs Conversion to String to get
    private static void encode(byte[] bytes, OutputStream out, String pass) throws Exception{
        initCipherKeyPair(pass);
        cipher.init( Cipher.ENCRYPT_MODE, key );
        pushEncodeStreamThroughCipher(bytes, out);
    }

    /* Wrapper Method to give Input via String
     * Maximum size of inputString is 10 letters
     * Password needs to be 16 Bytes else throws Error to remind you!
     */
    public static void exEncode(String inputString, OutputStream out, String pass) throws Exception {
        if(inputString.length()>10) throw new IllegalArgumentException();
        else
            encode(inputString.getBytes(),out,pass);
    }
}
