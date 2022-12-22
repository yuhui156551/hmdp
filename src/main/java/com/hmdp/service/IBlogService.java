package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhui
 * @since 2022-12-20
 */
public interface IBlogService extends IService<Blog> {

    /**
     * 按点赞数量排序查询笔记
     * @param current
     * @return
     */
    Result queryHotBlog(Integer current);

    /**
     * 根据id查找笔记
     * @param id 笔记id
     * @return
     */
    Result queryBlogById(Long id);

    /**
     * 点赞笔记
     * @param id 笔记id
     * @return
     */
    Result likeBlog(Long id);

    /**
     * 查找笔记前五喜欢
     * @param id 笔记id
     * @return
     */
    Result queryBlogLikes(Long id);

    /**
     * 保存笔记
     * @param blog 笔记信息
     * @return
     */
    Result saveBlog(Blog blog);
}
