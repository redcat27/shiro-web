package com.baizhi.utils;

import java.util.Random;

public class Securitymd5Salt {
    /**
     * 生成随机盐的方法
     */
    public static String getSalt(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        System.out.println("随机盐为：---   " + salt);
        return salt;
    }

}
