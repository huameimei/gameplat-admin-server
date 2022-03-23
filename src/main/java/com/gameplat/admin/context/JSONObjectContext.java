package com.gameplat.admin.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.NamedThreadLocal;

public class JSONObjectContext implements AutoCloseable {

  /** 存入线程 */
  private static final ThreadLocal<JSONObject> JSON_OBJECT_CONTEXT =
      new NamedThreadLocal<JSONObject>("jsonObjectContext");

  /**
   * 获取某个参数
   *
   * @return 获取需要的参数
   */
  public static String getJSONObjectContext(String key) {
    JSONObject jsonObject = JSON_OBJECT_CONTEXT.get();
    return jsonObject != null ? jsonObject.getString(key) : null;
  }
  /** 获取整个JSON */
  public static JSONObject getJSONObjectContext() {
    return JSON_OBJECT_CONTEXT.get();
  }

  /**
   * 将json字符串转换存入本地线程 或 将JSONObject存入本地线程
   *
   * @param
   */
  public static void setJSONObjectContext(String str) {
    JSON_OBJECT_CONTEXT.set(JSON.parseObject(str));
  }

  public static void setJSONObjectContext(JSONObject jsonObject) {
    JSON_OBJECT_CONTEXT.set(jsonObject);
  }

  /**
   * 强制清空本地线程
   *
   * <p>防止内存泄漏，如手动调用了push可调用此方法确保清除
   */
  public static void clear() {
    JSON_OBJECT_CONTEXT.remove();
  }

  @Override
  public void close() throws Exception {
    clear();
  }
}
