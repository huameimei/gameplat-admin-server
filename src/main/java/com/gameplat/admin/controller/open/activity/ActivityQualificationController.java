package com.gameplat.admin.controller.open.activity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 活动资格列表
     *
     * @param page
     * @param activityQualificationDTO
     * @return
     */
    @ApiOperation(value = "活动资格列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:qualification:list')")
    public IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationDTO activityQualificationDTO) {
        return activityQualificationService.list(page, activityQualificationDTO);
    }

    /**
     * 新增活动资格
     *
     * @param activityQualificationAddDTO
     */
    @ApiOperation(value = "新增活动资格")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:qualification:add')")
    public void add(@RequestBody ActivityQualificationAddDTO activityQualificationAddDTO) {
        activityQualificationService.add(activityQualificationAddDTO);
    }

    /**
     * 修改活动资格
     *
     * @param activityQualificationUpdateDTO
     */
    @ApiOperation(value = "修改活动资格")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:qualification:edit')")
    public void update(@RequestBody ActivityQualificationUpdateDTO activityQualificationUpdateDTO) {
        if (activityQualificationUpdateDTO.getId() == null || activityQualificationUpdateDTO.getId() == 0) {
            throw new ServiceException("id不能为空");
        }
        activityQualificationService.update(activityQualificationUpdateDTO);
    }

    /**
     * 更新活动资格状态
     *
     * @param activityQualificationUpdateStatusDTO
     */
    @ApiOperation(value = "更新活动资格状态")
    @PutMapping("/updateStatus")
    @PreAuthorize("hasAuthority('activity:qualification:edit')")
    public void updateStatus(@RequestBody ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO) {
        if (CollectionUtils.isEmpty(activityQualificationUpdateStatusDTO.getQualificationIds())) {
            throw new ServiceException("qualificationIds不能为空");
        }
        activityQualificationService.updateStatus(activityQualificationUpdateStatusDTO);
    }

    /**
     * 更新活动资格状态
     *
     * @param activityQualificationUpdateStatusDTO
     */
    @ApiOperation(value = "更新活动资格状态")
    @PutMapping("/updateQualificationStatus")
    @PreAuthorize("hasAuthority('activity:qualification:edit')")
    public void updateQualificationStatus(@RequestBody ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO) {
        activityQualificationService.updateQualificationStatus(activityQualificationUpdateStatusDTO);
    }

    /**
     * 删除活动资格
     *
     * @param ids
     */
    @ApiOperation(value = "删除活动资格")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('activity:qualification:remove')")
    public void delete(@RequestBody String ids) {
        activityQualificationService.delete(ids);
    }


}
