package com.gameplat.admin.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.GameMemberReportMapper;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 联运设置实现
 */
@Service
@Slf4j
public class SpreadUnionServiceImpl extends ServiceImpl<SpreadUnionMapper, SpreadUnion>
        implements SpreadUnionService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberBackupService memberBackupService;

    @Autowired
    private SpreadUnionConvert spreadUnionConvert;

    @Autowired
    private SpreadLinkInfoService spreadLinkInfoService;

    @Autowired
    private GameMemberReportMapper reportMapper;


    /**
     * 创建联运设置
     */
    @Override
    @SentinelResource(value = "creatUnion")
    public void creatUnion(SpreadUnionDTO spreadUnionDTO) {
        List<SpreadLinkInfo> spreadList = spreadLinkInfoService.getSpreadList(spreadUnionDTO.getAgentAccount());
        if (spreadList.size() == 0){
            throw new ServiceException("未获取到您需要绑定的代理信息");
        }
        if(!this.save(spreadUnionConvert.toSpreadUnionDTO(spreadUnionDTO))) {
            log.error("新建联运设置参数信息  spreadUnionDTO：{}",spreadUnionDTO);
            throw new ServiceException("插入失败,请联系管理员");
        }
    }

    /**
     * 获取联运列表
     * 检索条件
     *  联盟名称，代理账号，渠道类型
     */
    @Override
    @SentinelResource(value = "getUnion")
    public IPage<SpreadUnionVO> getUnion(PageDTO<SpreadUnion> page ,SpreadUnionDTO spreadUnionDTO) {
        IPage<SpreadUnionVO> convert = this.lambdaQuery()
                .eq(spreadUnionDTO.getUnionName() != null, SpreadUnion::getUnionName, spreadUnionDTO.getUnionName())
                .eq(spreadUnionDTO.getAgentAccount() != null, SpreadUnion::getAgentAccount, spreadUnionDTO.getAgentAccount())
                .eq(spreadUnionDTO.getChannel() != null, SpreadUnion::getChannel, spreadUnionDTO.getChannel())
                .orderByDesc(SpreadUnion::getId).page(page).convert(spreadUnionConvert::toSpreadUnionVO);
        return convert;
    }

    /**
     * 修改联盟设置
     */
    @Override
    @SentinelResource(value = "editUnion")
    public void editUnion(SpreadUnionDTO spreadUnionDTO) {
        List<SpreadLinkInfo> spreadList = spreadLinkInfoService.getSpreadList(spreadUnionDTO.getAgentAccount());
        if (spreadList.size() == 0){
            log.info("查询代理相关信息 参数 {}，返回结果：{}",spreadUnionDTO.getAgentAccount() , spreadList);
            throw new ServiceException("未获取到您需要绑定的代理信息");
        }

        if(!this.lambdaUpdate()
                .set(SpreadUnion::getUnionName,spreadUnionDTO.getUnionName())
                .set(SpreadUnion::getAgentAccount,spreadUnionDTO.getAgentAccount())
                .set(SpreadUnion::getChannel,spreadUnionDTO.getChannel())
                .eq(SpreadUnion::getId,spreadUnionDTO.getId()).update()){
            log.error("修改联盟设置失败,传入的参数  spreadUnionDTO：{}",spreadUnionDTO);
            throw new ServiceException("修改失败,请联系管理员");
        }
    }

    /**
     * 删除联盟设置
     */
    @Override
    @Transactional
    public void removeUnion(List<Long> id) {
        if (!this.removeByIds(id)){
            throw new ServiceException("删除联盟设置异常");
        }
    }


    /**
     * 获取联盟报表
     * @return
     *  一级  todo 日期   联盟名称  联运类型  代理账号  联运平台  联运收益  充值金额  提现金额  注册人数  更新时间
     */
    @Override
    public Object getUnionReportList(SpreadUnionDTO dto) {
        if (StringUtils.isEmpty(dto.getStartTime()) || StringUtils.isEmpty(dto.getStartTime())){
            throw new RuntimeException("请确认需要查询的时间段范围");
        }

        List<SpreadUnionVO> list = this.lambdaQuery().eq(SpreadUnion::getAgentAccount,dto.getAgentAccount()).like(SpreadUnion::getUnionName,dto.getUnionName()).list().stream().map(spreadUnionConvert :: toSpreadUnionVO).collect(Collectors.toList());;
//
//        //会员表。获取代理线下的所有会员
//        List<Member> agentMember = memberMapper.getAgentMember(list,dto.getStartTime(), dto.getEndTime());
//        //会员记录表，获取在相关时间段内有进行过转代理等操作的相关代理线下的会员
//        List<String> content = memberBackupService.getContent(null, dto.getStartTime(), dto.getEndTime());
//        //取出满足条件的会员
//        List<String> accountAgent = new ArrayList<>();
//        agentMember.forEach(x->{
//            String agentPat = "/"+x.getAccount()+"[\\s\\S]*";
//            content.forEach(y->{
//                JSONObject yJson = JSONObject.parseObject(y);
//                String superPath = yJson.getString("superPath");
//                //利用正则表达式匹配 获取满足条件的会员
//                if (Pattern.matches(agentPat, superPath)){
//                    accountAgent.add(x.getAccount());
//                }
//            });
//        });
//        List<String> memberAccount = new ArrayList<>();
//        agentMember.forEach(y->{
//            memberAccount.add(y.getAccount());
//        });
//        List<String> accountList = new ArrayList<>();
//        if (accountAgent.size() >0) {
//            memberAccount.removeAll(accountAgent);
//            accountList.addAll(accountAgent);
//        }
//        accountList.addAll(memberAccount);
        List<JSONObject> spreadReport = reportMapper.getSpreadReport(list, dto.getStartTime(), dto.getEndTime());
        for (SpreadUnionVO x : list) {
            String agentPat = "/" + x.getAgentAccount() + "[\\s\\S]*";
            BigDecimal reAmount = new BigDecimal("0");
            BigDecimal wiAmount = new BigDecimal("0");
            BigDecimal reIncome = new BigDecimal("0");
            int i = 0;
            for (JSONObject y : spreadReport) {
                if (Pattern.matches(agentPat, y.getString("agentPath"))) {
                    BigDecimal rechargeAmount = y.getBigDecimal("rechargeAmount");
                    BigDecimal withdrawAmount = y.getBigDecimal("withdrawAmount");
                    BigDecimal income = y.getBigDecimal("income");
                    reAmount = reAmount.add(rechargeAmount);
                    wiAmount = wiAmount.add(withdrawAmount);
                    reIncome = reIncome.add(income);
                    i ++;
                }
            }
            x.setRechargeAmount(reAmount);
            x.setWithdrawAmount(wiAmount);
            x.setIncome(reIncome);
            x.setCount(i);
        }

        //统计总数
        return list;

    }

    /**
     * 联运详情
     * @return
     */
    public List<JSONObject> unionReportInfo(String account,String startTime,String endTime){
       return reportMapper.getSpreadReportInfo(account, startTime, endTime);
    }

    public static void main(String[] args) {
        BigDecimal reAmount = new BigDecimal("0");
        JSONObject jsonObject = new JSONObject(){{
            put("r",10);
        }};
         BigDecimal r = reAmount.add(jsonObject.getBigDecimal("r"));
        System.out.println(r);
    }

}
