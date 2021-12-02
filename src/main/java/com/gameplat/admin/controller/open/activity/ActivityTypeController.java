package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityType;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 活动类型管理
 *
 * @author kevin
 * @since 2020-08-17
 */
@Slf4j
@Api(tags = "活动类型、活动板块管理")
@RestController
@RequestMapping("/api/admin/activity/type")
@RequiredArgsConstructor
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    /**
     * 活动类型列表
     */
    @ApiOperation(value = "活动类型列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('activity:type:list')")
    public IPage<ActivityTypeVO> list(PageDTO<ActivityType> page, ActivityTypeDTO activityTypeDTO,
                                      @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (StringUtils.isBlank(activityTypeDTO.getLanguage())) {
            activityTypeDTO.setLanguage(country);
        }
        return activityTypeService.list(page, activityTypeDTO);
    }


    @ApiOperation(value = "活动板块管理->新增活动类型")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('activity:type:add')")
    public void add(@RequestBody ActivityTypeAddDTO activityTypeAddDTO,
                    @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (StringUtils.isBlank(activityTypeAddDTO.getLanguage())) {
            activityTypeAddDTO.setLanguage(country);
        }
        if (StringUtils.isBlank(activityTypeAddDTO.getTypeName())) {
            throw new ServiceException("活动类型名称不能为空");
        }
        if (activityTypeAddDTO.getSort() == null || activityTypeAddDTO.getSort() == 0) {
            throw new ServiceException("排序不能为空");
        }
        if (activityTypeAddDTO.getFloatStatus() != null && activityTypeAddDTO.getFloatStatus() != 0) {
            if (StringUtils.isBlank(activityTypeAddDTO.getFloatLogo())) {
                throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
            }
        }
        activityTypeService.add(activityTypeAddDTO);
    }

    @ApiOperation(value = "更新活动类型")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('activity:type:edit')")
    public void update(@RequestBody ActivityTypeUpdateDTO activityTypeUpdateDTO,
                       @RequestHeader(value = "country", defaultValue = "zh-CN", required = false) String country) {
        if (activityTypeUpdateDTO.getId() == null || activityTypeUpdateDTO.getId() == 0) {
            throw new ServiceException("更新活动类型，ID不能为空");
        }
        if (StringUtils.isBlank(activityTypeUpdateDTO.getLanguage())) {
            activityTypeUpdateDTO.setLanguage(country);
        }
        if (activityTypeUpdateDTO.getSort() == null || activityTypeUpdateDTO.getSort() == 0) {
            throw new ServiceException("排序不能为空");
        }
        if (activityTypeUpdateDTO.getFloatStatus() != null && activityTypeUpdateDTO.getFloatStatus() != 0) {
            if (StringUtils.isBlank(activityTypeUpdateDTO.getFloatLogo())) {
                throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
            }
        }
        activityTypeService.update(activityTypeUpdateDTO);
    }

    @ApiOperation(value = "删除活动类型，单个或者多个")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('activity:type:remove')")
    public void remove(@RequestBody String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new ServiceException("ids不能为空");
        }
        activityTypeService.remove(ids);
    }

}
