package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberWealConvert;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBlackList;
import com.gameplat.admin.model.domain.MemberWeal;
import com.gameplat.admin.model.domain.MemberWealDetail;
import com.gameplat.admin.model.dto.MemberEditDTO;
import com.gameplat.admin.model.dto.MemberWealAddDTO;
import com.gameplat.admin.model.dto.MemberWealDTO;
import com.gameplat.admin.model.dto.MemberWealEditDTO;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.MemberBlackListService;
import com.gameplat.admin.service.MemberWealDetailService;
import com.gameplat.admin.service.MemberWealService;
import com.gameplat.common.context.GlobalContextHolder;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author lily
 * @description 福利发放业务处理
 * @date 2021/11/22
 */

@Service
@RequiredArgsConstructor
public class MemberWealServiceImpl extends ServiceImpl<MemberWealMapper, MemberWeal> implements MemberWealService {

    @Autowired private MemberWealConvert wealConvert;
    @Autowired private MemberWealMapper mapper;
    @Autowired private MemberGrowthStatisMapper statisMapper;
    @Autowired private RechargeOrderMapper rechargeOrderMapper;
    @Autowired private MemberDayReportMapper dayReportMapper;
    @Autowired private MemberMapper memberMapper;
    @Autowired private MemberBlackListService blackListService;
    @Autowired private MemberWealDetailService wealDetailService;

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
                .orderByDesc(MemberWeal::getCreateTime)
                .page(page)
                .convert(wealConvert::toVo);
    }


    /**
     * 添加
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

    /**
     * 修改福利
     */
    @Override
    public void updateMemberWeal(MemberWealEditDTO dto) {
        if (ObjectUtils.isEmpty(dto.getId())){
            throw new ServiceException("id不能为空!");
        }
        MemberWeal weal = wealConvert.toEntity(dto);
        if (!this.updateById(weal)){
            throw new ServiceException("编辑失败!");
        }
    }

    /**
     * 删除福利
     */
    @Override
    public void deleteMemberWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        if (!this.removeById(id)){
            throw new ServiceException("删除失败！");
        }
    }

    /**
     * 结算
     */
    @Override
    public void settleWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        MemberWeal memberWeal = mapper.selectById(id);
        if(memberWeal == null){
            throw new ServiceException("该福利不存在!");
        }
        //福利类型
        Integer type = memberWeal.getType();
        //福利状态
        Integer status = memberWeal.getStatus();
        if (status != 0 && status != 1){
            throw new IllegalArgumentException("福利状态异常,不能结算！");
        }
        //获取会员对应等级的福利金额
        List<MemberWealDetail> memberSalaryInfoList = statisMapper.getMemberSalaryInfo(type);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查找符合充值金额和打码量资格的会员
        List<String> rechargeAccountList = new ArrayList<>();
        if (memberWeal.getMinRechargeAmount() != null && memberWeal.getMinRechargeAmount().compareTo(new BigDecimal("0")) > 0) {
            rechargeAccountList = rechargeOrderMapper.getSatisfyRechargeAccount(
                    String.valueOf(memberWeal.getMinRechargeAmount()),
                    formatter.format(memberWeal.getStartDate()),
                    formatter.format(memberWeal.getEndDate()));
        } else {
            rechargeAccountList = memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
        }

        //获取达到有效投注金额的会员账号
        List<String> betAccountList = new ArrayList<>();
        if(memberWeal.getMinBetAmount() != null && memberWeal.getMinBetAmount().compareTo(new BigDecimal("0")) > 0){
            betAccountList = dayReportMapper.getSatisfyBetAccount(
                    String.valueOf(memberWeal.getMinBetAmount()),
                    formatter.format(memberWeal.getStartDate()),
                    formatter.format(memberWeal.getEndDate())
            );
        } else {
            betAccountList = memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
        }

        //取达到充值金额的会员账号&&达到有效投注金额的会员账号的交集
        List<String> intersectionList = rechargeAccountList.stream().filter(betAccountList::contains).collect(toList());

        if (type == 0 || type == 1) {
            //周 月 俸禄 结算
        } else if (type == 2){
            // 生日礼金
            //过滤每个会员的生日是否在周期内
            if (intersectionList != null && intersectionList.size() > 0){
                List<Member> users = memberMapper.findByUserNameList(intersectionList);
                intersectionList = users.stream().filter(item -> item.getBirthday() != null && DateUtil.parse(DateUtil.format(item.getBirthday(),"MM-dd"),"MM-dd").compareTo(DateUtil.parse(DateUtil.format(memberWeal.getStartDate(),"MM-dd"),"MM-dd")) >= 0
                        && DateUtil.parse(DateUtil.format(item.getBirthday(),"MM-dd"),"MM-dd").compareTo(DateUtil.parse(DateUtil.format(memberWeal.getEndDate(),"MM-dd"),"MM-dd")) <= 0).map(item -> item.getNickname())
                        .filter(intersectionList::contains).collect(toList());
            }
        } else if (type == 3){//每月红包

        }
        //取享有福利的会员账号，再次跟上面 intersectionList 集合账号取交集
        List<String> finalIntersectionList1 = intersectionList;
        List<MemberWealDetail> finalIntersectionList = new ArrayList<>();
        finalIntersectionList = memberSalaryInfoList.stream()
                .filter(item -> finalIntersectionList1.stream().map(String::toString).collect(Collectors.toList()).contains(item.getUserName())).collect(Collectors.toList());

        List<MemberWealDetail> list = new ArrayList<>();

        if(CollectionUtil.isNotEmpty(finalIntersectionList)) {
            //过滤黑名单
            List<String> blackList = new ArrayList<>();
            List<MemberBlackList> memberBlackList = blackListService.findMemberBlackList(new MemberBlackList());
            if (CollectionUtil.isNotEmpty(memberBlackList)) {
                blackList = memberBlackList.stream().map(memberBlack -> memberBlack.getUserAccount()).collect(Collectors.toList());
            }
            if (CollectionUtil.isNotEmpty(blackList)) {
                for (String userAccount : blackList) {
                    finalIntersectionList = finalIntersectionList.stream().filter(item1 -> !item1.getUserName().equalsIgnoreCase(userAccount)).collect(Collectors.toList());
                }
            }
            //装配福利详情等字段
            int totalUserCount = 0;
            BigDecimal totalPayMoney = new BigDecimal("0");
            for (MemberWealDetail memberWealDetail : finalIntersectionList) {
                MemberWealDetail model = new MemberWealDetail();
                model.setWealId(id);
                model.setUserId(memberWealDetail.getUserId());
                model.setUserName(memberWealDetail.getUserName());
                model.setLevel(memberWealDetail.getLevel());
                model.setRewordAmount(memberWealDetail.getRewordAmount());
                model.setStatus(1);
                model.setCreateBy(GlobalContextHolder.getContext().getUsername());
                list.add(model);
                totalUserCount++;
                totalPayMoney = totalPayMoney.add(memberWealDetail.getRewordAmount());
            }
            memberWeal.setTotalUserCount(totalUserCount);
            memberWeal.setTotalPayMoney(totalPayMoney);
        }
        //先删除，后保存
        wealDetailService.removeWealDetail(id);
        if (list != null && list.size() > 0){
            wealDetailService.batchSave(list);
        }
        memberWeal.setSettleTime(new Date());
        memberWeal.setStatus(1);
        memberWeal.setUpdateBy(GlobalContextHolder.getContext().getUsername());
        memberWeal.setUpdateTime(new Date());
        if (!this.updateById(memberWeal)){
            throw new ServiceException("结算失败!");
        }
    }

    /**
     * 福利派发
     */
    @Override
    public void distributeWeal(Long wealId, HttpServletRequest request) {

    }

    /**
     * 福利回收
     */
    @Override
    public void recycleWeal(Long wealId, HttpServletRequest request) {

    }


}
