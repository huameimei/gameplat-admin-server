package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.OperUserDTO;
import com.gameplat.admin.model.dto.UserInfoDTO;
import com.gameplat.admin.model.vo.UserInfoVo;
import com.gameplat.admin.model.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 用户类型转换
 * @author three
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

  @Mapping(source = "loginName", target = "userName")
  SysUser toEntity(UserInfoDTO infoDTO);

  @Mapping(source = "id", target = "userId")
  @Mapping(source = "account", target = "userName")
  SysUser toEntity(OperUserDTO infoDTO);

  @Mapping(source = "userId", target = "id")
  @Mapping(source = "userName", target = "loginName")
  UserInfoVo toVo(SysUser user);

  @Mapping(source = "userId", target = "id")
  @Mapping(source = "userName", target = "account")
  UserVo toUserVo(SysUser entity);
}
