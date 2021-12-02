package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动类
 *
 * @author admin
 * @since 2020-08-14
 */
@Data
@TableName("activity_info")
@EqualsAndHashCode(callSuper = false)
public class ActivityInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;
    /**
     * 类型
     */
//    private Long activityType;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动版块，活动类型
     * 体育1、彩票2、真人3、棋牌4、电竞5、电游6、捕鱼7、动物竞技8
     */
    private Long type;

    /**
     * 活动类型名称
     */
//    private String typeName;

    /**
     * 活动类型编码
     */
//    private String typeCode;

    /**
     * 主图片名称
     */
    private String mainPicName;

    /**
     * APP主图片路径
     */
    private String mainAppPic;

    /**
     * PC主图片路径
     */
    private String mainPcPic;

    /**
     * 副图片/缩略图名称
     */
    private String vicePicName;

    /**
     * APP副图片路径
     */
    private String viceAppPic;

    /**
     * PC副图片路径
     */
    private String vicePcPic;

    /**
     * 活动对象
     */
    private String activeObject;

    /**
     * 活动规则id
     */
    private Integer activityRuleId;

    /**
     * 申请方式 （1自动 2主动 3在线客服）
     */
    private Integer applyType;

    /**
     * 申请流程 （1自动 2主动 3客服）
     */
    private String applyProcess;

    /**
     * 活动有效状态（1永久有效 2有时限）
     */
    private Integer validStatus;

    /**
     * 活动开始时间
     */
    private String beginTime;

    /**
     * 活动结束时间
     */
    private String endTime;

    /**
     * 活动有效期
     */
//    private String validPeriod;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否弹窗默认0 不弹 1弹窗
     */
    //2.13点击2.1开启弹窗，页面新增弹窗图片设置，点击上传按钮，分别添加移动端和Web对应的弹窗图片。
    private String isPopup;

    /**
     * 移动端弹窗图片
     */
    private String appPopupPic;

    /**
     * Web端弹窗图片
     */
    private String pcPopupPic;
    //1.移动端列表图:点击上传,从本地选取展示在移动端列表中的活动图片
    //2.移动端活动展示页图:点击上传,从本地选取展示在移动端列表展示页中的活动图片
    //3.Web端列表图:点击上传,从本地选取展示在Web端列表中的活动图片。
    //4.Web端活动展示页图:点击上传,从本地选取展示在Web端列表展示页中的活动图片。
    //5.添加背景颜色值:复选按钮。开启后显示背景颜色值下拉列表.再次点击隐藏背景颜色值下拉列表。背景颜色色值下拉列表:默认隐藏，添加背景颜色值勾选后显示。共包含8个选项:白云白(ffffff)、高贵紫(e47aff)、冰川蓝(7df1ff)、深海蓝(3480ff)、中国红(ff0000)、深邃黑(170024)、草地绿(20944c)、霓虹彩(833ab4-fd1d1d-fcb045)。
    //6.移动端背景图:点击上传,从本地选取图片作为移动端背景图。
    //7.Web端背景图:点击上传,从本地选取图片作为Web端背景图。
    /**
     * 是否使用背景颜色
     */
    private String isBackdropColor;
    /**
     * 移动端背景颜色
     */
    private String appBackdropColor;

    /**
     * Web端背景颜色
     */
    private String pcBackdropColor;

    /**
     * 是否使用背景图片
     */
    private String isBackdropPic;

    /**
     * 移动端背景图片
     */
    private String appBackdropPic;

    /**
     * Web端背景图片
     */
    private String pcBackdropPic;

    /**
     * 移动端列表图
     */
    private String appListPic;

    /**
     * Web端列表图
     */
    private String pcListPic;

    /**
     * 移动端活动展示页图
     */
    private String appShowPic;

    /**
     * Web端活动展示页图
     */
    private String pcShowPic;

    //1.添加活动说明区域背景图:点击上传，从本地选取图片作为活动说明区域的背景图。
    //2.添加活动说明标题文本背景图:点击上传，从本地选取图片作为活动说明标题文本的背景图。
    //3.活动说明编辑框:活动说明内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
    /**
     * 活动说明
     */
    private String activeExplain;

    /**
     * 是否使用活动说明区域背景图
     */
    private String isExplainBackdropPic;

    /**
     * 移动端活动说明区域背景图
     */
    private String appExplainBackdropPic;

    /**
     * Web端活动说明区域背景图
     */
    private String pcExplainBackdropPic;

    /**
     * 是否使用活动说明标题文本背景图
     */
    private String isExplainTextBackdropPic;

    /**
     * 移动端活动说明标题文本背景图
     */
    private String appExplainTextBackdropPic;

    /**
     * Web端活动说明标题文本背景图
     */
    private String pcExplainTextBackdropPic;
    //1.添加活动内容区域背景图:点击上传，从本地选取图片作为活动内容区域的背景图。
    //2.添加活动内容标题文本背景图:点击上传，从本地选取图片作为活动内容标题文本的背景图。
    //3.活动内容编辑框:活动内容编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
    /**
     * 活动内容
     */
    private String activeContent;

    /**
     * 是否使用活动内容区域背景图
     */
    private String isContentBackdropPic;

    /**
     * 移动端活动内容区域背景图
     */
    private String appContentBackdropPic;

    /**
     * Web端活动内容区域背景图
     */
    private String pcContentBackdropPic;

    /**
     * 是否使用活动内容区域标题文本背景图
     */
    private String isTextContentBackdropPic;

    /**
     * 移动端活动内容区域标题文本背景图
     */
    private String appTextContentBackdropPic;

    /**
     * Web端活动内容区域标题文本背景图
     */
    private String pcTextContentBackdropPic;
    //1.添加活动规则区域背景图:点击上传，从本地选取图片作为活动规则区域的背景图。
    //2.添加活动规则标题文本背景图:点击上传，从本地选取图片作为活动规则标题文本的背景图。
    //3.活动规则编辑框:活动规则编辑区域。最多限制输入3000个字符。功能:支持文本，图片，表格、链接、表情、更改文本颜色、上标、下标等word功能编辑区域。
    /**
     * 活动规则
     */
    private String activeRule;

    /**
     * 是否使用活动规则区域背景图
     */
    private String isRuleBackdropPic;

    /**
     * 移动端活动规则区域背景图
     */
    private String appRuleBackdropPic;

    /**
     * Web端活动规则区域背景图
     */
    private String pcRuleBackdropPic;

    /**
     * 是否使用活动规则区域标题文本背景图
     */
    private String isRuleTextBackdropPic;

    /**
     * 移动端活动规则区域标题文本背景图
     */
    private String appRuleTextBackdropPic;

    /**
     * Web端活动规则区域标题文本背景图
     */
    private String pcRuleTextBackdropPic;

    /**
     * 使用平台默认0全部  1APP 2web
     */
    private String platformType;

    /**
     * 语种
     */
    private String language;

    /**
     * 关联活动大厅ID
     */
    private Long activityLobbyId;

    /**
     * 展示方式
     */
    private Integer showMode;

    /**
     * html兼容模式 0关闭1开启
     */
//    private Integer htmlCompatibleMode;

    /**
     * 活动说明html
     */
    private String explainHtml;

    /**
     * web活动说明html
     */
//    private String webExplainHtml;

    /**
     * h5活动说明html
     */
//    private String h5ExplainHtml;
}
