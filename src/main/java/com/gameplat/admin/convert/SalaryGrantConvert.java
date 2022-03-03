package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SalaryGrantDTO;
import com.gameplat.admin.model.vo.SalaryGrantVO;
import com.gameplat.model.entity.proxy.SalaryGrant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryGrantConvert {
    SalaryGrantVO toVo(SalaryGrant entity);

    SalaryGrant toEntity(SalaryGrantDTO salaryGrantDTO);
}
