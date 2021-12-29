package com.gameplat.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.mapper.SpreadUnionPackageMapper;
import com.gameplat.admin.model.domain.PayAccount;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.domain.SpreadUnionPackage;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.admin.service.SpreadUnionPackageService;
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
public class SpreadUnionPackageServiceImpl extends ServiceImpl<SpreadUnionPackageMapper, SpreadUnionPackage>
        implements SpreadUnionPackageService {

    @Autowired
    private SpreadUnionConvert spreadUnionConvert;

    @Autowired
    private SpreadLinkInfoService spreadLinkInfoService;

    @Autowired
    private SpreadUnionPackageMapper spreadUnionPackageMapper;



    /**
     * 联盟包设置列表
     *  检索条件
     *  代理账号，联盟名称，联运类型
     */
    @Override
    public List<SpreadUnionPackageVO> getUnionPackage(PageDTO<SpreadUnionPackage> page ,SpreadUnionPackageDTO spreadUnionPackageDTO) {
        List<SpreadUnionPackageVO> unionPackage = spreadUnionPackageMapper.getUnionPackage(spreadUnionPackageDTO);
        return unionPackage;
    }

    /**
     * 联盟包设置增加
     */
    @Override
    public void insertUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {
        if (!this.save(spreadUnionConvert.toSpreadUnionPackageDTO(spreadUnionPackageDTO))){
            log.error("增加联盟包设置失败,传入的参数 {}",spreadUnionPackageDTO);
            throw new ServiceException("增加联盟包设置失败");
        }
    }

    /**
     * 联盟包设置修改
     */
    @Override
    public void editUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {
        if (!this.lambdaUpdate()
                .set(spreadUnionPackageDTO.getUnionId() != null, SpreadUnionPackage::getUnionId, spreadUnionPackageDTO.getUnionId())
                .set(spreadUnionPackageDTO.getUnionPackageId() != null, SpreadUnionPackage::getUnionPackageId, spreadUnionPackageDTO.getUnionPackageId())
                .set(spreadUnionPackageDTO.getUnionPackageName() != null, SpreadUnionPackage::getUnionPackageName, spreadUnionPackageDTO.getUnionPackageName())
                .set(spreadUnionPackageDTO.getUnionPlatform() != null, SpreadUnionPackage::getUnionPlatform, spreadUnionPackageDTO.getUnionPlatform())
                .set(spreadUnionPackageDTO.getUnionStatus() != null, SpreadUnionPackage::getUnionStatus, spreadUnionPackageDTO.getUnionStatus())
                .set(spreadUnionPackageDTO.getIosDownloadUrl() != null, SpreadUnionPackage::getIosDownloadUrl, spreadUnionPackageDTO.getIosDownloadUrl())
                .set(spreadUnionPackageDTO.getAppDownloadUrl() != null, SpreadUnionPackage::getAppDownloadUrl, spreadUnionPackageDTO.getAppDownloadUrl())
                .set(spreadUnionPackageDTO.getPromotionDomain() != null, SpreadUnionPackage::getPromotionDomain, spreadUnionPackageDTO.getPromotionDomain())
                .eq(SpreadUnionPackage::getId,spreadUnionPackageDTO.getId())
                .update()){
            log.error("联盟包设置修改失败 ，传入的参数 {}",spreadUnionPackageDTO);
            throw new ServiceException("联盟包设置修改失败，请联系管理员");
        }
    }

    /**
     * 联盟包删除
     * @param id 编号Id
     */
    @Override
    public void removeUnionPackage(List<Long> id) {
        if (!this.removeByIds(id)){
            log.error("联盟包删除失败,传入的id集合是  {} ",id);
            throw  new ServiceException("联盟包设置删除失败，请联系管理员");
        }
    }


    /**
     * 联盟包删除
     * @param unionId 联盟设置编号
     */
    @Override
    @Transactional
    public void removeByUnionId(List<Long> unionId) {
        LambdaQueryWrapper<SpreadUnionPackage> query = Wrappers.lambdaQuery();
        query.in(SpreadUnionPackage::getUnionId,unionId);
        try{
            this.remove(query);
        }catch (Exception e){
            log.error("联盟包删除失败,异常信息 {}",e);
            throw new  ServiceException("删除失败");
        }
    }
}
