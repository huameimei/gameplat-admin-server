package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;

import java.util.List;

/**
 * 联运管理业务
 */
public interface SpreadUnionService extends IService<SpreadUnion> {

    /**
     * 联盟增加
     */
    void creatUnion(SpreadUnionDTO spreadUnionDTO);

    /**
     * 联盟查询
     */
    IPage<SpreadUnionVO> getUnion(PageDTO<SpreadUnion> page  ,SpreadUnionDTO spreadUnionDTO);

    /**
     * 联盟修改
     */
    void editUnion(SpreadUnionDTO spreadUnionDTO);

    /**
     * 联盟删除
     */
    void removeUnion(List<Long> id);

    /**
     * 获取联盟包设置列表
     */
    List<SpreadUnion> getUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO);

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

}
