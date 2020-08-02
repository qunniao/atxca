package com.atxca.Util.Token;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author 王志鹏
 * @title: TokenProccessor
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/26 17:38
 */
public class TokenProccessor {

    private static final TokenProccessor instance = new TokenProccessor();

    private TokenProccessor() {
    }

    public static TokenProccessor getInstance() {
        return instance;
    }

    /**
     * 生成Token
     *
     * @return
     */
    public String makeToken(){
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte md5[] = md.digest(token.getBytes());
            //BASE64Encoder encoder = new BASE64Encoder();
            //return encoder.encode(md5);
            return Base64.encodeBase64String(md5);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(TokenProccessor.getInstance().makeToken());
    }

}
