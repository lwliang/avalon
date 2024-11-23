/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import com.avalon.core.util.BCryptUtil;
import com.avalon.core.util.ObjectUtils;

public class RedisLock implements IRedisLock {

    private final String key;
    private String value;
    private Integer count;//记录获取锁的个数


    private final RedisCommon redisCommon;

    public RedisCommon getSysRedisUtil() {
        return redisCommon;
    }

    public RedisLock(RedisCommon sysRedisUtil, String key) {
        this.redisCommon = sysRedisUtil;
        this.key = key;
        this.value = BCryptUtil.simpleUUID();
        this.count = 0;
    }


    protected void addCount() {
        synchronized (this) {
            this.count++;
        }
    }

    protected void descCount() {
        synchronized (this) {
            this.count--;
        }
    }

    @Override
    public String getLockKey() {
        return key;
    }

    @Override
    public Boolean tryLock(Integer maxWaitTime) {
        if (this.count == 0) {
            Boolean aBoolean = doGetLock();
            if (aBoolean) {
                this.addCount();
                return true;
            } else {//延迟
                int waitTime = 1;
                maxWaitTime = maxWaitTime * 100;
                while (waitTime < maxWaitTime) {
                    try {
                        Thread.sleep(10);
                        aBoolean = doGetLock();
                        if (aBoolean) {
                            this.addCount();
                            return aBoolean;
                        }
                        waitTime++;
                    } catch (InterruptedException e) {
                        waitTime++;
                    }
                }
                return false;
            }
        }

        this.addCount();
        return true;

    }

    protected Boolean doGetLock() {
        return redisCommon.setIfAbsent(getLockKey(), this.value, 60);
    }


    @Override
    public Boolean unlock() {
        this.descCount();

        if (this.count == 0) {
            Object o = redisCommon.get(getLockKey());
            if (ObjectUtils.isNotNull(o) && o.equals(this.value)) {
                redisCommon.del(this.getLockKey());
                return true;
            }
            return false;
        } else {//重入锁减1
            return true;
        }
    }
}
