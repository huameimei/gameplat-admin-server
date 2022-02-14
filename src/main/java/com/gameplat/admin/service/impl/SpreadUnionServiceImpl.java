package com.gameplat.admin.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.admin.service.SpreadUnionService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 联运设置实现
 */
@Service
@Slf4j
public class SpreadUnionServiceImpl extends ServiceImpl<SpreadUnionMapper, SpreadUnion>
        implements SpreadUnionService {

    @Autowired
    private SpreadUnionConvert spreadUnionConvert;

    @Autowired
    private SpreadLinkInfoService spreadLinkInfoService;


    /**
     * 创建联运设置
     */
    @Override
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

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 获取联盟报表
     * @return
     *  一级  todo 日期   联盟名称  联运类型  代理账号  联运平台  联运收益  充值金额  提现金额  注册人数  更新时间
     */
    @Override
    public List<JSONObject> getUnionReportList() {
        List<SpreadUnion> list = this.list();
        //获取代理线下得信息
        List<Member> agentMember = memberMapper.getAgentMember(list);
        return null;
    }

}
