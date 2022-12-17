package com.hmdp.utils;

/**
 * @author yuhui
 */
public interface ILock {
    /**
     * 尝试获取锁
     * @param timeoutSec 超时时间
     * @return 成功或失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();
}
