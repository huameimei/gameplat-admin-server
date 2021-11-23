package com.gameplat.admin.controller.open.activity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lyq
 * @Description 控制层
 * @date 2020-08-20 11:32:32
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/qualification")
@Api(tags = "资格管理")
@RequiredArgsConstructor
public class ActivityQualificationController {

    private final QualificationManageService qualificationManageService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/find_list.json")
    public TableDataInfo findQualificationManageList(QualificationManageDTO qualificationManageDTO) {
        try {
            PageUtils.startPage();
            //查询列表数据
            List<QualificationManage> list = qualificationManageService.findQualificationManageList(qualificationManageDTO);
            long pageSize = new PageInfo(list).getTotal();
            return PageUtils.getDataTable(BeanUtils.mapList(list, QualificationManageVO.class), pageSize);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @PostMapping("/save.json")
    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "新增")
    public AjaxResult saveQualificationManage(@RequestBody QualificationManageDTO qualificationManageDTO) {
        try {
            //实体转换
            QualificationManage list = BeanUtils.map(qualificationManageDTO, QualificationManage.class);

            return qualificationManageService.saveQualificationManage(list);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update.json")
    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "修改")
    public AjaxResult updateQualificationManage(@RequestBody QualificationManageDTO qualificationManageDTO) {
        try {
            //实体转换
            QualificationManage list = BeanUtils.map(qualificationManageDTO, QualificationManage.class);

            return qualificationManageService.updateQualificationManage(list);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 审核通过
     */
    @ApiOperation(value = "审核通过")
    @PutMapping("/update_status.json")
    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "审核通过")
    @AutoIdempotent(expir = 3000, argIndex = {0})
    public AjaxResult passAuditQualificationManage(@RequestBody QualificationManageDTO qualificationManageDTO) {
        try {
            //实体转换
            QualificationManage list = BeanUtils.map(qualificationManageDTO, QualificationManage.class);
            return qualificationManageService.passAuditQualificationManage(list);
        } catch (ServiceException e) {
            return RetUtils.error(e.getMessage());
        }
    }

    /**
     * 修改状态
     */
    @ApiOperation(value = "修改资格状态0 禁用，1 启用")
    @PutMapping("/update_qualification_status.json")
    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "修改状态资格0 禁用，1 启用")
    public AjaxResult updateQualificationStatus(@RequestBody QualificationManageDTO qualificationManageDTO) {
        try {
            //实体转换
            QualificationManage list = BeanUtils.map(qualificationManageDTO, QualificationManage.class);
            return qualificationManageService.updateQualificationStatus(list);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 修改删除状态
     */
    @ApiOperation(value = "修改删除状态0 已删除，1 未删除")
    @PutMapping("/update_delete_status.json")
    @LogAnnotation(module = "activity-center", recordRequestParam = false, desc = "修改删除资格0 已删除，1 未删除")
    public AjaxResult updateDeleteStatus(@RequestBody QualificationManageDTO qualificationManageDTO) {
        try {
            //实体转换
            QualificationManage list = BeanUtils.map(qualificationManageDTO, QualificationManage.class);
            return qualificationManageService.updateDeleteStatus(list);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


}
