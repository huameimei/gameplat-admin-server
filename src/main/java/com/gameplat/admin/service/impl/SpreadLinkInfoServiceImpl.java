package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.validator.ValidatorUtil;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.AgentBackendConfig;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.proxy.DivideLayerConfig;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import com.gameplat.model.entity.sys.SysDictData;
import lombok.Cleanup;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 域名推广配置 服务实现层
 *
 * @author three
 */
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SpreadLinkInfoServiceImpl extends ServiceImpl<SpreadLinkInfoMapper, SpreadLinkInfo>
        implements SpreadLinkInfoService {

  @Autowired private SpreadLinkInfoConvert spreadLinkInfoConvert;

  @Autowired private MemberService memberService;

  @Autowired private MemberInfoMapper memberInfoMapper;

  @Autowired private DivideLayerConfigMapper layerConfigMapper;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private ConfigService configService;

  private static final String STR_URL = "/r/";

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
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
      if (StrUtil.isNotBlank(obj.getExternalUrl()) && !obj.getExternalUrl().contains(STR_URL)) {
        obj.setRcDomain(
                MessageFormat.format("{0}{1}{2}", obj.getExternalUrl(), STR_URL, obj.getCode()));
      }
    }
    return convert;
  }

  /**
   * 导出
   *
   * @param dto
   * @param response
   */
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
                      new ExportParams("域名推广列表导出", "域名推广列表"), SpreadLinkInfo.class, list);
      outputStream = response.getOutputStream();
      workbook.write(outputStream);
    } catch (IOException e) {
      throw new ServiceException("导出失败:" + e);
    }
  }

  /**
   * 新增推广信息 1.推广域名是否为空： 不为空 需要校验（是否被其它代理作为了专属域名）
   *
   * @param dto 需求: 1、代理专属推广地址唯一 2、推广码唯一 3、公共推广地址下允许多个推广码 4、代理专属推广地址允许多个推广码   默认推广域名下可不绑定推广账号 以及 推广码
   */
  @Override
  public void add(SpreadLinkInfoAddDTO dto) {
    //专属域名验证
    if (dto.getExclusiveFlag() == 1){
      if (StrUtil.isBlank(dto.getAgentAccount()) ) {
        throw new ServiceException("代理账号不能为空！");
      }
      if (StrUtil.isBlank(dto.getSourceDomain())){
        throw new ServiceException("请输入专属域名地址");
      }
      if(StrUtil.isBlank(dto.getExternalUrl())){
        throw new ServiceException("请输入跳转域名地址");
      }
      if (StrUtil.isNotBlank(dto.getSourceDomain())) {
        boolean exists =
                this.lambdaQuery()
                        .ne(SpreadLinkInfo::getAgentAccount, dto.getAgentAccount())
                        .eq(SpreadLinkInfo::getExternalUrl, dto.getSourceDomain())
                        .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                        .exists();
        if (exists) {
          throw new ServiceException("此专属域名已被使用！");
        }
      }
    }

    // 实体转换
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
    if (StringUtils.isNotEmpty(linkInfo.getAgentAccount())) {
      // 校验账号的用户类型
      Member member =
              memberService
                      .getByAccount(linkInfo.getAgentAccount())
                      .orElseThrow(() -> new ServiceException("代理账号不存在!"));
      Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "账号类型不支持！");
    }
    AgentBackendConfig agentBackendConfig = configService.get(DictTypeEnum.AGENT_BACKEND_CONFIG, AgentBackendConfig.class);
    // 推广码最少字符限制
    Integer agentMinCodeNum = Optional.ofNullable(agentBackendConfig.getMinSpreadLength()).orElse(0);
    // 代理推广码最大条数
    Integer agentMaxSpreadNum = Optional.ofNullable(agentBackendConfig.getMaxSpreadNum()).orElse(0);
    // 如果推广码为空 并且推广地址为空  随机生成 4-20位
//    if (StrUtil.isBlank(linkInfo.getCode()) && StrUtil.isBlank(dto.getExternalUrl())) {
    if (StrUtil.isBlank(linkInfo.getCode())) {
      linkInfo.setCode(
              RandomStringUtils.random(agentMinCodeNum == 0 ? 6 : agentMinCodeNum, true, true)
                      .toLowerCase());
    }
    linkInfo.setIsOpenDividePreset(dto.getIsOpenDividePreset());
    // 校验推广码格式 并且是否已经存在
    if(!StrUtil.isBlank(linkInfo.getCode())) {
      this.checkCode(linkInfo.getCode(), agentMinCodeNum);
    }
    if (agentMaxSpreadNum > 0) {
      // 校验此推广拥有几个推广码连接条数
      Long count =
              this.lambdaQuery().eq(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount()).count();
      Assert.isTrue((count + 1) <= agentMaxSpreadNum, "单个代理账户不能超过" + agentMaxSpreadNum + "条推广码链接！");
    }
    // 当推广链接不为空时 需要校验 此推广链接地址是否被其它代理作为了专属域名
    if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
      boolean exists =
              this.lambdaQuery()
                      .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
                      .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
                      .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                      .exists();
      if (exists) {
        throw new ServiceException("推广链接地址已被使用！");
      }
    }
    boolean saveResult = this.save(linkInfo);
    Assert.isTrue(saveResult, "创建失败！");
    if (saveResult
            && linkInfo.getIsOpenDividePreset() == 1
            && linkInfo.getUserType() == 1
            && BeanUtil.isNotEmpty(dto.getOwnerConfigMap())) {
      this.saveOrEditDivideConfig(
              linkInfo.getId(), linkInfo.getAgentAccount(), dto.getOwnerConfigMap());
    }
  }

  /**
   * 修改
   *
   * @param dto
   */
  @Override
  public void update(SpreadLinkInfoEditDTO dto) {
    SpreadLinkInfo linkInfo = spreadLinkInfoConvert.toEntity(dto);
//    if (StrUtil.isBlank(linkInfo.getAgentAccount())) {
//      throw new ServiceException("代理账号不能为空！");
//    }
    if (StrUtil.isBlank(linkInfo.getCode())) {
      throw new ServiceException("推广码不能为空！");
    }
    if (StrUtil.isNotBlank(linkInfo.getAgentAccount())) {
      // 校验账号的用户类型
      Member member =
              memberService
                      .getByAccount(linkInfo.getAgentAccount())
                      .orElseThrow(() -> new ServiceException("代理账号不存在!"));
      Assert.isTrue(UserTypes.AGENT.value().equalsIgnoreCase(member.getUserType()), "账号类型不支持！");
      linkInfo.setIsOpenDividePreset(dto.getIsOpenDividePreset());
    }

    // 当推广链接不为空时 需要校验 此推广链接地址是否被其它代理作为了专属域名
    if (StrUtil.isNotBlank(linkInfo.getExternalUrl())) {
      boolean exists =
              this.lambdaQuery()
                      .ne(SpreadLinkInfo::getAgentAccount, linkInfo.getAgentAccount())
                      .eq(SpreadLinkInfo::getExternalUrl, linkInfo.getExternalUrl())
                      .eq(SpreadLinkInfo::getExclusiveFlag, BooleanEnum.YES.value())
                      .exists();
      if (exists) {
        throw new ServiceException("推广链接地址已被使用！");
      }
    }
    boolean updateResult = this.updateById(linkInfo);
    Assert.isTrue(updateResult, "编辑失败！");
    if (updateResult
            && linkInfo.getIsOpenDividePreset() == 1
            && linkInfo.getUserType() == 1
            && BeanUtil.isNotEmpty(dto.getOwnerConfigMap())) {
      this.saveOrEditDivideConfig(
              linkInfo.getId(), linkInfo.getAgentAccount(), dto.getOwnerConfigMap());
    }
  }

  /**
   * 根据主键删除
   *
   * @param id
   */
  @Override
  public void deleteById(Long id) {
    this.removeById(id);
  }

  /**
   * 改变状态
   *
   * @param dto
   */
  @Override
  public void changeStatus(SpreadLinkInfoEditDTO dto) {
    if (!this.updateById(spreadLinkInfoConvert.toEntity(dto))) {
      throw new ServiceException("修改失败");
    }
  }

  /**
   * 增加推广码时间
   *
   * @param id Long
   */
  @Override
  public void changeReleaseTime(Long id) {
    if (!this.updateById(SpreadLinkInfo.builder().id(id).createTime(new Date()).build())) {
      throw new ServiceException("修改失败");
    }
  }

  /**
   * 批量启用
   *
   * @param ids
   */
  @Override
  public void batchEnableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
            .in(SpreadLinkInfo::getId, ids)
            .set(SpreadLinkInfo::getStatus, EnableEnum.ENABLED.code())
            .update(new SpreadLinkInfo())) {
      throw new ServiceException("批量修改状态失败!");
    }
  }

  /**
   * 批量关闭状态
   *
   * @param ids List
   */
  @Override
  public void batchDisableStatus(List<Long> ids) {
    if (!this.lambdaUpdate()
            .in(SpreadLinkInfo::getId, ids)
            .set(SpreadLinkInfo::getStatus, EnableEnum.DISABLED.code())
            .update(new SpreadLinkInfo())) {
      throw new ServiceException("批量修改状态失败!");
    }
  }

  /**
   * 批量删除
   *
   * @param ids List
   */
  @Override
  public void batchDeleteByIds(List<Long> ids) {
    if (!this.removeByIds(ids)) {
      throw new ServiceException("批量删除失败");
    }
  }

  /**
   * 根据代理账号获取代理信息
   *
   * @param agentAccount
   * @return
   */
  @Override
  public List<SpreadLinkInfo> getSpreadList(String agentAccount) {
    return this.lambdaQuery().eq(SpreadLinkInfo::getAgentAccount, agentAccount).list();
  }

  /**
   * 校验推广码
   *
   * @param code String
   */
  @Override
  public void checkCode(String code, Integer agentMinCodeNum) {
    String reg = "^[a-zA-Z0-9]{" + agentMinCodeNum + ",20}$";
    if (!code.matches(reg)) {
      String eStr = agentMinCodeNum == 20 ? "" : (agentMinCodeNum + "-");
      throw new ServiceException("推广码必须由" + eStr + "20位数字或字母组成！");
    }

    if (this.lambdaQuery()
            .eq(ObjectUtils.isNotEmpty(code), SpreadLinkInfo::getCode, code)
            .exists()) {
      throw new ServiceException("推广码已被使用！");
    }
  }

  /**
   * 根据用户名获取返点等级下拉
   *
   * @param account String
   * @param statisMax Boolean
   * @param statisMin Boolean
   * @return JSONArray
   */
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
        // 获取直属下级的最大返点等级
        BigDecimal userLowerMaxRebate = memberInfoMapper.findUserLowerMaxRebate(account);
        min =
                ObjectUtils.isNull(userLowerMaxRebate)
                        ? new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP)
                        : userLowerMaxRebate.setScale(2, BigDecimal.ROUND_HALF_UP);
        // 再获取此账号推广码的最大返点等级
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
   * 添加或编辑推广码分红配置预设
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
      // 获取到自己的分红配置
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

  /**
   * 获取最大推广码代理返点等级配置列表
   *
   * @param account
   * @return
   */
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


  /**
   * 查询默认域名
   */
  public Long getLinkCount(String link){
    return this.lambdaQuery().eq(SpreadLinkInfo::getExclusiveFlag,2).like(SpreadLinkInfo::getExternalUrl,link).count();
  }


  /**
   * 获取默认的推广链接信息
   */
  @Override
  public List<SpreadConfigVO> getDefaultLink(){
    List<SpreadConfigVO> collect = this.lambdaQuery().eq(SpreadLinkInfo::getExclusiveFlag, 2).list().stream().map(spreadLinkInfoConvert::toVo)
            .collect(Collectors.toList());
    return collect;
  }
}
