package com.gameplat.admin.handler;

import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.common.enums.SubjectEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.base.common.log.SysLog;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.log.handler.LogBuilder;
import com.gameplat.security.SecurityUserHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Hoover
 * @Date: 20/11/2021 下午 6:49
 **/
@Configuration
public class AdminLogBuilder implements LogBuilder {

    @Value("${syslog.tenant:#{null}}")
    private String dbSuffix;

    @Override
    public SysLog build() {

        UserTypes userTypes =
                SecurityUserHolder.isSuperAdmin() ?
                        UserTypes.ADMIN : UserTypes.SUBUSER;

        String tenant = DyDataSourceContextHolder.getTenant();
        tenant = StringUtils.isEmpty(tenant) ? dbSuffix : tenant;
        return SysLog.builder()
                .dbSuffix(tenant)
                .userType(userTypes.key())
                .registerTime(SecurityUserHolder.getCredential().getRegisterTime())
                .username(SecurityUserHolder.getUsername())
                .subject(SubjectEnum.ADMIN.getKey())
                .build();
    }
}
