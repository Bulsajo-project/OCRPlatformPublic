package kr.co.chunjae.ocrplatform.service.management;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    public static String getEncrypt(String pwd) {
        String result = "";
        String salt = "Encrypt";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pwd + salt).getBytes());
            byte[] pwdsalt = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : pwdsalt) {
                sb.append(String.format("%02x", b));
            }

            result = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }
}
