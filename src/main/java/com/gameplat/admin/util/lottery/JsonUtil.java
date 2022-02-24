package com.gameplat.admin.util.lottery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: summer
 * @description: json工具类
 * @create: 2020/7/27 13:19
 **/
@Slf4j
public class JsonUtil {

    private static final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getInstance() {
        return gson;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String list2Json(List<T> list) {
        if (null != list && list.size() > 0) {
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray.toString();
        }
        return "";
    }

    public static <T> List<T> json2List(String jsonStr, Class<T> objectClass) {
        if (StringUtils.isNotBlank(jsonStr)) {
            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
            List<T> list = (List<T>)JSONArray.toCollection(jsonArray, objectClass);
            return list;
        }
        return null;
    }

    public static String object2Json(Object object) {
        if (null != object) {
            JSONArray jsonArray = JSONArray.fromObject(object);
            return jsonArray.toString();
        }
        return "";
    }

    public static <T> T json2Ojbect(String jsonStr, Class<T> objectClass) {
        if (null != jsonStr) {
            String leftStr = jsonStr.substring(0, 2);
            String rightStr = jsonStr.substring(jsonStr.length() - 2, jsonStr.length());
            if (leftStr.equals("[{"))
                jsonStr = jsonStr.substring(1, jsonStr.length());
            if (rightStr.equals("}]"))
                jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            JSONObject jsonStu = JSONObject.fromObject(jsonStr);
            return (T)JSONObject.toBean(jsonStu, objectClass);
        }
        return null;
    }

    public static String jsonArrayToJSONString(JSONArray jsonArray) {
        if (null != jsonArray)
            return jsonArray.toString();
        return "";
    }

    public static String jsonObjectToJSONString(JSONObject jsonObject) {
        if (null != jsonObject)
            return jsonObject.toString();
        return "";
    }

    public static JSONObject object2JsonObject(Object object) {
        if (null != object)
            return JSONObject.fromObject(object);
        return null;
    }

    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 对象装换map
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class") && !key.equals("pageSize") && !key.equals("pageNum")&& !key.equals("country")&& !key.equals("locale")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (!ObjectUtils.isEmpty(value)){
                        if (key.equals("ids")){
                            String str = value.toString();
                            if (str.startsWith("[")) {
                                str =  (str.substring("[".length()));
                            }
                            if (str.endsWith("]")) {
                                str =  (str.substring(0,str.length()-"]".length()));
                            }
                            map.put(key, str);
                        }else {
                            map.put(key, value);
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.info("transBean2Map Error " + e);
        }
        return map;
    }
}
