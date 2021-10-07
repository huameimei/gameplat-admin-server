package com.gameplat.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.entity.MemberTree;

public interface MemberTreeMapper extends BaseMapper<MemberTree> {


    /**
     * 查询某个节点的子树中所有的节点的id，不包括参数所指定的节点
     *
     * @param id 节点id
     * @return 子节点id
     */
    Long[] selectDescendantId(Long id);


    /**
     * 查询某个节点的第N级父节点。如果id指定的节点不存在、操作错误或是数据库被外部修改，
     * 则可能查询不到父节点，此时返回null。
     *
     * @param id 节点id
     * @param n  祖先距离（0表示自己，1表示直属父节点）
     * @return 父节点id，如果不存在则返回null
     */
    Long selectAncestor(Long id, int n);


    /**
     * 查找某节点下的所有直属子节点的id
     * 该方法与上面的<code>selectSubLayer</code>不同，它只查询节点的id，效率高点
     *
     * @param parent 分类id
     * @return 子类id数组
     */
    Long[] selectSubId(Long parent);

    /**
     * 查询某节点到它某个祖先节点的距离
     *
     * @param ancestor 父节点id
     * @param id       节点id
     * @return 距离（0表示到自己的距离）,如果ancestor并不是其祖先节点则返回null
     */
    Integer selectDistance(Long ancestor, Long id);

    /**
     * 复制父节点的路径结构,并修改descendant和distance
     *
     * @param id     节点id
     * @param parent 父节点id
     */
    void insertPath(Long id, Long parent);

    /**
     * 在关系表中插入对自身的连接
     *
     * @param id 节点id
     */
    void insertNode(Long id);

    /**
     * 从树中删除某节点的路径。注意指定的节点可能存在子树，而子树的节点在该节点之上的路径并没有改变，
     * 所以使用该方法后还必须手动修改子节点的路径以确保树的正确性
     *
     * @param id 节点id
     */
    void deletePath(Long id);
}
