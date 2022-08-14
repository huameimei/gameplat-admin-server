package com.gameplat.admin.util;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author aBen
 * @date 2022/8/14 18:34
 * @desc
 */
public final class EasyPoiUtil {

  /**
   * 动态更改EasyPoi中控制列显示的值
   *
   * @param columnName 需要转换的列属性名称
   * @param target     默认true
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  public static void hiddenColumn(String columnName, Boolean target, Class<?> pojoClass) {
    try {
      //获取目标对象的属性值
      Field field = pojoClass.getDeclaredField(columnName);
      //获取注解反射对象
      Excel excelAnnon = field.getAnnotation(Excel.class);
      //获取代理
      InvocationHandler invocationHandler = Proxy.getInvocationHandler(excelAnnon);
      Field excelField = invocationHandler.getClass().getDeclaredField("memberValues");
      excelField.setAccessible(true);
      Map memberValues = (Map) excelField.get(invocationHandler);
      memberValues.put("isColumnHidden", target);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
