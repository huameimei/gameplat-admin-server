package com.gameplat.admin.service.impl;

import static java.util.stream.Collectors.toList;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.constant.MemberServiceKeyConstant;
import com.gameplat.admin.convert.MemberWealConvert;
import com.gameplat.admin.convert.MessageInfoConvert;
import com.gameplat.admin.enums.LanguageEnum;
import com.gameplat.admin.enums.MemberBillTransTypeEnum;
import com.gameplat.admin.enums.PushMessageEnum;
import com.gameplat.admin.mapper.*;
import com.gameplat.admin.model.domain.*;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MemberWealVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.context.GlobalContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.IPUtils;
import com.gameplat.base.common.util.RandomUtil;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.redis.redisson.DistributedLocker;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description 福利发放业务处理
 * @date 2021/11/22
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberWealServiceImpl extends ServiceImpl<MemberWealMapper, MemberWeal> implements MemberWealService {

    @Autowired private MemberWealConvert wealConvert;
    @Autowired private MemberWealMapper mapper;
    @Autowired private MemberGrowthRecordMapper memberGrowthRecordMapper;
    @Autowired private RechargeOrderMapper rechargeOrderMapper;
    @Autowired private MemberDayReportMapper dayReportMapper;
    @Autowired private MemberMapper memberMapper;
    @Autowired private MemberBlackListService blackListService;
    @Autowired private MemberWealDetailService wealDetailService;
    @Autowired private MemberGrowthConfigMapper growthConfigMapper;
    @Autowired private MemberGrowthLevelMapper growthLevelMapper;
    @Autowired private ValidWithdrawService validWithdrawService;
    @Autowired private DistributedLocker distributedLocker;
    @Autowired private MemberWealRewordService wealRewordService;
    @Autowired private RedissonClient redissonClient;
    @Autowired private MessageMapper messageMapper;
    @Autowired private MessageDistributeService messageDistributeService;
    @Autowired private MemberInfoService memberInfoService;
    @Autowired private MemberBillService memberBillService;
    @Autowired private MessageInfoConvert messageInfoConvert;

    /**
     * 获取等级俸禄达标会员
     */
    @Override
    public IPage<MemberWealVO> findMemberWealList(IPage<MemberWeal> page, MemberWealDTO queryDTO) {
        return this.lambdaQuery()
                .like(ObjectUtils.isNotEmpty(queryDTO.getName()), MemberWeal::getName, queryDTO.getName())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MemberWeal::getStatus, queryDTO.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryDTO.getType()), MemberWeal::getType, queryDTO.getType())
                .ge(ObjectUtils.isNotEmpty(queryDTO.getStartDate()), MemberWeal::getCreateTime, queryDTO.getStartDate())
                .le(ObjectUtils.isNotEmpty(queryDTO.getEndDate()), MemberWeal::getCreateTime, queryDTO.getEndDate())
                .orderByDesc(MemberWeal::getCreateTime)
                .page(page)
                .convert(wealConvert::toVo);
    }

    /**
     * 添加
     */
    @Override
    public void addMemberWeal(MemberWealAddDTO dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new ServiceException("名称不能为空！");
        }
        if (dto.getType() == null) {
            throw new ServiceException("类型不能为空！");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new ServiceException("周期不能为空！");
        }
        if (dto.getMinRechargeAmount() == null) {
            throw new ServiceException("最低充值金额不能为空！");
        }
        if (dto.getMinBetAmount() == null) {
            throw new ServiceException("最低有效投注不能为空！");
        }
        dto.setStatus(0);

        MemberWeal memberWeal = wealConvert.toEntity(dto);

        if (!this.save(memberWeal)) {
            throw new ServiceException("新增失败！");
        }
    }

    /**
     * 修改福利
     */
    @Override
    public void updateMemberWeal(MemberWealEditDTO dto) {
        if (ObjectUtils.isEmpty(dto.getId())){
            throw new ServiceException("id不能为空!");
        }
        MemberWeal weal = wealConvert.toEntity(dto);
        if (!this.updateById(weal)){
            throw new ServiceException("编辑失败!");
        }
    }

    /**
     * 删除福利
     */
    @Override
    public void deleteMemberWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        if (!this.removeById(id)){
            throw new ServiceException("删除失败！");
        }
    }

    /**
     * 结算
     */
    @Override
    public void settleWeal(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new ServiceException("id不能为空!");
        }
        MemberWeal memberWeal = mapper.selectById(id);
        if(memberWeal == null){
            throw new ServiceException("该福利不存在!");
        }
        //福利类型
        Integer type = memberWeal.getType();
        //福利状态
        Integer status = memberWeal.getStatus();
        if (status != 0 && status != 1){
            throw new IllegalArgumentException("福利状态异常,不能结算！");
        }
        //获取会员对应等级的福利金额
        List<MemberWealDetail> memberSalaryInfoList = memberGrowthRecordMapper.getMemberSalaryInfo(type);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查找符合充值金额和打码量资格的会员
        List<String> rechargeAccountList = new ArrayList<>();
        if (memberWeal.getMinRechargeAmount() != null && memberWeal.getMinRechargeAmount().compareTo(new BigDecimal("0")) > 0) {
            rechargeAccountList = rechargeOrderMapper.getSatisfyRechargeAccount(
                    String.valueOf(memberWeal.getMinRechargeAmount()),
                    formatter.format(memberWeal.getStartDate()),
                    formatter.format(memberWeal.getEndDate()));
        } else {
            rechargeAccountList = memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
        }

        //获取达到有效投注金额的会员账号
        List<String> betAccountList = new ArrayList<>();
        if(memberWeal.getMinBetAmount() != null && memberWeal.getMinBetAmount().compareTo(new BigDecimal("0")) > 0){
            betAccountList = dayReportMapper.getSatisfyBetAccount(
                    String.valueOf(memberWeal.getMinBetAmount()),
                    formatter.format(memberWeal.getStartDate()),
                    formatter.format(memberWeal.getEndDate())
            );
        } else {
            betAccountList = memberSalaryInfoList.stream().map(MemberWealDetail::getUserName).collect(toList());
        }

        //取达到充值金额的会员账号&&达到有效投注金额的会员账号的交集
        List<String> intersectionList = rechargeAccountList.stream().filter(betAccountList::contains).collect(toList());
        if (type == 2){
            // 生日礼金
            //过滤每个会员的生日是否在周期内
            if (intersectionList != null && intersectionList.size() > 0){
                List<Member> users = memberMapper.findByUserNameList(intersectionList);
                intersectionList = users.stream().filter(item -> item.getBirthday() != null && DateUtil.parse(DateUtil.format(item.getBirthday(),"MM-dd"),"MM-dd").compareTo(DateUtil.parse(DateUtil.format(memberWeal.getStartDate(),"MM-dd"),"MM-dd")) >= 0
                        && DateUtil.parse(DateUtil.format(item.getBirthday(),"MM-dd"),"MM-dd").compareTo(DateUtil.parse(DateUtil.format(memberWeal.getEndDate(),"MM-dd"),"MM-dd")) <= 0).map(item -> item.getNickname())
                        .filter(intersectionList::contains).collect(toList());
            }
        }

        //取享有福利的会员账号，再次跟上面 intersectionList 集合账号取交集
        List<String> finalIntersectionList1 = intersectionList;
        List<MemberWealDetail> finalIntersectionList = new ArrayList<>();
        finalIntersectionList = memberSalaryInfoList.stream()
                .filter(item -> finalIntersectionList1.stream().map(String::toString).collect(Collectors.toList()).contains(item.getUserName())).collect(Collectors.toList());

        List<MemberWealDetail> list = new ArrayList<>();

        if(CollectionUtil.isNotEmpty(finalIntersectionList)) {
            //过滤黑名单
            List<String> blackList = new ArrayList<>();
            List<MemberBlackList> memberBlackList = blackListService.findMemberBlackList(new MemberBlackList());
            if (CollectionUtil.isNotEmpty(memberBlackList)) {
                blackList = memberBlackList.stream().map(memberBlack -> memberBlack.getUserAccount()).collect(Collectors.toList());
            }
            if (CollectionUtil.isNotEmpty(blackList)) {
                for (String userAccount : blackList) {
                    finalIntersectionList = finalIntersectionList.stream().filter(item1 -> !item1.getUserName().equalsIgnoreCase(userAccount)).collect(Collectors.toList());
                }
            }
            //装配福利详情等字段
            int totalUserCount = 0;
            BigDecimal totalPayMoney = new BigDecimal("0");
            for (MemberWealDetail memberWealDetail : finalIntersectionList) {
                MemberWealDetail model = new MemberWealDetail();
                model.setWealId(id);
                model.setUserId(memberWealDetail.getUserId());
                model.setUserName(memberWealDetail.getUserName());
                model.setLevel(memberWealDetail.getLevel());
                model.setRewordAmount(memberWealDetail.getRewordAmount());
                model.setStatus(1);
                model.setCreateBy(GlobalContextHolder.getContext().getUsername());
                list.add(model);
                totalUserCount++;
                totalPayMoney = totalPayMoney.add(memberWealDetail.getRewordAmount());
            }
            memberWeal.setTotalUserCount(totalUserCount);
            memberWeal.setTotalPayMoney(totalPayMoney);
        }
        List<MemberWealDetail> memberWealDetailList = wealDetailService.findSatisfyMember(new MemberWealDetail() {{
            setWealId(id);
        }});

        //修改了福利信息时，要重新结算，所以应该先删除
        wealDetailService.removeWealDetail(id);
        if (list != null && list.size() > 0){
            wealDetailService.batchSave(list);
        }
        memberWeal.setSettleTime(new Date());
        memberWeal.setStatus(1);
        memberWeal.setUpdateBy(GlobalContextHolder.getContext().getUsername());
        memberWeal.setUpdateTime(new Date());
        if (!this.updateById(memberWeal)){
            throw new ServiceException("结算失败!");
        }
    }

    /**
     * 福利派发
     */
    @Override
    public void distributeWeal(Long wealId, HttpServletRequest request) {
        //按福利id去锁 防止重复派发
        String key =  "weal:pay:" + wealId;
        RLock lock = redissonClient.getLock(key);
        if (lock.isLocked()){
            return;
        }
        distributedLocker.lock(key);

        try {
            MemberWealDetail memberWealDetail = new MemberWealDetail();
            memberWealDetail.setWealId(wealId);
            //获取本次福利需要派发的会员
            List<MemberWealDetail> list = wealDetailService.findSatisfyMember(memberWealDetail);
            String serialNumber = IdWorker.getIdStr();
            //根据福利id查询该福利
            MemberWeal memberWeal = mapper.selectById(wealId);
            if(memberWeal == null){
                throw new ServiceException("该福利不存在!");
            }
            if (CollectionUtil.isNotEmpty(list)) {
                //过滤出已派发或已取消的
                list = list.stream().filter(item -> item.getStatus() == 1).collect(toList());
                //0：周俸禄  1：月俸禄  2：生日礼金 3：每月红包
                Integer type = memberWeal.getType();
                //查询成长值配置
                MemberGrowthConfig growthConfig = growthConfigMapper.findOneConfig(LanguageEnum.app_zh_CN.getCode());
                //先计算出升级奖励的金额  可能会连升几级
                Integer limitLevel = growthConfig.getLimitLevel();
                if (limitLevel == null) {
                    limitLevel = 50;
                }
                //查询会员成长等级
                List<MemberGrowthLevel> levels = growthLevelMapper.findList(limitLevel + 1, LanguageEnum.app_zh_CN.getCode());
                //每次7000条执行
                Integer pageSize = 7000;
                //需要派发的会员记录数
                Integer size = list.size();
                log.info("需要派发的会员记录数:" + size);
                Integer part = size / pageSize;
                for (int j = 1; j <= part + 1; j++){
                    Integer fromIndex = (j - 1) * pageSize;
                    Integer toIndex = j * pageSize;
                    //不需要分页
                    if (size < toIndex){
                        toIndex = size;
                    }
                    log.info("当前fromIndex和toIndex：（{}），（{}）",fromIndex,toIndex);
                    List<MemberWealDetail> pageList = list.subList(fromIndex, toIndex);
                    //开始处理
                    if (CollectionUtil.isNotEmpty(pageList)){
                        log.info("福利{},第{}批开始执行,条数:{}",wealId,j,pageList.size());
                        for (MemberWealDetail item : pageList) {
                            try {
                                if (item.getStatus() != 1){
                                    return;
                                }
                                Integer sourceType = 0;
                                Integer level = item.getLevel();
                                if (level >= levels.size()){
                                    level = levels.size()-1;
                                }
                                String content = "";
                                //周俸禄
                                if (type == 0){
                                    sourceType = MemberBillTransTypeEnum.WEEK_WEAL.getCode();
                                    content = "本周俸禄奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                                    //月俸禄
                                } else if(type == 1){
                                    sourceType = MemberBillTransTypeEnum.MONTH_WEAL.getCode();
                                    content = "本月俸禄奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                                    //生日礼金
                                } else if(type == 2){
                                    sourceType = MemberBillTransTypeEnum.BIRTH_WEAL.getCode();
                                    content = "您的生日礼金奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                                    //每月红包
                                } else if(type == 3){
                                    sourceType = MemberBillTransTypeEnum.RED_ENVELOPE_WEAL.getCode();
                                    content = "当月红包奖励已派发 金额:" + item.getRewordAmount() + "，请领取";
                                }
                                //查询会员信息
                                Member member = memberMapper.selectById(item.getUserId());
                                if (BeanUtil.isEmpty(member)){
                                    return;
                                }
                                //福利奖励
                                MemberWealRewordAddDTO memberWealReword = new MemberWealRewordAddDTO();
                                memberWealReword.setUserId(member.getId());
                                memberWealReword.setUserName(member.getAccount());
                                memberWealReword.setOldLevel(item.getLevel());
                                memberWealReword.setCurrentLevel(item.getLevel());
                                memberWealReword.setRewordAmount(item.getRewordAmount());
                                //0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包
                                memberWealReword.setType(type+1);
                                memberWealReword.setSerialNumber(serialNumber);
                                //自动派发
                                if (BooleanEnum.YES.match(growthConfig.getIsAutoPayReword())) {
                                    //将通知消息去掉请领取
                                    content = content.replaceAll("，请领取","");
                                    String sourceId = String.valueOf(IdWorker.getId());
                                    // 新增打码量记录
                                    ValidWithdraw validWithdraw = new ValidWithdraw();
                                    validWithdraw.setAccount(member.getAccount());
                                    validWithdraw.setMemberId(member.getId());
                                    validWithdraw.setRechId(sourceId);
                                    validWithdraw.setRechMoney(item.getRewordAmount().setScale(2, RoundingMode.HALF_UP));
                                    validWithdraw.setCreateTime(new Date());
                                    validWithdraw.setType(0);
                                    validWithdraw.setStatus(0);
                                    validWithdraw.setDiscountMoney(BigDecimal.ZERO);
                                    validWithdraw.setRemark(member.getAccount() + content + "增加打码量");
                                    validWithdrawService.saveValidWithdraw(validWithdraw);
                                    //todo 修改钱包 记录现金流水  插入`已完成`状态的 userGrowthReword 记录
                                    // 给用户增加真币资产
                                    //账户资金锁
                                    String lockKey = MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
                                    try {
                                        // 获取资金锁（等待6秒，租期120秒）
                                        boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 6, 120);
                                        // 6秒获取不到资金锁，进入下一次循环，下一个定时任务，再进行派彩
                                        if (!flag) {
                                            throw new ServiceException("操作失败");
                                        }
                                        //添加流水记录
                                        MemberBill memberBill = new MemberBill();
                                        memberBill.setMemberId(member.getId());
                                        memberBill.setAccount(member.getAccount());
                                        memberBill.setMemberPath(member.getSuperPath());
                                        memberBill.setTranType(sourceType);
                                        memberBill.setOrderNo(sourceId);
                                        memberBill.setAmount(item.getRewordAmount());
                                        memberBill.setBalance(memberInfoService.getById(member.getId()).getBalance());
                                        memberBill.setRemark(content);
                                        memberBill.setContent(content);
                                        memberBill.setOperator("system");
                                        memberBill.setTableIndex(member.getTableIndex());
                                        memberBillService.save(memberBill);

                                        //已完成
                                        memberWealReword.setStatus(2);
                                        //默认当前时间为领取时间  实际上不需要领取
                                        memberWealReword.setDrawTime(DateTime.now());
                                    } catch (Exception e) {
                                        log.error(MessageFormat.format("会员{0}，VIP资金变动, 失败原因：{1}",
                                                member.getAccount(), e));
                                        // 释放资金锁
                                        distributedLocker.unlock(lockKey);
                                    } finally {
                                        //释放资金锁
                                        distributedLocker.unlock(lockKey);
                                    }
                                } else {
                                    //不自动派发则 需要领取
                                    memberWealReword.setStatus(1);
                                }
                                //6.5 通知 发个人消息
                                MessageInfoAddDTO messageInfoAddDTO = new MessageInfoAddDTO();
                                MessageInfo messageInfo = messageInfoConvert.toEntity(messageInfoAddDTO);

                                messageInfo.setTitle("VIP福利奖励");
                                messageInfo.setContent(content);
                                messageInfo.setCategory(PushMessageEnum.MessageCategory.SYS_SEND.getValue());
                                messageInfo.setPosition(PushMessageEnum.MessageCategory.CATE_DEF.getValue());
                                messageInfo.setShowType(PushMessageEnum.MessageShowType.SHOW_DEF.getValue());
                                messageInfo.setPopsCount(PushMessageEnum.PopCount.POP_COUNT_DEF.getValue());
                                messageInfo.setPushRange(PushMessageEnum.UserRange.SOME_MEMBERS.getValue());
                                messageInfo.setLinkAccount(member.getAccount());
                                messageInfo.setType(1);
                                messageInfo.setStatus(1);
                                messageInfo.setCreateBy("System");
                                messageMapper.saveReturnId(messageInfo);

                                MessageDistribute messageDistribute = new MessageDistribute();
                                messageDistribute.setMessageId(messageInfo.getId());
                                messageDistribute.setUserId(member.getId());
                                messageDistribute.setUserAccount(member.getAccount());
                                messageDistribute.setRechargeLevel(member.getUserLevel());
                                messageDistribute.setVipLevel(memberInfoService.getById(member.getId()).getLevel());
                                messageDistribute.setReadStatus(0);
                                messageDistribute.setCreateBy("System");
                                messageDistributeService.save(messageDistribute);
                                //添加奖励记录
                                wealRewordService.insertMemberWealReword(memberWealReword);
                                //修改福利详情状态 为已完成
                                wealDetailService.updateByWealStatus(item.getWealId(),2);
                            } catch (Exception e) {
                                continue;
                            } finally {
                                continue;
                            }
                        }
                        //本批次执行完 睡眠下
//                        try {
//                            log.info("开始分批睡眠执行派发");
//                            Thread.sleep(10000);
//                        } catch (InterruptedException e) {
//                            log.info("睡眠时发生错误了!");
//                            e.printStackTrace();
//                        }
                    }
                }
            }
            log.info("会员派发处理完毕!当前时间:{}", DateTime.now());
            //修改福利表状态
            //不管需不需要领取 福利和详情都应为 已完成 状态
            memberWeal.setStatus(2);
            memberWeal.setPayTime(DateTime.now());
            memberWeal.setSerialNumber(serialNumber);
            if(!this.updateById(memberWeal)){
                throw new ServiceException("操作失败");
            };
            log.info("福利:{}派发完毕!", wealId);
        } catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            distributedLocker.unlock(key);
        }
    }

    /**
     * 福利回收
     */
    @Override
    public void recycleWeal(Long wealId, HttpServletRequest request) {
        //通过流水号去回收福利奖励-->未领取的直接回收掉但不扣钱  已完成的直接扣钱回收
        //根据福利id查询该福利
        MemberWeal memberWeal = mapper.selectById(wealId);
        //福利类型
        Integer type = memberWeal.getType();
        Integer sourceType = type == 0 ? MemberBillTransTypeEnum.WEEK_WEAL_RECYCLE.getCode()
                : type == 1 ? MemberBillTransTypeEnum.MONTH_WEAL_RECYCLE.getCode()
                : type == 2 ? MemberBillTransTypeEnum.BIRTH_WEAL_RECYCLE.getCode()
                : type == 3 ? MemberBillTransTypeEnum.RED_ENVELOPE_WEAL_RECYCLE.getCode()
                : MemberBillTransTypeEnum.WEEK_WEAL_RECYCLE.getCode();
        String remark = type == 0 ? MemberBillTransTypeEnum.WEEK_WEAL_RECYCLE.getDetail()
                : type == 1 ? MemberBillTransTypeEnum.MONTH_WEAL_RECYCLE.getDetail()
                : type == 2 ? MemberBillTransTypeEnum.BIRTH_WEAL_RECYCLE.getDetail()
                : type == 3 ? MemberBillTransTypeEnum.RED_ENVELOPE_WEAL_RECYCLE.getDetail()
                : MemberBillTransTypeEnum.WEEK_WEAL_RECYCLE.getDetail();
        //流水号
        String serialNumber = memberWeal.getSerialNumber();
        List<MemberWealReword> rewordList = wealRewordService.findList(new MemberWealRewordDTO() {{
            setSerialNumber(serialNumber);
        }});
        //登录ip
        String ipAddress = IPUtils.getIpAddress(request);
        if (CollectionUtil.isNotEmpty(rewordList)) {
            //过滤已派发成功状态的数据
            Integer pageSize = 7000;//每次7000条执行
            Integer size = rewordList.size();
            log.info("需要回收的会员记录数:" + size);
            Integer part = size / pageSize;
            for (int j = 1; j <= part + 1; j++) {
                Integer fromIndex = (j - 1) * pageSize;
                Integer toIndex = j * pageSize;
                if (size < toIndex) {//不需要分页
                    toIndex = size;
                }
                log.info("当前fromIndex和toIndex：（{}），（{}）", fromIndex, toIndex);
                List<MemberWealReword> pageList = rewordList.subList(fromIndex, toIndex);
                //开始处理
                if (CollectionUtil.isNotEmpty(pageList)) {
                    log.info("福利回收{},第{}批开始执行,条数:{}", wealId, j, pageList.size());
                    for (MemberWealReword reword : pageList) {
                        try {
                            if (reword.getStatus() == 2) {//已完成
                                //查询会员信息
                                Member member = memberMapper.selectById(reword.getUserId());
                                //派发金额
                                BigDecimal negate = reword.getRewordAmount().negate();
                                // 账户资金锁
                                String lockKey = MessageFormat.format(MemberServiceKeyConstant.MEMBER_FINANCIAL_LOCK, member.getAccount());
                                try {
                                    // 获取资金锁（等待8秒，租期120秒）
                                    boolean flag = distributedLocker.tryLock(lockKey, TimeUnit.SECONDS, 8, 120);
                                    if (!flag) {
                                        return;
                                    }
                                    //添加流水记录
                                    MemberBill memberBill = new MemberBill();
                                    memberBill.setMemberId(member.getId());
                                    memberBill.setAccount(member.getAccount());
                                    memberBill.setMemberPath(member.getSuperPath());
                                    memberBill.setTranType(sourceType);
                                    memberBill.setOrderNo(RandomUtil.generateNumber(22));
                                    memberBill.setAmount(negate.negate());
                                    memberBill.setBalance(memberInfoService.getById(member.getId()).getBalance());
                                    memberBill.setRemark(remark);
                                    memberBill.setContent(remark);
                                    memberBill.setOperator("system");
                                    memberBill.setTableIndex(member.getTableIndex());
                                    memberBillService.save(memberBill);
                                } catch (Exception e) {
                                    log.error(MessageFormat.format("会员：{0}，VIP资金变动, 失败原因：{2}", member.getAccount(), e));
                                    // 释放资金锁
                                    distributedLocker.unlock(lockKey);
                                } finally {
                                    //释放资金锁
                                    distributedLocker.unlock(lockKey);
                                }
                            }
                            //已失效状态
                            reword.setStatus(3);
                            //失效时间
                            reword.setInvalidTime(DateTime.now());
                            //修改福利奖励
                            wealRewordService.updateWealRecord(reword);
                        } catch (Exception e) {
                            continue;
                        } finally {
                            continue;
                        }
                    }
                    //本批次执行完 睡眠下
//                    try {
//                        log.info("开始分批睡眠执行派发");
//                        //睡眠5s
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        log.info("睡眠时发生错误了!");
//                        e.printStackTrace();
//                    }
                }
            }
        }
        log.info("福利回收处理完毕!当前时间:{}", DateTime.now());

        //修改福利详情(状态)
        wealDetailService.updateByWealStatus(memberWeal.getId(), 3);

        //修改福利发放
        memberWeal.setStatus(3);
        if(!this.updateById(memberWeal)){
            throw new ServiceException("操作失败");
        };
    }


}
