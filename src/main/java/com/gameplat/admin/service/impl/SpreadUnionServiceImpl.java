package com.gameplat.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.domain.MemberBill;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.admin.service.SpreadUnionService;
import com.gameplat.base.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 联运设置实现
 */
@Service
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
            throw new ServiceException("未获取道相关设置的代理，请先进行代理设置");
        }
        if(!this.save(spreadUnionConvert.toSpreadUnionDTO(spreadUnionDTO))) {
            throw new ServiceException("联盟名称重复");
        }
    }

    /**
     * 获取联运列表
     * 检索条件
     *  联盟名称，代理账号，渠道类型
     */
    @Override
    public IPage<SpreadUnionVO> getUnion(PageDTO<SpreadUnion> page ,SpreadUnionDTO spreadUnionDTO) {
        LambdaQueryChainWrapper<SpreadUnion> queryChainWrapper = this.lambdaQuery();
        IPage<SpreadUnionVO> convert = queryChainWrapper
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
        this.lambdaUpdate()
                .eq(SpreadUnion::getId,spreadUnionDTO.getId())
                .set(SpreadUnion::getUnionName,spreadUnionDTO.getUnionName())
                .set(SpreadUnion::getAgentAccount,spreadUnionDTO.getAgentAccount())
                .set(SpreadUnion::getChannel,spreadUnionDTO.getChannel());
    }

    /**
     * 删除联盟设置
     */
    @Override
    public void removeUnion(List<Long> id) {
        this.removeByIds(id);
    }

    /**
     * 联盟包设置列表
     *  检索条件
     *  代理账号，联盟名称，联运类型
     */
    @Override
    public List<SpreadUnion> getUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {
        List<SpreadUnion> list = this.lambdaQuery()
                .eq(spreadUnionPackageDTO.getAgentAccount() != null, SpreadUnion::getAgentAccount, spreadUnionPackageDTO.getAgentAccount())
                .eq(spreadUnionPackageDTO.getUnionName() != null, SpreadUnion::getUnionName, spreadUnionPackageDTO.getUnionName())
                .eq(spreadUnionPackageDTO.getChannel() != null, SpreadUnion::getChannel, spreadUnionPackageDTO.getChannel()).list();
        return list;
    }

    /**
     * 联盟包设置增加
     */
    @Override
    public void insertUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {
        this.lambdaUpdate().eq(SpreadUnion::getUnionName,spreadUnionPackageDTO.getUnionName())
                .set(spreadUnionPackageDTO.getUnionPackageId() != null, SpreadUnion::getUnionPackageId, spreadUnionPackageDTO.getUnionPackageId())
                .set(spreadUnionPackageDTO.getUnionPackageName() != null, SpreadUnion::getUnionPackageName, spreadUnionPackageDTO.getUnionPackageName())
                .set(spreadUnionPackageDTO.getUnionPlatform() != null, SpreadUnion::getUnionPlatform, spreadUnionPackageDTO.getUnionPlatform())
                .set(spreadUnionPackageDTO.getPromotionDomain() != null, SpreadUnion::getPromotionDomain, spreadUnionPackageDTO.getPromotionDomain())
                .set(spreadUnionPackageDTO.getUnionStatus() != null, SpreadUnion::getUnionStatus , spreadUnionPackageDTO.getUnionStatus())
                .set(spreadUnionPackageDTO.getIosDownloadUrl() != null, SpreadUnion::getIosDownloadUrl , spreadUnionPackageDTO.getIosDownloadUrl())
                .set(spreadUnionPackageDTO.getAppDownloadUrl() != null ,SpreadUnion::getAppDownloadUrl ,spreadUnionPackageDTO.getAppDownloadUrl());
    }

    /**
     * 联盟包设置修改
     */
    @Override
    public void editUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {

    }

    /**
     * 联盟包删除
     * @param id 编号Id
     */
    @Override
    public void removeUnionPackage(List<Long> id) {
        this.removeByIds(id);
    }
}
