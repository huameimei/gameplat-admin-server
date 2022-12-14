package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.DivideConfigDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.vo.DivideLayerConfigVo;
import com.gameplat.admin.service.DivideLayerConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.DivideLayerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "层层代分红模式")
@RestController
@RequestMapping("/api/admin/divide/layer")
public class DivideLayerConfigController {

  @Autowired private DivideLayerConfigService layerConfigService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('agent:bonusconfig:view')")
  public IPage<DivideLayerConfigVo> list(
      PageDTO<DivideLayerConfig> page, DivideConfigDTO divideConfigDTO) {
    return layerConfigService.page(page, divideConfigDTO);
  }

  /**
   * 编辑层层代分红配置前获取
   *
   * @param dto DivideConfigDTO
   * @return Map
   */
  @GetMapping("/getLayerConfigForEdit")
  @PreAuthorize("hasAuthority('divide:layer:edit')")
  public Map<String, Object> getLayerConfigForEdit(DivideConfigDTO dto) {
    return layerConfigService.getLayerConfigForEdit(dto.getUserName(), "zh-CN");
  }

  /**
   * 添加代理推广链接前获取
   *
   * @param dto DivideConfigDTO
   * @return Map
   */
  @GetMapping("/getLayerConfigForLinkAdd")
  public Map<String, Object> getLayerConfigForLinkAdd(DivideConfigDTO dto) {
    return layerConfigService.getLayerConfigForLinkAdd(dto.getUserName(), "zh-CN");
  }

  /** 编辑代理推广链接前获取 */
  @GetMapping("/getLayerConfigForLinkEdit")
  public Map<String, Object> getLayerConfigForLinkEdit(SpreadLinkInfoDTO dto) {
    return layerConfigService.getLayerConfigForLinkEdit(dto.getId(), "zh-CN");
  }

  /**
   * 添加
   *
   * @param dto
   */
  @PostMapping("/add")
  @Operation(summary = "新增层层代分红配置")
  @PreAuthorize("hasAuthority('agent:bonusconfig:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增层层代分红配置'")
  public void add(@Validated @RequestBody DivideConfigDTO dto) {
    layerConfigService.add(dto.getUserName(), "zh-CN");
  }

  /**
   * 编辑
   *
   * @param dto
   */
  @PostMapping("/edit")
  @Operation(summary = "编辑层层代分红配置")
  @PreAuthorize("hasAuthority('agent:bonusconfig:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.AGENT, desc = "'新增层层代分红配置'")
  public void edit(@Validated @RequestBody DivideConfigDTO dto) {
    layerConfigService.edit(dto, "zh-CN");
  }

  /**
   * 删除
   *
   * @param ids
   */
  @Operation(summary = "删除分红配置")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('agent:bonusconfig:remove')")
  public void remove(@RequestBody String ids) {
    if (StrUtil.isNotBlank(ids)) {
      JSONObject jsonObject = JSONUtil.parseObj(ids);
      layerConfigService.remove(jsonObject.getStr("ids"));
    }
  }
}
