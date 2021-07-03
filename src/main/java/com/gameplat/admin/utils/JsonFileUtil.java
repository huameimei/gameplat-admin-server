package com.gameplat.admin.utils;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** JSON文件工具类 */
@Slf4j
@Component
public class JsonFileUtil {

  /** 定义文件后缀 */
  private static final String SUFFIX = ".json";

  /**
   * 获取json数据
   *
   * @param fileName
   * @return
   */
  public static JSONObject getJson(String PREFIX, String fileName) {

    String Path = PREFIX + fileName + SUFFIX;
    BufferedReader reader = null;
    String laststr = "";
    try {
      FileInputStream fileInputStream = new FileInputStream(Path);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
      reader = new BufferedReader(inputStreamReader);
      String tempString = null;
      while ((tempString = reader.readLine()) != null) {
        laststr += tempString;
      }
      reader.close();
    } catch (IOException e) {
      log.info("异常原因{}", e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          log.info("异常原因{}", e);
        }
      }
    }
    return JSONObject.parseObject(laststr);
  }

  /**
   * 写入到json文件
   *
   * @param json
   * @param fileName
   */
  public static void setToFile(JSONObject json, String PREFIX, String fileName) {
    log.info("正在写入{}", fileName);
    BufferedWriter writer = null;
    File file = new File(PREFIX + fileName + SUFFIX);
    // 如果文件不存在，则新建一个
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        log.info("异常原因{}", e);
      }
    }
    // 写入
    try {
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
      writer.write(json.toJSONString());
    } catch (IOException e) {
      log.info("异常原因{}", e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        log.info("异常原因{}", e);
      }
    }
    log.info("写入成功！");
  }

  /** 清理操作 */
  public static void clear(String PREFIX, String fileName) {
    log.info("正在清理{}", fileName);
    BufferedWriter writer = null;
    File file = new File(PREFIX + fileName + SUFFIX);
    // 如果文件不存在，则新建一个
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        log.info("异常原因{}", e);
      }
    }
    // 写入
    try {
      JSONObject json = new JSONObject();
      writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
      writer.write(json.toJSONString());
    } catch (IOException e) {
      log.info("异常原因{}", e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        log.info("异常原因{}", e);
      }
    }
    log.info("清理成功！");
  }
}
