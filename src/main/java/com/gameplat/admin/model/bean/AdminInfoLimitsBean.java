package com.gameplat.admin.model.bean;

import cn.hutool.json.JSONUtil;
import com.gameplat.admin.enums.AdminTypeEnum;
import com.gameplat.admin.model.entity.SysUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/** AdminInfo限制信息转换类 */
public class AdminInfoLimitsBean {

  /** 存款审核限额 */
  private Double maxRechargeAmountAudit;

  /** 取款审核限额 */
  private Double maxWithdrawAmountAudit;

  /** 人工存款限额 */
  private Double maxManualRechargeAmountAudit;

  /** 人工取款限额 */
  private Double maxManualWithdrawAmountAudit;

  public AdminInfoLimitsBean() {}

  public AdminInfoLimitsBean(
      Double maxRechargeAmountAudit,
      Double maxWithdrawAmountAudit,
      Double maxManualRechargeAmountAudit,
      Double maxManualWithdrawAmountAudit) {
    this.maxRechargeAmountAudit = maxRechargeAmountAudit;
    this.maxWithdrawAmountAudit = maxWithdrawAmountAudit;
    this.maxManualRechargeAmountAudit = maxManualRechargeAmountAudit;
    this.maxManualWithdrawAmountAudit = maxManualWithdrawAmountAudit;
  }

  public Double getMaxRechargeAmountAudit() {
    if (null == maxRechargeAmountAudit) {
      return 0d;
    }
    return maxRechargeAmountAudit;
  }

  public void setMaxRechargeAmountAudit(Double maxRechargeAmountAudit) {
    this.maxRechargeAmountAudit = maxRechargeAmountAudit;
  }

  public Double getMaxWithdrawAmountAudit() {
    if (null == maxWithdrawAmountAudit) {
      return 0d;
    }
    return maxWithdrawAmountAudit;
  }

  public void setMaxWithdrawAmountAudit(Double maxWithdrawAmountAudit) {
    this.maxWithdrawAmountAudit = maxWithdrawAmountAudit;
  }

  public Double getMaxManualRechargeAmountAudit() {
    if (null == maxManualRechargeAmountAudit) {
      return 0d;
    }
    return maxManualRechargeAmountAudit;
  }

  public void setMaxManualRechargeAmountAudit(Double maxManualRechargeAmountAudit) {
    this.maxManualRechargeAmountAudit = maxManualRechargeAmountAudit;
  }

  public Double getMaxManualWithdrawAmountAudit() {
    if (null == maxManualWithdrawAmountAudit) {
      return 0d;
    }
    return maxManualWithdrawAmountAudit;
  }

  public void setMaxManualWithdrawAmountAudit(Double maxManualWithdrawAmountAudit) {
    this.maxManualWithdrawAmountAudit = maxManualWithdrawAmountAudit;
  }

  public static String conver2LimitStr(AdminInfoLimitsBean adminInfoLimitsBean) {
    return JSONUtil.toJsonStr(adminInfoLimitsBean);
  }

  public static AdminInfoLimitsBean conver2Bean(String beanStr) {
    if (StringUtils.isBlank(beanStr)) {
      return new AdminInfoLimitsBean();
    }
    return JSONUtil.toBean(beanStr, AdminInfoLimitsBean.class);
  }

  public static AdminInfo convert2AdminInfo(SysUser admin) {
    if (null == admin) {
      return null;
    }
    AdminInfo adminInfo = new AdminInfo();
    BeanUtils.copyProperties(admin, adminInfo);
    if (!AdminTypeEnum.NORMAL.getValue().equals(admin.getUserType())) {
      return adminInfo;
    }
    /** 子账号设置操作金额限制 */
    if (StringUtils.isNotBlank(admin.getLimitInfo())) {
      AdminInfoLimitsBean limitsBean = AdminInfoLimitsBean.conver2Bean(admin.getLimitInfo());
      BeanUtils.copyProperties(limitsBean, adminInfo);
    } else {
      AdminInfoLimitsBean limitsBean = new AdminInfoLimitsBean();
      BeanUtils.copyProperties(limitsBean, adminInfo);
    }
    return adminInfo;
  }

  public static SysUser convert2Admin(AdminInfo adminInfo) {
    SysUser admin = new SysUser();
    BeanUtils.copyProperties(adminInfo, admin);
    if (!AdminTypeEnum.NORMAL.getValue().equals(admin.getUserType())) {
      return admin;
    }
    AdminInfoLimitsBean limitsBean =
        new AdminInfoLimitsBean(
            adminInfo.getMaxRechargeAmountAudit(),
            adminInfo.getMaxWithdrawAmountAudit(),
            adminInfo.getMaxManualRechargeAmountAudit(),
            adminInfo.getMaxManualWithdrawAmountAudit());
    admin.setLimitInfo(AdminInfoLimitsBean.conver2LimitStr(limitsBean));
    return admin;
  }

  @Override
  public String toString() {
    return "AdminInfoLimitsBean{"
        + "maxRechargeAmountAudit="
        + maxRechargeAmountAudit
        + ", maxWithdrawAmountAudit="
        + maxWithdrawAmountAudit
        + ", maxManualRechargeAmountAudit="
        + maxManualRechargeAmountAudit
        + ", maxManualWithdrawAmountAudit="
        + maxManualWithdrawAmountAudit
        + '}';
  }
}
