package ebiNeutrinoSDK.utils;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * This class allow to encrypt and decrypt a password
 *
 */

public class Encrypter {
    Cipher ecipher;
    Cipher dcipher;

    // 8-byte Salt
    byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    int iterationCount = 19;

    public Encrypter(final String passPhrase) {
        try {

            final KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (final java.security.InvalidAlgorithmParameterException e) {
        } catch (final java.security.spec.InvalidKeySpecException e) {
        } catch (final javax.crypto.NoSuchPaddingException e) {
        } catch (final java.security.NoSuchAlgorithmException e) {
        } catch (final java.security.InvalidKeyException e) {
        }
    }

    public String encrypt(final String str) {
        try {
            final byte[] utf8 = str.getBytes("UTF8");
            final byte[] enc = ecipher.doFinal(utf8);
            
            return  Base64.getEncoder().encodeToString(enc);
            
        } catch (final javax.crypto.BadPaddingException e) {
        } catch (final IllegalBlockSizeException e) {
        } catch (final java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(final String str) {
        try {
        	
            final byte[] dec = Base64.getDecoder().decode(str);
            final byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (final javax.crypto.BadPaddingException e) {
        } catch (final IllegalBlockSizeException e) {
        } catch (final java.io.IOException e) {
        }
        return null;
    }
}