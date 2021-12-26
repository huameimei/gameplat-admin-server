package com.gameplat.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadUnionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 联运设置实现
 */
@Service
public class SpreadUnionServiceImpl extends ServiceImpl<SpreadUnionMapper, SpreadUnion>
        implements SpreadUnionService {

    @Autowired
    private SpreadUnionConvert spreadUnionConvert;


    /**
     * 创建联运设置
     */
    @Override
    public void creatUnion(SpreadUnionDTO spreadUnionDTO) {
        this.save(spreadUnionConvert.toSpreadUnionDTO(spreadUnionDTO));
    }

    /**
     * 获取联运列表
     * 检索条件
     *  联盟名称，代理账号，渠道类型
     */
    @Override
    public List<SpreadUnion> getUnion(SpreadUnionDTO spreadUnionDTO) {
        List<SpreadUnion> list = this.lambdaQuery()
                .eq(spreadUnionDTO.getUnionName() != null, SpreadUnion::getUnionName, spreadUnionDTO.getUnionName())
                .eq(spreadUnionDTO.getAgentAccount() != null,SpreadUnion::getAgentAccount,spreadUnionDTO.getAgentAccount())
                .eq(spreadUnionDTO.getChannel() != null, SpreadUnion::getChannel, spreadUnionDTO.getChannel()).list();
        return list;
    }

    /**
     * 修改联盟设置
     */
    @Override
    public void editUnion(SpreadUnionDTO spreadUnionDTO) {
        this.lambdaUpdate()
                .eq(SpreadUnion::getId,spreadUnionDTO)
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
//        this.lambdaUpdate().eq(SpreadUnion::getUnionName,spreadUnionPackageDTO.getUnionName())
//                .set(spreadUnionPackageDTO.getUnionPackageId() != null,SpreadUnion::getUnionPackageId,spreadUnionPackageDTO.getUnionPackageId())
//                .set(spreadUnionPackageDTO)
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

    }
}
