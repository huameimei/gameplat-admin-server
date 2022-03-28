package com.gameplat.admin.handler;

import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.SubjectEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.log.handler.LogBuilder;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: @Author: Hoover @Date: 20/11/2021 下午 6:49
 */
@Configuration
public class AdminLogBuilder implements LogBuilder {

  @Value("${syslog.tenant:#{null}}")
  private String dbSuffix;

  @Override
  public SysLog build() throws Exception {
    UserTypes userTypes = SecurityUserHolder.isSuperAdmin() ? UserTypes.ADMIN : UserTypes.SUBUSER;
    String tenant = DyDataSourceContextHolder.getTenant();
    tenant = StringUtils.isEmpty(tenant) ? dbSuffix : tenant;
    UserCredential credential = SecurityUserHolder.getCredential();

    return SysLog.builder()
        .dbSuffix(tenant)
        .userType(userTypes.key())
        .registerTime(
            DateUtils.get0ZoneDate(credential.getRegisterTime(), DateUtils.DATE_TIME_PATTERN))
        .username(credential.getUsername())
        .subject(SubjectEnum.ADMIN.getKey())
        .build();
  }

  /**
   * 获取租户标识
   *
   * @return String
   */
  public String getDbSuffix() {
    return dbSuffix;
  }
}
