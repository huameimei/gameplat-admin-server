package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SysSMS;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 短信记录 数据层
 *
 * @author three
 */
public interface SysSmsMapper extends BaseMapper<SysSMS> {

  /** 清空 */
  @Select("TRUNCATE TABLE sys_sms")
  void clean();
}
