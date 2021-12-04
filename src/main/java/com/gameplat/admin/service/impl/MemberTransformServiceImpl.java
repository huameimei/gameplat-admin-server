package com.gameplat.admin.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.enums.MemberBackupEnums;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MemberBackup;
import com.gameplat.admin.model.dto.MemberTransBackupDTO;
import com.gameplat.admin.model.dto.MemberTransformDTO;
import com.gameplat.admin.model.dto.UpdateLowerNumDTO;
import com.gameplat.admin.service.MemberBackupService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.MemberTransformService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.base.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberTransformServiceImpl implements MemberTransformService {

  @Autowired private MemberService memberService;

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberBackupService memberBackupService;

  @Override
  public void transform(MemberTransformDTO dto) {
    // 获取需转移的会员信息
    Member source = memberService.getById(dto.getId());

    // 根据账号获取代理信息
    Member target =
        memberService
            .getAgentByAccount(dto.getAgentAccount())
            .orElseThrow(() -> new ServiceException("账号不存在或当前账号不是代理账号！"));

    // 检查条件
    this.preCheck(source, target, dto.getExcludeSelf());

    // 转移
    this.doTransform(source, target, dto.getExcludeSelf(), dto.getSerialNo());
  }

  @Override
  public void recover(String serialNo) {
    List<MemberBackup> backups = memberBackupService.getBySerialNo(serialNo);
    // 解析备份内容
    List<MemberTransBackupDTO> contents = new ArrayList<>();
    backups.forEach(backup -> contents.addAll(this.parseBackupContent(backup.getContent())));

    // 批量恢复
    contents.forEach(
        e -> {
          log.info("恢复账号{}代理关系至{}", e.getAccount(), e.getParentName());
          Member source =
              memberService
                  .getByAccount(e.getAccount())
                  .orElseThrow(() -> new ServiceException("恢复失败，用户信息不存在!"));

          // 原上级
          Member target =
              memberService
                  .getByAccount(e.getParentName())
                  .orElseThrow(() -> new ServiceException("恢复失败，代理不存在!"));

          // 转代理
          this.doTransform(source, target);
        });

    // 更新最后恢复时间
    memberBackupService.updateBatchById(backups);
  }

  /**
   * 转代理
   *
   * @param source 会员
   * @param target 目标代理
   */
  private void doTransform(Member source, Member target) {
    this.doTransform(source, target, false, null);
  }

  /**
   * 转代理
   *
   * @param source 会员
   * @param target 目标代理
   * @param excludeSelf 是否包含会员本身
   * @param serialNo 流水号存在时备份
   */
  private void doTransform(Member source, Member target, Boolean excludeSelf, String serialNo) {
    Member updateMember =
        Member.builder()
            .account(source.getAccount())
            .agentLevel(source.getAgentLevel())
            .superPath(target.getSuperPath())
            .superAgentLevel(target.getAgentLevel())
            .build();

    // 流水号不为空时备份
    if (StringUtils.isNotEmpty(serialNo)) {
      this.addBackup(serialNo, source, target.getAccount(), excludeSelf);
    }

    if (Boolean.TRUE.equals(excludeSelf)) {
      // 更新原直属下级的直接上级
      this.updateDirectSuper(source.getAccount(), target);
    } else {
      // 转移全部时，只需更新当前会员的直属上级
      source.setParentId(target.getId());
      source.setParentName(target.getAccount());
      if (!source.updateById()) {
        throw new ServiceException("更新会员信息失败!");
      }
    }

    // 更新代理路径
    this.batchUpdateSuperPath(updateMember, excludeSelf);

    // 更新下级人数
    this.batchUpdateLowerNum(source, target.getSuperPath(), excludeSelf);
  }

  private void batchUpdateSuperPath(Member update, Boolean excludeSelf) {
    if (Boolean.TRUE.equals(excludeSelf)) {
      // 仅更新下级代理路径
      memberMapper.batchUpdateSuperPathExcludeSelf(update);
    } else {
      // 更新全部
      memberMapper.batchUpdateSuperPath(update);
    }
  }

  private void preCheck(Member source, Member target, Boolean excludeSelf) {
    if (null == source) {
      throw new ServiceException("会员信息不存在！");
    }

    if (StringUtils.equals(source.getAccount(), target.getAccount())) {
      throw new ServiceException("不能自己转移自己！");
    }

    if (Boolean.FALSE.equals(excludeSelf)
        && StringUtils.equals(source.getParentName(), target.getAccount())) {
      throw new ServiceException("已是目标代理，不允许重复转移！");
    }

    if (SystemConstant.DEFAULT_WEB_ROOT.equals(source.getAccount())) {
      throw new ServiceException("不能转移系统保留账号！");
    }

    if (SystemConstant.DEFAULT_WAP_ROOT.equals(source.getAccount())) {
      throw new ServiceException("不能转移系统保留账号！");
    }

    if (SystemConstant.DEFAULT_TEST_ROOT.equals(source.getAccount())) {
      throw new ServiceException("不能转移系统保留账号！");
    }

    if (target.getSuperPath().startsWith(source.getSuperPath())) {
      throw new ServiceException("不能转移到自己的下级代理线下！");
    }

    // 不包含自身时检查是否存在下级
    if (Boolean.TRUE.equals(excludeSelf) && source.getLowerNum() == 0) {
      throw new ServiceException("当前会员没有可转移的下级");
    }
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
  private void batchUpdateLowerNum(Member source, String targetSuperPath, Boolean excludeSelf) {
    int lowerNum = source.getLowerNum();
    List<UpdateLowerNumDTO> list = new ArrayList<>();
    this.splitSuperPath(source.getSuperPath())
        .forEach(
            account -> {
              if (Boolean.TRUE.equals(excludeSelf)) {
                // 仅转移下级时，修改自身和上级的下级人数
                list.add(new UpdateLowerNumDTO(account, -lowerNum));
              } else if (account.equals(source.getAccount())) {
                // 转移全部时当前会员下级人数归零
                list.add(new UpdateLowerNumDTO(account, -lowerNum));
              } else {
                // 减去当前会员
                list.add(new UpdateLowerNumDTO(account, -lowerNum - 1));
              }
            });

    // 处理新上级下级人数
    this.splitSuperPath(targetSuperPath)
        .forEach(
            account -> {
              if (Boolean.TRUE.equals(excludeSelf)) {
                list.add(new UpdateLowerNumDTO(account, lowerNum));
              } else {
                // 转移全部时包含自身
                list.add(new UpdateLowerNumDTO(account, lowerNum + 1));
              }
            });

    // 修改下级人数
    list.forEach(
        e -> {
          log.info("变更代理线更新下级人数，账号{}下级人数{}", e.getAccount(), e.getLowerNum());
          memberMapper.updateLowerNumByAccount(e.getAccount(), e.getLowerNum());
        });
  }

  /**
   * 添加备份
   *
   * @param serialNo 流水号
   * @param member 会员信息
   * @param target 目标代理
   */
  private void addBackup(String serialNo, Member member, String target, Boolean excludeSelf) {
    List<Member> backupMembers = new ArrayList<>();
    if (Boolean.TRUE.equals(excludeSelf)) {
      // 备份当前会员的直属下级
      backupMembers.addAll(memberService.getByParentName(member.getAccount()));
    } else {
      // 备份当前会员
      backupMembers.add(member);
    }

    // 添加备份
    memberBackupService.save(
        MemberBackup.builder()
            .serialNo(serialNo)
            .content(JsonUtils.toJson(this.builderBackupContent(backupMembers, target)))
            .type(MemberBackupEnums.Type.AGENT.value())
            .build());
  }

  private List<MemberTransBackupDTO> builderBackupContent(
      List<Member> backupMembers, String target) {
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

  private List<MemberTransBackupDTO> parseBackupContent(String content) {
    return JsonUtils.parse(content, new TypeReference<List<MemberTransBackupDTO>>() {});
  }

  private List<String> splitSuperPath(String superPath) {
    return Arrays.asList(superPath.substring(1, superPath.length() - 1).split("/"));
  }
}
