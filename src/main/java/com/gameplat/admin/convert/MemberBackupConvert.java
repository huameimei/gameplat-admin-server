package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberBackupVO;
import com.gameplat.model.entity.member.MemberBackup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberBackupConvert {
  MemberBackupVO toVo(MemberBackup entity);
}
