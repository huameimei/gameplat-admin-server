package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberBackup;
import com.gameplat.admin.model.vo.MemberBackupVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberBackupConvert {
  MemberBackupVO toVo(MemberBackup entity);
}
