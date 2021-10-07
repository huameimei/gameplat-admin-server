package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysBizBlacklistAddDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistUpdateDTO;
import com.gameplat.admin.model.entity.SysBizBlacklist;
import com.gameplat.admin.model.vo.SysBizBlacklistVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysBizBlacklistConvert {
    SysBizBlacklist toEntity(SysBizBlacklistAddDTO sysBizBlacklistAddDTO);

    SysBizBlacklistVO toVo(SysBizBlacklist sysBizBlacklist);

    SysBizBlacklist toEntity(SysBizBlacklistUpdateDTO sysBizBlacklistUpdateDTO);
}
