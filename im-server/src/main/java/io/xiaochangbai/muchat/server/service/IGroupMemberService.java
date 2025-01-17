package io.xiaochangbai.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.xiaochangbai.muchat.server.entity.GroupMember;

import java.util.List;


public interface IGroupMemberService extends IService<GroupMember> {



    GroupMember findByGroupAndUserId(Long groupId,Long userId);

    List<GroupMember>  findByUserId(Long userId);

    List<GroupMember>  findByGroupId(Long groupId);

    List<Long> findUserIdsByGroupId(Long groupId);

    boolean save(GroupMember member);

    boolean saveOrUpdateBatch(Long groupId,List<GroupMember> members);

    void removeByGroupId(Long groupId);

    void removeByGroupAndUserId(Long groupId,Long userId);
}
