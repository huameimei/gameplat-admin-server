package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PushMessageConvert;
import com.gameplat.admin.enums.NoticeEnum;
import com.gameplat.admin.mapper.PushMessageMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.admin.service.PushMessageService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.BeanUtils;
import com.gameplat.common.util.DateUtils;
import com.gameplat.security.context.UserCredential;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kenvin
 */
@Service
public class PushMessageServiceImpl extends ServiceImpl<PushMessageMapper, PushMessage>
        implements PushMessageService {

    @Autowired
    private PushMessageConvert pushMessageConvert;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OnlineUserService onlineUserService;

    @Override
    public IPage<PushMessageVO> findPushMessageList(PageDTO<PushMessage> page, PushMessageDTO pushMessageDTO) {
        LambdaQueryChainWrapper<PushMessage> queryWrapper = this.lambdaQuery();
        queryWrapper
                //标题
                .like(ObjectUtils.isNotEmpty(pushMessageDTO.getMessageTitle()), PushMessage::getMessageTitle, pushMessageDTO.getMessageTitle())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getUserAccount()), PushMessage::getUserAccount, pushMessageDTO.getUserAccount())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getImmediateFlag()), PushMessage::getImmediateFlag, pushMessageDTO.getImmediateFlag())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getReadStatus()), PushMessage::getReadStatus, pushMessageDTO.getReadStatus())
                .ne(PushMessage::getAcceptRemoveFlag, 1)
                .ne(PushMessage::getSendRemoveFlag, 1);
        //推送时间
        addPushTimeCondition(queryWrapper, pushMessageDTO.getBeginDate(), pushMessageDTO.getEndDate());
        return queryWrapper.page(page).convert(pushMessageConvert::toVo);
    }

    /**
     * 添加推送时间解析
     *
     * @param queryWrapper
     * @param beginDate
     * @param endDate
     */
    private void addPushTimeCondition(LambdaQueryChainWrapper<PushMessage> queryWrapper, String beginDate, String endDate) {
        if (StringUtils.isNotBlank(beginDate)
                && StringUtils.isNotBlank(endDate)) {
            queryWrapper.between(PushMessage::getCreateTime,
                    DateUtils.parseDate(beginDate, DateUtils.DATE_PATTERN)
                    , DateUtils.parseDate(endDate, DateUtils.DATE_PATTERN));
        } else if (StringUtils.isNotBlank(beginDate)
                && StringUtils.isBlank(endDate)) {
            queryWrapper.ge(PushMessage::getCreateTime, DateUtils.parseDate(beginDate, DateUtils.DATE_PATTERN));
        } else if (StringUtils.isBlank(beginDate)
                && StringUtils.isNotBlank(endDate)) {
            queryWrapper.lt(PushMessage::getCreateTime, DateUtils.parseDate(endDate, DateUtils.DATE_PATTERN));
        }
    }

    @Override
    public void insertPushMessage(PushMessageAddDTO pushMessageAddDTO) {
        if (pushMessageAddDTO.getUserRange() == null || pushMessageAddDTO.getUserRange() == 0) {
            throw new ServiceException("推送范围不能为空");
        }
        int perSize = 1000;
        List<PushMessage> pushMessageList = new ArrayList<>();
        //根据不同的类型，进行新增消息
        switch (pushMessageAddDTO.getUserRange()) {
            case 1://1-部分会员,获取部分会有账号
                if (StringUtils.isBlank(pushMessageAddDTO.getUserAccount())) {
                    throw new ServiceException("会员帐号不能为空");
                }
                List<String> userAccountList = new ArrayList<>();
                //多个账号查询
                if (pushMessageAddDTO.getUserAccount().contains(",")) {
                    String[] userAccountArr = pushMessageAddDTO.getUserAccount().split(",");
                    userAccountList.addAll(Arrays.asList(userAccountArr));
                } else {
                    userAccountList.add(pushMessageAddDTO.getUserAccount());
                }
                //校验用户账号是否存在，并查询账号id
                for (String userAccount : userAccountList) {
                    MemberInfoVO memberInfo = memberService.getMemberInfo(userAccount);
                    if (memberInfo == null) {
                        throw new ServiceException("会员帐号(" + userAccount + ")不存在，请检查");
                    }
                    PushMessage pushMessage = pushMessageConvert.toEntity(pushMessageAddDTO);
                    pushMessage.setUserId(memberInfo.getId());
                    pushMessage.setUserAccount(userAccount);
                    pushMessage.setAcceptRemoveFlag(NoticeEnum.ACCEPT_REMOVE_FLAG_NO.getValue());
                    pushMessage.setSendRemoveFlag(NoticeEnum.SEND_REMOVE_FLAG_NO.getValue());
                    pushMessage.setReadStatus(NoticeEnum.READ_STATUS_UNREAD.getValue());
                    if (pushMessageAddDTO.getImmediateFlag() == null) {
                        pushMessage.setImmediateFlag(NoticeEnum.IMMEDIATE_FLAG_NO.getValue());
                    }
                    pushMessageList.add(pushMessage);
                }
                break;
            case 2://2-所有会有
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
                                Member member = new Member();
                                BeanUtils.copyBeanProp(member, memberVO);
                                buildPushMessage(pushMessageAddDTO, pushMessageList, member);
                            }
                        }

                    }
                }
                break;
            case 3:
                // 3-在线会员
                onlineUserService
                        .getOnlineUsers()
                        .forEach(user -> buildPushMessage(pushMessageAddDTO, pushMessageList, user));
                break;
            case 4:
                //4-指定层级
                if (ObjectUtils.isNull(pushMessageAddDTO.getUserLevel()) || pushMessageAddDTO.getUserLevel().length < 1) {
                    throw new ServiceException("充值层级不能为空");
                }
                List<String> userLevelList = Arrays.asList(pushMessageAddDTO.getUserLevel());
                //查询指定层级的用户
                List<Member> memberList = memberService.getListByUserLevel(userLevelList);
                if (CollectionUtils.isNotEmpty(memberList)) {
                    for (Member member : memberList) {
                        buildPushMessage(pushMessageAddDTO, pushMessageList, member);
                    }
                }
                break;
            case 5://5-代理线
                if (StringUtils.isBlank(pushMessageAddDTO.getAgentAccount())) {
                    throw new ServiceException("代理帐号不能为空");
                }
                //通过代理账号查询对应的会员信息
                String agentAccout = pushMessageAddDTO.getAgentAccount();
                MemberInfoVO agnetMemberInfo = memberService.getMemberInfo(agentAccout);
                if (agnetMemberInfo == null || !"A".equals(agnetMemberInfo.getUserType())) {
                    throw new ServiceException("代理帐号不存在");
                }
                //查询管理的所有代理线的会员信息
                List<Member> memberList1 = memberService.getListByAgentAccount(agentAccout);
                if (CollectionUtils.isNotEmpty(memberList1)) {
                    for (Member member : memberList1) {
                        String superPath = member.getSuperPath();
                        superPath = superPath.substring(0, superPath.length() - 1);
                        //会员收到的消息不包含账号agentAccout
                        if (!superPath.endsWith(agentAccout)) {
                            buildPushMessage(pushMessageAddDTO, pushMessageList, member);
                        }
                    }
                }
                break;
            default:
        }
        //批量保存需要发送的消息
        if (!this.saveBatch(pushMessageList)) {
            throw new ServiceException("添加个人消息失败！");
        }
    }

    private void buildPushMessage(PushMessageAddDTO pushMessageAddDTO, List<PushMessage> pushMessageList, UserCredential credential) {
        buildPushMessage(pushMessageAddDTO, pushMessageList, credential.getUserId(), credential.getUsername());
    }

    private void buildPushMessage(PushMessageAddDTO pushMessageAddDTO, List<PushMessage> pushMessageList, Member member) {
        buildPushMessage(pushMessageAddDTO, pushMessageList, member.getId(), member.getAccount());
    }

    private void buildPushMessage(PushMessageAddDTO pushMessageAddDTO, List<PushMessage> pushMessageList, Long userId, String username) {
        PushMessage pushMessage = pushMessageConvert.toEntity(pushMessageAddDTO);
        pushMessage.setUserId(userId);
        pushMessage.setUserAccount(username);
        pushMessage.setAcceptRemoveFlag(NoticeEnum.ACCEPT_REMOVE_FLAG_NO.getValue());
        pushMessage.setSendRemoveFlag(NoticeEnum.SEND_REMOVE_FLAG_NO.getValue());
        pushMessage.setReadStatus(NoticeEnum.READ_STATUS_UNREAD.getValue());
        if (pushMessageAddDTO.getImmediateFlag() == null) {
            pushMessage.setImmediateFlag(NoticeEnum.IMMEDIATE_FLAG_NO.getValue());
        }
        pushMessageList.add(pushMessage);
    }

    @Override
    public void deleteBatchPushMessage(String ids) {
        if (ObjectUtils.isNull(ids)) {
            throw new ServiceException("ids不能为空!");
        }
        List<String> idArr = Arrays.asList(com.gameplat.common.util.StringUtils.split(ids, ","));
        for (String id : idArr) {
            PushMessage pushMessage = this.getById(Long.valueOf(id));
            pushMessage.setAcceptRemoveFlag(NoticeEnum.ACCEPT_REMOVE_FLAG_YES.getValue());
            pushMessage.setSendRemoveFlag(NoticeEnum.SEND_REMOVE_FLAG_YES.getValue());
            this.saveOrUpdate(pushMessage);
        }
    }

    @Override
    public void deleteByCondition(PushMessageRemoveDTO pushMessageRemoveDTO) {
        if (pushMessageRemoveDTO.getReadStatus() == null
                && StringUtils.isBlank(pushMessageRemoveDTO.getBeginDate())
                && StringUtils.isBlank(pushMessageRemoveDTO.getEndDate())) {
            throw new ServiceException("按条件删除消息，消息状态和推送时间必须填写");
        }
        //  先查询满足条件的数据
        LambdaQueryChainWrapper<PushMessage> queryWrapper = this.lambdaQuery();
        queryWrapper
                .eq(ObjectUtils.isNotEmpty(pushMessageRemoveDTO.getReadStatus()), PushMessage::getReadStatus, pushMessageRemoveDTO.getReadStatus())
                .like(ObjectUtils.isNotEmpty(pushMessageRemoveDTO.getMessageTitle()), PushMessage::getMessageTitle, pushMessageRemoveDTO.getMessageTitle())
                .like(ObjectUtils.isNotEmpty(pushMessageRemoveDTO.getMessageContent()), PushMessage::getMessageContent, pushMessageRemoveDTO.getMessageContent());

        addPushTimeCondition(queryWrapper, pushMessageRemoveDTO.getBeginDate(), pushMessageRemoveDTO.getEndDate());
        List<PushMessage> pushMessageList = queryWrapper.list();
        //再进行删除操作
        if (CollectionUtils.isNotEmpty(pushMessageList)) {
            for (PushMessage pushMessage : pushMessageList) {
                pushMessage.setAcceptRemoveFlag(NoticeEnum.ACCEPT_REMOVE_FLAG_YES.getValue());
                pushMessage.setSendRemoveFlag(NoticeEnum.SEND_REMOVE_FLAG_YES.getValue());
                this.saveOrUpdate(pushMessage);
            }
        }
    }

    @Override
    public void readMsg(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        PushMessage pushMessage = this.getById(id);
        if (pushMessage == null) {
            throw new ServiceException("该消息不存在!");
        }
        pushMessage.setReadStatus(1);
        this.saveOrUpdate(pushMessage);
    }
}
