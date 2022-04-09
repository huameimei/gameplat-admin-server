package com.gameplat.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MemberBankConvert;
import com.gameplat.admin.enums.MemberBankEnums;
import com.gameplat.admin.mapper.MemberBankMapper;
import com.gameplat.admin.model.dto.MemberBankAddDTO;
import com.gameplat.admin.model.dto.MemberBankEditDTO;
import com.gameplat.admin.model.vo.MemberBankVO;
import com.gameplat.admin.service.MemberBankService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberBank;
import com.gameplat.model.entity.sys.SysDictData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberBankServiceImpl extends ServiceImpl<MemberBankMapper, MemberBank>
    implements MemberBankService {

  @Autowired private MemberService memberService;

  @Autowired private MemberBankConvert memberBankConvert;

  @Autowired private MemberBankMapper memberBankMapper;

  @Autowired
  private SysDictDataService sysDictDataService;

  @Override
  public List<MemberBankVO> getMemberBankList(Long memberId, String type) {
    return memberBankMapper.getMemberBankList(memberId, type);
  }

  @Override
  public void add(MemberBankAddDTO dto) {
    if (MemberBankEnums.TYPE.BANKCARD.match(dto.getType())) {
      this.addBankcard(dto);
    } else {
      this.addCryptocurrency(dto);
    }
  }

  @Override
  public void edit(MemberBankEditDTO dto) {
    MemberBank memberBank =
        Optional.ofNullable(this.getById(dto.getId()))
            .orElseThrow(() -> new ServiceException("银行卡信息不存在!"));
    if (MemberBankEnums.TYPE.BANKCARD.match(memberBank.getType())) {
      this.checkBankcardParams(dto.getCardHolder(), dto.getBankName(), dto.getAddress());
      MemberBank memberBanks = memberBankConvert.toEntity(dto);
      memberBanks.setIcon(getBankAddress(dto.getBankName()));
      Assert.isTrue(this.updateById(memberBanks), () -> new ServiceException("修改失败!"));
    } else {
      MemberBank memberBanks = memberBankConvert.toEntity(dto);
      this.checkCryptocurrencyParams(dto.getAlias(), dto.getNetwork(), dto.getCurrency());
      Assert.isTrue(this.updateById(memberBanks), () -> new ServiceException("修改失败!"));
    }
  }

  @Override
  public void setDefault(Long id) {
    MemberBank memberBank =
        Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ServiceException("银行卡信息不存在!"));

    Assert.isFalse(
        MemberBankEnums.DEFAULT.Y.match(memberBank.getIsDefault()),
        () -> new ServiceException("当前卡号已经是默认卡号"));

    // 取消其他默认
    this.resetDefault(memberBank.getMemberId(), memberBank.getType());

    // 设置当前卡为默认
    Assert.isTrue(
        this.lambdaUpdate()
            .set(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.Y.value())
            .eq(MemberBank::getId, id)
            .update(new MemberBank()),
        () -> new ServiceException("设置默认卡失败"));
  }

  private void checkBankcardParams(String cardholder, String bankName, String address) {
    Assert.notBlank(cardholder, () -> new ServiceException("持卡人不能为空"));
    Assert.notBlank(bankName, () -> new ServiceException("银行卡号不能为空"));
    Assert.notBlank(address, () -> new ServiceException("开户行地址不能为空"));
  }

  private void checkCryptocurrencyParams(String alias, String network, String currency) {
    Assert.notBlank(alias, () -> new ServiceException("地址别名不能为空"));
    Assert.notBlank(network, () -> new ServiceException("转账网络不能为空"));
    Assert.notBlank(currency, () -> new ServiceException("币种不能为空"));
  }

  private void addCryptocurrency(MemberBankAddDTO dto) {
    this.checkCryptocurrencyParams(dto.getAlias(), dto.getNetwork(), dto.getCurrency());

    // 将其他卡设置为非默认
    Member member = memberService.getById(dto.getMemberId());
    if (MemberBankEnums.DEFAULT.Y.match(dto.getIsDefault())) {
      this.resetDefault(member.getId(), dto.getType());
    }

    MemberBank memberBank = memberBankConvert.toEntity(dto);
    Assert.isTrue(this.save(memberBank), () -> new ServiceException("添加虚拟货币地址失败!"));
  }

  private void addBankcard(MemberBankAddDTO dto) {
    this.checkBankcardParams(dto.getCardHolder(), dto.getBankName(), dto.getAddress());

    Member member = memberService.getById(dto.getMemberId());
    if (StringUtils.isEmpty(member.getRealName())) {
      // 更新真实姓名
      memberService.updateRealName(member.getId(), dto.getCardHolder());
    } else if (!member.getRealName().equals(dto.getCardHolder())) {
      throw new ServiceException("银行卡真实姓名与系统预留不匹配");
    }

    // 将其他卡设置为非默认
    if (MemberBankEnums.DEFAULT.Y.match(dto.getIsDefault())) {
      this.resetDefault(member.getId(), dto.getType());
    }

    MemberBank memberBank = memberBankConvert.toEntity(dto);
    memberBank.setIcon(getBankAddress(dto.getBankName()));
    Assert.isTrue(this.save(memberBank), () -> new ServiceException("添加会员银行卡失败!"));
  }


  public String getBankAddress(String bankName) {

    // 获取银行卡图片
    SysDictData sysDictData =
            sysDictDataService.getDictData(DictTypeEnum.RECH_BANK.getValue(), bankName);

    return sysDictData.getRemark();
  }

  /**
   * 重置默认卡
   *
   * @param memberId Long
   * @param type String
   */
  private void resetDefault(Long memberId, String type) {
    this.lambdaUpdate()
        .set(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.N.value())
        .eq(MemberBank::getType, type)
        .eq(MemberBank::getMemberId, memberId)
        .update();
  }
}
