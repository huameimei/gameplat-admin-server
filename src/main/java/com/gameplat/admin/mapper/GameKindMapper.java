package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.model.entity.game.GameKind;
import org.apache.ibatis.annotations.Select;

public interface GameKindMapper extends BaseMapper<GameKind> {
    @Select("select * from game_kind where code = #{code} limit 1")
    GameKindVO getByCode(String code);
}
