package com.chen.library.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    public static int getRandomNumber() {
        return (int) (Math.random() * 1000);
    }

    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static <T> Map<? extends String, ? extends String> bean2Map(T bean) {
        Map<String, String> reflectMap = new HashMap<>();
        try {
            Class cl = Class.forName(bean.getClass().getName());
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(bean) != null && !"".equals(field.get(bean))) {
                    if (field.getType() == String.class) {
                        reflectMap.put(field.getName(), field.get(bean).toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reflectMap;
    }
}
