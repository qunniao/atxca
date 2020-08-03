package com.atxca.Util;

import com.atxca.aop.LogicRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 王志鹏
 * @title: RandomUtil
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/1916:29
 */
public class RandomUtil {
    public static String[] chars = new String[]
            {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
            };


    public static String getShortUuid() {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int strInteger = Integer.parseInt(str, 16);
            stringBuffer.append(chars[strInteger % 0x3E]);
        }

        return stringBuffer.toString();
    }

    public static int getRandom_Six() {
        int flag = new Random().nextInt(999999);
        if (flag < 100000) {
            flag += 100000;
        }
//        System.out.println(flag);
//        int i = (int)(Math.random()*900 + 1000);
//        String myStr = Integer.toString(i);
//        System.out.println(myStr);
        return flag;
    }

    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getShortUuid());
        System.out.println(getRandom_Six());
    }

    /**
     * 校验手机号码格式
     * @param phone
     */
    public  void checkPhoneFormat(String phone) {
        if (StringUtils.isBlank(phone)) {
            throw new LogicRuntimeException("手机号码不能为空");
        }
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[0-9])|(16[6]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            throw new LogicRuntimeException("手机号码格式不正确");
        }
    }
}
