package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lily
 * @description
 * @date 2021/12/27
 */
@Data
public class MessageDictDataVO implements Serializable {

  @Schema(description = "推送范围")
  private List<SysDictData> userRange = new ArrayList<>();

  @Schema(description = "弹出界面")
  private List<SysDictData> location = new ArrayList<>();

  @Schema(description = "弹出次数")
  private List<SysDictData> popCount = new ArrayList<>();

  @Schema(description = "消息类别")
  private List<SysDictData> messageCate = new ArrayList<>();

  @Schema(description = "消息展示类型")
  private List<SysDictData> messageShowType = new ArrayList<>();
}
