package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.hmdp.utils.RedisConstants.CACHE_TPTE_LIST;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuhui
 * @since 2022-12-12
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryList() {
        String key = CACHE_TPTE_LIST;
        //从Redis查询类型缓存
        String typeJson = stringRedisTemplate.opsForValue().get(key);
        //缓存不为空，直接返回
        if (StrUtil.isNotBlank(typeJson)) {
            List<ShopType> shopTypes = JSONUtil.toList(typeJson, ShopType.class);
            return Result.ok(shopTypes);
        }
        //为空，查询数据库，直接把之前控制层的代码复制过来
        List<ShopType> shopTypes = query().orderByAsc("sort").list();
        //将数据写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopTypes));
        //返回
        return Result.ok(shopTypes);
    }
}
