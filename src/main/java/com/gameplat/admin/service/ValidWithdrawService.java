package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.ValidWithdraw;

public interface ValidWithdrawService extends IService<ValidWithdraw> {

    int saveValidWithdraw(ValidWithdraw validWithdraw);
}
