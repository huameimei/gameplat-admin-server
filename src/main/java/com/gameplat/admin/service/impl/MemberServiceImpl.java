package com.gameplat.admin.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
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
import org.apache.poi.ss.usermodel.Workbook;
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

  // ??????
  private static final String REALNAME_CODE = "([\\d\\D]{1})(.*)";

  private static final String REALNAME_REPLACEMENT = "$1**";

  // ?????????
  private static final String PHONE_CODE = "(\\d{3})\\d{4}(\\d{4})";

  private static final String PHONE_REPLACEMENT = "$1****$2";

  // ??????
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
   * ????????????
   *
   * @param realName String
   * @return String
   */
  private static String hideRealName(String realName) {
    return realName.replaceAll(REALNAME_CODE, REALNAME_REPLACEMENT);
  }

  /**
   * ?????????
   *
   * @param phone String
   * @return String
   */
  private static String desensitizedPhoneNumber(String phone) {
    if (phone.length() == 11) {
      return phone.replaceAll(PHONE_CODE, PHONE_REPLACEMENT);
    } else {
      if (phone.length() < 9) {
        String b = "*";
        for (int i = 1; i < phone.length(); i++) {
          b+="*";
        }
        return b;
      } else {
        String substring = phone.substring(0, 3);
        int length = phone.length();
        String lastString = phone.substring(length - 2, length);
        String b = "*";
        for (int i = 1; i < phone.length()-5; i++){
          b+="*";
        }
        return substring + b + lastString;
      }
    }
  }


  /** ???????????? */
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
   * ??????id????????????
   *
   * @param id
   * @return
   */
  @Override
  public MemberInfoVO getMemberDateils(Long id) {
    MemberInfoVO memberInfo = memberMapper.getMemberInfo(id);
    // ????????????
    if (StringUtils.isNotEmpty(memberInfo.getRealName())) {
      memberInfo.setRealName(hideRealName(memberInfo.getRealName()));
    }
    // ?????????
    if (StringUtils.isNotEmpty(memberInfo.getPhone())) {
      memberInfo.setPhone(desensitizedPhoneNumber(memberInfo.getPhone()));
    }
    // ??????
    if (StringUtils.isNotEmpty(memberInfo.getEmail())) {
      memberInfo.setEmail(getEmail(memberInfo.getEmail()));
    }
    // QQ
    if (StringUtils.isNotEmpty(memberInfo.getQq())) {
      memberInfo.setQq(desensitizedPhoneNumber(memberInfo.getQq()));
    }
    // ??????
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

    // ????????????
    this.setMemberParent(member);

    // ???????????????????????????
    Assert.isTrue(this.save(member), "??????????????????!");

    MemberInfo memberInfo =
        MemberInfo.builder()
            .memberId(member.getId())
            .rebate(dto.getRebate())
            .salaryFlag(dto.getSalaryFlag())
            .build();
    Assert.isTrue(memberInfoService.save(memberInfo), "??????????????????!");
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

    // ?????????????????????????????????
    Assert.isTrue(
        this.updateById(memberConvert.toEntity(dto)) && memberInfoService.updateById(memberInfo),
        "????????????????????????!");

    // ??????????????????
    Integer status = dto.getStatus();
    if (status != null) {
      // ????????????????????????????????????????????????redis??????????????????
      if (MemberEnums.Status.FROZEN.match(status) || MemberEnums.Status.ENABlED.match(status)) {
          Object object = redisTemplateObj.opsForValue().get(MEMBER_TOKEN_PREFIX + member.getAccount());
        if (object != null) {
          UserCredential userCredential = (UserCredential) object;
          userCredential.setStatus(status);
          redisTemplateObj.opsForValue().set(MEMBER_TOKEN_PREFIX + member.getAccount(), userCredential);
        }
        // ?????????????????????????????????????????????
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

    // ???????????????????????????????????????
    List<Member> memberList = this.lambdaQuery().in(Member::getId, ids).list();

    // ??????????????????????????????????????????
    for(Member member : memberList){
      redisTemplateObj.delete(MEMBER_TOKEN_PREFIX + member.getAccount());
    }

    // TODO ???????????????????????????????????????????????????????????????????????????
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
        .orElseThrow(() -> new ServiceException("?????????????????????!"));
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
    Assert.isTrue(this.updateById(memberConvert.toEntity(dto)), "??????????????????????????????!");
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
        "????????????????????????!");

    // ??????????????????
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
        "??????????????????????????????!");
  }

  @Override
  public void changeWithdrawFlag(Long id, String flag) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getWithdrawFlag, flag)
            .eq(Member::getId, id)
            .update(new Member()),
        "??????????????????????????????!");
  }

  @Override
  public void resetRealName(MemberResetRealNameDTO dto) {
    Member member = this.getById(dto.getId());
    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getRealName, dto.getRealName())
            .eq(Member::getId, member.getId())
            .update(new Member()),
        "??????????????????????????????!");
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
        "??????????????????????????????!");
  }

  @Override
  @CacheInvalidate(name = CachedKeys.MEMBER_REMARK_CACHE, key = "#memberIds", multi = true)
  public void updateRemark(List<Long> memberIds, String remark) {
    Assert.isTrue(
        this.lambdaUpdate()
            .in(Member::getId, memberIds)
            .set(Member::getRemark, remark)
            .update(new Member()),
        "??????????????????????????????!");
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
   * ???????????????????????????
   *
   * @param account String
   * @return boolean
   */
  private boolean isExist(String account) {
    return this.lambdaQuery().eq(Member::getAccount, account).exists();
  }

  /**
   * ??????????????????
   *
   * @param vo MemberVO
   * @return MemberVO
   */
  private MemberVO setOnlineStatus(MemberVO vo) {
    vo.setOnline(onlineUserService.isOnline(vo.getAccount()));
    return vo;
  }

  /**
   * ??????????????????
   *
   * @param member Member
   */
  private void setMemberParent(Member member) {
    String parentName =
        StringUtils.getIfEmpty(
            member.getParentName(), () -> this.getMemberRoot(member.getUserType()));

    Member parent =
        this.getByAccount(parentName).orElseThrow(() -> new ServiceException("?????????????????????!"));

    member.setParentId(parent.getId());
    member.setParentName(parent.getAccount());
    member.setSuperPath(parent.getSuperPath().concat(member.getAccount()).concat("/"));

    if (MemberEnums.Type.AGENT.match(member.getUserType())) {
      member.setAgentLevel(parent.getAgentLevel() + 1);
    }

    // ??????????????????
    memberMapper.updateLowerNumByAccount(parent.getAccount(), 1);
  }

  @Override
  public MemberContactVo getMemberDetail(Long id) {
    MemberInfoVO memberInfo = this.memberMapper.getMemberInfo(id);
    if (memberInfo == null) {
      throw new ServiceException("???????????????");
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
   * ??????excel ??????
   *
   * @param dto
   * @param response
   */
  @Override
  public void exportMembersReport(MemberQueryDTO dto, HttpServletResponse response) {
    try {
      List<MemberVO> members = this.queryList(dto);
      // ??????ZIP????????????
      String zipFileName = "????????????";
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
      log.info("??????--??????excel????????????{}", DateTime.now());
      Workbook workbook =
          ExcelExportUtil.exportExcel(new ExportParams("??????????????????", "????????????"), MemberVO.class, members);
      log.info("??????--??????excel????????????{}", DateTime.now());
      String fileName = "????????????.xlsx";
      FileOutputStream fo = new FileOutputStream(new File(dir + File.separator + fileName));
      // ?????????????????????????????????
      workbook.write(fo);
      workbook.close();
      fo.flush();
      fo.close();

      ZipFile zipFile = new ZipFile(tmpUrl.concat(".zip"));
      ZipParameters parameters = new ZipParameters();
      // ????????????
      parameters.setCompressionMethod(CompressionMethod.DEFLATE);
      // ????????????
      parameters.setCompressionLevel(CompressionLevel.NORMAL);
      // ????????????????????????
      parameters.setEncryptFiles(true);
      // ??????????????????
      parameters.setEncryptionMethod(EncryptionMethod.AES);
      // ??????AES???????????????????????????
      parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
      // ????????????
      if (StrUtil.isNotBlank(dto.getZipPswd())) {
        zipFile.setPassword(dto.getZipPswd().toCharArray());
      }
      // ?????????????????????
      File[] fList = dir.listFiles();

      // ??????test???????????????????????????????????????
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
      throw new ServiceException("??????????????????IO??????:{}", e);
    }
  }

  @Override
  @Async
  public void asynExportMembersReport(MemberQueryDTO dto) {
    List<MemberVO> members = this.queryList(dto);
    // ??????ZIP????????????
    String zipFileName = DateTime.now() + "????????????";
    String tmpUrl =
        System.getProperty("java.io.tmpdir") + File.separator + "excel-" + UUIDUtils.getUUID32();
    final File dir = new File(tmpUrl);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    log.info("??????--??????excel????????????{}", DateTime.now());
    Workbook workbook =
        ExcelExportUtil.exportExcel(new ExportParams("??????????????????", "????????????"), MemberVO.class, members);
    log.info("??????--??????excel????????????{}", DateTime.now());
    String fileName = "????????????.xlsx";
    FileOutputStream fo = null;
    try {
      fo = new FileOutputStream(new File(dir + File.separator + fileName));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    // ?????????????????????????????????
    try {
      workbook.write(fo);
      workbook.close();
      fo.flush();
      fo.close();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }

    ZipFile zipFile = new ZipFile(tmpUrl.concat(".zip"));
    ZipParameters parameters = new ZipParameters();
    // ????????????
    parameters.setCompressionMethod(CompressionMethod.DEFLATE);
    // ????????????
    parameters.setCompressionLevel(CompressionLevel.NORMAL);
    // ????????????????????????
    parameters.setEncryptFiles(true);
    // ??????????????????
    parameters.setEncryptionMethod(EncryptionMethod.AES);
    // ??????AES???????????????????????????
    parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
    // ????????????
    if (StrUtil.isNotBlank(dto.getZipPswd())) {
      zipFile.setPassword(dto.getZipPswd().toCharArray());
    }
    // ?????????????????????
    File[] fList = dir.listFiles();

    // ??????test???????????????????????????????????????
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
      // ????????????????????????
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
   * ??????????????????????????????
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

    // ???????????????????????????
    Member member = getByAccount(agentAccount).orElseThrow(() -> new ServiceException("?????????????????????!"));
    max = Optional.ofNullable(memberInfoService.findUserRebate(member.getParentName())).orElse(max);

    // ???????????????????????????????????????
    min = Optional.ofNullable(memberInfoService.findUserLowerMaxRebate(agentAccount)).orElse(min);

    // ????????????????????????????????????????????????
    BigDecimal linkMaxRebate = spreadLinkInfoService.getMaxSpreadLinkRebate(agentAccount);
    min = NumberUtil.max(min, linkMaxRebate);
    return this.getLotteryRebates(max, min);
  }

  @Override
  public void updateDaySalary(String ids, Integer state) {
    Assert.notNull(ids, "??????????????????????????????????????????");
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
    Assert.isTrue(!this.isExist(dto.getAccount()), "??????????????????!");
  }

  @Override
  public Member getMemberAndFillGameAccount(String account) {
    Member member = this.getByAccount(account).orElseThrow(() -> new ServiceException("?????????????????????!"));
    if (StringUtils.isBlank(member.getGameAccount())) {
      Assert.notNull(sysTheme.getTenantCode(), "???????????????????????????????????????");
      // ??????13???
      StringBuffer gameAccount = new StringBuffer(sysTheme.getTenantCode()).append(member.getId());
      String suffix = RandomUtil.randomString(13 - gameAccount.length());
      member.setGameAccount(gameAccount.append(suffix).toString());
      Assert.isTrue(this.updateById(member), "??????????????????????????????!");
    }
    // ?????????????????????????????????
    if (ObjectUtil.isNull(gameTransferInfoService.getInfoByMemberId(member.getId()))) {
      GameTransferInfo gameTransferInfo = new GameTransferInfo();
      gameTransferInfo.setPlatformCode(TransferTypesEnum.SELF.getCode());
      gameTransferInfo.setAccount(member.getAccount());
      gameTransferInfo.setMemberId(member.getId());
      gameTransferInfoService.saveOrUpdate(gameTransferInfo);
    }
    return member;
  }

  @Override
  public void clearPromoteMemberBalance(CleanAccountDTO dto) {
    log.info("?????????????????????????????????????????????{}????????????{}", SecurityUserHolder.getUsername(), dto);
    List<Member> memberList = null;
    if (ObjectUtil.equals(dto.getIsCleanAll(), 0)) {
      // ???????????????????????????
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
        throw new ServiceException("?????????????????????");
      }
      // ?????????????????????????????????
      Member member =
          memberList.stream()
              .filter(
                  a ->
                      MemberEnums.Type.MEMBER.value().equalsIgnoreCase(a.getUserType())
                          || MemberEnums.Type.AGENT.value().equalsIgnoreCase(a.getUserType()))
              .findAny()
              .orElse(new Member());
      if (MemberEnums.Type.MEMBER.value().equalsIgnoreCase(member.getUserType())) {
        throw new ServiceException("????????????????????????????????????!");
      }
      if (MemberEnums.Type.AGENT.value().equalsIgnoreCase(member.getUserType())) {
        throw new ServiceException("????????????????????????????????????!");
      }
    } else if (ObjectUtil.equals(dto.getIsCleanAll(), 1)) {
      memberList = this.lambdaQuery().eq(Member::getUserType, "P").list();
      Assert.notNull(memberList, "?????????????????????????????????");
    } else {
      throw new ServiceException("isCleanAll????????????????????????");
    }
    // ????????????
    memberList.parallelStream()
        .map(Member::getAccount)
        .forEach(gameAdminService::recyclingAmountByAccount);
    memberInfoService.updateClearGTMember(dto);
    log.info("?????????????????????????????????????????????{}????????????{}", SecurityUserHolder.getUsername(), dto);
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
    boolean flag = false;
    // ??????????????????????????????
    if (ObjectUtil.isEmpty(authorities.stream().filter(ex -> ex.getAuthority().equalsIgnoreCase(ROLES)).collect(Collectors.toList()))
                                            && !UserTypes.ADMIN.value().equals(credential.getUserType())) {
      flag = true;
    }

    List<String> accounts = list.stream().map(MemberVO::getAccount).collect(Collectors.toList());
    List<MemberDayReport> dayReports = this.getGameData(accounts);
    boolean finalFlag = flag;
    list.forEach(
        a -> {
          if (finalFlag && ObjectUtil.isNotNull(a.getRealName())) {
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
   * ?????????????????????????????????
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
   * ????????????????????????
   *
   * @param ids List
   * @param status int
   */
  private void changeStatus(List<Long> ids, int status) {
    Assert.notEmpty(ids, "??????ID????????????");

    Assert.isTrue(
        this.lambdaUpdate()
            .set(Member::getStatus, status)
            .in(Member::getId, ids)
            .update(new Member()),
        "??????????????????!");
  }
}
