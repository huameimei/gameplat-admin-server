package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MessageDictDataVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MessageDistributeService;
import com.gameplat.admin.service.MessageInfoService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.message.Message;
import com.gameplat.model.entity.message.MessageDistribute;
import com.gameplat.model.entity.sys.SysDictData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 个人消息、站内信
 *
 * @author admin
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MessageInfoServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageInfoService {

  @Autowired private MessageInfoConvert messageInfoConvert;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private MemberService memberService;

  @Autowired private MessageDistributeService messageDistributeService;

  @Override
  public MessageDictDataVO getDictData() {
    List<String> types = new ArrayList<>();
    types.add("MESSAGE_MEMBER_RANGE");
    types.add("MESSAGE_LOCATION");
    types.add("MESSAGE_POP_COUNT");
    types.add("MESSAGE_CATEGORY");
    types.add("MESSAGE_SHOW_TYPE");
    List<SysDictData> dictDataByTypes = sysDictDataService.getDictDataByTypes(types);

    MessageDictDataVO vo = new MessageDictDataVO();
    for (SysDictData dictData : dictDataByTypes) {
      if ("MESSAGE_MEMBER_RANGE".equals(dictData.getDictType())) {
        vo.getUserRange().add(dictData);
      }
      if ("MESSAGE_LOCATION".equals(dictData.getDictType())) {
        vo.getLocation().add(dictData);
      }
      if ("MESSAGE_POP_COUNT".equals(dictData.getDictType())) {
        vo.getPopCount().add(dictData);
      }
      if ("MESSAGE_CATEGORY".equals(dictData.getDictType())) {
        vo.getMessageCate().add(dictData);
      }
      if ("MESSAGE_SHOW_TYPE".equals(dictData.getDictType())) {
        vo.getMessageShowType().add(dictData);
      }
    }
    return vo;
  }

  @Override
  public IPage<MessageInfoVO> findMessageList(PageDTO<Message> page, MessageInfoQueryDTO dto) {
    LambdaQueryChainWrapper<Message> queryChainWrapper = this.lambdaQuery();
    queryChainWrapper
        .in(ObjectUtil.isNull(dto.getType()), Message::getType, 2, 3)
        .eq(ObjectUtil.isNotEmpty(dto.getType()), Message::getType, dto.getType())
        .eq(StringUtils.isNotBlank(dto.getTitle()), Message::getTitle, dto.getTitle())
        .eq(StringUtils.isNotBlank(dto.getContent()), Message::getContent, dto.getContent())
        .eq(StringUtils.isNotBlank(dto.getLanguage()), Message::getLanguage, dto.getLanguage())
        .ge(
            ObjectUtil.isNotEmpty(dto.getBeginTime()),
            Message::getCreateTime,
            dto.getBeginTime() + " " + "00:00:00")
        .le(
            ObjectUtil.isNotEmpty(dto.getEndTime()),
            Message::getCreateTime,
            dto.getEndTime() + " " + "23:59:59");

    if (dto.getStatus() != null) {
      // 消息状态--无效
      if (dto.getStatus() == BooleanEnum.NO.value()) {
        queryChainWrapper.and(
            wrapper ->
                wrapper
                    .eq(Message::getStatus, BooleanEnum.NO.value())
                    .or()
                    .lt(Message::getEndTime, new Date()));
        // 消息状态--有效
      } else if (dto.getStatus() == BooleanEnum.YES.value()) {
        String currentTime = DateUtil.now();
        queryChainWrapper.and(
            wrapper ->
                wrapper
                    .eq(Message::getStatus, BooleanEnum.YES.value())
                    .last("and " + "'" + currentTime + "' " + "between begin_time and end_time"));
      }
    }

    IPage<MessageInfoVO> iPage =
        queryChainWrapper
            .orderByDesc(Message::getCreateTime)
            .page(page)
            .convert(messageInfoConvert::toVo);

    if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
      for (MessageInfoVO messageInfoVO : iPage.getRecords()) {
        // 时间超过了后，消息失效
        if (messageInfoVO.getEndTime() != null
            && new Date().after(messageInfoVO.getEndTime())
            && Objects.equals(messageInfoVO.getStatus(), SwitchStatusEnum.ENABLED.getValue())) {
          messageInfoVO.setStatus(SwitchStatusEnum.DISABLED.getValue());
          Message messageInfo = new Message();
          BeanUtils.copyBeanProp(messageInfo, messageInfoVO);
          this.updateById(messageInfo);
        }
      }
    }
    return iPage;
  }

  @Override
  public void insertMessage(MessageInfoAddDTO dto) {
    if (dto.getShowType() != null && dto.getShowType() == 3){
      if (StringUtils.isEmpty(dto.getPcImage())){
        throw new ServiceException("请上传PC弹窗图片");
      }
      if (StringUtils.isEmpty(dto.getAppImage())){
        throw new ServiceException("请上传APP弹窗图片");
      }
    }
    Message messageInfo = messageInfoConvert.toEntity(dto);
    messageInfo.setStatus(BooleanEnum.YES.value());
    this.save(messageInfo);
  }

  @Override
  public void deleteBatchMessage(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    this.removeByIds(Arrays.asList(ids.split(",")));
  }

  @Override
  public void editMessage(MessageInfoEditDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("id不能为空");
    }

    if (dto.getShowType() == PushMessageEnum.MessageShowType.PIC_POPUP.value()
        && (StringUtils.isBlank(dto.getAppImage()) || StringUtils.isBlank(dto.getPcImage()))) {
      throw new ServiceException("选择消息类型为图片弹窗，web端和移动端图片不能为空");
    }

    MessageInfoAddDTO messageInfoAddDTO = new MessageInfoAddDTO();
    BeanUtils.copyBeanProp(messageInfoAddDTO, dto);
    validMessageInfo(messageInfoAddDTO);

    Message messageInfo = messageInfoConvert.toEntity(dto);
    this.updateById(messageInfo);
  }

  /**
   * 验证传入的数据
   *
   * @param dto MessageInfoAddDTO
   */
  private void validMessageInfo(MessageInfoAddDTO dto) {
    if (dto.getShowType() == PushMessageEnum.MessageShowType.PIC_POPUP.value()
        && (StringUtils.isBlank(dto.getAppImage()) || StringUtils.isBlank(dto.getPcImage()))) {
      throw new ServiceException("选择消息类型为图片弹窗，web端和移动端图片不能为空");
    }
  }

  @Override
  public IPage<MessageDistributeVO> findMessageDistributeList(
      Page<Member> page, MessageDistributeQueryDTO dto) {
    if (ObjectUtil.isNull(dto.getPushRange())) {
      throw new ServiceException("推送范围不能为空");
    }
    MemberQueryDTO memberQueryDTO = new MemberQueryDTO();
    if (ObjectUtil.isNotNull(dto.getUserAccount())) {
      memberQueryDTO.setAccount(dto.getUserAccount());
    }
    if (ObjectUtil.isNotNull(dto.getVipLevel())) {
      memberQueryDTO.setVipLevel(dto.getVipLevel());
    }
    if (ObjectUtil.isNotNull(dto.getRechargeLevel())) {
      memberQueryDTO.setUserLevel(dto.getRechargeLevel());
    }
    // 全部会员
    if (dto.getPushRange().equals(1)) {
      memberQueryDTO.setUserType("M");
      memberQueryDTO.setStatus(1);
      return readStatus(page, memberQueryDTO, dto);
    }
    // 部分会员
    if (dto.getPushRange().equals(2)) {
      String[] linkAccountArray = dto.getLinkAccount().split(",");

      IPage<MessageDistributeVO> messageDistributePage = new PageDTO<>();
      List<MessageDistributeVO> records = new ArrayList<>();

      for (String linkAccount : linkAccountArray) {
        memberQueryDTO.setAccount(linkAccount);
        List<MessageDistributeVO> records1 = readStatus(page, memberQueryDTO, dto).getRecords();
        if (CollectionUtil.isNotEmpty(records1)) {
          MessageDistributeVO messageDistributeVO = records1.get(0);
          records.add(messageDistributeVO);
        }
      }

      long total = (int) records.size();
      long size = page.getSize();
      long pages = total % size == 0 ? total / size : (total / size) + 1;
      messageDistributePage.setCurrent(page.getCurrent());
      messageDistributePage.setSize(size);
      messageDistributePage.setTotal(total);
      messageDistributePage.setPages(pages);
      messageDistributePage.setRecords(records);
      return messageDistributePage;
    }
    // 在线会员
    if (dto.getPushRange().equals(3)) {
      memberQueryDTO.setUserType("M");
      memberQueryDTO.setStatus(1);
      IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, dto);
      List<MessageDistributeVO> collect =
          messageDistributePage.getRecords().stream()
              .filter(MessageDistributeVO::getOnline)
              .collect(Collectors.toList());
      long total = (int) collect.size();
      long size = page.getSize();
      long pages = total % size == 0 ? total / size : (total / size) + 1;
      messageDistributePage.setCurrent(page.getCurrent());
      messageDistributePage.setSize(size);
      messageDistributePage.setTotal(total);
      messageDistributePage.setPages(pages);
      messageDistributePage.setRecords(collect);
      return messageDistributePage;
    }
    // 充值层级
    if (dto.getPushRange().equals(4)) {
      memberQueryDTO.setUserType("M");
      memberQueryDTO.setStatus(1);
      memberQueryDTO.setUserLevel(Integer.parseInt(dto.getLinkAccount()));
      return readStatus(page, memberQueryDTO, dto);
    }
    // VIP等级
    if (dto.getPushRange().equals(5)) {
      memberQueryDTO.setUserType("M");
      memberQueryDTO.setStatus(1);
      memberQueryDTO.setVipLevel(Integer.parseInt(dto.getLinkAccount()));
      return readStatus(page, memberQueryDTO, dto);
    }
    // 代理线
    if (dto.getPushRange().equals(6)) {
      memberQueryDTO.setUserType("M");
      memberQueryDTO.setStatus(1);
      memberQueryDTO.setParentName(dto.getLinkAccount());
      return readStatus(page, memberQueryDTO, dto);
    } else {
      throw new ServiceException("请选择正确的推送范围");
    }
  }

  public IPage<MessageDistributeVO> readStatus(
      Page<Member> page, MemberQueryDTO dto, MessageDistributeQueryDTO queryDTO) {
    // 会员信息
    IPage<MessageDistributeVO> memberPage = memberService.pageMessageDistribute(page, dto);
    List<MessageDistributeVO> records = memberPage.getRecords();
    for (MessageDistributeVO record : records) {
      record.setMessageId(queryDTO.getMessageId());
      MessageDistribute messageDistribute =
          messageDistributeService
              .lambdaQuery()
              .eq(MessageDistribute::getMessageId, queryDTO.getMessageId())
              .eq(MessageDistribute::getUserAccount, record.getUserAccount())
              .one();
      if (ObjectUtil.isNull(messageDistribute)) {
        record.setReadStatus(0);
      } else {
        record.setReadStatus(1);
      }
    }

    memberPage.setRecords(records);
    if (ObjectUtil.isNotNull(queryDTO.getRead())) {
      List<MessageDistributeVO> collect =
          memberPage.getRecords().stream()
              .filter(n -> n.getReadStatus().equals(queryDTO.getRead()))
              .collect(Collectors.toList());

      long total = collect.size();
      long size = page.getSize();
      long pages = total % size == 0 ? total / size : (total / size) + 1;
      memberPage.setCurrent(page.getCurrent());
      memberPage.setSize(size);
      memberPage.setTotal(total);
      memberPage.setPages(pages);
      memberPage.setRecords(collect);
    }
    return memberPage;
  }
}
