package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.entity.MemberInfo;

import java.util.List;

public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

    /**
     * 查询某个节点的子树中所有的节点，不包括参数所指定的节点
     *
     * @param id 节点id
     * @return 子节点
     */
    List<MemberInfo> selectDescendant(Long id);

    /**
     * 查询某个节点的第n级子节点
     *
     * @param ancestor 祖先节点ID
     * @param n        距离（0表示自己，1表示直属子节点）
     * @return 子节点列表
     */
    List<MemberInfo> selectSubLayer(Long ancestor, int n);

    /**
     * 查询由id指定节点(含)到根节点(不含)的路径
     * 比下面的<code>selectPathToAncestor</code>简单些。
     *
     * @param id 节点ID
     * @return 路径列表。如果节点不存在，则返回空列表
     */
    List<MemberInfo> selectPathToRoot(Long id);

    /**
     * 查询由id指定节点(含)到指定上级节点(不含)的路径
     *
     * @param id       节点ID
     * @param ancestor 上级节点的ID
     * @return 路径列表。如果节点不存在，或上级节点不存在，则返回空列表
     */
    List<MemberInfo> selectPathToAncestor(Long id, int ancestor);


    /**
     * 判断分类是否存在
     *
     * @param id 分类id
     * @return true表示存在，null或false表示不存在
     */

    Boolean contains(Long id);

}
