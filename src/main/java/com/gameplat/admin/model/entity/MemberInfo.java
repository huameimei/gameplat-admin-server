package com.gameplat.admin.model.entity;

import com.gameplat.admin.dao.MemberInfoMapper;
import com.gameplat.admin.dao.MemberTreeMapper;
import com.gameplat.admin.utils.TreeUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 会员信息
 *
 * @author Lenovo
 */
@EqualsAndHashCode(of = "id")
@Data
@NoArgsConstructor
public class MemberInfo {

    public Long id;
    @Autowired
    private MemberInfoMapper memberInfoMapper;
    @Autowired
    private MemberTreeMapper memberTreeMapper;
    @ApiModelProperty(value = "会员名")
    private String account;

    @ApiModelProperty(value = "会员昵称")
    private String nickName;

    @ApiModelProperty(value = "会员层级或代理层级")
    private Integer userLevel;

    @ApiModelProperty(value = "上级ID")
    private Long parentId;

    @ApiModelProperty(value = "上级名称")
    private String parentName;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    @ApiModelProperty(value = "密码强度")
    private String passStrength;

    @ApiModelProperty(value = "是否已改密码 0:未改 1:已改")
    private Integer changeFlag;

    @ApiModelProperty(value = "是否删除 0:未删除  1:已删除")
    private Integer delFlag;

    @ApiModelProperty(value = "账号状态(0-- 停用，1-- 正常，2 --- 冻结)")
    private Integer status;

    @ApiModelProperty(value = "用户类型（1:会员账号 2:代理账号 3:推广账号 4:试玩账号 5:代理子账号）")
    private Integer userType;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "QQ")
    private String qq;

    @ApiModelProperty(value = "生日(格式0706)")
    private String birthday;

    @ApiModelProperty(value = "注册IP")
    private String registerIp;

    @ApiModelProperty(value = "注册来源")
    private String registerSrc;

    @ApiModelProperty(value = "注册时间")
    private Date registerTime;

    @ApiModelProperty(value = "注册方式:1-后台添加，2-推广注册 3-在线注册 4-代理添加")
    private Integer regWay;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 获取分类的父分类。
     *
     * @return 父分类实体对象，如果指定的分类是一级分类，则返回null
     */
    public MemberInfo getParent() {
        return getAncestor(1);
    }

    /**
     * 查询指定分类往上第n级分类。
     *
     * @param n 距离
     * @return 上级分类，如果不存在则为null
     */
    public MemberInfo getAncestor(int n) {
        TreeUtils.checkPositive(n, "distant");
        Long parent = memberTreeMapper.selectAncestor(id, n);
        return parent == null ? null : memberInfoMapper.selectById(parent);
    }

    /**
     * 分类的下的直属子分类。
     *
     * @return 直属子类列表，如果id所指定的分类不存在、或没有符合条件的分类，则返回空列表
     * @throws IllegalArgumentException 如果id小于0
     */
    public List<MemberInfo> getChildren() {
        return getChildren(1);
    }

    /**
     * 分类的下的第n级子分类。
     *
     * @param n 向下级数，1表示直属子分类
     * @return 子类列表，如果id所指定的分类不存在、或没有符合条件的分类，则返回空列表
     * @throws IllegalArgumentException 如果id小于0，或n不是正数
     */
    public List<MemberInfo> getChildren(int n) {
        TreeUtils.checkNotNegative(id, "id");
        TreeUtils.checkPositive(n, "n");
        return memberInfoMapper.selectSubLayer(id, n);
    }

    /**
     * 获取由顶级分类到此分类(含)路径上的所有的分类对象。 如果指定的分类不存在，则返回空列表。
     *
     * @return 分类实体列表，越靠上的分类在列表中的位置越靠前
     */
    public List<MemberInfo> getPath() {
        return memberInfoMapper.selectPathToRoot(id);
    }

    /**
     * 获取此分类(含)到其某个的上级分类（不含）之间的所有分类的实体对象（仅查询id和name属性）。 如果指定的分类、上级分类不存在，或是上级分类不是指定分类的上级，则返回空列表
     *
     * @param ancestor 上级分类的id，若为0则表示获取到一级分类（含）的列表。
     * @return 分类实体列表，越靠上的分类在列表中的位置越靠前。
     * @throws IllegalArgumentException 如果ancestor小于1。
     */
    public List<MemberInfo> getPathRelativeTo(int ancestor) {
        TreeUtils.checkPositive(ancestor, "ancestor");
        return memberInfoMapper.selectPathToAncestor(id, ancestor);
    }

    /**
     * 查询分类是哪一级的，根分类级别是0。
     *
     * @return 级别
     */
    int getLevel() {
        return memberTreeMapper.selectDistance(0L, id);
    }

    /**
     * 将一个分类移动到目标分类下面（成为其子分类）。被移动分类的子类将自动上浮（成为指定分类 父类的子分类），即使目标是指定分类原本的父类。
     *
     * <p>例如下图(省略顶级分类)： 1 1 | / | \ 2 3 4 5 / | \ (id=2).moveTo(7) / \ 3 4 5 -----------------> 6 7 /
     * \ / / | \ 6 7 8 9 10 2 / / \ 8 9 10
     *
     * @param target 目标分类的id
     * @throws IllegalArgumentException 如果target所表示的分类不存在、或此分类的id==target
     */
    public void moveTo(Long target) {
        if (id.equals(target)) {
            throw new IllegalArgumentException("不能移动到自己下面");
        }
        TreeUtils.checkNotNegative(target, "target");
        if (target > 0 && memberInfoMapper.contains(target) == null) {
            throw new IllegalArgumentException("指定的上级分类不存在");
        }
        moveSubTree(id, memberTreeMapper.selectAncestor(id, 1));
        moveNode(id, target);
    }

    /**
     * 将一个分类移动到目标分类下面（成为其子分类），被移动分类的子分类也会随着移动。 如果目标分类是被移动分类的子类，则先将目标分类（连带子类）移动到被移动分类原来的
     * 的位置，再移动需要被移动的分类。
     *
     * <p>例如下图(省略顶级分类)： 1 1 | | 2 7 / | \ (id=2).moveTreeTo(7) / | \ 3 4 5 --------------------> 9 10
     * 2 / \ / | \ 6 7 3 4 5 / / \ | 8 9 10 6 | 8
     *
     * @param target 目标分类的id
     * @throws IllegalArgumentException 如果id或target所表示的分类不存在、或id==target
     */
    public void moveTreeTo(Long target) {
        TreeUtils.checkNotNegative(target, "target");
        if (target > 0 && memberInfoMapper.contains(target) == null) {
            throw new IllegalArgumentException("指定的上级分类不存在");
        }

        /* 移动分移到自己子树下和无关节点下两种情况 */
        Integer distance = memberTreeMapper.selectDistance(id, target);

        // noinspection StatementWithEmptyBody
        if (distance == null) {
            // 移动到父节点或其他无关系节点，不需要做额外动作
        } else if (distance == 0) {
            throw new IllegalArgumentException("不能移动到自己下面");
        } else {
            // 如果移动的目标是其子类，需要先把子类移动到本类的位置
            Long parent = memberTreeMapper.selectAncestor(id, 1);
            moveNode(target, parent);
            moveSubTree(target, target);
        }

        moveNode(id, target);
        moveSubTree(id, id);
    }

    /**
     * 将指定节点移动到另某节点下面，该方法不修改子节点的相关记录， 为了保证数据的完整性，需要与moveSubTree()方法配合使用。
     *
     * @param id     指定节点id
     * @param parent 某节点id
     */
    private void moveNode(Long id, Long parent) {
        memberTreeMapper.deletePath(id);
        memberTreeMapper.insertPath(id, parent);
        memberTreeMapper.insertNode(id);
    }

    public void moveSubTree(Long parent) {
        moveSubTree(id, parent);
    }

    /**
     * 将指定节点的所有子树移动到某节点下 如果两个参数相同，则相当于重建子树，用于父节点移动后更新路径
     *
     * @param id     指定节点id
     * @param parent 某节点id
     */
    private void moveSubTree(Long id, Long parent) {
        Long[] subs = memberTreeMapper.selectSubId(id);
        for (Long sub : subs) {
            moveNode(sub, parent);
            moveSubTree(sub, sub);
        }
    }
}
