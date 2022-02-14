package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
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
     * 获取联盟报表
     */
    List<JSONObject> getUnionReportList();


}
