package com.yiu.arcap.util;


import java.security.SecureRandom;
import java.util.Random;

public class RandomCodeGenerator {
    private final static int CODE_LENGTH = 8;
    public static String createCode() {
        // 원하는 코드 길이 설정
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String alphanum = upper + digits;
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(alphanum.charAt(random.nextInt(alphanum.length())));
        }
        return sb.toString();
    }

}
