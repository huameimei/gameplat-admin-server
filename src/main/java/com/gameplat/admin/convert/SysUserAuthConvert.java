package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysUserAuthDto;
import com.gameplat.admin.model.vo.SysUserAuthVo;
import com.gameplat.model.entity.sys.SysUserAuth;
import org.mapstruct.Mapper;

/**
 * @Author kb @Date 2022/3/12 17:23 @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface SysUserAuthConvert {

  SysUserAuth toEntity(SysUserAuthDto dto);

  SysUserAuthVo toVo(SysUserAuth entity);
}
