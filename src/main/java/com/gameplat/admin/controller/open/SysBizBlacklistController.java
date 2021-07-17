package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.enums.BizBlackListTypeEnum;
import com.gameplat.admin.model.dto.OptionDTO;
import com.gameplat.admin.model.dto.SysBizBlackListQueryDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistAddDTO;
import com.gameplat.admin.model.dto.SysBizBlacklistUpdateDTO;
import com.gameplat.admin.model.entity.SysBizBlacklist;
import com.gameplat.admin.model.vo.SysBizBlacklistVO;
import com.gameplat.admin.service.SysBizBlacklistService;
import com.gameplat.common.constant.ServiceApi;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ServiceApi.API + "/sysBizBlacklist")
public class SysBizBlacklistController {

  @Autowired private SysBizBlacklistService sysBizBlacklistService;

  /**
   * 获取所有黑名单类型
   *
   * @return
   */
  @GetMapping(value = "biz-blacklist-types")
  public List<OptionDTO<String>> bizBlacklistTypes() {
    return Stream.of(BizBlackListTypeEnum.values())
        .map(t -> new OptionDTO<>(t.getValue(), t.getText()))
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/list")
  public IPage<SysBizBlacklistVO> queryAll(
      IPage<SysBizBlacklist> sysBizBlacklist, SysBizBlackListQueryDTO queryDto) {
    return sysBizBlacklistService.queryPage(sysBizBlacklist, queryDto);
  }

  /**
   * 获取单个对象
   *
   * @param id
   * @return
   * @throws Exception
   */
  @GetMapping(value = "{id}")
  public SysBizBlacklist get(@PathVariable Long id) throws Exception {
    return sysBizBlacklistService.getById(id);
  }

  @PostMapping(value = "/save")
  public void save(SysBizBlacklistAddDTO sysBizBlacklistAddDTO) {
    sysBizBlacklistService.save(sysBizBlacklistAddDTO);
  }

  @PostMapping(value = "/update")
  public void update(SysBizBlacklistUpdateDTO sysBizBlacklistUpdateDTO) {
    sysBizBlacklistService.update(sysBizBlacklistUpdateDTO);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id) {
    sysBizBlacklistService.delete(id);
  }
}
