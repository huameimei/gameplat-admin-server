package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.bean.OnlineCount;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 在线用户VO
 *
 * @author three
 */
@Data
@Builder
public class OnlineUserVo implements Serializable {

  private IPage<UserToken> page;

  private OnlineCount onlineCount;
}
