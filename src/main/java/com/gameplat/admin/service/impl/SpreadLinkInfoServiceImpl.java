package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SpreadLinkInfoConvert;
import com.gameplat.admin.mapper.DivideLayerConfigMapper;
import com.gameplat.admin.mapper.MemberInfoMapper;
import com.gameplat.admin.mapper.SpreadLinkInfoMapper;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.GameDivideVo;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.validator.ValidatorUtil;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.AgentBackendConfig;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.proxy.DivideLayerConfig;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import lombok.Cleanup;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * ?????????????????? ???????????????
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SpreadLinkInfoServiceImpl extends ServiceImpl<SpreadLinkInfoMapper, SpreadLinkInfo>
    implements SpreadLinkInfoService {

  @Autowired private SpreadLinkInfoConvert spreadLinkInfoConvert;

  @Lazy @Autowired private MemberService memberService;

  @Autowired private MemberInfoMapper memberInfoMapper;

  @Autowired private DivideLayerConfigMapper layerConfigMapper;

  @Autowired private ConfigService configService;

  @Autowired private SpreadLinkInfoMapper spreadLinkInfoMapper;

  private static final String STR_URL = "/r/";

  @Override
  public IPage<SpreadConfigVO> page(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO dto) {
    IPage<SpreadConfigVO> convert =
        this.lambdaQuery()
            .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
            .eq(
                ObjectUtils.isNotEmpty(dto.getAgentAccount()),
                SpreadLinkInfo::getAgentAccount,
                dto.getAgentAccount())
            .eq(
                ObjectUtils.isNotEmpty(dto.getSpreadType()),
                SpreadLinkInfo::getSpreadType,
                dto.getSpreadType())
            .eq(
                ObjectUtils.isNotNull(dto.getUserType()),
                SpreadLinkInfo::getUserType,
                dto.getUserType())
            .eq(ObjectUtils.isNotNull(dto.getStatus()), SpreadLinkInfo::getStatus, dto.getStatus())
            .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
            .orderBy(
                StringUtils.equals(dto.getOrderByColumn(), "createTime"),
                ValidatorUtil.isAsc(dto.getSortBy()),
                SpreadLinkInfo::getCreateTime)
            .orderBy(
                StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
                ValidatorUtil.isAsc(dto.getSortBy()),
                SpreadLinkInfo::getVisitCount)
            .orderBy(
                StringUtils.equals(dto.getOrderByColumn(), "registCount"),
                ValidatorUtil.isAsc(dto.getSortBy()),
                SpreadLinkInfo::getRegistCount)
            .page(page)
            .convert(spreadLinkInfoConvert::toVo);
    for (SpreadConfigVO obj : convert.getRecords()) {
      String externalUrl = obj.getExternalUrl();
      if (StrUtil.isNotBlank(externalUrl) && !obj.getExternalUrl().contains(STR_URL)) {
        if ((externalUrl.lastIndexOf("/") + 1) == externalUrl.length()) {
          externalUrl = externalUrl.substring(0, externalUrl.length() - 1);
        }
        obj.setRcDomain(MessageFormat.format("{0}{1}{2}", externalUrl, STR_URL, obj.getCode()));
      }
    }
    return convert;
  }

  @Override
  public void exportList(SpreadLinkInfoDTO dto, HttpServletResponse response) {
    try {
      List<SpreadLinkInfo> list =
          this.lambdaQuery()
              .eq(ObjectUtils.isNotNull(dto.getId()), SpreadLinkInfo::getId, dto.getId())
              .eq(
                  ObjectUtils.isNotEmpty(dto.getAgentAccount()),
                  SpreadLinkInfo::getAgentAccount,
                  dto.getAgentAccount())
              .eq(
                  ObjectUtils.isNotEmpty(dto.getSpreadType()),
                  SpreadLinkInfo::getSpreadType,
                  dto.getSpreadType())
              .eq(
                  ObjectUtils.isNotNull(dto.getUserType()),
                  SpreadLinkInfo::getUserType,
                  dto.getUserType())
              .eq(
                  ObjectUtils.isNotNull(dto.getStatus()),
                  SpreadLinkInfo::getStatus,
                  dto.getStatus())
              .eq(ObjectUtils.isNotEmpty(dto.getCode()), SpreadLinkInfo::getCode, dto.getCode())
              .orderBy(
                  StringUtils.equals(dto.getOrderByColumn(), "createTime"),
                  ValidatorUtil.isAsc(dto.getSortBy()),
                  SpreadLinkInfo::getCreateTime)
              .orderBy(
                  StringUtils.equals(dto.getOrderByColumn(), "visitCount"),
                  ValidatorUtil.isAsc(dto.getSortBy()),
                  SpreadLinkInfo::getVisitCount)
              .orderBy(
                  StringUtils.equals(dto.getOrderByColumn(), "registCount"),
                  ValidatorUtil.isAsc(dto.getSortBy()),
                  SpreadLinkInfo::getRegistCount)
              .list();
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Content-disposition", "attachment;filename = myExcel.xls");
      @Cleanup OutputStream outputStream = null;
      Workbook workbook =
          ExcelExportUtil.exportExcel(
              new ExportParams("????????????????????????", "??????????????????"), SpreadLinkInfo.class, list);
      outputStream = response.getOutputStream();
      workbook.write(outputStream);
    } catch (IOException e) {
      throw new ServiceException("????????????:" + e);
    }
  }

  @Override
  public void add(SpreadLinkInfoAddDTO dto) {
    // ??????????????????
    if (dto.getExclusiveFlag() == 1) {
      if (StrUtil.isBlank(dto.getAgentAccount())) {
        throw new ServiceException("???????????????????????????");
      }
      if (StrUtil.isBlank(dto.getSourceDomain())) {
        throw new ServiceException("???????????????????????????");
      }
      if (StrUtil.isBlank(dto.getExternalUrl())) {
        throw new ServiceException("???????????????????????????");
      }
      if (StrUtil.isNotBlank(dto.getSourceDomain())) {
        boolean exists =
            this.lambdaQuery()
                .ne(SpreadLinkInfo::getAgentAccount, dto.getAgentAccount())
                .eq(SpreadLinkInfo::getExternalUrl, dto.getSourceDomain())
                .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                .exists();
        if (exists) {
          throw new ServiceException("??????????????????????????????");
        }
      }
    }

    // ????????????
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
    if (StringUtils.isNotEmpty(linkInfo.getAgentAccount())) {
      // ???????????????????????????
      Member member =
          memberService
              .getByAccount(linkInfo.getAgentAccount())
              .orElseThrow(() -> new ServiceException("?????????????????????!"));
      Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "????????????????????????");
    }
    AgentBackendConfig agentBackendConfig =
        configService.get(DictTypeEnum.AGENT_BACKEND_CONFIG, AgentBackendConfig.class);
    // ???????????????????????????
    Integer agentMinCodeNum =
        Optional.ofNullable(agentBackendConfig.getMinSpreadLength()).orElse(0);
    // ???????????????????????????
    Integer agentMaxSpreadNum = Optional.ofNullable(agentBackendConfig.getMaxSpreadNum()).orElse(0);
    // ????????????????????? ????????????????????????  ???????????? 4-20???
    if (StrUtil.isBlank(linkInfo.getCode())) {
      linkInfo.setCode(
          RandomStringUtils.random(agentMinCodeNum == 0 ? 6 : agentMinCodeNum, true, true)
              .toLowerCase());
    }
    linkInfo.setIsOpenDividePreset(dto.getIsOpenDividePreset());
    // ????????????????????? ????????????????????????
    if (!StrUtil.isBlank(linkInfo.getCode())) {
      this.checkCode(linkInfo.getCode(), agentMinCodeNum);
    }
    if (agentMaxSpreadNum > 0) {
      // ????????????????????????????????????????????????
      Long count =
          this.lambdaQuery()
              .eq(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
              .count();
      Assert.isTrue((count + 1) <= agentMaxSpreadNum, "??????????????????????????????" + agentMaxSpreadNum + "?????????????????????");
    }
    // ??????????????????????????? ???????????? ???????????????????????????????????????????????????????????????
    if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
      boolean exists =
          this.lambdaQuery()
              .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
              .eq(SpreadLinkInfo::getSourceDomain, linkInfo.getExternalUrl())
              .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
              .exists();
      if (exists) {
        throw new ServiceException("?????????????????????????????????");
      }
    }
    boolean saveResult = this.save(linkInfo);
    Assert.isTrue(saveResult, "???????????????");
    if (saveResult
        && linkInfo.getIsOpenDividePreset() == 1
        && linkInfo.getUserType() == 1
        && BeanUtil.isNotEmpty(dto.getOwnerConfigMap())) {
      this.saveOrEditDivideConfig(
          linkInfo.getId(), linkInfo.getAgentAccount(), dto.getOwnerConfigMap());
    }
  }

  @Override
  public void update(SpreadLinkInfoEditDTO dto) {
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
    //    if (StrUtil.isBlank(linkInfo.getAgentAccount())) {
    //      throw new ServiceException("???????????????????????????");
    //    }
    if (StrUtil.isBlank(linkInfo.getCode())) {
      throw new ServiceException("????????????????????????");
    }
    if (StrUtil.isNotBlank(linkInfo.getAgentAccount())) {
      // ???????????????????????????
      Member member =
          memberService
              .getByAccount(linkInfo.getAgentAccount())
              .orElseThrow(() -> new ServiceException("?????????????????????!"));
      Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "????????????????????????");
      linkInfo.setIsOpenDividePreset(dto.getIsOpenDividePreset());
    }

    // ??????????????????????????? ???????????? ???????????????????????????????????????????????????????????????
    if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
      boolean exists =
          this.lambdaQuery()
              .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
              .eq(SpreadLinkInfo::getSourceDomain, linkInfo.getExternalUrl())
              .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
              .exists();
      if (exists) {
        throw new ServiceException("?????????????????????????????????");
      }
    }
    boolean updateResult = this.updateById(linkInfo);
    Assert.isTrue(updateResult, "???????????????");
    if (updateResult
        && linkInfo.getIsOpenDividePreset() == 1
        && linkInfo.getUserType() == 1
        && BeanUtil.isNotEmpty(dto.getOwnerConfigMap())) {
      this.saveOrEditDivideConfig(
          linkInfo.getId(), linkInfo.getAgentAccount(), dto.getOwnerConfigMap());
    }
  }

  @Override
  public void deleteById(Long id) {
    this.removeById(id);
  }

  @Override
  public void changeStatus(SpreadLinkInfoEditDTO dto) {
    if (!this.updateById(spreadLinkInfoConvert.toEntity(dto))) {
      throw new ServiceException("????????????");
    }
  }

  @Override
  public void changeReleaseTime(Long id) {
    if (!this.updateById(SpreadLinkInfo.builder().id(id).createTime(new Date()).build())) {
      throw new ServiceException("????????????");
    }
  }

  @Override
  public void batchEnableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
        .in(SpreadLinkInfo::getId, ids)
        .set(SpreadLinkInfo::getStatus, EnableEnum.ENABLED.code())
        .update(new SpreadLinkInfo())) {
      throw new ServiceException("????????????????????????!");
    }
  }

  @Override
  public void batchDisableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
        .in(SpreadLinkInfo::getId, ids)
        .set(SpreadLinkInfo::getStatus, EnableEnum.DISABLED.code())
        .update(new SpreadLinkInfo())) {
      throw new ServiceException("????????????????????????!");
    }
  }

  @Override
  public void batchDeleteByIds(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("??????????????????");
    }
  }

  @Override
  public List<SpreadLinkInfo> getSpreadList(String agentAccount) {
    return this.lambdaQuery().eq(SpreadLinkInfo::getAgentAccount, agentAccount).list();
  }

  @Override
  public void checkCode(String code, Integer agentMinCodeNum) {
    String reg = "^[a-zA-Z0-9]{" + agentMinCodeNum + ",20}$";
    if (!code.matches(reg)) {
      String eStr = agentMinCodeNum == 20 ? "" : (agentMinCodeNum + "-");
      throw new ServiceException("??????????????????" + eStr + "20???????????????????????????");
    }

    if (this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(code), SpreadLinkInfo::getCode, code)
        .exists()) {
      throw new ServiceException("????????????????????????");
    }
  }

  @Override
  public JSONArray getSpreadLinkRebate(String account, Boolean statisMax, Boolean statisMin) {
    BigDecimal min = BigDecimal.ZERO;
    BigDecimal max = BigDecimal.ZERO;
    if (StrUtil.isBlank(account)) {
      min = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
      max = new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      if (statisMax) {
        BigDecimal userRebate = memberInfoMapper.findUserRebate(account);
        max =
            ObjectUtils.isNull(userRebate)
                ? new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP)
                : userRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
      } else {
        max = new BigDecimal("9").setScale(2, BigDecimal.ROUND_HALF_UP);
      }
      if (statisMin) {
        // ???????????????????????????????????????
        BigDecimal userLowerMaxRebate = memberInfoMapper.findUserLowerMaxRebate(account);
        min =
            ObjectUtils.isNull(userLowerMaxRebate)
                ? new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP)
                : userLowerMaxRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
        // ????????????????????????????????????????????????
        LambdaQueryWrapper<SpreadLinkInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
            .eq(SpreadLinkInfo::getAgentAccount, account)
            .orderByDesc(SpreadLinkInfo::getRebate)
            .last("limit 1")
            .select(SpreadLinkInfo::getRebate);
        SpreadLinkInfo linkMinRebateObj = this.getOne(queryWrapper);
        BigDecimal linkMinRebate = BigDecimal.ZERO;
        if (BeanUtil.isEmpty(linkMinRebateObj)) {
          linkMinRebate = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
          linkMinRebate = linkMinRebateObj.getRebate().setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        min = min.compareTo(linkMinRebate) >= 0 ? min : linkMinRebate;
      } else {
        min = new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP);
      }
    }

    JSONArray jsonArray = new JSONArray();
    BigDecimal rebate = max;
    int base = 1800;
    BigDecimal value = BigDecimal.ZERO;
    String text = "";
    while (rebate.compareTo(min) >= 0) {
      JSONObject jsonObject = new JSONObject();
      value = rebate;
      int baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
      text = rebate.toString().concat("% ---- ").concat(Integer.toString(baseData));
      jsonObject.set("value", value);
      jsonObject.set("text", text);
      jsonArray.add(jsonObject);
      rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    return jsonArray;
  }

  /**
   * ??????????????????????????????????????????
   *
   * @param linkId
   * @param agentAccount
   * @param paramOwnerConfigMap
   */
  @Override
  public void saveOrEditDivideConfig(
      Long linkId, String agentAccount, Map<String, List<GameDivideVo>> paramOwnerConfigMap) {
    if (BeanUtil.isEmpty(paramOwnerConfigMap)) {
      return;
    }
    Map<String, JSONObject> saveMap = new HashMap<>();
    for (Map.Entry<String, List<GameDivideVo>> map : paramOwnerConfigMap.entrySet()) {
      List<GameDivideVo> value = map.getValue();
      for (GameDivideVo vo : value) {
        saveMap.put(vo.getCode(), JSONUtil.parseObj(vo));
      }
    }
    if (CollectionUtil.isNotEmpty(saveMap)) {
      // ??????????????????????????????
      DivideLayerConfig layerConfig = layerConfigMapper.getByUserName(agentAccount);
      if (BeanUtil.isEmpty(layerConfig) || StrUtil.isBlank(layerConfig.getDivideConfig())) {
        return;
      }
      Map<String, JSONObject> map = JSONUtil.toBean(layerConfig.getDivideConfig(), Map.class);
      for (Map.Entry<String, JSONObject> m : saveMap.entrySet()) {
        m.getValue()
            .put(
                "parentDivideRatio",
                map.get(m.getKey())
                    .getBigDecimal("divideRatio")
                    .subtract(m.getValue().getBigDecimal("divideRatio")));
      }
      this.lambdaUpdate()
          .set(SpreadLinkInfo::getDivideConfig, JSONUtil.toJsonStr(saveMap))
          .eq(SpreadLinkInfo::getId, linkId)
          .update(new SpreadLinkInfo());
    }
  }

  @Override
  public BigDecimal getMaxSpreadLinkRebate(String account) {
    return this.lambdaQuery()
        .select(SpreadLinkInfo::getRebate)
        .eq(SpreadLinkInfo::getAgentAccount, account)
        .orderByDesc(SpreadLinkInfo::getRebate)
        .last("LIMIT 1")
        .oneOpt()
        .map(SpreadLinkInfo::getRebate)
        .orElse(BigDecimal.ZERO);
  }

  @Override
  public List<Map<String, Object>> getDefaultLink() {
    QueryWrapper<SpreadLinkInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("DISTINCT external_url").eq("exclusive_flag", 0).isNotNull("external_url");
    return spreadLinkInfoMapper.selectMaps(queryWrapper);
  }
}
