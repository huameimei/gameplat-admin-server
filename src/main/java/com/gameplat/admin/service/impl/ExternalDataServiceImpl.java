package com.gameplat.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gameplat.admin.mapper.MemberLevelMapper;
import com.gameplat.admin.mapper.MemberMapper;
import com.gameplat.admin.model.vo.ExternalDataVo;
import com.gameplat.admin.service.ExternalDataService;
import com.gameplat.base.common.util.EasyExcelUtil;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.member.MemberLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ExternalDataServiceImpl implements ExternalDataService {

  @Autowired private MemberMapper memberMapper;

  @Autowired private MemberLevelMapper memberLevelMapper;

    /**
     * 开始处理外部数据导入
     * @param username
     * @param file
     * @param request
     */
    @Override
    public void dealData(String username, MultipartFile file, HttpServletRequest request) {
        //1. 解析Excel
        try {
            List<ExternalDataVo> paramlist = EasyExcelUtil.readExcel(file.getInputStream(), ExternalDataVo.class);
            log.info("共需导入{}条数据", paramlist.size());
      List<String> repeatAccount = new ArrayList<>();
      // 每次执行50000条
      Integer pageSize = 50000;
      Integer size = paramlist.size();
      Integer part = size / pageSize;
      for (int j = 1; j <= part + 1; j++) {
        Integer fromIndex = (j - 1) * pageSize;
        Integer toIndex = j * pageSize;
        // 如果总共只需要分一批执行
        if (size < toIndex) {
          toIndex = size;
        }
        log.info("当前fromIndex和toIndex：（{}），（{}）", fromIndex, toIndex);
        List<ExternalDataVo> pageSubList = paramlist.subList(fromIndex, toIndex);
        if (CollectionUtil.isNotEmpty(pageSubList)) {
          for (ExternalDataVo externalDataVo : pageSubList) {
            if (StrUtil.isBlank(externalDataVo.getAccount())) {
              continue;
            }
            if (isRepeatAccount(externalDataVo.getAccount()) > 0) {
              repeatAccount.add(externalDataVo.getAccount());
              continue;
            }
            Member saveMember = new Member();
            saveMember.setAccount(externalDataVo.getAccount());
            saveMember.setRegisterType(6);
            saveMember.setUserLevel(dealUserLevel(externalDataVo.getUserLevelName()));
            saveMember.setUserType(externalDataVo.getUserType());
            if (StrUtil.isNotBlank(externalDataVo.getDlLevel())
                && StrUtil.isNotBlank(externalDataVo.getParentName())
                && StrUtil.isNotBlank(externalDataVo.getSuperPath())) {

            } else {
              saveMember.setAgentLevel(2);
              saveMember.setParentName("webRoot");
              saveMember.setSuperPath("/webRoot/");
            }

            memberMapper.insert(saveMember);
          }

          try {
            log.info("开始分批睡眠执行插入用户数据,当前批次：第{}批", j);
            Thread.sleep(1500);
          } catch (InterruptedException e) {
            log.info("睡眠时发生错误了!");
            e.printStackTrace();
          }
        }
      }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

  public Integer isRepeatAccount(String account) {
    // 先校验是否存在
    QueryWrapper<Member> countWrapper = new QueryWrapper<>();
    countWrapper.eq("account", account);
    Long aLong = memberMapper.selectCount(countWrapper);
    return aLong == null ? 0 : aLong.intValue();
  }

  public Integer isRepeatRealName(String realname) {
    // 先校验是否存在
    QueryWrapper<Member> countWrapper = new QueryWrapper<>();
    countWrapper.eq("real_name", realname);
    Long aLong = memberMapper.selectCount(countWrapper);
    return aLong == null ? 0 : aLong.intValue();
  }

  public Integer dealUserLevel(String levelName) {
    if (StrUtil.isBlank(levelName)) {
      // 返回默认层级
      return 0;
    }
    MemberLevel levelLike = memberLevelMapper.getLevelLike(levelName);
    if (BeanUtil.isEmpty(levelLike)) {
      // 新增
      MemberLevel saveObj = new MemberLevel();
      saveObj.setLevelName(levelName);
      saveObj.setLevelValue(memberLevelMapper.getMaxLevelValue() + 1);
      memberLevelMapper.insert(saveObj);
      return saveObj.getLevelValue();
    } else {
      return levelLike.getLevelValue();
    }
  }
}
