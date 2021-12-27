package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SpreadUnionPackage;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;

import java.util.List;

public interface SpreadUnionPackageMapper extends BaseMapper<SpreadUnionPackage> {

    /**
     * 联盟包设置列表
     * @param page
     * @param spreadUnionPackageDTO
     * @return
     */
    List<SpreadUnionPackageVO> getUnionPackage(PageDTO<SpreadUnionPackage> page , SpreadUnionPackageDTO spreadUnionPackageDTO);


}
