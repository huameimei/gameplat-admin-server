package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;

import java.util.List;


/**
 * 参加活动资格查询
 *
 * @author lyq
 * @Description 业务实现层
 * @date 2020-08-20 11:32:32
 */
public interface ActivityQualificationService {

    /**
     * 根据条件查询资格列表
     *
     * @param activityQualification
     * @return
     */
    List<ActivityQualification> findQualificationList(ActivityQualification activityQualification);

    /**
     * 查询活动资格列表
     *
     * @param page
     * @param activityQualificationQueryDTO
     * @return
     */
    IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationQueryDTO activityQualificationQueryDTO);

    /**
     * 新增活动资格
     *
     * @param activityQualificationAddDTO
     */
    void add(ActivityQualificationAddDTO activityQualificationAddDTO);

    /**
     * 审核活动资格
     *
     * @param activityQualificationAuditStatusDTO
     */
    void auditStatus(ActivityQualificationAuditStatusDTO activityQualificationAuditStatusDTO);

    /**
     * 更新状态
     *
     * @param activityQualificationUpdateStatusDTO
     */
    void updateQualificationStatus(ActivityQualificationUpdateStatusDTO activityQualificationUpdateStatusDTO);

    /**
     * 更新活动资格状态
     *
     * @param activityQualification
     */
    void updateQualificationStatus(ActivityQualification activityQualification);

    /**
     * 删除活动资格
     *
     * @param ids
     */
    void delete(String ids);

    /**
     * 资格检测
     *
     * @param activityQualificationCheckDTO
     */
    void checkQualification(ActivityQualificationCheckDTO activityQualificationCheckDTO);
}
