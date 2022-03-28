package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.ValidWithdraw;

public interface ValidWithdrawMappper extends BaseMapper<ValidWithdraw> {

  ValidWithdraw findValidWithdraw(ValidWithdraw validWithdraw);

  int save(ValidWithdraw validWithdraw);

  int updateByUserId(ValidWithdraw validWithdraw);
}
