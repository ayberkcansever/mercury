package com.github.ayberkcansever.mercury.util;

public class StringUtil {

    public static String getServerUrl(String ip, String port) {
        return ip.concat(":").concat(port);
    }

    public static String getServerUrl(String ip, int port) {
        return ip.concat(":").concat(String.valueOf(port));
    }

}
