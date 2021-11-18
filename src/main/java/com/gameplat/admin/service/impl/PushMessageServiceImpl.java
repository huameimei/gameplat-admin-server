package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.PushMessageConvert;
import com.gameplat.admin.mapper.PushMessageMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.PushMessageAddDTO;
import com.gameplat.admin.model.dto.PushMessageDTO;
import com.gameplat.admin.model.dto.PushMessageRemoveDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.PushMessageVO;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.PushMessageService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.DateUtils;
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


    @Override
    public IPage<PushMessageVO> findPushMessageList(PageDTO<PushMessage> page, PushMessageDTO pushMessageDTO) {
        LambdaQueryChainWrapper<PushMessage> queryWrapper = this.lambdaQuery();
        queryWrapper
                //标题
                .like(ObjectUtils.isNotEmpty(pushMessageDTO.getMessageTitle()), PushMessage::getMessageTitle, pushMessageDTO.getMessageTitle())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getUserAccount()), PushMessage::getUserAccount, pushMessageDTO.getUserAccount())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getImmediateFlag()), PushMessage::getImmediateFlag, pushMessageDTO.getImmediateFlag())
                .eq(ObjectUtils.isNotEmpty(pushMessageDTO.getReadStatus()), PushMessage::getReadStatus, pushMessageDTO.getReadStatus());
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
                    pushMessage.setAcceptRemoveFlag(0);
                    pushMessage.setSendRemoveFlag(0);
                    pushMessage.setReadStatus(0);
                    if (pushMessageAddDTO.getImmediateFlag() == null) {
                        pushMessage.setImmediateFlag(0);
                    }
                    pushMessageList.add(pushMessage);
                }
                break;
            case 2://2-所有会有


                break;
            case 3://3-在线会员


                break;
            case 4://4-指定层级
                if (StringUtils.isBlank(pushMessageAddDTO.getUserLevel())) {
                    throw new ServiceException("充值层级不能为空");
                }
                List<String> userLevelList = new ArrayList<>();
                if (pushMessageAddDTO.getUserLevel().contains(",")) {
                    userLevelList.addAll(Arrays.asList(pushMessageAddDTO.getUserLevel().split(",")));
                } else {
                    userLevelList.add(pushMessageAddDTO.getUserLevel());
                }
                //查询指定层级的用户
                List<Member> memberList = memberService.getListByUserLevel(userLevelList);
                if (CollectionUtils.isNotEmpty(memberList)) {
                    for (Member member : memberList) {
                        PushMessage pushMessage = pushMessageConvert.toEntity(pushMessageAddDTO);
                        pushMessage.setUserId(member.getId());
                        pushMessage.setUserAccount(member.getAccount());
                        pushMessage.setAcceptRemoveFlag(0);
                        pushMessage.setSendRemoveFlag(0);
                        pushMessage.setReadStatus(0);
                        if (pushMessageAddDTO.getImmediateFlag() == null) {
                            pushMessage.setImmediateFlag(0);
                        }
                        pushMessageList.add(pushMessage);
                    }
                }
                break;
            case 5://5-代理线
                if (StringUtils.isBlank(pushMessageAddDTO.getAgentAccount())) {
                    throw new ServiceException("代理帐号不能为空");
                }
                List<String> agentAccountList = new ArrayList<>();
                if (pushMessageAddDTO.getAgentAccount().contains(",")) {
                    agentAccountList.addAll(Arrays.asList(pushMessageAddDTO.getAgentAccount().split(",")));
                } else {
                    agentAccountList.add(pushMessageAddDTO.getAgentAccount());
                }

                //通过代理账号查询对应的会员信息


                break;

        }
        //批量保存需要发送的消息
        if (!this.saveBatch(pushMessageList)) {
            throw new ServiceException("添加个人消息失败！");
        }
    }

    @Override
    public void deletePushMessage(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        if (!this.removeById(id)) {
            throw new ServiceException("删除公告信息失败！");
        }
    }

    @Override
    public void deleteBatchPushMessage(Long[] ids) {
        if (ObjectUtils.isNull(ids)) {
            throw new ServiceException("ids不能为空!");
        }
        for (Long id : ids) {
            deletePushMessage(id);
        }
    }

    @Override
    public void deleteByCondition(PushMessageRemoveDTO pushMessageRemoveDTO) {
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
                this.removeById(pushMessage.getId());
            }
        }
    }
}
