package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberBillConvert;
import com.gameplat.admin.mapper.MemberBillMapper;
import com.gameplat.admin.model.dto.MemberBillDTO;
import com.gameplat.admin.model.vo.MemberBillVO;
import com.gameplat.admin.service.MemberBillService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.enums.TranTypes;
import com.gameplat.common.model.bean.TranTypeBean;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBillServiceImpl extends ServiceImpl<MemberBillMapper, MemberBill>
    implements MemberBillService {

  @Autowired private MemberBillMapper memberBillMapper;

  @Autowired private MemberBillConvert memberBillConvert;

  @Autowired private MemberService memberService;

  @Override
  public void save(Member member, MemberBill memberBill) throws Exception {
    memberBill.setAccount(member.getAccount());
    memberBill.setMemberPath(member.getSuperPath());
    memberBill.setMemberId(member.getId());
    this.save(memberBill);
  }

  @Override
  public List<TranTypeBean> findTranTypes() {
    return TranTypes.getAllTranList();
  }

  @Override
  public IPage<MemberBillVO> findMemberBilllistPage(PageDTO<MemberBill> page, MemberBillDTO dto) {
    QueryWrapper<Member> memberQuery = Wrappers.query();
    memberQuery.eq(true, "account", dto.getAccount());
    IPage<MemberBillVO> pageList =
        memberBillMapper.findy(
            page,
            dto.getAccount(),
            dto.getOrderNo(),
            dto.getTranTypes(),
            dto.getBeginTime(),
            dto.getEndTime()
        );
    return pageList;
  }

  @Override
  public List<MemberBillVO> findMemberBillList(MemberBillDTO dto) {

    if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getEndTime())) {
      throw new ServiceException("查询账变记录需选择时间段！");
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date nowDate = null;
    Date beginTime = null;
    Date endTime = null;
    try {
      nowDate = sdf.parse(sdf.format(new Date()));
      beginTime = sdf.parse(dto.getBeginTime());
      endTime = sdf.parse(dto.getEndTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    // 今天
    if (beginTime.compareTo(nowDate) == 0 && endTime.compareTo(nowDate) == 0) {
      dto.setBeginTime(dto.getBeginTime() + " 00:00:00");
      dto.setEndTime(dto.getEndTime() + " 23:59:59");
    } else if (ObjectUtils.isEmpty(dto.getAccount())) {
      throw new ServiceException("查询【非当日】账变记录需提供【会员帐号】！");
    }
    QueryWrapper<Member> memberQuery = Wrappers.query();
    memberQuery.eq(true, "account", dto.getAccount());
    return memberBillMapper.findy(
        dto.getAccount(),
        dto.getOrderNo(),
        dto.getTranTypes(),
        dto.getBeginTime(),
        dto.getEndTime());
  }

  @Override
  public MemberBill queryLiveBill(Long id, String orderNo, int tranType) {
    // TODO 获取额度转换流水记录
    MemberBill memberBill =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(id), MemberBill::getMemberId, id)
            .eq(ObjectUtils.isNotEmpty(orderNo), MemberBill::getOrderNo, orderNo)
            .eq(ObjectUtils.isNotEmpty(tranType), MemberBill::getTranType, tranType)
            .one();
    if (memberBill == null) {
      Member member = memberService.getById(id);
      if (member == null) {
        throw new ServiceException("用户不存在");
      }
      memberBill = memberBillMapper.findBill(orderNo, tranType);
    }
    return memberBill;
  }

  public List<MemberBillVO> getList(MemberBillDTO dto) {
    if (ObjectUtils.isEmpty(dto.getBeginTime()) || ObjectUtils.isEmpty(dto.getEndTime())) {
      throw new ServiceException("查询账变记录需选择时间段！");
    }
    List<MemberBillVO> voList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date nowDate = null;
    Date beginTime = null;
    Date endTime = null;
    try {
      nowDate = sdf.parse(sdf.format(new Date()));
      beginTime = sdf.parse(dto.getBeginTime());
      endTime = sdf.parse(dto.getEndTime());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    // 今天
    if (beginTime.compareTo(nowDate) == 0 && endTime.compareTo(nowDate) == 0) {
      dto.setBeginTime(dto.getBeginTime() + " 00:00:00");
      dto.setEndTime(dto.getEndTime() + " 23:59:59");
      List<MemberBill> list =
          this.lambdaQuery()
              .eq(
                  ObjectUtils.isNotEmpty(dto.getAccount()),
                  MemberBill::getAccount,
                  dto.getAccount())
              .eq(
                  ObjectUtils.isNotEmpty(dto.getOrderNo()),
                  MemberBill::getOrderNo,
                  dto.getOrderNo())
              .ge(
                  ObjectUtils.isNotEmpty(dto.getBeginTime()),
                  MemberBill::getCreateTime,
                  dto.getBeginTime())
              .le(
                  ObjectUtils.isNotEmpty(dto.getEndTime()),
                  MemberBill::getCreateTime,
                  dto.getEndTime())
              .in(
                  ObjectUtils.isNotEmpty(dto.getTranTypes()),
                  MemberBill::getTranType,
                  dto.getTranTypes())
              .orderByDesc(MemberBill::getCreateTime)
              .orderByDesc(MemberBill::getId)
              .list();

      for (int i = 0; i < list.size(); i++) {
        voList.add(memberBillConvert.toVo(list.get(i)));
      }
      return voList;
      // 历史
    } else {
      if (ObjectUtils.isEmpty(dto.getAccount())) {
        throw new ServiceException("查询【非当日】账变记录需提供【会员帐号】！");
      }
      voList =
          memberBillMapper.findy(
              dto.getAccount(),
              dto.getOrderNo(),
              dto.getTranTypes(),
              dto.getBeginTime(),
              dto.getEndTime());
      return voList;
    }
  }
}
