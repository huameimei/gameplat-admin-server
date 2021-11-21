package com.gameplat.admin.mapper.activity;

import com.gameplat.admin.model.domain.activity.MemberLuckValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bhf
 * @Description 用户幸运值数据层
 * @date 2020-06-12 19:15:07
 */
@Mapper
public interface MemberLuckValueDao {

    List<MemberLuckValue> findList(MemberLuckValue memberLuckValue);

    int save(MemberLuckValue memberLuckValue);

    int update(MemberLuckValue memberLuckValue);

    int deleteBatch(@Param("ids") List<Long> ids);

    MemberLuckValue findMemberLuckValue(MemberLuckValue memberLuckValue);
}
