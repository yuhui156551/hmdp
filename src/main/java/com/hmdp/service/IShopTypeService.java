package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhui
 * @since 2022-12-12
 */
public interface IShopTypeService extends IService<ShopType> {

    Result queryList();
}
