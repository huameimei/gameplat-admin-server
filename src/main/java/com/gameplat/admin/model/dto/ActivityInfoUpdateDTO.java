package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 新增活动DTO
 *
 * @author kenvin
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityInfoUpdateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "编号ID不能为空")
  @Min(value = 1, message = "编号ID必须大于0")
  @ApiModelProperty(value = "编号ID")
  private Long id;

  @ApiModelProperty(value = "类型")
  private Integer activityType;

  @NotNull(message = "活动标题不能为空")
  @ApiModelProperty(value = "活动标题")
  private String title;

  @ApiModelProperty(value = "活动类型")
  private Integer type;

  @ApiModelProperty(value = "活动类型名称")
  private String typeName;

  @ApiModelProperty(value = "活动类型编码")
  private String typeCode;

  @ApiModelProperty(value = "主图片名称")
  private String mainPicName;

  @ApiModelProperty(value = "APP主图片路径")
  private String mainAppPic;

  @ApiModelProperty(value = "PC主图片路径")
  private String mainPcPic;

  @ApiModelProperty(value = "副图片/缩略图名称")
  private String vicePicName;

  @ApiModelProperty(value = "APP副图片路径")
  private String viceAppPic;

  @ApiModelProperty(value = "PC副图片路径")
  private String vicePcPic;

  @ApiModelProperty(value = "活动对象")
  private String activeObject;

  @ApiModelProperty(value = "活动规则id")
  private Integer activityRuleId;

  @ApiModelProperty(value = "申请方式 （1自动 2主动 3在线客服）")
  private Integer applyType;

  @ApiModelProperty(value = "申请流程 （1自动 2主动 3客服）")
  private String applyProcess;

  @ApiModelProperty(value = "活动有效状态（1永久有效 2有时限）")
  private Integer validStatus;

  @NotNull(message = "活动开始时间不能为空")
  @ApiModelProperty(value = "活动开始时间")
  private String beginTime;

  @NotNull(message = "活动结束时间不能为空")
  @ApiModelProperty(value = "活动结束时间")
  private String endTime;

  @ApiModelProperty(value = "活动有效期")
  private String validPeriod;

  @ApiModelProperty(value = "活动状态,1有效 0无效")
  private Integer status;

  @ApiModelProperty(value = "排序")
  private Integer sort;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "备注")
  private String remark;

  // 2.13点击2.1开启弹窗，页面新增弹窗图片设置，点击上传按钮，分别添加移动端和Web对应的弹窗图片。
  @ApiModelProperty(value = "是否弹窗默认0 不弹 1弹窗")
  private Boolean isPopup;

  @ApiModelProperty(value = "移动端弹窗图片")
  private String appPopupPic;

  @ApiModelProperty(value = "Web端弹窗图片")
  private String pcPopupPic;
  // 1.移动端列表图:点击上传,从本地选取展示在移动端列表中的活动图片
  // 2.移动端活动展示页图:点击上传,从本地选取展示在移动端列表展示页中的活动图片
  // 3.Web端列表图:点击上传,从本地选取展示在Web端列表中的活动图片。
  // 4.Web端活动展示页图:点击上传,从本地选取展示在Web端列表展示页中的活动图片。
  // 5.添加背景颜色值:复选按钮。开启后显示背景颜色值下拉列表.再次点击隐藏背景颜色值下拉列表。背景颜色色值下拉列表:默认隐藏，添加背景颜色值勾选后显示。共包含8个选项:白云白(ffffff)、高贵紫(e47aff)、冰川蓝(7df1ff)、深海蓝(3480ff)、中国红(ff0000)、深邃黑(170024)、草地绿(20944c)、霓虹彩(833ab4-fd1d1d-fcb045)。
  // 6.移动端背景图:点击上传,从本地选取图片作为移动端背景图。
  // 7.Web端背景图:点击上传,从本地选取图片作为Web端背景图。
  @ApiModelProperty(value = "是否使用背景颜色")
  private String isBackdropColor;

  @ApiModelProperty(value = "移动端背景颜色")
  private String appBackdropColor;

  @ApiModelProperty(value = "Web端背景颜色")
  private String pcBackdropColor;

  @ApiModelProperty(value = "是否使用背景图片")
  private String isBackdropPic;

  @ApiModelProperty(value = "移动端背景图片")
  private String appBackdropPic;

  @ApiModelProperty(value = "Web端背景图片")
  private String pcBackdropPic;

  @ApiModelProperty(value = "移动端列表图")
  private String appListPic;

  @ApiModelProperty(value = "Web端列表图")
  private String pcListPic;

  @ApiModelProperty(value = "移动端活动展示页图")
  private String appShowPic;

  @ApiModelProperty(value = "Web端活动展示页图")
  private String pcShowPic;

  // 1.添加活动说明区域背景图:点击上传，从本地选取图片作为活动说明区域的背景图。
  // 2.添加活动说明标题文本背景图:点击上传，从本地选取图片作为活动说明标题文本的背景图。
  // 3.活动说明编辑框:活动说明内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @ApiModelProperty(value = "活动说明")
  private String activeExplain;

  @ApiModelProperty(value = "是否使用活动说明区域背景图")
  private String isExplainBackdropPic;

  @ApiModelProperty(value = "移动端活动说明区域背景图")
  private String appExplainBackdropPic;

  @ApiModelProperty(value = "Web端活动说明区域背景图")
  private String pcExplainBackdropPic;

  @ApiModelProperty(value = "是否使用活动说明标题文本背景图")
  private String isExplainTextBackdropPic;

  @ApiModelProperty(value = "移动端活动说明标题文本背景图")
  private String appExplainTextBackdropPic;

  @ApiModelProperty(value = "Web端活动说明标题文本背景图")
  private String pcExplainTextBackdropPic;
  // 1.添加活动内容区域背景图:点击上传，从本地选取图片作为活动内容区域的背景图。
  // 2.添加活动内容标题文本背景图:点击上传，从本地选取图片作为活动内容标题文本的背景图。
  // 3.活动内容编辑框:活动内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @ApiModelProperty(value = "活动内容")
  private String activeContent;

  @ApiModelProperty(value = "是否使用活动内容区域背景图")
  private String isContentBackdropPic;

  @ApiModelProperty(value = "移动端活动内容区域背景图")
  private String appContentBackdropPic;

  @ApiModelProperty(value = "Web端活动内容区域背景图")
  private String pcContentBackdropPic;

  @ApiModelProperty(value = "是否使用活动内容区域标题文本背景图")
  private String isTextContentBackdropPic;

  @ApiModelProperty(value = "移动端活动内容区域标题文本背景图")
  private String appTextContentBackdropPic;

  @ApiModelProperty(value = "Web端活动内容区域标题文本背景图")
  private String pcTextContentBackdropPic;
  // 1.添加活动规则区域背景图:点击上传，从本地选取图片作为活动规则区域的背景图。
  // 2.添加活动规则标题文本背景图:点击上传，从本地选取图片作为活动规则标题文本的背景图。
  // 3.活动规则编辑框:活动规则编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @ApiModelProperty(value = "活动规则")
  private String activeRule;

  @ApiModelProperty(value = "是否使用活动规则区域背景图")
  private String isRuleBackdropPic;

  @ApiModelProperty(value = "移动端活动规则区域背景图")
  private String appRuleBackdropPic;

  @ApiModelProperty(value = "Web端活动规则区域背景图")
  private String pcRuleBackdropPic;

  @ApiModelProperty(value = "是否使用活动规则区域标题文本背景图")
  private String isRuleTextBackdropPic;

  @ApiModelProperty(value = "移动端活动规则区域标题文本背景图")
  private String appRuleTextBackdropPic;

  @ApiModelProperty(value = "Web端活动规则区域标题文本背景图")
  private String pcRuleTextBackdropPic;

  @ApiModelProperty(value = "使用平台默认0全部  1APP 2web")
  private String platformType;

  @ApiModelProperty(value = "语种")
  private String language;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "关联活动大厅ID")
  private Long activityLobbyId;

  @ApiModelProperty(value = "展示方式")
  private Integer showMode;

  @ApiModelProperty(value = "html兼容模式 0关闭1开启")
  private Integer htmlCompatibleMode;

  @ApiModelProperty(value = "活动说明html")
  private String explainHtml;

  @ApiModelProperty(value = "web活动说明html")
  private String webExplainHtml;

  @ApiModelProperty(value = "h5活动说明html")
  private String h5ExplainHtml;
}
