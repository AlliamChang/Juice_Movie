package cn.cseiii.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 53068 on 2017/5/9 0009.
 */
public interface MessageShield {

    String encoder(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException;

}
