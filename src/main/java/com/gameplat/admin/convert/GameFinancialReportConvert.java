package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.TotalGameFinancialReportVO;
import com.gameplat.model.entity.report.GameFinancialReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameFinancialReportConvert {

    TotalGameFinancialReportVO toTotalVO(GameFinancialReport gameFinancialReport);
}
