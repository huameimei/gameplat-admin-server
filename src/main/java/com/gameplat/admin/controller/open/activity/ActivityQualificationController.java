package com.gameplat.admin.controller.open.activity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityInfo;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 活动资格管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/qualification")
@Api(tags = "活动资格管理")
public class ActivityQualificationController {

    @Autowired
    private ActivityQualificationService activityQualificationService;

    /**
     * 活动列表
     */
    @ApiOperation(value = "活动资格列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:qualification:list')")
    public IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationDTO activityQualificationDTO) {
        return activityQualificationService.list(page, activityQualificationDTO);
    }

    @ApiOperation(value = "新增活动资格")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:qualification:add')")
    public void add(@RequestBody ActivityQualificationAddDTO activityQualificationAddDTO) {
        activityQualificationService.add(activityQualificationAddDTO);
    }

    /**
     * 修改
     *
     * @param activityQualificationUpdateDTO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:qualification:edit')")
    public void update(@RequestBody ActivityQualificationUpdateDTO activityQualificationUpdateDTO) {
        if (activityQualificationUpdateDTO.getId() == null || activityQualificationUpdateDTO.getId() == 0) {
            throw new ServiceException("id不能为空");
        }
        activityQualificationService.update(activityQualificationUpdateDTO);
    }


}
