package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class ActivityInfoAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "编号")
  private Long id;

  @NotNull(message = "活动类型不能为空")
  @Schema(description = "类型")
  private Integer activityType;

  @NotBlank(message = "活动标题不能为空")
  @Schema(description = "活动标题")
  private String title;

  @NotNull(message = "活动类型不能为空")
  @Min(value = 1, message = "活动类型必须为正数")
  @Schema(description = "活动类型") // 体育1、彩票2、真人3、棋牌4、电竞5、电游6、捕鱼7、动物竞技8
  private Integer type;

  @Schema(description = "活动类型名称")
  private String typeName;

  @Schema(description = "活动类型编码")
  private String typeCode;

  @Schema(description = "主图片名称")
  private String mainPicName;

  @Schema(description = "APP主图片路径")
  private String mainAppPic;

  @Schema(description = "PC主图片路径")
  private String mainPcPic;

  @Schema(description = "副图片/缩略图名称")
  private String vicePicName;

  @Schema(description = "APP副图片路径")
  private String viceAppPic;

  @Schema(description = "PC副图片路径")
  private String vicePcPic;

  @Schema(description = "活动对象")
  private String activeObject;

  @Schema(description = "活动规则id")
  private Integer activityRuleId;

  @Schema(description = "申请方式 （1自动 2主动 3在线客服）")
  private Integer applyType;

  @Schema(description = "申请流程 （1自动 2主动 3客服）")
  private String applyProcess;

  @Schema(description = "活动有效状态（1永久有效 2有时限）")
  private Integer validStatus;

  @Schema(description = "活动开始时间")
  private String beginTime;

  @Schema(description = "活动结束时间")
  private String endTime;

  @Schema(description = "活动有效期")
  private String validPeriod;

  @Schema(description = "活动状态,1有效 0无效")
  private Integer status;

  @Schema(description = "排序")
  private Integer sort;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "创建时间")
  private Date createTime;

  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "备注")
  private String remark;

  // 2.13点击2.1开启弹窗，页面新增弹窗图片设置，点击上传按钮，分别添加移动端和Web对应的弹窗图片。
  @Schema(description = "是否弹窗默认0 不弹 1弹窗")
  private Boolean isPopup;

  @Schema(description = "移动端弹窗图片")
  private String appPopupPic;

  @Schema(description = "Web端弹窗图片")
  private String pcPopupPic;
  // 1.移动端列表图:点击上传,从本地选取展示在移动端列表中的活动图片
  // 2.移动端活动展示页图:点击上传,从本地选取展示在移动端列表展示页中的活动图片
  // 3.Web端列表图:点击上传,从本地选取展示在Web端列表中的活动图片。
  // 4.Web端活动展示页图:点击上传,从本地选取展示在Web端列表展示页中的活动图片。
  // 5.添加背景颜色值:复选按钮。开启后显示背景颜色值下拉列表.再次点击隐藏背景颜色值下拉列表。背景颜色色值下拉列表:默认隐藏，添加背景颜色值勾选后显示。共包含8个选项:白云白(ffffff)、高贵紫(e47aff)、冰川蓝(7df1ff)、深海蓝(3480ff)、中国红(ff0000)、深邃黑(170024)、草地绿(20944c)、霓虹彩(833ab4-fd1d1d-fcb045)。
  // 6.移动端背景图:点击上传,从本地选取图片作为移动端背景图。
  // 7.Web端背景图:点击上传,从本地选取图片作为Web端背景图。
  @Schema(description = "是否使用背景颜色")
  private String isBackdropColor;

  @Schema(description = "移动端背景颜色")
  private String appBackdropColor;

  @Schema(description = "Web端背景颜色")
  private String pcBackdropColor;

  @Schema(description = "是否使用背景图片")
  private String isBackdropPic;

  @Schema(description = "移动端背景图片")
  private String appBackdropPic;

  @Schema(description = "Web端背景图片")
  private String pcBackdropPic;

  @Schema(description = "移动端列表图")
  private String appListPic;

  @Schema(description = "H5列表图")
  private String h5ListPic;

  @Schema(description = "Web端列表图")
  private String pcListPic;

  @Schema(description = "移动端活动展示页图")
  private String appShowPic;

  @Schema(description = "Web端活动展示页图")
  private String pcShowPic;

  // 1.添加活动说明区域背景图:点击上传，从本地选取图片作为活动说明区域的背景图。
  // 2.添加活动说明标题文本背景图:点击上传，从本地选取图片作为活动说明标题文本的背景图。
  // 3.活动说明编辑框:活动说明内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @Schema(description = "活动说明")
  private String activeExplain;

  @Schema(description = "是否使用活动说明区域背景图")
  private String isExplainBackdropPic;

  @Schema(description = "移动端活动说明区域背景图")
  private String appExplainBackdropPic;

  @Schema(description = "Web端活动说明区域背景图")
  private String pcExplainBackdropPic;

  @Schema(description = "是否使用活动说明标题文本背景图")
  private String isExplainTextBackdropPic;

  @Schema(description = "移动端活动说明标题文本背景图")
  private String appExplainTextBackdropPic;

  @Schema(description = "Web端活动说明标题文本背景图")
  private String pcExplainTextBackdropPic;
  // 1.添加活动内容区域背景图:点击上传，从本地选取图片作为活动内容区域的背景图。
  // 2.添加活动内容标题文本背景图:点击上传，从本地选取图片作为活动内容标题文本的背景图。
  // 3.活动内容编辑框:活动内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @Schema(description = "活动内容")
  private String activeContent;

  @Schema(description = "是否使用活动内容区域背景图")
  private String isContentBackdropPic;

  @Schema(description = "移动端活动内容区域背景图")
  private String appContentBackdropPic;

  @Schema(description = "Web端活动内容区域背景图")
  private String pcContentBackdropPic;

  @Schema(description = "是否使用活动内容区域标题文本背景图")
  private String isTextContentBackdropPic;

  @Schema(description = "移动端活动内容区域标题文本背景图")
  private String appTextContentBackdropPic;

  @Schema(description = "Web端活动内容区域标题文本背景图")
  private String pcTextContentBackdropPic;
  // 1.添加活动规则区域背景图:点击上传，从本地选取图片作为活动规则区域的背景图。
  // 2.添加活动规则标题文本背景图:点击上传，从本地选取图片作为活动规则标题文本的背景图。
  // 3.活动规则编辑框:活动规则编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
  @Schema(description = "活动规则")
  private String activeRule;

  @Schema(description = "是否使用活动规则区域背景图")
  private String isRuleBackdropPic;

  @Schema(description = "移动端活动规则区域背景图")
  private String appRuleBackdropPic;

  @Schema(description = "Web端活动规则区域背景图")
  private String pcRuleBackdropPic;

  @Schema(description = "是否使用活动规则区域标题文本背景图")
  private String isRuleTextBackdropPic;

  @Schema(description = "移动端活动规则区域标题文本背景图")
  private String appRuleTextBackdropPic;

  @Schema(description = "Web端活动规则区域标题文本背景图")
  private String pcRuleTextBackdropPic;

  @Schema(description = "使用平台默认0全部  1APP 2web")
  private String platformType;

  @Schema(description = "语种")
  private String language;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "关联活动大厅ID")
  private Long activityLobbyId;

  @Schema(description = "展示方式")
  private Integer showMode;

  @Schema(description = "html兼容模式 0关闭1开启")
  private Integer htmlCompatibleMode;

  @Schema(description = "活动说明html")
  private String explainHtml;

  @Schema(description = "web活动说明html")
  private String webExplainHtml;

  @Schema(description = "h5活动说明html")
  private String h5ExplainHtml;
}
