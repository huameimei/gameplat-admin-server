package com.gameplat.admin.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadUnionConvert;
import com.gameplat.admin.mapper.SpreadUnionMapper;
import com.gameplat.admin.mapper.SpreadUnionPackageMapper;
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
        return null;
    }

    /**
     * 联盟包设置增加
     */
    @Override
    public void insertUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO) {
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


    /**
     * 联盟包删除
     * @param unionId 联盟设置编号
     */
    @Override
    @Transactional
    public void removeByUnionId(List<Long> unionId) {
        LambdaQueryChainWrapper<SpreadUnionPackage> spreadUnionPackageLambdaQueryChainWrapper = this.lambdaQuery();
        spreadUnionPackageLambdaQueryChainWrapper.in(SpreadUnionPackage::getUnionId,unionId);
        if (!this.remove(spreadUnionPackageLambdaQueryChainWrapper)){
            throw new  ServiceException("删除失败");
        }
    }
}
