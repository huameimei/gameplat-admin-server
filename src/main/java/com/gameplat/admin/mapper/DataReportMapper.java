package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataReportMapper extends BaseMapper<GameRechDataReportVO> {

    /**
     *
     * @param dto 查询充值入参
     * @return GameRWDataReportVO
     */
    GameRechDataReportVO findRechReport(GameRWDataReportDto dto);

    /**
     * @param dto 查询充值入参
     * @return List<Map < String ,   O bject>>
     */
    List<Map<String,Object>> findRechReportNums(GameRWDataReportDto dto);




    /**
     *
     * @param dto 查询提现入参
     * @return GameRWDataReportVO
     */
    GameWithDataReportVO findWithReport(GameRWDataReportDto dto);


    /**
     * @param dto  查询提现入参
     * @return List<Map < String ,   O bject>>
     */
    List<Map<String,Object>> findWithReportNums(GameRWDataReportDto dto);


    /**
     * @param dto  查询游戏投注
     * @return st<GameBetDataReportVO>
     */
    List<GameBetDataReportVO> findGameReport(GameRWDataReportDto dto);


    /**
     * @param dto  查询返水
     * @return List<GameWaterDataReportVO>
     */
    List<GameWaterDataReportVO> findGameWaterReport(GameRWDataReportDto dto);


    /**
     * @param dto  查询游戏人数
     * @return List<String>
     */
    List<String> findGameDataUserNum(GameRWDataReportDto dto);

    /**
     * @param dto  查询注册人数
     * @return int
     */
    int findReportMemberRegNum(GameRWDataReportDto dto);

    /**
     * @param dto  查询登录人数
     * @return int
     */
    int findReportMemberLogNum(GameRWDataReportDto dto);


    /**
     * @param dto  查询会员余额
     * @return int
     */
    List<AccountReportVo> findReportMemberBalance(@Param("dto") GameRWDataReportDto dto, @Param("pageSize") long pageSize, @Param("pageCurrent")long pageCurrent);

    int findReportMemberBalanceCount(GameRWDataReportDto dto);


    /**
     * @param dto  查询余额
     * @return double
     */
    BigDecimal findReportMemberAllBalance(GameRWDataReportDto dto);


    /**
     *
     * @param dto 查询优惠
     * @return Map
     */
    Map<String,BigDecimal> findDiscountDataReport(GameRWDataReportDto dto);


    /**
     *
     * @param dto 查询红利
     * @return Map
     */
    BigDecimal findDividendDataReport(GameRWDataReportDto dto);

    /**
     *
     * @param dto 查询活动
     * @return Map
     */
    BigDecimal findActivityDataReport(GameRWDataReportDto dto);

    /**
     *
     * @param dto 查询红包
     * @return Map
     */
    BigDecimal findRedDataReport(GameRWDataReportDto dto);
}
