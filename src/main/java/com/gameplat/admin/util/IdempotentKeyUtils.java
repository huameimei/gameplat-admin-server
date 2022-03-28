package com.gameplat.admin.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdempotentKeyUtils {

  /**
   * 对接口的参数进行处理生成固定key
   *
   * @param method Method
   * @param argsIndex int[]
   * @param args Object
   * @return String
   */
  public static String generate(Method method, int[] argsIndex, Object... args) {
    String stringBuilder = getKeyOriginalString(method, argsIndex, args);
    // 进行md5等长加密
    return md5(stringBuilder);
  }

  /**
   * 通过注解的spelKey设定key的生成方法。
   *
   * @param method Method
   * @param spelKey String
   * @return String
   */
  public static String generate(Method method, String spelKey) {
    // 进行md5等长加密
    return md5(method.toString() + spelKey);
  }

  /**
   * 原生的key字符串。
   *
   * @param method Method
   * @param argsIndex int[]
   * @param args Object[]
   * @return String
   */
  public static String getKeyOriginalString(Method method, int[] argsIndex, Object[] args) {
    StringBuilder stringBuilder = new StringBuilder(method.toString());
    int i = 0;
    for (Object arg : args) {
      if (isIncludeArgIndex(argsIndex, i)) {
        stringBuilder.append(toString(arg));
      }
      i++;
    }
    return stringBuilder.toString();
  }

  /**
   * 判断当前参数是否包含在注解中的自定义序列当中。
   *
   * @param argsIndex int[]
   * @param i int i
   * @return boolean
   */
  private static boolean isIncludeArgIndex(int[] argsIndex, int i) {
    // 如果没自定义作为key的参数index序号，直接返回true，意味加入到生成key的序列
    if (argsIndex.length == 0) {
      return true;
    }

    boolean includeIndex = false;
    for (int index : argsIndex) {
      if (index == i) {
        includeIndex = true;
        break;
      }
    }
    return includeIndex;
  }

  /**
   * 使用jsonObject对数据进行toString,(保持数据一致性)
   *
   * @param obj Object
   * @return String
   */
  public static String toString(Object obj) {
    if (obj == null) {
      return "-";
    }
    return JSON.toJSONString(obj);
  }

  /**
   * 对数据进行MD5等长加密
   *
   * @param str String
   * @return String
   */
  public static String md5(String str) {
    StringBuilder stringBuilder = new StringBuilder();
    try {
      // 选择MD5作为加密方式
      MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(str.getBytes());
      byte[] b = mDigest.digest();
      int j = 0;
      for (int i = 0, max = b.length; i < max; i++) {
        j = b[i];
        if (j < 0) {
          i += 256;
        } else if (j < 16) {
          stringBuilder.append(0);
        }
        stringBuilder.append(Integer.toHexString(j));
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}
