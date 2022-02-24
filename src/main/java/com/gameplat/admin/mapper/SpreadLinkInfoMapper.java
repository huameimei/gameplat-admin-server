package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.vo.SpreadLinkInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 推广配置 数据层
 *
 * @author three
 */
public interface SpreadLinkInfoMapper extends BaseMapper<SpreadLinkInfo> {
    List<SpreadLinkInfoVo> getTeamLinkInfo(SpreadLinkInfoDTO spreadLinkInfoDTO);

    @Select("select count(1) from spread_link_info where agent_account = #{agentAccount} and divide_config != '' and divide_config is not null")
    Integer countTeamLinkInfo(@Param("agentAccount") String agentAccount);

    BigDecimal getlinkMaxDivideRatio(@Param("userName") String userName, @Param("code") String code);
}
