package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberBankConvert;
import com.gameplat.admin.enums.MemberBankEnums;
import com.gameplat.admin.mapper.MemberBankMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBank;
import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBankServiceImpl extends ServiceImpl<MemberBankMapper, MemberBank>
    implements MemberBankService {

  @Autowired private MemberService memberService;

  @Autowired private MemberBankConvert memberBankConvert;

  @Autowired private MemberBankMapper memberBankMapper;

  @Override
  public List<MemberBankVO> getMemberBankList(Long memberId, String type) {
    return memberBankMapper.getMemberBankList(memberId, type);
  }

  @Override
  public void add(MemberBankAddDTO dto) {
    Member member = memberService.getById(dto.getMemberId());
    if (null == member) {
      throw new ServiceException("会员信息不存在");
    }

    // 处理银行卡真实姓名
    if (MemberBankEnums.TYPE.BANKCARD.match(dto.getType())) {
      if (StringUtils.isEmpty(member.getRealName())) {
        // 更新真实姓名
        if (!memberService.updateById(
            Member.builder().id(member.getId()).realName(dto.getRealName()).build())) {
          throw new ServiceException("更新会员真实姓名失败!");
        }
      } else if (!member.getRealName().equals(dto.getRealName())) {
        throw new ServiceException("银行卡真实姓名与系统预留不匹配");
      }
    }

    // 判断是否存在了默认卡号
    if (MemberBankEnums.DEFAULT.Y.match(dto.getIsDefault()) && this.hasDefaultBankcard(dto)) {
      throw new ServiceException("已存在默认卡号");
    }

    if (!this.save(memberBankConvert.toEntity(dto))) {
      throw new ServiceException("添加会员银行卡失败!");
    }
  }

  @Override
  public void edit(MemberBankEditDTO dto) {
    MemberBank memberBank = this.getById(dto.getId());
    if (null == memberBank) {
      throw new ServiceException("信息不存在!");
    }

    if (!this.updateById(memberBankConvert.toEntity(dto))) {
      throw new ServiceException("修改失败!");
    }
  }

  @Override
  public void setDefault(Long id) {
    MemberBank memberBank = this.getById(id);
    if (null == memberBank) {
      throw new ServiceException("银行卡信息不存在!");
    }

    if (MemberBankEnums.DEFAULT.Y.match(memberBank.getIsDefault())) {
      throw new ServiceException("当前卡号已经是默认卡号");
    }

    // 取消其他默认
    if (!this.lambdaUpdate()
        .set(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.N.value())
        .eq(MemberBank::getType, memberBank.getType())
        .eq(MemberBank::getMemberId, memberBank.getMemberId())
        .update()) {
      throw new ServiceException("更新卡信息失败!");
    }

    // 设置当前卡为默认
    if (!this.lambdaUpdate()
        .set(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.Y.value())
        .eq(MemberBank::getId, id)
        .update(new MemberBank())) {
      throw new ServiceException("设置默认卡失败");
    }
  }

  /**
   * 是否有默认卡号
   *
   * @param dto MemberBankAddDTO
   * @return boolean
   */
  private boolean hasDefaultBankcard(MemberBankAddDTO dto) {
    return 0
        < this.lambdaQuery()
            .eq(MemberBank::getMemberId, dto.getMemberId())
            .eq(MemberBank::getType, dto.getType())
            .eq(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.Y.value())
            .count();
  }
}
