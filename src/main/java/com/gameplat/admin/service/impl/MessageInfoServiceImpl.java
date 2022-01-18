package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.convert.MessageDistributeConvert;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.SwitchStatusEnum;
import com.gameplat.common.enums.UserTypes;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 个人消息、站内信
 *
 * @author admin
 */
@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageMapper, MessageInfo>
    implements MessageInfoService {

  @Autowired private MessageInfoConvert messageInfoConvert;

  @Autowired private MessageDistributeConvert messageDistributeConvert;

  @Autowired private MessageDistributeService messageDistributeService;

  @Autowired private MemberService memberService;

  @Autowired private OnlineUserService onlineUserService;

  @Autowired private SysDictDataService sysDictDataService;

  // 分页条数
  private int perSize = 1000;

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
    List userRange = new ArrayList();
    List location = new ArrayList();
    List popCount = new ArrayList();
    List messageCate = new ArrayList();
    List messageShowType = new ArrayList();
    for (SysDictData dictDataByType : dictDataByTypes) {
      if (dictDataByType.getDictType().equals("MESSAGE_MEMBER_RANGE")) {
        userRange.add(dictDataByType);
      }
      if (dictDataByType.getDictType().equals("MESSAGE_LOCATION")) {
        location.add(dictDataByType);
      }
      if (dictDataByType.getDictType().equals("MESSAGE_POP_COUNT")) {
        popCount.add(dictDataByType);
      }
      if (dictDataByType.getDictType().equals("MESSAGE_CATEGORY")) {
        messageCate.add(dictDataByType);
      }
      if (dictDataByType.getDictType().equals("MESSAGE_SHOW_TYPE")) {
        messageShowType.add(dictDataByType);
      }
    }
    vo.setUserRange(userRange);
    vo.setLocation(location);
    vo.setPopCount(popCount);
    vo.setMessageCate(messageCate);
    vo.setMessageShowType(messageShowType);
    return vo;
  }

  @Override
  public IPage<MessageInfoVO> findMessageList(
      PageDTO<MessageInfo> page, MessageInfoQueryDTO messageInfoQueryDTO) {
    IPage<MessageInfoVO> iPage =
        this.lambdaQuery()
            .eq(ObjectUtil.isNotEmpty(messageInfoQueryDTO.getType()),
                MessageInfo::getType,
                messageInfoQueryDTO.getType())
            .eq(
                StringUtils.isNotBlank(messageInfoQueryDTO.getTitle()),
                MessageInfo::getTitle,
                messageInfoQueryDTO.getTitle())
            .eq(
                StringUtils.isNotBlank(messageInfoQueryDTO.getContent()),
                MessageInfo::getContent,
                messageInfoQueryDTO.getContent())
            .eq(
                messageInfoQueryDTO.getStatus() != null,
                MessageInfo::getStatus,
                messageInfoQueryDTO.getStatus())
            .eq(
                StringUtils.isNotBlank(messageInfoQueryDTO.getLanguage()),
                MessageInfo::getLanguage,
                messageInfoQueryDTO.getLanguage())
            .ge(
                ObjectUtil.isNotEmpty(messageInfoQueryDTO.getBeginTime()),
                MessageInfo::getBeginTime,
                messageInfoQueryDTO.getBeginTime()+" "+"00:00:00")
            .le(
                ObjectUtil.isNotEmpty(messageInfoQueryDTO.getEndTime()),
                MessageInfo::getEndTime,
                messageInfoQueryDTO.getEndTime()+" "+"23:59:59")
            .page(page)
            .convert(messageInfoConvert::toVo);
    if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
      for (MessageInfoVO messageInfoVO : iPage.getRecords()) {
        // 时间超过了后，消息失效
        if (messageInfoVO.getStatus() == SwitchStatusEnum.ENABLED.getValue()
            && new Date().after(messageInfoVO.getEndTime())) {
          messageInfoVO.setStatus(SwitchStatusEnum.DISABLED.getValue());
        }
      }
    }
    return iPage;
  }

  @Override
  public void insertMessage(MessageInfoAddDTO messageInfoAddDTO) {
    validMessageInfo(messageInfoAddDTO);

    MessageInfo messageInfo = messageInfoConvert.toEntity(messageInfoAddDTO);
    messageInfo.setStatus(BooleanEnum.YES.value());
    this.save(messageInfo);

    List<MessageDistribute> messageList = new ArrayList<>();
    buildMembeDistributeList(messageInfoAddDTO, messageList, messageInfo);

    // 批量保存消息
    messageDistributeService.saveBatch(messageList);
  }

  /**
   * 查找会员信息
   *
   * @param messageInfoAddDTO
   * @param messageList
   */
  private void buildMembeDistributeList(
      MessageInfoAddDTO messageInfoAddDTO,
      List<MessageDistribute> messageList,
      MessageInfo messageInfo) {
    PushMessageEnum.UserRange userRange =
        PushMessageEnum.UserRange.get(messageInfoAddDTO.getPushRange());
    switch (userRange) {
      case ALL_MEMBERS:
        // 1-所有会有,会员登录查询的时候才进行日志消息添加
        Page<Member> page1 = new Page<>();
        page1.setCurrent(1);
        page1.setSize(perSize);
        MemberQueryDTO memberQueryDTO = new MemberQueryDTO();
        IPage<MemberVO> memberVOIPage = memberService.queryPage(page1, memberQueryDTO);
        if (memberVOIPage != null && memberVOIPage.getTotal() > 0) {
          long total = memberVOIPage.getTotal();
          // 总页数
          long pageTotal = total / perSize + 1;
          for (int i = 1; i <= pageTotal; i++) {
            if (i != 1) {
              page1.setCurrent(i);
              memberVOIPage = memberService.queryPage(page1, memberQueryDTO);
            }
            if (CollectionUtils.isNotEmpty(memberVOIPage.getRecords())) {
              for (MemberVO memberVO : memberVOIPage.getRecords()) {
                MemberInfoVO memberInfoVO = new MemberInfoVO();
                BeanUtils.copyBeanProp(memberInfoVO, memberVO);
                buildMessageDistribute(messageInfo, memberInfoVO, messageList);
              }
            }
          }
        }
        break;
      case SOME_MEMBERS:
        // 2-部分会员,获取部分会员账号
        if (StringUtils.isBlank(messageInfoAddDTO.getLinkAccount())) {
          throw new ServiceException("推送范围选择部分会员，会员账号不能为空");
        }
        List<String> accountList = new ArrayList<>();
        if (messageInfoAddDTO.getLinkAccount().contains(",")) {
          accountList.addAll(Arrays.asList(messageInfoAddDTO.getLinkAccount().split(",")));
        } else {
          accountList.add(messageInfoAddDTO.getLinkAccount());
        }
        // 验证会员是否存在
        if (CollectionUtils.isNotEmpty(accountList)) {
          List<MemberInfoVO> memberInfoVOList = new ArrayList<>();
          StringBuilder notExistAccountStr = new StringBuilder();
          for (String account : accountList) {
            MemberInfoVO memberInfo = memberService.getMemberInfo(account);
            if (memberInfo == null) {
              if (StringUtils.isBlank(notExistAccountStr)) {
                notExistAccountStr.append(account);
              } else {
                notExistAccountStr.append(",").append(account);
              }
            } else {
              memberInfoVOList.add(memberInfo);
            }
          }
          if (StringUtils.isNotBlank(notExistAccountStr)) {
            throw new ServiceException("会员帐号(" + notExistAccountStr + ")不存在，请检查");
          }

          // 保存推送会员列表
          for (MemberInfoVO memberInfoVO : memberInfoVOList) {
            buildMessageDistribute(messageInfo, memberInfoVO, messageList);
          }
        } else {
          throw new ServiceException("会员账号不能为空");
        }
        break;
      case ONLINE_MEMBER:
        // 3-在线会员
        onlineUserService
            .getOnlineUsers()
            .forEach(
                user -> {
                  MemberInfoVO memberInfoVO = new MemberInfoVO();
                  BeanUtils.copyBeanProp(memberInfoVO, user);
                  buildMessageDistribute(messageInfo, memberInfoVO, messageList);
                });
        break;
      case USER_LEVEL:
        // 4-会员层级
        if (StringUtils.isBlank(messageInfoAddDTO.getLinkAccount())) {
          throw new ServiceException("充值层级值不能为空");
        }
        List<String> userLevelList = Arrays.asList(messageInfoAddDTO.getLinkAccount().split(","));
        // 查询指定层级的用户
        List<Member> memberList = memberService.getListByUserLevel(userLevelList);
        if (CollectionUtils.isNotEmpty(memberList)) {
          for (Member member : memberList) {
            MemberInfoVO memberInfoVO = new MemberInfoVO();
            BeanUtils.copyBeanProp(memberInfoVO, member);
            buildMessageDistribute(messageInfo, memberInfoVO, messageList);
          }
        }
        break;
      case VIP_LEVEL:
        // 5-VIP等级
        if (StringUtils.isBlank(messageInfoAddDTO.getLinkAccount())) {
          throw new ServiceException("充值层级值不能为空");
        }
        List<String> vipLevelList = Arrays.asList(messageInfoAddDTO.getLinkAccount().split(","));
        // 查询指定层级的用户
        List<MemberVO> memberList1 =
            memberService.queryList(
                new MemberQueryDTO() {
                  {
                    setLevels(vipLevelList);
                  }
                });
        if (CollectionUtils.isNotEmpty(memberList1)) {
          for (MemberVO member : memberList1) {
            MemberInfoVO memberInfoVO = new MemberInfoVO();
            BeanUtils.copyBeanProp(memberInfoVO, member);
            buildMessageDistribute(messageInfo, memberInfoVO, messageList);
          }
        }
        break;
      case AGENT_LINE:
        // 6-代理线
        if (StringUtils.isBlank(messageInfoAddDTO.getLinkAccount())) {
          throw new ServiceException("代理线不能为空");
        }
        String agentAccout = messageInfoAddDTO.getLinkAccount();
        MemberInfoVO agnetMemberInfo = memberService.getMemberInfo(agentAccout);
        if (agnetMemberInfo == null
            || !UserTypes.AGENT.value().equals(agnetMemberInfo.getUserType())) {
          throw new ServiceException("代理帐号不存在");
        }
        // 查询管理的所有代理线的会员信息
        List<Member> memberList2 = memberService.getListByAgentAccount(agentAccout);
        if (CollectionUtils.isNotEmpty(memberList2)) {
          for (Member member : memberList2) {
            String superPath = member.getSuperPath();
            superPath = superPath.substring(0, superPath.length() - 1);
            // 会员收到的消息不包含账号agentAccout
            if (!superPath.endsWith(agentAccout)) {
              MemberInfoVO memberInfoVO = new MemberInfoVO();
              BeanUtils.copyBeanProp(memberInfoVO, member);
              buildMessageDistribute(messageInfo, memberInfoVO, messageList);
            }
          }
        }
        break;
      default:
    }
  }

  /**
   * 组装消息
   *
   * @param messageInfo
   * @param memberInfoVO
   * @param messageList
   */
  private void buildMessageDistribute(
      MessageInfo messageInfo, MemberInfoVO memberInfoVO, List<MessageDistribute> messageList) {
    MessageDistribute messageDistribute = new MessageDistribute();
    messageDistribute.setMessageId(messageInfo.getId());
    messageDistribute.setUserId(memberInfoVO.getId());
    messageDistribute.setUserAccount(memberInfoVO.getAccount());
    messageDistribute.setAgentLevel(memberInfoVO.getSuperPath());
    messageDistribute.setReadStatus(BooleanEnum.NO.value());
    // 会员层级
    messageDistribute.setRechargeLevel(memberInfoVO.getUserLevel());
    messageDistribute.setVipLevel(memberInfoVO.getVipLevel());
    messageDistribute.setAcceptRemoveFlag(BooleanEnum.NO.value());
    messageDistribute.setReadStatus(BooleanEnum.NO.value());
    messageDistribute.setSendRemoveFlag(BooleanEnum.NO.value());

    messageList.add(messageDistribute);
  }

  @Override
  public void deleteBatchMessage(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    this.removeByIds(Arrays.asList(ids.split(",")));
  }

  @Override
  public void editMessage(MessageInfoEditDTO messageInfoEditDTO) {
    if (messageInfoEditDTO.getId() == null || messageInfoEditDTO.getId() == 0) {
      throw new ServiceException("id不能为空");
    }

    if (messageInfoEditDTO.getShowType() == PushMessageEnum.MessageShowType.PIC_POPUP.value()
        && (StringUtils.isBlank(messageInfoEditDTO.getAppImage())
            || StringUtils.isBlank(messageInfoEditDTO.getPcImage()))) {
      throw new ServiceException("选择消息类型为图片弹窗，web端和移动端图片不能为空");
    }

    MessageInfoAddDTO messageInfoAddDTO = new MessageInfoAddDTO();
    BeanUtils.copyBeanProp(messageInfoAddDTO, messageInfoEditDTO);
    validMessageInfo(messageInfoAddDTO);

    MessageInfo messageInfo = messageInfoConvert.toEntity(messageInfoEditDTO);
    this.updateById(messageInfo);

    List<MessageDistribute> messageList = new ArrayList<>();
    buildMembeDistributeList(messageInfoAddDTO, messageList, messageInfo);

    // 删除原来会员分发数据，再插入数据
    QueryWrapper<MessageDistribute> wrapper = new QueryWrapper<>();
    wrapper.eq("message_id", messageInfo.getId());
    messageDistributeService.remove(wrapper);

    // 批量保存消息
    messageDistributeService.saveBatch(messageList);
  }

  /**
   * 验证传入的数据
   *
   * @param messageInfoAddDTO
   */
  private void validMessageInfo(MessageInfoAddDTO messageInfoAddDTO) {
    if (messageInfoAddDTO.getShowType() == PushMessageEnum.MessageShowType.PIC_POPUP.value()
        && (StringUtils.isBlank(messageInfoAddDTO.getAppImage())
            || StringUtils.isBlank(messageInfoAddDTO.getPcImage()))) {
      throw new ServiceException("选择消息类型为图片弹窗，web端和移动端图片不能为空");
    }
  }

  @Override
  public IPage<MessageDistributeVO> findMessageDistributeList(
      PageDTO<MessageDistribute> page, MessageDistributeQueryDTO messageDistributeQueryDTO) {
    return messageDistributeService
        .lambdaQuery()
        .eq(MessageDistribute::getMessageId, messageDistributeQueryDTO.getMessageId())
        .page(page)
        .convert(messageDistributeConvert::toVo);
  }
}
