package com.gameplat.admin.util.lottery;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/** 彩票服务器返回数据格式转换 */
@Slf4j
public class FormatConvert {

  /** 成功 code 码 */
  private static final int SUCCEED = 200;

  private FormatConvert() {}

  /**
   * 分页数据格式转换
   *
   * @param convert 彩票服务返回的分页数据
   * @return 我们后台要的格式
   */
  public static JSONObject pageConvert(JSONObject convert) {
    JSONObject result = new JSONObject();
    Integer code = convert.getInteger("code");
    result.put("code", code);
    if (code == SUCCEED) {
      result.put("total", convert.getJSONObject("data").getInteger("totalCount"));
      result.put("rows", convert.getJSONObject("data").getJSONArray("list"));
    } else {
      log.error("彩票服务业务错误: {}", convert.getString("msg"));
      result.put("message", convert.getString("msg"));
      result.put("total", 0);
      result.put("rows", new JSONArray());
    }
    return result;
  }

  /**
   * 非分页数据格式转换
   *
   * @param convert 彩票服务返回的分页数据
   * @return 我们后台要的格式
   */
  public static JSONObject objConvert(JSONObject convert) {
    JSONObject result = new JSONObject();
    Integer code = convert.getInteger("code");
    result.put("code", code);
    if (code == SUCCEED) {
      result.put("rows", convert.get("data"));
    } else {
      log.error("彩票服务业务错误: {}", convert.getString("msg"));
      result.put("message", convert.getString("msg"));
      result.put("rows", null);
    }
    return result;
  }
}
