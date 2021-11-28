package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ValidWithdrawMappper;
import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.service.ValidWithdrawService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ap.internal.model.assignment.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author lily
 * @description 打码量
 * @date 2021/11/27
 */

@Service
@RequiredArgsConstructor
public class ValidWithdrawServiceImpl extends ServiceImpl<ValidWithdrawMappper, ValidWithdraw> implements ValidWithdrawService {

@Autowired private ValidWithdrawMappper mappper;
    @Override
    public int saveValidWithdraw(ValidWithdraw validWithdraw) {
        ValidWithdraw validWithdraw1 = mappper.findValidWithdraw(new ValidWithdraw() {{
            setMemberId(validWithdraw.getMemberId());
        }});
        int save = mappper.save(validWithdraw);
        if (save > 0) {
            if (validWithdraw1 != null) {
                validWithdraw1.setUpdateTime(new Date());
                mappper.updateByUserId(validWithdraw1);
            }

        }
        return save;
    }
}
