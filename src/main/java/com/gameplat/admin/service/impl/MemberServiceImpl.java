package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.component.MemberQueryCondition;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.constant.SystemConstant;
import com.gameplat.admin.convert.MemberConvert;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.mapper.SysFileManagerMapper;
import com.gameplat.admin.model.bean.RechargeMemberFileBean;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.*;
import com.gameplat.admin.service.*;
import com.gameplat.admin.util.JxlsExcelUtils;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.base.common.util.UUIDUtils;
import com.gameplat.common.compent.oss.FileStorageEnum;
import com.gameplat.common.compent.oss.FileStorageStrategyContext;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.enums.MemberEnums;
import com.gameplat.common.enums.TransferTypesEnum;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.util.FileUtils;
import com.gameplat.model.entity.game.GameTransferInfo;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberDayReport;
import com.gameplat.model.entity.member.MemberInfo;
import com.gameplat.model.entity.sys.SysFileManager;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import com.google.common.collect.Lists;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.log4j.Log4j2;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sun.security.provider.certpath.BuildStep.SUCCEED;

@Log4j2
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

  private static final String DL_FORMAL_TYPE = "A";

  // 姓名
  private static final String REALNAME_CODE = "([\\d\\D]{1})(.*)";

  private static final String REALNAME_REPLACEMENT = "$1**";

  // 手机号
  private static final String PHONE_CODE = "(\\d{3})\\d{4}(\\d{4})";

  private static final String PHONE_REPLACEMENT = "$1****$2";

  // 邮箱
  private static final String EMAIL_CODE = "(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)";

  private static final String EMAIL_REPLACEMENT = "$1****$3$4";

  private static final String ROLES = "system:member:contact:detail";


  @Autowired(required = false)
  private MemberMapper memberMapper;

  @Autowired private MemberConvert memberConvert;

  @Autowired private MemberInfoService memberInfoService;

  @Autowired private MemberQueryCondition memberQueryCondition;

  @Autowired private PasswordService passwordService;

  @Autowired private MemberRemarkService memberRemarkService;

  @Autowired private OnlineUserService onlineUserService;

  @Autowired private SysTheme sysTheme;

  @Autowired private GameTransferInfoService gameTransferInfoService;

  @Autowired private SpreadLinkInfoService spreadLinkInfoService;

  @Autowired private GameAdminService gameAdminService;

  @Autowired(required = false)
  private RedisTemplate<String, Integer> redisTemplate;

  @Autowired(required = false)
  private RedisTemplate<String, Object> redisTemplateObj;

  @Autowired private MemberDayReportService memberDayReportService;


  private static final String MEMBER_TOKEN_PREFIX = "token:web:";

  @Resource private FileConfig config;

  @Autowired private FileStorageStrategyContext fileStorageStrategyContext;

  @Autowired private SysFileManagerMapper sysFileManagerMapper;

  @Autowired private OssService ossService;

  @Override
  public IPage<MessageDistributeVO> pageMessageDistribute(Page<Member> page, MemberQueryDTO dto) {
    return memberMapper
        .queryPage(page, memberQueryCondition.builderQueryWrapper(dto))
        .convert(this::setOnlineStatus)
        .convert(memberConvert::toVo);
  }


  /**
   * 真实姓名
   *
   * @param realName String
   * @return String
   */
  private static String hideRealName(String realName) {
    return realName.replaceAll(REALNAME_CODE, REALNAME_REPLACEMENT);
  }

  /**
   * 手机号
   *
   * @param phone String
   * @return String
   */
  private static String desensitizedPhoneNumber(String phone) {
    if (phone.length() == 11) {
      return phone.replaceAll(PHONE_CODE, PHONE_REPLACEMENT);
    } else {
      String substring = phone.substring(0, 3);
      int length = phone.length();
      String lastString = phone.substring(length - 2, length);
      return substring + "****" + lastString;
    }
  }


  /** 邮箱影藏 */
  private static String getEmail(String email) {
    return email.replaceAll(EMAIL_CODE, EMAIL_REPLACEMENT);
  }


  @Override
  public List<MemberVO> queryList(MemberQueryDTO dto) {
    return memberMapper.queryList(memberQueryCondition.builderQueryWrapper(dto));
  }

  @Override
  public Integer countMembers(MemberQueryDTO dto) {
    return memberMapper.countMmebers(memberQueryCondition.builderQueryWrapper(dto));
  }

  @Override
  public MemberInfoVO getInfo(Long id) {
    return memberMapper.getMemberInfo(id);
  }

  /**
   * 根据id获取详情
   *
   * @param id
   * @return
   */
  @Override
  public MemberInfoVO getMemberDateils(Long id) {
    MemberInfoVO memberInfo = memberMapper.getMemberInfo(id);
    // 真实姓名
    if (StringUtils.isNotEmpty(memberInfo.getRealName())) {
      memberInfo.setRealName(hideRealName(memberInfo.getRealName()));
    }
    // 手机号
    if (StringUtils.isNotEmpty(memberInfo.getPhone())) {
      memberInfo.setPhone(desensitizedPhoneNumber(memberInfo.getPhone()));
    }
    // 邮箱
    if (StringUtils.isNotEmpty(memberInfo.getEmail())) {
      memberInfo.setEmail(getEmail(memberInfo.getEmail()));
    }
    // QQ
    if (StringUtils.isNotEmpty(memberInfo.getQq())) {
      memberInfo.setQq(desensitizedPhoneNumber(memberInfo.getQq()));
    }
    // 微信
    if (StringUtils.isNotEmpty(memberInfo.getWechat())) {
      memberInfo.setWechat(desensitizedPhoneNumber(memberInfo.getWechat()));
    }

    return memberInfo;
  }


  @Override
  public MemberInfoVO getMemberInfo(Long id) {
    return memberMapper.getMemberInfo(id);
  }

  @Override
  public MemberInfoVO getMemberInfo(String account) {
    return memberMapper.getMemberInfoByAccount(account);
  }

  @Override
  public void add(MemberAddDTO dto) {
    this.preAddCheck(dto);

    Member member = memberConvert.toEntity(dto);
    member.setAccount(dto.getAccount().toLowerCase());
    member.setRegisterType(MemberEnums.RegisterType.ADMIN_ADD.value());
    member.setRegisterSource(MemberEnums.RegisterSource.WEB.value());
    member.setPassword(
        passwordService.encode(member.getPassword(), dto.getAccount().toLowerCase()));

    // 设置上级
    this.setMemberParent(member);

    // 保存会员和会员详情
    Assert.isTrue(this.save(member), "新增会员失败!");

    MemberInfo memberInfo =
        MemberInfo.builder()
            .memberId(member.getId())
            .rebate(dto.getRebate())
            .salaryFlag(dto.getSalaryFlag())
            .build();
    Assert.isTrue(memberInfoService.save(memberInfo), "新增会员失败!");
  }

  @Override
  public void update(MemberEditDTO dto) {
    Member member = this.getById(dto.getId());

    MemberInfo memberInfo =
        MemberInfo.builder()
            .memberId(dto.getId())
            .rebate(dto.getRebate())
            .salaryFlag(dto.getSalaryFlag())
            .build();

    // 更新会员信息和会员详情
    Assert.isTrue(
        this.updateById(memberConvert.toEntity(dto)) && memberInfoService.updateById(memberInfo),
        "修改会员信息失败!");

    // 获取会员状态
    Integer status = dto.getStatus();
    if (status != null) {
      // 如果状态是正常或冻结，则需要更新redis里的会员信息
      if (MemberEnums.Status.FROZEN.match(status) || MemberEnums.Status.ENABlED.match(status)) {
          Object object = redisTemplateObj.opsForValue().get(MEMBER_TOKEN_PREFIX + member.getAccount());
        if (object != null) {
          UserCredential userCredential = (UserCredential) object;
          userCredential.setStatus(status);
          redisTemplateObj.opsForValue().set(MEMBER_TOKEN_PREFIX + member.getAccount(), userCredential);
        }
        // 如果状态是禁用，则需要踢线操作
      } else if (MemberEnums.Status.DISABLED.match(status)) {
        redisTemplateObj.delete(MEMBER_TOKEN_PREFIX + member.getAccount());
      }
    }
  }


  @Override
  public void enable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.ENABlED.value());
  }

  @Override
  public void disable(List<Long> ids) {
    this.changeStatus(ids, MemberEnums.Status.DISABLED.value());

    // 查询所有需要禁用的会员账号
    List<Member> memberList = this.lambdaQuery().in(Member::getId, ids).list();

    // 对每个禁用的会员账号踢线处理
    for(Member member : memberList){
      redisTemplateObj.delete(MEMBER_TOKEN_PREFIX + member.getAccount());
    }

    // TODO 查询是否有进入第三方游戏，第三方游戏踢线并回收额度
  }

  @Override
  public void clearContact(MemberContactCleanDTO dto) {
    this.update()
        .func(query -> dto.getFields().forEach(field -> query.set(field, null)))
        .between("create_time", dto.getStartTime(), dto.getEndTime())
        .update(new Member());
  }

  @Override
  public Member getById(Long id) {
    return this.lambdaQuery()
        .eq(Member::getId, id)
        .oneOpt()
        .orElseThrow(() -> new ServiceException("会员信息不存在!"));
  }

  @Override
  public Optional<Member> getByAccount(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).oneOpt();
  }

  @Override
  public Optional<Member> getAgentByAccount(String account) {
    return this.lambdaQuery()
        .eq(Member::getAccount, account)
        .eq(Member::getUserType, MemberEnums.Type.AGENT.value())
        .oneOpt();
  }

  @Override
  public Optional<String> getSupperPath(String account) {
    return this.lambdaQuery()
        .select(Member::getSuperPath)
        .eq(Member::getAccount, account)
        .oneOpt()
        .map(Member::getSuperPath);
  }

  @Override
  public List<Member> getByParentName(String parentName) {
    return this.lambdaQuery().eq(Member::getParentName, parentName).list();
  }

  @Override
  public List<String> findAccountByUserLevelIn(List<String> levelsLists) {
    return this.lambdaQuery()
        .select(Member::getAccount)
        .in(Member::getUserLevel, levelsLists)
        .list()
        .stream()
        .map(Member::getAccount)
        .collect(Collectors.toList());
  }

  @Override
  public void updateContact(MemberContactUpdateDTO dto) {
    Assert.isTrue(this.updateById(memberConvert.toEntity(dto)), "更新会员联系方式失败!");
  }

  @Override
  public void resetPassword(MemberPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    String password = passwordService.encode(dto.getPassword(), member.getAccount().toLowerCase());

    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getPassword, password)
                .set(Member::getRegisterType, MemberEnums.RegisterType.ONLINE.value())
            .eq(Member::getId, member.getId())
            .update(new Member()),
        "重置会员密码失败!");

    // 更新会员备注
    if (StringUtils.isNotBlank(dto.getRemark())) {
      memberRemarkService.update(dto.getId(), dto.getRemark());
    }
  }

  @Override
  public void resetWithdrawPassword(MemberWithdrawPwdUpdateDTO dto) {
    Member member = this.getById(dto.getId());
    Assert.isTrue(
        memberInfoService
            .lambdaUpdate()
                .set(MemberInfo::getCashPassword, "")
            .eq(MemberInfo::getMemberId, member.getId())
            .update(new MemberInfo()),
        "重置会员提现密码失败!");
  }

  @Override
  public void changeWithdrawFlag(Long id, String flag) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getWithdrawFlag, flag)
            .eq(Member::getId, id)
            .update(new Member()),
        "修改会员提现状态失败!");
  }

  @Override
  public void resetRealName(MemberResetRealNameDTO dto) {
    Member member = this.getById(dto.getId());
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getRealName, dto.getRealName())
            .eq(Member::getId, member.getId())
            .update(new Member()),
        "重置会员真实姓名失败!");
  }

  @Override
  public List<Member> getListByUserLevel(List<String> userLevelList) {
    return Optional.ofNullable(userLevelList)
        .filter(CollectionUtil::isNotEmpty)
        .map(e -> this.lambdaQuery().in(Member::getUserLevel, e).list())
        .orElse(null);
  }

  @Override
  public List<Member> getListByAgentAccount(String agentAccout) {
    return memberMapper.getListByAgentAccout(agentAccout);
  }

  @Override
  public void updateRealName(Long memberId, String realName) {
    Assert.isTrue(
        this.lambdaUpdate().set(Member::getRealName, realName).eq(Member::getId, memberId).update(),
        "修改会员真实姓名失败!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberIds", multi = true)
  public void updateRemark(List<Long> memberIds, String remark) {
    Assert.isTrue(
        this.lambdaUpdate()
            .in(Member::getId, memberIds)
            .set(Member::getRemark, remark)
            .update(new Member()),
        "批量修改会员备注失败!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberId")
  public void updateRemark(Long memberId, String remark) {
    this.updateRemark(Collections.singletonList(memberId), remark);
  }

  @Override
  public IPage<MemberVO> queryPage(Page<Member> page, MemberQueryDTO dto) {
    IPage<MemberVO> pageVo =
        memberMapper
            .queryPage(page, memberQueryCondition.builderQueryWrapper(dto))
            .convert(this::setOnlineStatus);

    this.setGameData(pageVo.getRecords());
    return pageVo;
  }

  /**
   * 检查用户名是否存在
   *
   * @param account String
   * @return boolean
   */
  private boolean isExist(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).exists();
  }

  /**
   * 设置在线状态
   *
   * @param vo MemberVO
   * @return MemberVO
   */
  private MemberVO setOnlineStatus(MemberVO vo) {
    vo.setOnline(onlineUserService.isOnline(vo.getAccount()));
    return vo;
  }

  /**
   * 设置会员上级
   *
   * @param member Member
   */
  private void setMemberParent(Member member) {
    String parentName =
        StringUtils.getIfEmpty(
            member.getParentName(), () -> this.getMemberRoot(member.getUserType()));

    Member parent =
        this.getByAccount(parentName).orElseThrow(() -> new ServiceException("代理账号不存在!"));

    member.setParentId(parent.getId());
    member.setParentName(parent.getAccount());
    member.setSuperPath(parent.getSuperPath().concat(member.getAccount()).concat("/"));

    if (MemberEnums.Type.AGENT.match(member.getUserType())
        || MemberEnums.Type.PROMOTION.match(member.getUserType())) {
      member.setAgentLevel(parent.getAgentLevel() + 1);
    }

    // 更新下级人数
    memberMapper.updateLowerNumByAccount(parent.getAccount(), 1);
  }

  @Override
  public MemberContactVo getMemberDetail(Long id) {
    MemberInfoVO memberInfo = this.memberMapper.getMemberInfo(id);
    if (memberInfo == null) {
      throw new ServiceException("会员不存在");
    }

    return MemberContactVo.builder()
        .email(memberInfo.getEmail())
        .phone(memberInfo.getPhone())
        .qq(memberInfo.getQq())
        .realName(memberInfo.getRealName())
        .wechat(memberInfo.getWechat())
        .dialCode(memberInfo.getDialCode())
        .build();
  }

  @Override
  public void batchLevel(MemberLevelBatchDTO dto) {
    this.lambdaUpdate()
            .set(Member::getUserLevel, dto.getLevel())
            .in(Member::getId, dto.getIds())
            .update();
  }

  /**
   * 导出excel 文件
   *
   * @param dto
   * @param response
   */
  @Override
  public void exportMembersReport(MemberQueryDTO dto, HttpServletResponse response) {
    try {
      List<MemberVO> members = this.queryList(dto);
      // 定义ZIP包的包名
      String zipFileName = "会员信息";
      response.setHeader(
          "Content-Disposition",
          "attachment;fileName=" + URLEncoder.encode(zipFileName + ".zip", "UTF-8"));
      response.setContentType("application/zip");
      String tmpUrl =
          System.getProperty("java.io.tmpdir") + File.separator + "excel-" + UUIDUtils.getUUID32();
      final File dir = new File(tmpUrl);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      Map<String, Object> map = new HashMap<>();
      map.put("dataList", members);
      String fileName = "会员信息.xlsx";
      FileOutputStream fo = null;
      try {
        fo = new FileOutputStream(new File(dir + File.separator + fileName));
      } catch (FileNotFoundException e1) {
        e1.printStackTrace();
      }
      try {
        JxlsExcelUtils.downLoadExcel(map, "membersTemplate.xlsx", fo);
      } catch (InvalidFormatException | IOException e1) {
        e1.printStackTrace();
      } finally {
        if (fo != null) {
          try {
            fo.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }

      ZipFile zipFile = new ZipFile(tmpUrl.concat(".zip"));
      ZipParameters parameters = new ZipParameters();
      // 压缩方式
      parameters.setCompressionMethod(CompressionMethod.DEFLATE);
      // 压缩级别
      parameters.setCompressionLevel(CompressionLevel.NORMAL);
      // 是否设置加密文件
      parameters.setEncryptFiles(true);
      // 设置加密算法
      parameters.setEncryptionMethod(EncryptionMethod.AES);
      // 设置AES加密密钥的密钥强度
      parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
      // 设置密码
      if (StrUtil.isNotBlank(dto.getZipPswd())) {
        zipFile.setPassword(dto.getZipPswd().toCharArray());
      }
      // 要打包的文件夹
      File[] fList = dir.listFiles();

      // 遍历test文件夹下所有的文件、文件夹
      for (File f : fList) {
        if (f.isDirectory()) {
          zipFile.addFolder(f, parameters);
        } else {
          zipFile.addFile(f, parameters);
        }
      }

      OutputStream out = response.getOutputStream();
      out.write(FileUtil.readBytes(zipFile.getFile()));
      out.flush();
      FileUtil.del(dir);
      FileUtil.del(tmpUrl.concat(".zip"));
    } catch (Exception e) {
      throw new ServiceException("会员信息导出IO错误:{}", e);
    }
  }

  @Override
  @Async
  public void asynExportMembersReport(MemberQueryDTO dto) {
    List<MemberVO> members = this.queryList(dto);
    // 定义ZIP包的包名
    String zipFileName = "会员信息";
    String tmpUrl =
        System.getProperty("java.io.tmpdir") + File.separator + "excel-" + UUIDUtils.getUUID32();
    final File dir = new File(tmpUrl);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    Map<String, Object> map = new HashMap<>();
    map.put("dataList", members);
    String fileName = "会员信息.xlsx";
    FileOutputStream fo = null;
    try {
      fo = new FileOutputStream(new File(dir + File.separator + fileName));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      JxlsExcelUtils.downLoadExcel(map, "membersTemplate.xlsx", fo);
    } catch (InvalidFormatException | IOException e1) {
      e1.printStackTrace();
    } finally {
      if (fo != null) {
        try {
          fo.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    ZipFile zipFile = new ZipFile(tmpUrl.concat(".zip"));
    ZipParameters parameters = new ZipParameters();
    // 压缩方式
    parameters.setCompressionMethod(CompressionMethod.DEFLATE);
    // 压缩级别
    parameters.setCompressionLevel(CompressionLevel.NORMAL);
    // 是否设置加密文件
    parameters.setEncryptFiles(true);
    // 设置加密算法
    parameters.setEncryptionMethod(EncryptionMethod.AES);
    // 设置AES加密密钥的密钥强度
    parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
    // 设置密码
    if (StrUtil.isNotBlank(dto.getZipPswd())) {
      zipFile.setPassword(dto.getZipPswd().toCharArray());
    }
    // 要打包的文件夹
    File[] fList = dir.listFiles();

    // 遍历test文件夹下所有的文件、文件夹
    for (File f : fList) {
      if (f.isDirectory()) {
        try {
          zipFile.addFolder(f, parameters);
        } catch (ZipException e) {
          e.printStackTrace();
        }
      } else {
        try {
          zipFile.addFile(f, parameters);
        } catch (ZipException e) {
          e.printStackTrace();
        }
      }
    }
    FileInputStream input = null;
    try {
      input = new FileInputStream(zipFile.getFile());
      MultipartFile multipartFile =
          new MockMultipartFile(
              "file", zipFileName, "application/x-zip-compressed", IOUtils.toByteArray(input));
      fileStorageStrategyContext
          .getProvider(config)
          .upload(multipartFile.getInputStream(), "application/x-zip-compressed", zipFileName);
      String accessUrl = "";
      if (org.apache.commons.lang.StringUtils.isNotEmpty(config.getAccessDomain())) {
        accessUrl =
            config
                .getAccessDomain()
                .concat("/")
                .concat(config.getBucket())
                .concat("/")
                .concat(zipFileName);
      } else {
        accessUrl =
            config
                .getEndpoint()
                .concat("/")
                .concat(config.getBucket())
                .concat("/")
                .concat(zipFileName);
      }
      // 异步保存文件记录
      SysFileManager sysFileManager = new SysFileManager();
      sysFileManager.setServiceProvider(config.getProvider());
      sysFileManager.setProviderName(FileStorageEnum.valueOf(config.getProvider()).getDesc());
      sysFileManager.setOldFileName(multipartFile.getOriginalFilename());
      sysFileManager.setStoreFileName(FilenameUtils.getName(accessUrl));
      sysFileManager.setFileUrl(accessUrl);
      sysFileManager.setFileType("application/x-zip-compressed");
      sysFileManager.setFileSize(FileUtils.getSize(multipartFile.getSize()));
      sysFileManager.setStatus(SUCCEED);
      sysFileManager.setCreateBy(SecurityUserHolder.getUsername());
      int insert = sysFileManagerMapper.insert(sysFileManager);
    } catch (Exception e) {
    } finally {
      try {
        input.close();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
      FileUtil.del(dir);
      FileUtil.del(tmpUrl.concat(".zip"));
    }
  }

  @Override
  public Integer getMaxLevel() {
    return memberMapper.getMaxLevel();
  }

  /**
   * 获取开启了工资的代理
   *
   * @param list List
   * @return List
   */
  @Override
  public List<Member> getOpenSalaryAgent(List<Integer> list) {
    return memberMapper.getOpenSalaryAgent(list);
  }

  @Override
  public List<Member> getListByAccountList(List<String> accountList) {
    return Optional.ofNullable(accountList)
        .filter(CollectionUtil::isNotEmpty)
        .map(e -> this.lambdaQuery().in(Member::getAccount, e).list())
        .orElse(null);
  }

  @Override
  public List<MemberLevelVO> getUserLevelAccountNum() {
    return memberMapper.getUserLevelAccountNum();
  }

  @Override
  public Integer getUserLevelTotalAccountNum(Integer userLevel) {
    return memberMapper.getUserLevelTotalAccountNum(userLevel);
  }

  @Override
  public List<Member> getMemberListByAgentAccount(MemberQueryDTO memberQueryDTO) {
    return memberMapper.getMemberListByAgentAccount(memberQueryDTO);
  }

  @Override
  public List<Map<String, String>> getRebateForAdd(String agentAccount) {
    BigDecimal max = new BigDecimal("9").setScale(2, RoundingMode.HALF_UP);
    BigDecimal min = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
    if (StrUtil.isNotBlank(agentAccount)) {
      BigDecimal userRebate = memberInfoService.findUserRebate(agentAccount);
      max = ObjectUtils.isNull(userRebate) ? max : userRebate.setScale(2, RoundingMode.HALF_UP);
    }

    return this.getLotteryRebates(max, min);
  }

  @Override
  public List<Map<String, String>> getRebateForEdit(String agentAccount) {
    BigDecimal min = new BigDecimal("0");
    BigDecimal max = new BigDecimal("9");
    if (StrUtil.isBlank(agentAccount)) {
      return this.getLotteryRebates(max, min);
    }

    // 获取上级代理的返点
    Member member = getByAccount(agentAccount).orElseThrow(() -> new ServiceException("代理账号不存在!"));
    max = Optional.ofNullable(memberInfoService.findUserRebate(member.getParentName())).orElse(max);

    // 获取直属下级的最大返点等级
    min = Optional.ofNullable(memberInfoService.findUserLowerMaxRebate(agentAccount)).orElse(min);

    // 再获取此账号推广码的最大返点等级
    BigDecimal linkMaxRebate = spreadLinkInfoService.getMaxSpreadLinkRebate(agentAccount);
    min = NumberUtil.max(min, linkMaxRebate);
    return this.getLotteryRebates(max, min);
  }

  @Override
  public void updateDaySalary(String ids, Integer state) {
    Assert.notNull(ids, "请选择需要修改日工资的账户！");
    String[] id = ids.split(",");
    for (String memberId : id) {
      Member member = this.getById(memberId);
      if (member != null && DL_FORMAL_TYPE.equalsIgnoreCase(member.getUserType())) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setMemberId(Convert.toLong(memberId));
        memberInfo.setSalaryFlag(state);
        memberInfoService.updateById(memberInfo);
      }
    }
  }

  @Override
  public IPage<MemberBalanceVO> findPromoteMemberBalance(Page<Member> page, MemberQueryDTO dto) {
    dto.setUserType(MemberEnums.Type.PROMOTION.value());
    return memberMapper
        .queryPage(page, memberQueryCondition.builderQueryWrapper(dto))
        .convert(memberConvert::toBalanceVo);
  }

  @Override
  public void releaseLoginLimit(Long id) {
    Member member = this.getById(id);
    if (null != member) {
      redisTemplate.delete(String.format(CachedKeys.MEMBER_PWD_ERROR_COUNT, member.getAccount()));
    }
  }

  @Override
  public MemberBalanceVO findMemberVip(String username) {
    return memberMapper.findMemberVip(username);
  }


  @Override
  public List<RechargeMemberFileBean> findMemberRechVip(String level, String vipGrade) {
    List<RechargeMemberFileBean> list = new ArrayList<>();
    if (ObjectUtil.isNotEmpty(vipGrade)) {
      List<RechargeMemberFileBean> memberRechVip = memberMapper.findMemberRechVip(vipGrade);
      if (ObjectUtil.isNotEmpty(memberRechVip)) {
        list.addAll(memberRechVip);
      }
    }
    if (ObjectUtil.isNotEmpty(level)) {
      List<RechargeMemberFileBean> memberRechLevel = memberMapper.findMemberRechLevel(level);
      if (ObjectUtil.isNotEmpty(memberRechLevel)) {
        list.addAll(memberRechLevel);
      }
    }
    return list.stream().distinct().collect(Collectors.toList());
  }


  private void preAddCheck(MemberAddDTO dto) {
    Assert.isTrue(!this.isExist(dto.getAccount()), "用户名已存在!");
  }

  @Override
  public Member getMemberAndFillGameAccount(String account) {
    Member member = this.getByAccount(account).orElseThrow(() -> new ServiceException("会员信息不存在!"));
    if (StringUtils.isBlank(member.getGameAccount())) {
      Assert.notNull(sysTheme.getTenantCode(), "平台编码未配置，请联系客服");
      // 固定13位
      StringBuffer gameAccount = new StringBuffer(sysTheme.getTenantCode()).append(member.getId());
      String suffix = RandomUtil.randomString(13 - gameAccount.length());
      member.setGameAccount(gameAccount.append(suffix).toString());
      Assert.isTrue(this.updateById(member), "添加会员游戏账号信息!");
    }
    return member;
  }

  @Override
  public void clearPromoteMemberBalance(CleanAccountDTO dto) {
    log.info("批量清理账号余额开始，操作人：{}，参数：{}", SecurityUserHolder.getUsername(), dto);
    List<Member> memberList = null;
    if (ObjectUtil.equals(dto.getIsCleanAll(), 0)) {
      // 清除选中的推广会员
      String[] userNames = dto.getUserNames().split(",");
      List<String> userNameList = Lists.newArrayList(userNames);
      dto.setUserNameList(userNameList);
      if (ObjectUtil.equals(dto.getUserType(), 4)) {
        memberList =
            this.lambdaQuery()
                .in(Member::getAccount, dto.getUserNames().split(","))
                .eq(Member::getUserType, MemberEnums.Type.PROMOTION.value())
                .list();
      }
      if (StringUtils.isEmpty(memberList) || memberList.size() != userNames.length) {
        throw new ServiceException("未找到账号信息");
      }
      // 过滤是否存在非推广会员
      Member member =
          memberList.stream()
              .filter(
                  a ->
                      MemberEnums.Type.MEMBER.value().equalsIgnoreCase(a.getUserType())
                          || MemberEnums.Type.AGENT.value().equalsIgnoreCase(a.getUserType()))
              .findAny()
              .orElse(new Member());
      if (MemberEnums.Type.MEMBER.value().equalsIgnoreCase(member.getUserType())) {
        throw new ServiceException("你选的账号中含有正式会员!");
      }
      if (MemberEnums.Type.AGENT.value().equalsIgnoreCase(member.getUserType())) {
        throw new ServiceException("你选的账号中含有代理会员!");
      }
    } else if (ObjectUtil.equals(dto.getIsCleanAll(), 1)) {
      memberList = this.lambdaQuery().eq(Member::getUserType, "P").list();
      Assert.notNull(memberList, "没有当前类型的会员账号");
    } else {
      throw new ServiceException("isCleanAll是错误的传参数据");
    }
    // 额度回收
    memberList.parallelStream()
        .map(Member::getAccount)
        .forEach(gameAdminService::recyclingAmountByAccount);
    memberInfoService.updateClearGTMember(dto);
    log.info("批量清理账号余额结束，操作人：{}，参数：{}", SecurityUserHolder.getUsername(), dto);
  }

  private List<Map<String, String>> getLotteryRebates(BigDecimal max, BigDecimal min) {
    int base = 1800;
    BigDecimal rebate = max, value;
    String text;
    DecimalFormat format = new DecimalFormat("0.00#");

    List<Map<String, String>> rebates = new ArrayList<>();
    while (rebate.compareTo(min) >= 0) {
      Map<String, String> map = new HashMap<>();
      value = rebate;
      int baseData = base + value.multiply(BigDecimal.valueOf(20L)).intValue();
      text = rebate.toString().concat("% ---- ").concat(Integer.toString(baseData));
      map.put("value", format.format(value));
      map.put("text", text);
      rebates.add(map);
      rebate = rebate.subtract(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.HALF_UP);
    }

    return rebates;
  }

  private void setGameData(List<MemberVO> list) {
    if (CollUtil.isEmpty(list)) {
      return;
    }

    UserCredential credential = SecurityUserHolder.getCredential();
    Collection<? extends GrantedAuthority> authorities = credential.getAuthorities();
    log.info("权限列表={}", authorities);
    boolean flag = false;
    // 无权限并且不为管理员
    if (ObjectUtil.isEmpty(authorities.stream().filter(ex -> ex.getAuthority().equalsIgnoreCase(ROLES)).collect(Collectors.toList()))
                                            && !UserTypes.ADMIN.value().equals(credential.getUserType())) {
      flag = true;
      log.info("flag1111={}", flag);
    }

    List<String> accounts = list.stream().map(MemberVO::getAccount).collect(Collectors.toList());
    List<MemberDayReport> dayReports = this.getGameData(accounts);
    boolean finalFlag = flag;
    list.forEach(
        a -> {
          if (finalFlag && ObjectUtil.isNotNull(a.getRealName())) {
            a.setRealName(hideRealName(a.getRealName()));
          }

          if (ObjectUtil.isNull(
                  authorities.stream()
                          .filter(ex -> ex.getAuthority().equals(ROLES))
                          .collect(Collectors.toList()))) {
            log.info("flag2222={}", finalFlag);
            a.setRealName(hideRealName(a.getRealName()));
          }
          if (CollUtil.isEmpty(dayReports)) {
            return;
          }
          BigDecimal winAmount =
              dayReports.stream()
                  .filter(b -> a.getAccount().equalsIgnoreCase(b.getUserName()))
                  .map(MemberDayReport::getWinAmount)
                  .reduce(BigDecimal.ZERO, BigDecimal::add);

          BigDecimal waterAmount =
              dayReports.stream()
                  .filter(b -> a.getAccount().equalsIgnoreCase(b.getUserName()))
                  .map(MemberDayReport::getWaterAmount)
                  .reduce(BigDecimal.ZERO, BigDecimal::add);

          a.setWaterAmount(waterAmount);
          a.setWinAmount(winAmount);
        });
  }

  private List<MemberDayReport> getGameData(List<String> accounts) {
    QueryWrapper<MemberDayReport> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("sum(win_amount) win_amount, sum(water_amount) water_amount,user_name");
    queryWrapper.in("user_name", accounts);
    queryWrapper.groupBy("user_name");
    return memberDayReportService.list(queryWrapper);
  }

  /**
   * 根据会员类型获取根代理
   *
   * @param userType String
   * @return String
   */
  private String getMemberRoot(String userType) {
    return MemberEnums.Type.TEST.match(userType) || MemberEnums.Type.PROMOTION.match(userType)
        ? SystemConstant.DEFAULT_TEST_ROOT
        : SystemConstant.DEFAULT_WEB_ROOT;
  }

  /**
   * 批量修改会员状态
   *
   * @param ids List
   * @param status int
   */
  private void changeStatus(List<Long> ids, int status) {
    Assert.notEmpty(ids, "会员ID不能为空");

    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getStatus, status)
            .in(Member::getId, ids)
            .update(new Member()),
        "批量启用失败!");
  }
}
