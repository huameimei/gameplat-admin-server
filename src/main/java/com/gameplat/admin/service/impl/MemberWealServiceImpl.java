package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealConvert;
import com.gameplat.admin.mapper.MemberWealMapper;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author lily
 * @description 福利发放业务处理
 * @date 2021/11/22
 */

@Service
@RequiredArgsConstructor
public class MemberWealServiceImpl extends ServiceImpl<MemberWealMapper, MemberWeal> implements MemberWealService {

    @Autowired private MemberWealConvert wealConvert;

    /**
     * 获取等级俸禄达标会员
     */
    @Override
    public IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO) {
        return this.lambdaQuery()
                .like(ObjectUtils.isNotEmpty(queryDTO.getName()), MemberWeal::getName, queryDTO.getName())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWeal::getStatus, queryDTO.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWeal::getType, queryDTO.getType())
                .ge(ObjectUtils.isNotEmpty(queryDTO.getStartDate()), MemberWeal::getCreateTime, queryDTO.getStartDate())
                .le(ObjectUtils.isNotEmpty(queryDTO.getEndDate()), MemberWeal::getCreateTime, queryDTO.getEndDate())
                .page(page)
                .convert(wealConvert::toVo);
    }


    /**
     * 修改会员俸禄
     */
    @Override
    public void addMemberWeal(MemberWealAddDTO dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new ServiceException("名称不能为空！");
        }
        if (dto.getType() == null) {
            throw new ServiceException("类型不能为空！");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new ServiceException("周期不能为空！");
        }
        if (dto.getMinRechargeAmount() == null) {
            throw new ServiceException("最低充值金额不能为空！");
        }
        if (dto.getMinBetAmount() == null) {
            throw new ServiceException("最低有效投注不能为空！");
        }
        dto.setStatus(0);




        MemberWeal memberWeal = wealConvert.toEntity(dto);

        if (!this.save(memberWeal)) {
            throw new ServiceException("新增失败！");
        }
    }


}
