package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationAddDTO;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import com.gameplat.admin.model.dto.ActivityQualificationUpdateDTO;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import org.springframework.stereotype.Service;

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
     * @param activityQualificationDTO
     * @return
     */
    IPage<ActivityQualificationVO> list(PageDTO<ActivityQualification> page, ActivityQualificationDTO activityQualificationDTO);

    /**
     * 新增活动资格
     *
     * @param activityQualificationAddDTO
     */
    void add(ActivityQualificationAddDTO activityQualificationAddDTO);

    /**
     * 更新活动资格
     *
     * @param activityQualificationUpdateDTO
     */
    void update(ActivityQualificationUpdateDTO activityQualificationUpdateDTO);
}