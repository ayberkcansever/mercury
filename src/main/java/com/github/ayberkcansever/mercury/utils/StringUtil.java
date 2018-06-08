package com.github.ayberkcansever.mercury.utils;

import java.util.Random;

public class StringUtil {

    public static String getServerUrl(String ip, String port) {
        return ip.concat(":").concat(port);
    }

    public static String getServerUrl(String ip, int port) {
        return ip.concat(":").concat(String.valueOf(port));
    }

    public static String generateId(){
        return new Random()
                .ints('a', 'z')
                .limit(10)
                .collect(
                        StringBuilder::new,
                        (builder, codePoint) -> builder.appendCodePoint(codePoint),
                        StringBuilder::append)
                .toString();
    }

}
