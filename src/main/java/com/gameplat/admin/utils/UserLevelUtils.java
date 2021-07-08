package com.gameplat.admin.utils;

import cn.hutool.core.util.StrUtil;
import com.gameplat.admin.enums.AdminTypeEnum;
import com.gameplat.admin.model.entity.SysUser;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserLevelUtils {

  public static List<String> getAdminUserLevels(SysUser sysUser) {
    if (AdminTypeEnum.isNormal(sysUser.getUserType())
        && StrUtil.isNotEmpty(sysUser.getUserLevel())) {
      return Stream.of(sysUser.getUserLevel().split(StrUtil.COMMA)).collect(Collectors.toList());
    }
    return null;
  }
}
