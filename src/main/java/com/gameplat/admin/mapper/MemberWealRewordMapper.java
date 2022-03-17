package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.MemberWealRewordDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import com.gameplat.admin.model.vo.TotalMemberWealRewordVO;
import com.gameplat.model.entity.member.MemberWealReword;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lily
 * @description vip福利记录
 * @date 2021/11/23
 */
@Mapper
public interface MemberWealRewordMapper extends BaseMapper<MemberWealReword> {

  List<MemberWealRewordVO> findMemberWealData(MemberWealRewordDTO dto);

  TotalMemberWealRewordVO findMemberWealRewordTotal(MemberWealRewordDTO dto);
}
