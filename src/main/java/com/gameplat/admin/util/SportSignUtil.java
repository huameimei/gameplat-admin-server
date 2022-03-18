package com.gameplat.admin.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 小金加密算法
 * @author aguai
 * @since 2020-08-14
 */
public class SportSignUtil {

    /**
     * 获取签名
     * @param map
     * @param appKey
     * @return
     */
    public static String getSportsign(Map<String, Object> map, String appKey) {
        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (map.get(k) != null && map.get(k).toString().trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(map.get(k).toString().trim()).append("&");
        }
        sb.append("key=").append(appKey);
        System.out.println(sb.toString());
        String sign = "";
        try {
            sign = HMACSHA256(sb.toString(), appKey);
        } catch (Exception e) {

        }
        return sign;
    }

    /**
     * HMACSHA256加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
