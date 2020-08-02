package com.atxca.Util;



import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

public class Base64Util {
    public static String Base64UtilEncoder(String str) {
        //BASE64Encoder encoder = new BASE64Encoder();
        //String encode = encoder.encode(str.getBytes());
        return Base64.encodeBase64String(str.getBytes());
    }

    public static String Base64UtilDecoder(String str) {
        //BASE64Decoder decoder = new BASE64Decoder();
        String decode = null;
            decode = new String(Base64.decodeBase64(str));

        return decode;
    }

    public static void main(String[] args) {
        System.out.println(Base64UtilEncoder("qwer%1.9safsdf"));
        System.out.println(Base64UtilDecoder("MTIzNDU2"));
    }
}
