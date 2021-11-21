package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.QualificationManage;
import com.gameplat.admin.model.dto.activity.QualificationManageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lyq
 * @Description 数据层
 * @date 2020-08-20 11:32:32
 */
@Mapper
public interface QualificationManageDao {

    List<QualificationManage> findQualificationManageList(QualificationManageDTO qualificationManage);

    int saveQualificationManage(QualificationManage qualificationManage);

    int updateQualificationManage(QualificationManage qualificationManage);

    int deleteBatchQualificationManage(@Param("ids") List<Long> qualificationIds);

    int updateQualificationManageList(List<QualificationManage> list);

    int insertBatchQualificationManageList(List<QualificationManage> list);

    int updateQualificationManageStatus(QualificationManage qualificationManage);

    int passAuditQualificationManage(QualificationManage qualificationManage);

    List<QualificationManage> findQualificationManageStatusList(List<Long> qualificationIds);

    int updateBatchQualificationManageEmploy(List<String> qualificationActivityId);

    List<QualificationManage> findUniques(List<QualificationManage> uniques);

    QualificationManage findUnique(QualificationManage qm);

    Integer checkRepetition(QualificationManage manage);

    int checkOtherRepetition(QualificationManage manage);

}
