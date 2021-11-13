package com.gameplat.admin.constant;

import java.util.concurrent.TimeUnit;

/**
 * 系统常量类
 *
 * @author three
 */
public interface SystemConstant {

  /** 验证码默认超时时间 */
  long CAPTCHA_CODE_TIMEOUT = TimeUnit.MINUTES.toSeconds(3);

  /** 验证码 Cookie 键名 */
  String CAPTCHA_COOKIE_KEY = "captcha_id";

  /** 管理后台登陆限制 */
  String ADMIN_LIMIT_LOGIN = "ADMIN_LIMIT_LOGIN";

  /** 开始时间 */
  String BEGIN_TIME = "beginTime";
  /** 结束时间 */
  String END_TIME = "endTime";

  /** web默认代理账号 */
  String DEFAULT_WEB_ROOT = "webRoot";

  /** wap默认代理账号 */
  String DEFAULT_WAP_ROOT = "wapRoot";

  /** 测试默认代理账号 */
  String DEFAULT_TEST_ROOT = "testRoot";
}
