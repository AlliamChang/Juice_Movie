package cn.cseiii.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by I Like Milk on 2017/5/26.
 */
public abstract class Encode {
    private static int KEY = 666;
    private static String SALT = "JuIce";
    private static char[] TEBEL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static int TOKEN_LEN = 32;

    public static String getMD5(String s) {
        if (s == null)
            return null;
        String md5 = null;
        BASE64Encoder base64Encoder = new BASE64Encoder();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md5 = base64Encoder.encode(md.digest(s.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String saltMD5(String s) {
        if (s == null)
            return null;
        return getMD5(s + SALT);
    }

    public static String xorEncode(String s) {
        if (s == null)
            return null;
        char[] temp = s.toCharArray();
        for (int i = 0; i < temp.length; i++)
            temp[i] ^= KEY;
        return String.copyValueOf(temp);
    }

    public static String getRandomToken(int len) {
        char[] token = new char[len];
        for (int i = 0; i < len; i++)
            token[i] = TEBEL[(int)(Math.random() * TEBEL.length)];
        return String.copyValueOf(token);
    }

    public static String getRandomNumToken(int len) {
        char[] token = new char[len];
        for (int i = 0; i < len; i++)
            token[i] = TEBEL[(int)(Math.random() * 10)];
        return String.copyValueOf(token);
    }
}
