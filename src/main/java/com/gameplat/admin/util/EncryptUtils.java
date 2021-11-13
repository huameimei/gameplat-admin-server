package com.gameplat.admin.util;

import org.apache.commons.lang3.StringUtils;

public class EncryptUtils {

    public static String summaryEncrypt(String ori) {
        if (StringUtils.isBlank(ori)) {
            return ori;
        }
        if (ori.length() == 1) {
            return "******";
        }
        if (ori.length() <= 4) {
            return ori.charAt(0) + "******";
        }
        return ori.charAt(0) + "******" + ori.substring(ori.length() - 3);
    }
}
