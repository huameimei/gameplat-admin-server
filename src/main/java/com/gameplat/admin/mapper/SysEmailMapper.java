package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.domain.SysEmail;
import org.apache.ibatis.annotations.Select;

/**
 * 邮件记录 数据层
 *
 * @author three
 */
public interface SysEmailMapper extends BaseMapper<SysEmail> {

  /** 清空 */
  @Select("TRUNCATE TABLE sys_email")
  void clean();
}
