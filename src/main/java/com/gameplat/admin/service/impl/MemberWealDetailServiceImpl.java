package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.MemberWealDetailMapper;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lily
 * @description 会员福利派发详情
 * @date 2021/11/25
 */
@Service
@RequiredArgsConstructor
public class MemberWealDetailServiceImpl extends ServiceImpl<MemberWealDetailMapper, MemberWealDetail> implements MemberWealDetailService{

    @Autowired private MemberWealDetailMapper detailMapper;

    @Override
    public void removeWealDetail(Long wealId) {
        if (ObjectUtils.isEmpty(wealId)) {
            throw new ServiceException("福利编号不能为空!");
        }
        if (!this.removeById(wealId)){
            throw new ServiceException("删除失败！");
        }
    }

    @Override
    public int batchSave(List<MemberWealDetail> list) {
        return detailMapper.batchSave(list);
    }
}
