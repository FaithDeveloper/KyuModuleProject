package com.iscreammedia.kyuutilslibrary;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * AES을 이용한 암호화
 * 참조. https://mdj1234.tistory.com/41
 * */
public class CryptoHashUtils {

    public static String TAG = "CryptoHashUtils";

    /**
     * SHA256 암호화
     * @param text
     * @return
     */
    public static String encryptSHA256(String text) {
        MessageDigest md = null;

        if (md == null) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                Log.d(TAG, "NoSuchAlgorithmException: " + e.getMessage());
            }
        }

        if (null != md) {
            // Change this to UTF-16 if needed
            md.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            String hex = String.format("%064x", new BigInteger(1, digest));
            Log.d(TAG, "encryptSHA256: " + hex.toUpperCase());
            return hex.toUpperCase();
        } else {
            Log.d(TAG, "md == null");
        }
        return text;
    }

    /**
     * AES 128 암호화
     * @param text
     * @param key
     * @return
     */
   public static String encryptAES128(String text, String key) throws Exception {
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       byte[] keyBytes = new byte[16];
       byte[] b = key.getBytes("UTF-8");
       int len= b.length;
       if (len > keyBytes.length) len = keyBytes.length;
       System.arraycopy(b, 0 , keyBytes, 0, len);
       SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
       IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
       cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

       byte[] results = cipher.doFinal(text.getBytes("UTF-8"));

       return Base64.encodeToString(results, Base64.DEFAULT);
   }

    /**
     * AES 128 복호화
     * @param text
     * @param key
     * @return
     */
   public static String decryptAES128(String text, String key) throws Exception {
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       byte[] keyBytes= new byte[16];
       byte[] b= key.getBytes("UTF-8");
       int len= b.length;
       if (len > keyBytes.length) len = keyBytes.length;
       System.arraycopy(b, 0, keyBytes, 0, len);
       SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
       IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
       cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

       byte [] results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
       return new String(results, "UTF-8");
   }


    /**
     * MD5 인코딩
     * @param str
     * @return
     */
    public static String encryptMD5(String str){
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = null;
        }

        return MD5;
    }

    /**
     * Encode Base64 String 리턴
     * @param txt
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String encryptBase64(String txt) throws UnsupportedEncodingException {
        byte[] data = txt.getBytes("UTF-8");
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * Decode Base64 String 리턴
     * @param txt
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String decryptBase64(String txt) throws UnsupportedEncodingException {
        return new String(Base64.decode(txt, Base64.DEFAULT), "UTF-8");
    }
}
