package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.enums.MemberBackupEnums;
import com.gameplat.admin.mapper.DivideLayerConfigMapper;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.SpreadLinkInfoMapper;
import com.gameplat.admin.mapper.TransferAgentMapper;
import com.gameplat.admin.model.dto.MemberTransBackupDTO;
import com.gameplat.admin.model.dto.MemberTransformDTO;
import com.gameplat.admin.model.dto.UpdateLowerNumDTO;
import com.gameplat.admin.model.vo.AgentInfoVo;
import com.gameplat.admin.model.vo.BetRecord;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.constant.ContextConstant;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.game.GameBizBean;
import com.gameplat.common.game.api.GameApi;
import com.gameplat.common.lang.Assert;
import com.gameplat.elasticsearch.service.IBaseElasticsearchService;
import com.gameplat.model.entity.game.GameBetRecord;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberTransferRecord;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberTransferAgentServiceImpl implements MemberTransferAgentService {

  @Autowired private MemberService memberService;

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberTransferRecordService memberTransferRecordService;

  @Autowired private ApplicationContext applicationContext;

  @Autowired private GameConfigService gameConfigService;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private DivideLayerConfigMapper layerConfigMapper;

  @Autowired private SpreadLinkInfoMapper spreadLinkInfoMapper;

  @Autowired private TransferAgentMapper transferAgentMapper;

  @Autowired private IBaseElasticsearchService baseElasticsearchService;

  @Autowired private SysTheme sysTheme;

  @Resource private ElasticsearchRestTemplate elasticsearchTemplate;

  @Override
  public void transform(MemberTransformDTO dto) {
    Member source = memberService.getById(dto.getId());
    Member target =
        memberService
            .getAgentByAccount(dto.getAgentAccount())
            .orElseThrow(() -> new ServiceException("账号不存在或当前账号不是代理账号！"));

    // 检查条件
    this.preCheck(source, target, dto.getExcludeSelf());
    // 转移
    this.doTransform(source, target, dto.getExcludeSelf(), dto.getSerialNo(), dto.getTransferWithData());
    // 更新彩票代理结构
    //    this.changeKgLotteryProxy(source, target);
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public GameApi getGameApi(String platformCode) {
    GameApi api =
      applicationContext.getBean(platformCode.toLowerCase() + GameApi.SUFFIX, GameApi.class);
    TransferTypesEnum tt = TransferTypesEnum.get(platformCode);
    // 1代表是否额度转换
    if (tt == null || tt.getType() != 1) {
      throw new ServiceException("游戏未接入");
    }
    return api;
  }

  private static final String backslash = "/";

  /**
   * 异步更新彩票代理结构
   */
  @Async
  public void changeKgLotteryProxy(Member source, Member target) {

    String superPath = target.getSuperPath().concat(source.getAccount()).concat(backslash);
    GameApi gameApi = getGameApi(GamePlatformEnum.KGNL.getCode());
    GameBizBean gameBizBean = GameBizBean.builder()
      .account(source.getAccount())
      .platformCode(GamePlatformEnum.KGNL.getCode())
      .superPath(superPath)
      .config(gameConfigService.getGameConfig(GamePlatformEnum.KGNL.getCode())).build();
    gameApi.changeGameProxy(gameBizBean);

  }

  @Override
  public void recover(String serialNo) {
    List<MemberTransferRecord> records = memberTransferRecordService.getBySerialNo(serialNo);
    // 解析备份内容
    List<MemberTransBackupDTO> contents = new ArrayList<>();
    records.forEach(record -> contents.addAll(this.parseContent(record.getContent())));

    // 批量恢复
    contents.forEach(
        e -> {
          log.info("恢复账号{}代理关系至{}", e.getAccount(), e.getParentName());
          Member source = this.getMemberByAccount(e.getParentName(), "恢复失败，用户信息不存在!");
          Member target = this.getMemberByAccount(e.getParentName(), "恢复失败，代理不存在!");
          this.doTransform(source, target);
        });

    // 更新最后恢复时间
    memberTransferRecordService.updateBatchById(records);
  }

  private Member getMemberByAccount(String account, String errorMessage) {
    return memberService
        .getByAccount(account)
        .orElseThrow(() -> new ServiceException(errorMessage));
  }

  /**
   * 转代理
   *
   * @param source 会员
   * @param target 目标代理
   */
  private void doTransform(Member source, Member target) {
    this.doTransform(source, target, false, null, false);
  }

  /**
   * 转代理
   *
   * @param source 会员
   * @param target 目标代理
   * @param excludeSelf 是否包含会员本身
   * @param serialNo 流水号存在时备份
   */
  private void doTransform(Member source, Member target, Boolean excludeSelf, String serialNo, Boolean transferWithData) {
    // 流水号不为空时添加记录
    if (StringUtils.isNotEmpty(serialNo)) {
      this.addTransferAgentRecord(serialNo, source, target.getAccount(), excludeSelf);
    }

    // 仅转移直属下级，更新原直属下级的直接上级
    if (Boolean.TRUE.equals(excludeSelf)) {
      this.updateDirectSuper(source.getAccount(), target);
    } else {
      // 转移全部时，只需更新当前会员的直属上级
      source.setParentId(target.getId());
      source.setParentName(target.getAccount());
      Assert.isTrue(source.updateById(), "更新会员信息失败!");
    }

    // 更新代理路径和代理/会员等级
    String newSuperPath = target.getSuperPath().concat(source.getAccount()).concat("/");
    int agentLevel = source.getAgentLevel() - target.getAgentLevel();
    BigDecimal userRebate = memberInfoService.findUserRebate(target.getAccount());

    // 批量删除 分红配置  推广分红预设 修改彩票返点
    this.delDivideConfig(excludeSelf ? source.getAccount() : null, source.getSuperPath(), userRebate);

    memberMapper.batchUpdateSuperPathAndAgentLevel(
        excludeSelf ? source.getAccount() : null, source.getSuperPath(), newSuperPath, agentLevel, userRebate);

    // 是否转移数据
    if (transferWithData) {
      this.transferData(excludeSelf ? source.getAccount() : null, source.getSuperPath(), newSuperPath);
    }

    // 更新下级人数
    this.updateLowerNum(source, target.getSuperPath(), excludeSelf);
  }

  /**
   * 转代理时 删除被转移账号的分红配置 推广链接的推广配置
   * @param account
   * @param originSuperPath
   * @param rebate
   */
  public void delDivideConfig(String account, String originSuperPath, BigDecimal rebate) {
    log.info("账号：{},旧的代理路径:{}", account, originSuperPath);
    Integer delCount = layerConfigMapper.batchDelConfigTrans(account, originSuperPath);
    log.info("清空分红配置成功:{}条,开始清空修改推广链接的分红预设和彩票代理返点", delCount);
    Integer updateCount = spreadLinkInfoMapper.dealDivide(account, originSuperPath, rebate);
    log.info("修改推广码链接成功:{}条", updateCount);
  }

  /**
   * 转移代理是否转移数据
   * @param account 代理账号
   * @param originSuperPath 旧的代理路径
   */
  public void transferData(String account,String originSuperPath, String newSuperPath){
    log.info("转移代理开始转移数据--旧的代理路径{}---新的代理路径{}", originSuperPath, newSuperPath);
    // 1.转移活动
    Integer integer = transferAgentMapper.transferActivityRecord(account, originSuperPath);
    log.info("转移活动数据完毕{}条", integer);
    // 2.资金流水
    Integer integer1 = transferAgentMapper.transferMemberBill(account, originSuperPath);
    log.info("转移资金流水完毕{}条", integer1);
    // 3.游戏日报
    Integer integer2 = transferAgentMapper.transferGameReport(account, originSuperPath);
    log.info("转移游戏日报完毕{}条", integer2);
    // 4.会员日报
    Integer integer3 = transferAgentMapper.transferMemberReport(account, originSuperPath);
    log.info("转移会员日报完毕{}条", integer3);
    // 5.充值订单
    Integer integer4 = transferAgentMapper.transferRechargeRecord(account, originSuperPath);
    log.info("转移充值订单完毕{}条", integer4);
    // 6.提现订单
    Integer integer5 = transferAgentMapper.transferWithdrawRecord(account, originSuperPath);
    log.info("转移提现订单完毕{}条", integer5);
    // 7.返水订单
    Integer integer6 = transferAgentMapper.transferGameRebateReport(account, originSuperPath);
    log.info("转移返水订单完毕{}条", integer6);
    // 8.VIP福利记录
    Integer integer7 = transferAgentMapper.transferWealRecord(account, originSuperPath);
    log.info("转移VIP福利记录完毕{}条", integer7);
    // 9.充提记录
    Integer integer8 = transferAgentMapper.transferRwRecord(account, originSuperPath);
    log.info("转移充提记录完毕{}条", integer8);
    log.info("转移代理基础数据完毕--旧的代理路径{}---新的代理路径{}", originSuperPath, newSuperPath);
    // 9.转移ES注单
    this.transferEsBetRecord(originSuperPath);
  }

  @Async
  public void transferEsBetRecord(String originSuperPath){
    List<GameBetRecord> resultList = new ArrayList();
    try {
      String tenantCode = sysTheme.getTenantCode();
      String indexName = ContextConstant.ES_INDEX.BET_RECORD_ + tenantCode;
      BoolQueryBuilder builder = QueryBuilders.boolQuery();
      builder.should(QueryBuilders.matchPhraseQuery("superPath", "*" + originSuperPath + "*"));
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(builder);
      SearchRequest searchRequest = new SearchRequest(indexName);
      searchRequest.source(searchSourceBuilder);
      log.info("转代理查询用户注单 DSL语句为：{}", searchRequest.source().toString());
      resultList = baseElasticsearchService.searchDocList(
              indexName, searchSourceBuilder, GameBetRecord.class);
      log.info("转代理获取到的转移的用户注册：{}", resultList.size());
      if (CollectionUtil.isNotEmpty(resultList)) {
        log.info("开始更新ES注单记录的代理路径");
        Map<String, AgentInfoVo> agentInfoVoMap = new HashMap<>();
        List<BetRecord> listRecord = new ArrayList<>();
        for (GameBetRecord gameBetRecord : resultList) {
          if (StrUtil.isBlank(gameBetRecord.getAccount())) {
            continue;
          }
          AgentInfoVo agentInfoVo = agentInfoVoMap.get(gameBetRecord.getAccount());
          if(BeanUtil.isEmpty(agentInfoVo)) {
            AgentInfoVo queryInfo = memberMapper.queryAgentInfo(gameBetRecord.getAccount());
            if (BeanUtil.isEmpty(queryInfo)) {
              continue;
            }
            agentInfoVoMap.put(gameBetRecord.getAccount(), queryInfo);
            gameBetRecord.setParentName(queryInfo.getParentName());
            gameBetRecord.setSuperPath(queryInfo.getAgentPath());
          } else {
            gameBetRecord.setParentName(agentInfoVo.getParentName());
            gameBetRecord.setSuperPath(agentInfoVo.getAgentPath());
          }


          BetRecord betRecord = new BetRecord();
          BeanUtil.copyProperties(gameBetRecord, betRecord);
          listRecord.add(betRecord);
        }

        if (CollectionUtil.isNotEmpty(listRecord)) {
          elasticsearchTemplate.save(
                  listRecord, IndexCoordinates.of(ContextConstant.ES_INDEX.BET_RECORD_ + tenantCode)
          );
        }
      }
    } catch (Exception e) {
      log.info("转移代理更新注单代理路径失败{}", e);
      return;
    }

    if (CollectionUtil.isNotEmpty(resultList)) {
      this.transferEsBetRecord(originSuperPath);
    }
  }

  private void preCheck(Member source, Member target, Boolean excludeSelf) {
    Assert.notNull(source, "会员信息不存在！");
    Assert.isFalse(isInnerAccount(source.getAccount()), "不能转移系统保留账号！");
    Assert.isFalse(StringUtils.equals(source.getAccount(), target.getAccount()), "不能自己转移自己！");
    Assert.isFalse(
        Boolean.FALSE.equals(excludeSelf)
            && StringUtils.equals(source.getParentName(), target.getAccount()),
        "已是目标代理，不允许重复转移");

    Assert.isFalse(target.getSuperPath().startsWith(source.getSuperPath()), "不能转移到自己的下级代理线下！");

    // 不包含自身时检查是否存在下级
    Assert.isFalse(Boolean.TRUE.equals(excludeSelf) && source.getLowerNum() == 0, "当前会员没有可转移的下级！");
  }

  private boolean isInnerAccount(String account) {
    return SystemConstant.DEFAULT_WEB_ROOT.equals(account)
        || SystemConstant.DEFAULT_WAP_ROOT.equals(account)
        || SystemConstant.DEFAULT_TEST_ROOT.equals(account);
  }

  /**
   * 更新直属上级信息
   *
   * @param account 会员账号
   * @param agent Member
   */
  private void updateDirectSuper(String account, Member agent) {
    memberService
        .lambdaUpdate()
        .set(Member::getParentName, agent.getAccount())
        .set(Member::getParentId, agent.getId())
        .eq(Member::getParentName, account)
        .update();
  }

  /**
   * 更新下级人数
   *
   * @param source 会员
   * @param targetSuperPath 目标代理路径
   * @param excludeSelf 为true不包含本身（仅转移下级)
   */
  private void updateLowerNum(Member source, String targetSuperPath, Boolean excludeSelf) {
    int lowerNum = source.getLowerNum();
    Map<String, Integer> originMap = this.getOriginLineLowNum(source, excludeSelf, lowerNum);
    Map<String, Integer> newMap = this.getNewtLineLowNum(targetSuperPath, excludeSelf, lowerNum);
    // 合并相同账号，减少修改次数
    originMap.forEach((k, v) -> newMap.merge(k, v, Integer::sum));
    this.batchUpdateLowerNumWithPartition(newMap);
  }

  /**
   * 处理新上级
   *
   * @param superPath 新上级代理路径
   * @param excludeSelf boolean
   * @param m 下级人数
   * @return List
   */
  private Map<String, Integer> getNewtLineLowNum(String superPath, Boolean excludeSelf, int m) {
    List<String> accounts = this.splitSuperPath(superPath);
    // 仅转移下级，新代理线下级人数增加M个；转移全部，新代理线下级人数增加M+1个
    Map<String, Integer> map = new HashMap<>(accounts.size());
    accounts.forEach(account -> map.put(account, Boolean.TRUE.equals(excludeSelf) ? m : m + 1));
    return map;
  }

  /**
   * 处理原上级
   *
   * @param source Member
   * @param excludeSelf boolean
   * @param m 下级人数
   * @return List<UpdateLowerNumDTO>
   */
  private Map<String, Integer> getOriginLineLowNum(Member source, Boolean excludeSelf, int m) {
    List<String> accounts = this.splitSuperPath(source.getSuperPath());
    // 转移全部时，删除自身

    accounts = CollUtil.removeWithAddIf(new ArrayList<>(), e -> Boolean.TRUE.equals(excludeSelf) && e.equals(source.getAccount()));

    // 仅转移下级，自身下级人数归零，原代理线下级人数减少M个；转移全部，其他代理线会员人数减少M+1个
    Map<String, Integer> map = new HashMap<>(accounts.size());
    accounts.forEach(account -> map.put(account, Boolean.FALSE.equals(excludeSelf) ? -m : -(m + 1)));
    return map;
  }

  /**
   * 分批修改会员下级人数
   *
   * @param map Map<String, Integer>
   */
  private void batchUpdateLowerNumWithPartition(Map<String, Integer> map) {
    List<UpdateLowerNumDTO> list = new ArrayList<>(map.size());
    map.forEach((k, v) -> list.add(new UpdateLowerNumDTO(k, v)));

    List<List<UpdateLowerNumDTO>> partition = Lists.partition(list, 50);
    partition.parallelStream().forEach(e -> memberMapper.batchUpdateLowerNumByAccount(e));
  }

  /**
   * 添加转代理记录
   *
   * @param serialNo 流水号
   * @param member 会员信息
   * @param target 目标代理
   */
  private void addTransferAgentRecord(
      String serialNo, Member member, String target, Boolean excludeSelf) {
    List<Member> backupMembers = new ArrayList<>();
    if (Boolean.TRUE.equals(excludeSelf)) {
      // 记录当前会员的直属下级
      backupMembers.addAll(memberService.getByParentName(member.getAccount()));
    } else {
      // 记录当前会员
      backupMembers.add(member);
    }

    memberTransferRecordService.save(
        MemberTransferRecord.builder()
            .serialNo(serialNo)
            .content(JsonUtils.toJson(this.builderContent(backupMembers, target)))
            .type(MemberBackupEnums.Type.AGENT.value())
            .build());
  }

  private List<MemberTransBackupDTO> builderContent(List<Member> backupMembers, String target) {
    return backupMembers.stream()
        .map(
            member ->
                MemberTransBackupDTO.builder()
                    .userId(member.getId())
                    .account(member.getAccount())
                    .agentLevel(member.getAgentLevel())
                    .parentId(member.getParentId())
                    .parentName(member.getParentName())
                    .superPath(member.getSuperPath())
                    .target(target)
                    .build())
        .collect(Collectors.toList());
  }

  private List<MemberTransBackupDTO> parseContent(String content) {
    return JsonUtils.parse(content, new TypeReference<List<MemberTransBackupDTO>>() {});
  }

  private List<String> splitSuperPath(String superPath) {
    return Arrays.asList(superPath.substring(1, superPath.length() - 1).split("/"));
  }
}
