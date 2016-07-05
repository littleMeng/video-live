package com.example.meng.videolive.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 小萌神_0 on 2016/5/28.
 */
public class Md5 {
    public static String strToMd5Low32(String str) {
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] bytes = md5.digest();
            for (byte b : bytes) {
                int digital = b&0xff;
                if (digital < 16)
                    builder.append(0);
                builder.append(Integer.toHexString(digital));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return builder.toString().toLowerCase();
    }
}
