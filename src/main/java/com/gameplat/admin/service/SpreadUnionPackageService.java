package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.domain.SpreadUnionPackage;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.admin.model.vo.SpreadUnionVO;

import java.util.List;

/**
 * 联运管理业务
 */
public interface SpreadUnionPackageService extends IService<SpreadUnionPackage> {


    /**
     * 获取联盟包设置列表
     */
    List<SpreadUnionPackageVO> getUnionPackage(PageDTO<SpreadUnionPackage> page, SpreadUnionPackageDTO spreadUnionPackageDTO);

    /**
     * 联盟包设置增加
     */
    void insertUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO);

    /**
     * 联盟包设置修改
     */
    void editUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO);

    /**
     * 联盟包设置删除
     */
    void removeUnionPackage(List<Long> id);

    /**
     * 根据联盟设置ID删除联盟包
     */
    void removeByUnionId(List<Long> id);
}
