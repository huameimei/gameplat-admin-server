package com.gameplat.admin.controller.open.activity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationAuditStatusDTO;
import com.gameplat.admin.model.dto.ActivityQualificationQueryDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
     * @param activityQualificationQueryDTO
     * @return
     */
    @ApiOperation(value = "活动资格列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:qualification:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数"),
    })
    public IPage<ActivityQualificationVO> list(@ApiIgnore PageDTO<ActivityQualification> page,
                                               ActivityQualificationQueryDTO activityQualificationQueryDTO) {
        return activityQualificationService.list(page, activityQualificationQueryDTO);
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
     * 批量审核活动资格
     *
     * @param activityQualificationAuditStatusDTO
     */
    @ApiOperation(value = "批量审核活动资格")
    @PutMapping("/auditStatus")
    @PreAuthorize("hasAuthority('activity:qualification:edit')")
    public void auditStatus(@RequestBody ActivityQualificationAuditStatusDTO activityQualificationAuditStatusDTO) {
        if (CollectionUtils.isEmpty(activityQualificationAuditStatusDTO.getIdList())) {
            throw new ServiceException("id不能为空");
        }
        activityQualificationService.auditStatus(activityQualificationAuditStatusDTO);
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
