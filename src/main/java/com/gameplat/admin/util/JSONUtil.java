package com.gameplat.admin.util;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * json 转换
 * @author aguai
 * @since 2020-08-14
 */
@Slf4j
public class JSONUtil {
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
                if (!key.equals("class") && !key.equals("pageNo") && !key.equals("country")) {
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
    }/**
     * 对象装换map 不为null
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map2(Object obj) {
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
                if (!key.equals("class") && !key.equals("pageNo") && !key.equals("country")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null){
                        map.put(key, value);
                    }

                }
            }
        } catch (Exception e) {
            log.info("transBean2Map2 Error " + e);
        }
        return map;
    }

    /*public static void main(String[] args) {
        Date date = new Date();
        Long newTime = DateUtil.getSportTimeConvert(date.getTime() / 1000, 8);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(new Date(newTime*1000));
        System.out.println(format1);


    }*/
    public static void main(String[] args) {
        JSONArray list1 = new JSONArray();
        for (int i = 0; i < 10; i++) {
            JSONObject obj = new JSONObject();
            obj.put("id",i);
            obj.put("name","aaabbb");
            obj.put("day",10 - i);
            obj.put("size","bbbccc");
            list1.add(obj);
        }
        System.out.println(list1);
        //list1.sort(Comparator.comparing(obj -> ((JSONObject)obj).getInteger("day")).reversed());
        //System.out.println(list1);
        JSONArray newList = new JSONArray();
        list1.forEach(obj ->{
            JSONObject data = JSONObject.parseObject(Convert.toStr(obj));
            data.put("name",data.getString("name") + "hahahahha");
            newList.add(data);
        });
        System.out.println(newList);
        Collections.reverse(newList);
        System.out.println(newList);

    }
}
