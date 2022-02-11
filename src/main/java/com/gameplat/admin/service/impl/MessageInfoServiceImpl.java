package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageInfoConvert;
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
public class MessageInfoServiceImpl extends ServiceImpl<MessageMapper, MessageInfo>
        implements MessageInfoService {

    @Autowired
    private MessageInfoConvert messageInfoConvert;

    @Autowired
    private SysDictDataService sysDictDataService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageDistributeService messageDistributeService;

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
            if ("MESSAGE_MEMBER_RANGE".equals(dictDataByType.getDictType())) {
                userRange.add(dictDataByType);
            }
            if ("MESSAGE_LOCATION".equals(dictDataByType.getDictType())) {
                location.add(dictDataByType);
            }
            if ("MESSAGE_POP_COUNT".equals(dictDataByType.getDictType())) {
                popCount.add(dictDataByType);
            }
            if ("MESSAGE_CATEGORY".equals(dictDataByType.getDictType())) {
                messageCate.add(dictDataByType);
            }
            if ("MESSAGE_SHOW_TYPE".equals(dictDataByType.getDictType())) {
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
        LambdaQueryChainWrapper<MessageInfo> queryChainWrapper = this.lambdaQuery();
        queryChainWrapper
                .in(ObjectUtil.isNull(messageInfoQueryDTO.getType()),
                        MessageInfo::getType,
                        2,3)
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
                        StringUtils.isNotBlank(messageInfoQueryDTO.getLanguage()),
                        MessageInfo::getLanguage,
                        messageInfoQueryDTO.getLanguage())
                .ge(
                        ObjectUtil.isNotEmpty(messageInfoQueryDTO.getBeginTime()),
                        MessageInfo::getBeginTime,
                        messageInfoQueryDTO.getBeginTime() + " " + "00:00:00")
                .le(
                        ObjectUtil.isNotEmpty(messageInfoQueryDTO.getEndTime()),
                        MessageInfo::getEndTime,
                        messageInfoQueryDTO.getEndTime() + " " + "23:59:59");


        if (messageInfoQueryDTO.getStatus() != null) {
            //消息状态--无效
            if (messageInfoQueryDTO.getStatus() == BooleanEnum.NO.value()) {
                queryChainWrapper
                        .and(wrapper -> wrapper.eq(MessageInfo::getStatus, BooleanEnum.NO.value())
                                .or()
                                .lt(MessageInfo::getEndTime, new Date())
                                .in(MessageInfo::getType, 2,3)
                        );
                //消息状态--有效
            } else if (messageInfoQueryDTO.getStatus() == BooleanEnum.YES.value()) {
                Date date = new Date();
                queryChainWrapper
                        .and(wrapper -> wrapper.eq(MessageInfo::getStatus, BooleanEnum.YES.value())
                                .ge(MessageInfo::getBeginTime, date)
                                .le(MessageInfo::getEndTime, date))
                                .in(MessageInfo::getType, 2,3)
                ;
            }
        }

        IPage<MessageInfoVO> iPage = queryChainWrapper.orderByDesc(MessageInfo::getCreateTime)
                .page(page)
                .convert(messageInfoConvert::toVo);

        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            for (MessageInfoVO messageInfoVO : iPage.getRecords()) {
                // 时间超过了后，消息失效
                if (messageInfoVO.getEndTime() != null &&
                        new Date().after(messageInfoVO.getEndTime()) &&
                        Objects.equals(messageInfoVO.getStatus(), SwitchStatusEnum.ENABLED.getValue())
                ) {
                    messageInfoVO.setStatus(SwitchStatusEnum.DISABLED.getValue());
                    MessageInfo messageInfo = new MessageInfo();
                    BeanUtils.copyBeanProp(messageInfo, messageInfoVO);
                    this.updateById(messageInfo);
                }
            }
        }
        return iPage;
    }

    @Override
    public void insertMessage(MessageInfoAddDTO messageInfoAddDTO) {
        MessageInfo messageInfo = messageInfoConvert.toEntity(messageInfoAddDTO);
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
    public IPage<MessageDistributeVO> findMessageDistributeList(Page<Member> page, MessageDistributeQueryDTO messageDistributeQueryDTO) {
        if (ObjectUtil.isNull(messageDistributeQueryDTO.getPushRange())){
            throw new ServiceException("推送范围不能为空");
        }
        MemberQueryDTO memberQueryDTO = new MemberQueryDTO();
        if(ObjectUtil.isNotNull(messageDistributeQueryDTO.getUserAccount())){
            memberQueryDTO.setAccount(messageDistributeQueryDTO.getUserAccount());
        }
        if(ObjectUtil.isNotNull(messageDistributeQueryDTO.getVipLevel())){
            memberQueryDTO.setLevel(messageDistributeQueryDTO.getVipLevel());
        }
        if(ObjectUtil.isNotNull(messageDistributeQueryDTO.getRechargeLevel())){
            memberQueryDTO.setUserLevel(messageDistributeQueryDTO.getRechargeLevel());
        }
        //全部会员
        if (messageDistributeQueryDTO.getPushRange().equals(1)){
            memberQueryDTO.setUserType("M");
            memberQueryDTO.setStatus(1);
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            return messageDistributePage;
        }
        //部分会员
        if (messageDistributeQueryDTO.getPushRange().equals(2)){
            memberQueryDTO.setAccount(messageDistributeQueryDTO.getLinkAccount());
            //设置读取状态
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            return messageDistributePage;
        }
        //在线会员
        if (messageDistributeQueryDTO.getPushRange().equals(3)){
            memberQueryDTO.setUserType("M");
            memberQueryDTO.setStatus(1);
            //设置读取状态
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            List<MessageDistributeVO> collect = messageDistributePage.getRecords().stream().filter(m -> m.getOnline()).collect(Collectors.toList());
            long total = (int)collect.size();
            long size = page.getSize();
            long pages = total % size == 0 ? total/size :(total/size)+1;
            messageDistributePage.setCurrent(page.getCurrent());
            messageDistributePage.setSize(size);
            messageDistributePage.setTotal(total);
            messageDistributePage.setPages(pages);
            messageDistributePage.setRecords(collect);
            return messageDistributePage;
        }
        //充值层级
        if (messageDistributeQueryDTO.getPushRange().equals(4)){
            memberQueryDTO.setUserType("M");
            memberQueryDTO.setStatus(1);
            memberQueryDTO.setUserLevel(messageDistributeQueryDTO.getRechargeLevel());
            //设置读取状态
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            return messageDistributePage;
        }
        //VIP等级
        if (messageDistributeQueryDTO.getPushRange().equals(5)){
            memberQueryDTO.setUserType("M");
            memberQueryDTO.setStatus(1);
            memberQueryDTO.setLevel(messageDistributeQueryDTO.getVipLevel());
            //设置读取状态
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            return messageDistributePage;
        }
        //代理线
        if (messageDistributeQueryDTO.getPushRange().equals(6)){
            memberQueryDTO.setUserType("M");
            memberQueryDTO.setStatus(1);
            memberQueryDTO.setAgentLevel(messageDistributeQueryDTO.getAgentLevel());
            //设置读取状态
            IPage<MessageDistributeVO> messageDistributePage = readStatus(page, memberQueryDTO, messageDistributeQueryDTO);
            return messageDistributePage;
        }else{
            throw new ServiceException("请选择正确的推送范围");
        }
    }

    public IPage<MessageDistributeVO> readStatus(Page<Member> page, MemberQueryDTO dto, MessageDistributeQueryDTO messageDistributeQueryDTO){
        //会员信息
        IPage<MessageDistributeVO> memberPage = memberService.pageMessageDistribute(page, dto);

        List<MessageDistributeVO> records = memberPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setMessageId(messageDistributeQueryDTO.getMessageId());
            MessageDistribute messageDistribute = messageDistributeService.lambdaQuery()
                    .eq(MessageDistribute::getMessageId, messageDistributeQueryDTO.getMessageId())
                    .eq(MessageDistribute::getUserAccount, records.get(i).getUserAccount())
                    .one();
            if(ObjectUtil.isNull(messageDistribute)){
                records.get(i).setReadStatus(0);
            }else{
                records.get(i).setReadStatus(1);
            }
        }
        memberPage.setRecords(records);
        if(ObjectUtil.isNotNull(messageDistributeQueryDTO.getRead())){
            List<MessageDistributeVO> collect = memberPage.getRecords().stream().filter(n -> n.getReadStatus().equals(messageDistributeQueryDTO.getRead())).collect(Collectors.toList());
            long total = (int)collect.size();
            long size = page.getSize();
            long pages = total % size == 0 ? total/size :(total/size)+1;
            memberPage.setCurrent(page.getCurrent());
            memberPage.setSize(size);
            memberPage.setTotal(total);
            memberPage.setPages(pages);
            memberPage.setRecords(collect);
        }
        return memberPage;
    }
}
