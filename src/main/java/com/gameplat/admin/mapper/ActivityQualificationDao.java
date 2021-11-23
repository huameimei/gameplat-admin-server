package com.gameplat.admin.mapper;

import com.gameplat.admin.model.domain.ActivityQualification;
import com.gameplat.admin.model.dto.ActivityQualificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-20 11:32:32
 */
@Mapper
public interface ActivityQualificationDao {

    List<ActivityQualification> findActivityQualificationList(ActivityQualificationDTO qualificationManage);

    int saveQualificationManage(ActivityQualification qualificationManage);

    int updateQualificationManage(ActivityQualification qualificationManage);

    int deleteBatchQualificationManage(@Param("ids") List<Long> qualificationIds);

    int updateQualificationManageList(List<ActivityQualification> list);

    int insertBatchQualificationManageList(List<ActivityQualification> list);

    int updateQualificationManageStatus(ActivityQualification qualificationManage);

    int passAuditQualificationManage(ActivityQualification qualificationManage);

    List<ActivityQualification> findQualificationManageStatusList(List<Long> qualificationIds);

    int updateBatchQualificationManageEmploy(List<String> qualificationActivityId);

    List<ActivityQualification> findUniques(List<ActivityQualification> uniques);

    ActivityQualification findUnique(ActivityQualification qm);

    Integer checkRepetition(ActivityQualification manage);

    int checkOtherRepetition(ActivityQualification manage);

}
