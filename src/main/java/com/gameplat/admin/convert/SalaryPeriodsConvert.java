package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SalaryPeriodsDTO;
import com.gameplat.admin.model.vo.SalaryPeriodsVO;
import com.gameplat.model.entity.proxy.SalaryPeriods;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaryPeriodsConvert {
  SalaryPeriodsVO toVo(SalaryPeriods entity);

  SalaryPeriods toEntity(SalaryPeriodsDTO salaryPeriodsDTO);
}
