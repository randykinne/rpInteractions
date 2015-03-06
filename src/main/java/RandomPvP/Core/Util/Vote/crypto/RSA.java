package RandomPvP.Core.Util.Vote.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

public class RSA
{
    public static byte[] encrypt(byte[] data, PublicKey key)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, PrivateKey key)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, key);
        return cipher.doFinal(data);
    }
}
