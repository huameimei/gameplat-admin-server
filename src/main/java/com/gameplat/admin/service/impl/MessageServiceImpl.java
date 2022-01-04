package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageConvert;
import com.gameplat.admin.convert.MessageDistributeConvert;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.MessageMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.Message;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MessageDistributeService;
import com.gameplat.admin.service.MessageService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.BeanUtils;
import com.gameplat.base.common.util.StringUtils;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageConvert messageConvert;

    @Autowired
    private MessageDistributeConvert messageDistributeConvert;

    @Autowired
    private MessageDistributeService messageDistributeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OnlineUserService onlineUserService;

    //分页条数
    private int perSize = 1000;

    @Override
    public IPage<MessageVO> findMessageList(PageDTO<Message> page, MessageQueryDTO messageQueryDTO) {
        IPage<MessageVO> iPage = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(messageQueryDTO.getMessageTitle()), Message::getMessageTitle, messageQueryDTO.getMessageTitle())
                .eq(StringUtils.isNotBlank(messageQueryDTO.getMessageContent()), Message::getMessageContent, messageQueryDTO.getMessageContent())
                .eq(messageQueryDTO.getStatus() != null, Message::getStatus, messageQueryDTO.getStatus())
                .eq(StringUtils.isNotBlank(messageQueryDTO.getLanguage()), Message::getLanguage, messageQueryDTO.getLanguage())
                .page(page).convert(messageConvert::toVo);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (MessageVO messageVO : iPage.getRecords()) {
                //时间超过了后，消息失效
                if (messageVO.getStatus() == 1 && new Date().after(messageVO.getEndTime())) {
                    messageVO.setStatus(0);
                }
            }
        }
        return iPage;
    }

    @Override
    public void insertMessage(MessageAddDTO messageAddDTO) {
        validMessageInfo(messageAddDTO);

        Message message = messageConvert.toEntity(messageAddDTO);
        message.setStatus(1);
        this.save(message);

        List<MessageDistribute> messageList = new ArrayList<>();
        buildMembeDistributeList(messageAddDTO, messageList, message);

        //批量保存消息
        messageDistributeService.saveBatch(messageList);
    }

    /**
     * 查找会员信息
     *
     * @param messageAddDTO
     * @param messageList
     */
    private void buildMembeDistributeList(MessageAddDTO messageAddDTO, List<MessageDistribute> messageList, Message message) {
        PushMessageEnum.UserRange userRange = PushMessageEnum.UserRange.get(messageAddDTO.getPushRange());
        switch (userRange) {
            case ALL_MEMBERS:
                //1-所有会有,会员登录查询的时候才进行日志消息添加
                Page<Member> page1 = new Page<>();
                page1.setCurrent(1);
                page1.setSize(perSize);
                MemberQueryDTO memberQueryDTO = new MemberQueryDTO();
                IPage<MemberVO> memberVOIPage = memberService.queryPage(page1, memberQueryDTO);
                if (memberVOIPage != null && memberVOIPage.getTotal() > 0) {
                    long total = memberVOIPage.getTotal();
                    //总页数
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
                                buildMessageDistribute(message, memberInfoVO, messageList);
                            }
                        }
                    }
                }
                break;
            case SOME_MEMBERS:
                //2-部分会员,获取部分会有账号
                if (StringUtils.isBlank(messageAddDTO.getPushTarget())) {
                    throw new ServiceException("推送范围选择部分会员，会员账号不能为空");
                }
                List<String> accountList = new ArrayList<>();
                if (messageAddDTO.getPushTarget().contains(",")) {
                    accountList.addAll(Arrays.asList(messageAddDTO.getPushTarget().split(",")));
                } else {
                    accountList.add(messageAddDTO.getPushTarget());
                }
                //验证会员是否存在
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

                    //保存推送会员列表
                    for (MemberInfoVO memberInfoVO : memberInfoVOList) {
                        buildMessageDistribute(message, memberInfoVO, messageList);
                    }
                } else {
                    throw new ServiceException("会员账号不能为空");
                }
                break;
            case ONLINE_MEMBER:
                //3-在线会员
                onlineUserService
                        .getOnlineUsers()
                        .forEach(user -> {
                            MemberInfoVO memberInfoVO = new MemberInfoVO();
                            BeanUtils.copyBeanProp(memberInfoVO, user);
                            buildMessageDistribute(message, memberInfoVO, messageList);
                        });
                break;
            case USER_LEVEL:
                //4-会员层级
                if (StringUtils.isBlank(messageAddDTO.getPushTarget())) {
                    throw new ServiceException("充值层级值不能为空");
                }
                List<String> userLevelList = Arrays.asList(messageAddDTO.getPushTarget().split(","));
                //查询指定层级的用户
                List<Member> memberList = memberService.getListByUserLevel(userLevelList);
                if (CollectionUtils.isNotEmpty(memberList)) {
                    for (Member member : memberList) {
                        MemberInfoVO memberInfoVO = new MemberInfoVO();
                        BeanUtils.copyBeanProp(memberInfoVO, member);
                        buildMessageDistribute(message, memberInfoVO, messageList);
                    }
                }
                break;
            case VIP_LEVEL:
                //5-VIP等级
                if (StringUtils.isBlank(messageAddDTO.getPushTarget())) {
                    throw new ServiceException("充值层级值不能为空");
                }
                List<String> vipLevelList = Arrays.asList(messageAddDTO.getPushTarget().split(","));
                //查询指定层级的用户
                List<Member> memberList1 = memberService.getListByVipLevel(vipLevelList);
                if (CollectionUtils.isNotEmpty(memberList1)) {
                    for (Member member : memberList1) {
                        MemberInfoVO memberInfoVO = new MemberInfoVO();
                        BeanUtils.copyBeanProp(memberInfoVO, member);
                        buildMessageDistribute(message, memberInfoVO, messageList);
                    }
                }
                break;
            case AGENT_LINE:
                //6-代理线
                if (StringUtils.isBlank(messageAddDTO.getPushTarget())) {
                    throw new ServiceException("代理线不能为空");
                }
                String agentAccout = messageAddDTO.getPushTarget();
                MemberInfoVO agnetMemberInfo = memberService.getMemberInfo(agentAccout);
                if (agnetMemberInfo == null || !"A".equals(agnetMemberInfo.getUserType())) {
                    throw new ServiceException("代理帐号不存在");
                }
                //查询管理的所有代理线的会员信息
                List<Member> memberList2 = memberService.getListByAgentAccount(agentAccout);
                if (CollectionUtils.isNotEmpty(memberList2)) {
                    for (Member member : memberList2) {
                        String superPath = member.getSuperPath();
                        superPath = superPath.substring(0, superPath.length() - 1);
                        //会员收到的消息不包含账号agentAccout
                        if (!superPath.endsWith(agentAccout)) {
                            MemberInfoVO memberInfoVO = new MemberInfoVO();
                            BeanUtils.copyBeanProp(memberInfoVO, member);
                            buildMessageDistribute(message, memberInfoVO, messageList);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 组装消息
     *
     * @param message
     * @param memberInfoVO
     * @param messageList
     */
    private void buildMessageDistribute(Message message, MemberInfoVO memberInfoVO, List<MessageDistribute> messageList) {
        MessageDistribute messageDistribute = new MessageDistribute();
        messageDistribute.setMessageId(message.getId());
        messageDistribute.setUserId(memberInfoVO.getId());
        messageDistribute.setUserAccount(memberInfoVO.getAccount());
        messageDistribute.setAgentLevel(memberInfoVO.getSuperPath());
        messageDistribute.setReadStatus(0);
        //会员层级
        messageDistribute.setRechargeLevel(memberInfoVO.getUserLevel());
        messageDistribute.setVipLevel(memberInfoVO.getLevel());
        messageDistribute.setAcceptRemoveFlag(0);
        messageDistribute.setReadStatus(0);
        messageDistribute.setSendRemoveFlag(0);

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
    public void editMessage(MessageEditDTO messageEditDTO) {
        if (messageEditDTO.getId() == null || messageEditDTO.getId() == 0) {
            throw new ServiceException("id不能为空");
        }

        MessageAddDTO messageAddDTO = new MessageAddDTO();
        BeanUtils.copyBeanProp(messageAddDTO, messageEditDTO);
        validMessageInfo(messageAddDTO);

        Message message = messageConvert.toEntity(messageEditDTO);
        this.updateById(message);

        List<MessageDistribute> messageList = new ArrayList<>();
        buildMembeDistributeList(messageAddDTO, messageList, message);

        //删除原来会员分发数据，再插入数据
        QueryWrapper<MessageDistribute> wrapper = new QueryWrapper<>();
        wrapper.eq("message_id", message.getId());
        messageDistributeService.remove(wrapper);

        //批量保存消息
        messageDistributeService.saveBatch(messageList);
    }

    /**
     * 验证传入的数据
     *
     * @param messageAddDTO
     */
    private void validMessageInfo(MessageAddDTO messageAddDTO) {
        if (StringUtils.isBlank(messageAddDTO.getLanguage())) {
            throw new ServiceException("语言不能为空");
        }
        if (messageAddDTO.getPushRange() == null || messageAddDTO.getPushRange() == 0) {
            throw new ServiceException("推送范围不能为空");
        }
        if (messageAddDTO.getMessageType() == null || messageAddDTO.getMessageType() == 0) {
            throw new ServiceException("消息类型不能为空");
        }
        if (StringUtils.isBlank(messageAddDTO.getPopupsType())) {
            throw new ServiceException("弹窗类型不能为空");
        }
        if (messageAddDTO.getPopupsFrequency() == null || messageAddDTO.getPopupsFrequency() == 0) {
            throw new ServiceException("弹出次数不能为空");
        }
        if (messageAddDTO.getBeginTime() == null || messageAddDTO.getEndTime() == null) {
            throw new ServiceException("时间范围不能为空");
        }
        if (StringUtils.isBlank(messageAddDTO.getMessageTitle())) {
            throw new ServiceException("消息标题不能为空");
        }
        if (StringUtils.isBlank(messageAddDTO.getMessageContent())) {
            throw new ServiceException("消息内容不能为空");
        }

        if (messageAddDTO.getMessageType() == 2
                && (StringUtils.isBlank(messageAddDTO.getAppImage())
                || StringUtils.isBlank(messageAddDTO.getPcImage()))) {
            throw new ServiceException("选择消息类型为图片弹窗，web端和移动端图片不能为空");
        }
    }

    @Override
    public IPage<MessageDistributeVO> findMessageDistributeList(PageDTO<MessageDistribute> page, MessageDistributeQueryDTO messageDistributeQueryDTO) {
        return messageDistributeService.lambdaQuery()
                .eq(MessageDistribute::getMessageId, messageDistributeQueryDTO.getMessageId())
                .page(page).convert(messageDistributeConvert::toVo);
    }
}
