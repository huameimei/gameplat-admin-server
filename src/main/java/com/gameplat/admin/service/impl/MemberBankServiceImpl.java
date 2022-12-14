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
            .orElseThrow(() -> new ServiceException("????????????????????????!"));
    if (MemberBankEnums.TYPE.BANKCARD.match(memberBank.getType())) {
      this.checkBankcardParams(dto.getCardHolder(), dto.getBankName(), dto.getAddress());
      MemberBank memberBanks = memberBankConvert.toEntity(dto);
      memberBanks.setIcon(getBankAddress(dto.getBankName()));
      Assert.isTrue(this.updateById(memberBanks), () -> new ServiceException("????????????!"));
    } else {
      MemberBank memberBanks = memberBankConvert.toEntity(dto);
      this.checkCryptocurrencyParams(dto.getAlias(), dto.getNetwork(), dto.getCurrency());
      Assert.isTrue(this.updateById(memberBanks), () -> new ServiceException("????????????!"));
    }
  }

  @Override
  public void setDefault(Long id) {
    MemberBank memberBank =
        Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ServiceException("????????????????????????!"));

    Assert.isFalse(
        MemberBankEnums.DEFAULT.Y.match(memberBank.getIsDefault()),
        () -> new ServiceException("?????????????????????????????????"));

    // ??????????????????
    this.resetDefault(memberBank.getMemberId(), memberBank.getType());

    // ????????????????????????
    Assert.isTrue(
        this.lambdaUpdate()
            .set(MemberBank::getIsDefault, MemberBankEnums.DEFAULT.Y.value())
            .eq(MemberBank::getId, id)
            .update(new MemberBank()),
        () -> new ServiceException("?????????????????????"));
  }

  private void checkBankcardParams(String cardholder, String bankName, String address) {
    Assert.notBlank(cardholder, () -> new ServiceException("?????????????????????"));
    Assert.notBlank(bankName, () -> new ServiceException("????????????????????????"));
    Assert.notBlank(address, () -> new ServiceException("???????????????????????????"));
  }

  private void checkCryptocurrencyParams(String alias, String network, String currency) {
    Assert.notBlank(alias, () -> new ServiceException("????????????????????????"));
    Assert.notBlank(network, () -> new ServiceException("????????????????????????"));
    Assert.notBlank(currency, () -> new ServiceException("??????????????????"));
  }

  private void addCryptocurrency(MemberBankAddDTO dto) {
    this.checkCryptocurrencyParams(dto.getAlias(), dto.getNetwork(), dto.getCurrency());

    // ??????????????????????????????
    Member member = memberService.getById(dto.getMemberId());
    if (MemberBankEnums.DEFAULT.Y.match(dto.getIsDefault())) {
      this.resetDefault(member.getId(), dto.getType());
    }

    MemberBank memberBank = memberBankConvert.toEntity(dto);
    Assert.isTrue(this.save(memberBank), () -> new ServiceException("??????????????????????????????!"));
  }

  private void addBankcard(MemberBankAddDTO dto) {
    this.checkBankcardParams(dto.getCardHolder(), dto.getBankName(), dto.getAddress());

    Member member = memberService.getById(dto.getMemberId());
    if (StringUtils.isEmpty(member.getRealName())) {
      // ??????????????????
      memberService.updateRealName(member.getId(), dto.getCardHolder());
    } else if (!member.getRealName().equals(dto.getCardHolder())) {
      throw new ServiceException("?????????????????????????????????????????????");
    }

    // ??????????????????????????????
    if (MemberBankEnums.DEFAULT.Y.match(dto.getIsDefault())) {
      this.resetDefault(member.getId(), dto.getType());
    }

    MemberBank memberBank = memberBankConvert.toEntity(dto);
    memberBank.setIcon(getBankAddress(dto.getBankName()));
    Assert.isTrue(this.save(memberBank), () -> new ServiceException("???????????????????????????!"));
  }


  public String getBankAddress(String bankName) {

    // ?????????????????????
    SysDictData sysDictData =
            sysDictDataService.getDictData(DictTypeEnum.RECH_BANK.getValue(), bankName);

    return sysDictData.getRemark();
  }

  /**
   * ???????????????
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
