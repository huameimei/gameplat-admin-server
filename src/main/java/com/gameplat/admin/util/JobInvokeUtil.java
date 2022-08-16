package com.gameplat.admin.util;

import cn.hutool.extra.spring.SpringUtil;
import com.gameplat.admin.model.bean.SysJob;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** 任务执行工具 */
public class JobInvokeUtil {
  /**
   * 执行方法
   *
   * @param sysJob 系统任务
   */
  public static void invokeMethod(SysJob sysJob) throws Exception {
    Object bean = SpringUtil.getBean(sysJob.getJobName());
    String methodName = sysJob.getMethodName();
    String methodParams = sysJob.getMethodParams();

    invokeSpringBean(bean, methodName, methodParams);
  }

  /**
   * 调用任务方法
   *
   * @param bean 目标对象
   * @param methodName 方法名称
   * @param methodParams 方法参数
   */
  private static void invokeSpringBean(Object bean, String methodName, String methodParams)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    if (StringUtils.isNotEmpty(methodParams)) {
      Method method = bean.getClass().getDeclaredMethod(methodName, String.class);
      method.invoke(bean, methodParams);
    } else {
      Method method = bean.getClass().getDeclaredMethod(methodName);
      method.invoke(bean);
    }
  }
}
