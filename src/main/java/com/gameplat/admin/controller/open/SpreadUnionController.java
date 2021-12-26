package com.gameplat.admin.controller.open;


import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.service.SpreadUnionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/spread/management")
public class SpreadUnionController {

    @Autowired
    private SpreadUnionService spreadUnionService;

    @ApiOperation(value = "联盟增加")
    @PostMapping("/creatUnion")
    @PreAuthorize("hasAuthority('spreadUnion:union:create')")
    public void creatUnion(SpreadUnionDTO spreadUnionDTO){
        spreadUnionService.creatUnion(spreadUnionDTO);
    }


    @ApiOperation(value = "联盟查询")
    @PostMapping("/unionList")
    @PreAuthorize("hasAuthority('spreadUnion:union:add')")
    public List<SpreadUnion> getUnion(SpreadUnionDTO spreadUnionDTO){
       return spreadUnionService.getUnion(spreadUnionDTO);
    }


    @ApiOperation(value = "联盟修改")
    @PostMapping("/editUnion")
    @PreAuthorize("hasAuthority('spreadUnion:union:edit')")
    public void editUnion(SpreadUnionDTO spreadUnionDTO){
        spreadUnionService.editUnion(spreadUnionDTO);
    }


    @ApiOperation(value = "联盟删除")
    @PostMapping("/removeUnion")
    @PreAuthorize("hasAuthority('spreadUnion:blacklist:remove')")
    public void removeUnion(List<Long> id){
        spreadUnionService.removeUnion(id);
    }


    @ApiOperation(value = "联盟包设置列表")
    @PostMapping("/getUnionPackage")
    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:lsit')")
    public List<SpreadUnion> getUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO){
       return spreadUnionService.getUnionPackage(spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置增加")
    @PostMapping("/insertUnionPackage")
    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:add')")
    public void insertUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO){
        spreadUnionService.insertUnionPackage(spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置修改")
    @PostMapping("/editUnionPackage")
    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:edit')")
    public void editUnionPackage(SpreadUnionPackageDTO spreadUnionPackageDTO){
        spreadUnionService.editUnionPackage(spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置删除")
    @PostMapping("/removeUnionPackage")
    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:remove')")
    public void removeUnionPackage(List<Long> id){
        spreadUnionService.removeUnionPackage(id);
    }
}
