/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

public interface IRedisLock {
    String getLockKey();//获取可名

    /**
     * 获取锁
     *
     * @param maxWaitTime 表示等待的最大时间 单位s
     * @return 返回true 表示加锁成功，false 加锁失败
     */
    Boolean tryLock(Integer maxWaitTime);

    /**
     * 释放锁
     *
     * @return true 成功 false 失败 值不一样时 会失败
     */
    Boolean unlock();

}
