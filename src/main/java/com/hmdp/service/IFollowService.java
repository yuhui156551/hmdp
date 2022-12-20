package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhui
 * @since 2022-12-20
 */
public interface IFollowService extends IService<Follow> {

    /**
     * 关注用户
     * @param followUserId 被关注的用户id
     * @param isFollow 是否已被关注
     * @return
     */
    Result follow(Long followUserId, Boolean isFollow);

    /**
     * 检验是否关注
     * @param followUserId 被关注的用户id
     * @return
     */
    Result isFollow(Long followUserId);

    /**
     * 实现查看共同关注用户
     * @param id 目标用户
     * @return
     */
    Result followCommons(Long id);

}
