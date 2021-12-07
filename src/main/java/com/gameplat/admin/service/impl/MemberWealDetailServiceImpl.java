package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealDetailConvert;
import com.gameplat.admin.mapper.MemberWealDetailMapper;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.dto.MemberWealDetailDTO;
import com.gameplat.admin.model.dto.MemberWealDetailEditDTO;
import com.gameplat.admin.model.dto.MemberWealDetailRemoveDTO;
import com.gameplat.admin.model.vo.MemberWealDetailVO;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.base.common.exception.ServiceException;
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

    @Autowired private MemberWealDetailConvert detailConvert;
    @Autowired private MemberWealDetailMapper detailMapper;

    @Override
    public IPage<MemberWealDetailVO> findWealDetailList(PageDTO<MemberWealDetail> page, MemberWealDetailDTO queryDTO) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(queryDTO.getWealId()), MemberWealDetail::getWealId, queryDTO.getWealId())
                .like(ObjectUtils.isNotEmpty(queryDTO.getUserName()), MemberWealDetail::getUserName, queryDTO.getUserName())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWealDetail::getStatus, queryDTO.getStatus())
                .orderByDesc(MemberWealDetail::getCreateTime)
                .page(page)
                .convert(detailConvert::toVo);
    }

    @Override
    public void removeWealDetail(Long wealId) {
        if (ObjectUtils.isEmpty(wealId)) {
            throw new ServiceException("福利编号不能为空!");
        }
        LambdaQueryWrapper<MemberWealDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberWealDetail::getWealId, wealId);
        if (!this.remove(wrapper)){
            throw new ServiceException("删除失败！");
        }
    }

    @Override
    public int batchSave(List<MemberWealDetail> list) {
        return detailMapper.batchSave(list);
    }

    @Override
    public List<MemberWealDetail> findSatisfyMember(MemberWealDetail wealDetail) {
        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(wealDetail.getWealId()), MemberWealDetail::getWealId, wealDetail.getWealId())
                .like(ObjectUtils.isNotEmpty(wealDetail.getUserName()), MemberWealDetail::getUserName, wealDetail.getUserName())
                .eq(ObjectUtils.isNotEmpty(wealDetail.getStatus()), MemberWealDetail::getStatus, wealDetail.getStatus())
                .orderByDesc(MemberWealDetail::getCreateTime)
                .list();
    }

    @Override
    public void updateByWealStatus(Long id, Integer status) {
        if (ObjectUtils.isEmpty(id)){
            throw new ServiceException("福利编号不能为空!");
        }

        if (!this.update(new LambdaUpdateWrapper<MemberWealDetail>()
                .eq(ObjectUtils.isNotEmpty(id), MemberWealDetail::getWealId, id)
                .set( MemberWealDetail::getStatus, status))){
            throw new ServiceException("更新福利记录状态失败:");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (ObjectUtils.isNull(id)){
            throw new ServiceException("id不能为空");
        }
        if (!this.removeById(id)){
            throw new ServiceException("删除失败");
        }
    }

    @Override
    public void editRewordAmount(MemberWealDetailEditDTO dto) {
        if (ObjectUtils.isNull(dto.getId())){
            throw new ServiceException("id不能为空");
        }
        if (!this.update(new LambdaUpdateWrapper<MemberWealDetail>()
                        .eq(MemberWealDetail::getId, dto.getId())
                        .set(MemberWealDetail::getRewordAmount, dto.getRewordAmount()))){
            throw new ServiceException("编辑失败！");
        }
    }
}
