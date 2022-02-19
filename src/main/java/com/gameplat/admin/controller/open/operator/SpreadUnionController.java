package com.gameplat.admin.controller.open.operator;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.domain.SpreadUnionPackage;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadUnionPackageService;
import com.gameplat.admin.service.SpreadUnionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/admin/operator/management")
public class SpreadUnionController {

    @Autowired
    private SpreadUnionService spreadUnionService;

    @Autowired
    private SpreadUnionPackageService spreadUnionPackageService;

    @ApiOperation(value = "联盟增加")
    @PostMapping("/creatUnion")
//    @PreAuthorize("hasAuthority('spreadUnion:union:create')")
    public void creatUnion(@RequestBody  SpreadUnionDTO spreadUnionDTO){
        spreadUnionService.creatUnion(spreadUnionDTO);
    }


    @ApiOperation(value = "联盟查询")
    @GetMapping("/unionList")
//    @PreAuthorize("hasAuthority('spreadUnion:union:add')")
    public IPage<SpreadUnionVO> getUnion(@ApiIgnore PageDTO<SpreadUnion> page ,SpreadUnionDTO spreadUnionDTO){
       return spreadUnionService.getUnion(page,spreadUnionDTO);
    }


    @ApiOperation(value = "联盟修改")
    @PostMapping("/editUnion")
//    @PreAuthorize("hasAuthority('spreadUnion:union:edit')")
    public void editUnion(@RequestBody SpreadUnionDTO spreadUnionDTO){
        spreadUnionService.editUnion(spreadUnionDTO);
    }


    @ApiOperation(value = "联盟删除")
    @DeleteMapping("/removeUnion")
//    @PreAuthorize("hasAuthority('spreadUnion:blacklist:remove')")
    public void removeUnion(@RequestBody List<Long> idList ){
        spreadUnionPackageService.removeByUnionId(idList);
        spreadUnionService.removeUnion(idList);
    }


    @ApiOperation(value = "联盟包设置列表")
    @GetMapping("/getUnionPackage")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:lsit')")
    public List<SpreadUnionPackageVO> getUnionPackage(@ApiIgnore PageDTO<SpreadUnionPackage> page , SpreadUnionPackageDTO spreadUnionPackageDTO){
       return spreadUnionPackageService.getUnionPackage(page, spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置增加")
    @PostMapping("/insertUnionPackage")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:add')")
    public void insertUnionPackage(@RequestBody SpreadUnionPackageDTO spreadUnionPackageDTO){
        spreadUnionPackageService.insertUnionPackage(spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置修改")
    @PostMapping("/editUnionPackage")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:edit')")
    public void editUnionPackage(@RequestBody SpreadUnionPackageDTO spreadUnionPackageDTO){
        spreadUnionPackageService.editUnionPackage(spreadUnionPackageDTO);
    }

    @ApiOperation(value = "联盟包设置删除")
    @DeleteMapping("/removeUnionPackage")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:remove')")
    public void removeUnionPackage(@RequestBody List<Long> idList){
        spreadUnionPackageService.removeUnionPackage(idList);
    }


    @ApiOperation(value = "联盟报表列表")
    @GetMapping("/unionReportList")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:remove')")
    public Object unionReportList(){
       return spreadUnionService.getUnionReportList();
    }


    @ApiOperation(value = "联盟报表详情")
    @GetMapping("/unionReportInfo")
//    @PreAuthorize("hasAuthority('spreadUnion:unionpackage:remove')")
    public Object unionReportInfo(){
        return null;
    }
}
