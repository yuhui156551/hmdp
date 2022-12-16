package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IVoucherService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.apache.ibatis.javassist.compiler.ast.Variable;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yuhui
 * @since 2022-12-15
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    //@Transactional// 涉及两张表的操作，最好加上事务处理
    public Result seckillVoucher(Long voucherId) {
        // 查询优惠券信息
        SeckillVoucher seckillVoucher = seckillVoucherService.getById(voucherId);
        // 判断秒杀是否开始
        if (seckillVoucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 秒杀未开始
            return Result.fail("秒杀已结束！");
        }
        // 判断秒杀是否结束
        if (seckillVoucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已结束！");
        }
        // 判断库存是否充足
        if (seckillVoucher.getStock() < 1) {
            // 库存不足
            return Result.fail("秒杀券已发放完！");
        }
        // 一人一单逻辑
        // 查询用户id
        Long userid = UserHolder.getUser().getId();
        // 等事务提交之后再释放锁，确保事务不出问题
        synchronized (userid.toString().intern()) {
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();// 代理对象
            return proxy.createVoucherOrder(voucherId);
        }
    }

    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        // 查询用户id
        Long userid = UserHolder.getUser().getId();
        // 查询此用户所抢这个优惠券之前的时候，这个优惠券的数量
        int count = query().eq("user_id", userid).eq("voucher_id", voucherId).count();
        // 判断是否存在
        if (count > 0) {
            // 说明此用户之前已经抢过这个优惠券
            return Result.fail("用户已经抢购过一次！");
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")// set stock = stock - 1
//                .eq("voucher_id",voucherId).eq("stock", seckillVoucher.getStock())// where voucher_id = ? and stock = ?
                // 上面这种方法失败率过高
                .eq("voucher_id", voucherId).gt("stock", 0)// where voucher_id = ? and stock > 0
                .update();
        if (!success) {
            // 扣减失败
            return Result.fail("秒杀券已发放完！");
        }
        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        // 订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        // 用户id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        // 代金券id
        voucherOrder.setVoucherId(voucherId);
        // 保存进数据库
        save(voucherOrder);
        // 返回订单id
        return Result.ok(orderId);
    }
}
