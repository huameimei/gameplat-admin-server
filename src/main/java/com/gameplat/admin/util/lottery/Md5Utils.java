package com.gameplat.admin.util.lottery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: summer
 * @description: md加密
 * @create: 2020/7/27 13:20
 */
public class Md5Utils {
  private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

  public static String MD5(String srcData) throws NoSuchAlgorithmException {
    char[] hexDigits = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    byte[] btInput = srcData.getBytes();
    MessageDigest mdInst = MessageDigest.getInstance("MD5");
    mdInst.update(btInput);
    byte[] md = mdInst.digest();
    int j = md.length;
    char[] str = new char[j * 2];
    int k = 0;
    for (int i = 0; i < j; i++) {
      byte byte0 = md[i];
      str[k++] = hexDigits[byte0 >>> 4 & 0xF];
      str[k++] = hexDigits[byte0 & 0xF];
    }
    return (new String(str)).toUpperCase();
  }

  public static String MD5ToLow32(String sourceStr) {
    try {
      StringBuffer buf = getMD5StringBuffer(sourceStr);
      return buf.toString().toLowerCase();
    } catch (Exception e) {
      log.info("异常原因{}", e);
      return null;
    }
  }

  public static String MD5ToUpp32(String sourceStr) {
    try {
      StringBuffer buf = getMD5StringBuffer(sourceStr);
      return buf.toString().toUpperCase();
    } catch (Exception e) {
      log.info("异常原因{}", e);
      return null;
    }
  }

  private static StringBuffer getMD5StringBuffer(String sourceStr) throws NoSuchAlgorithmException {
    MessageDigest mdInst = MessageDigest.getInstance("MD5");
    mdInst.update(sourceStr.getBytes());
    byte[] md = mdInst.digest();
    StringBuffer buf = new StringBuffer();
    for (int b : md) {
      int tmp = b;
      if (tmp < 0) {
        tmp += 256;
      }
      if (tmp < 16) {
        buf.append("0");
      }
      buf.append(Integer.toHexString(tmp));
    }
    return buf;
  }
}
