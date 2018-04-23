package cn.cseiii.util.impl;

import cn.cseiii.util.MessageShield;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 53068 on 2017/5/9 0009.
 */
public class MessageShieldByMd5 implements MessageShield {
    @Override
    public String encoder(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md5.digest(message.getBytes("utf-8")));
    }
}
