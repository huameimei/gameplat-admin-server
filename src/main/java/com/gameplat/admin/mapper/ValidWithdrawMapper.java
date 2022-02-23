package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.model.vo.ValidAccoutWithdrawVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ValidWithdrawMapper extends BaseMapper<ValidWithdraw> {

    ValidWithdraw findValidWithdraw(ValidWithdraw validWithdraw);

    int save(ValidWithdraw validWithdraw);

    int updateByUserId(ValidWithdraw validWithdraw);

    int updateByBetId(ValidAccoutWithdrawVo validWithdraw);

    List<ValidAccoutWithdrawVo> findAccountValidWithdraw(@Param("account") String account);


}
