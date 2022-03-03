package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SalaryConfigDTO;
import com.gameplat.admin.model.vo.SalaryConfigVO;
import com.gameplat.model.entity.proxy.SalaryConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryConfigConvert {
    SalaryConfigVO toVo(SalaryConfig entity);

    SalaryConfig toEntity(SalaryConfigDTO salaryConfigDTO);
}
