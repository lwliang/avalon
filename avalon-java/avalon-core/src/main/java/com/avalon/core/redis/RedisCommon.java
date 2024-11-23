/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import com.avalon.core.context.Context;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public abstract class RedisCommon {

    protected abstract RedisTemplate<String, Object> getRedisTemplate();

    /**
     * 发布订阅消息
     *
     * @param topic
     * @param message
     */
    public void convertAndSend(String topic, Object message) {
        getRedisTemplate().convertAndSend(topic, message);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间（秒）
     * @return true/false
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                getRedisTemplate().expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据 key 获取时间
     *
     * @param key 键
     * @return long time
     */
//	@SuppressWarnings("all")
    public Long getExpire(String key) {
        return getRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断键是否存在 TODO 有BUG
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 删除缓存
     * SuppressWarnings("unchecked") 忽略类型转换警告
     *
     * @param key 键 一个或者多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                getRedisTemplate().delete(key[0]);
            } else {
                getRedisTemplate().delete(Arrays.asList(key));
            }
        }
    }

    // ============================= String ============================

    /**
     * 获取 登陆 token
     * @param token
     * @return
     */
    public Object getToken(String token) {
        return token == null ? null : getRedisTemplate().opsForValue().get(SystemConstant.TOKEN_PREFIX + token);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : getRedisTemplate().opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true/false
     */
    public boolean set(String key, Object value) {
        try {
            getRedisTemplate().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒），如果time<0则设置无限时间
     * @return true/false
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                getRedisTemplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 递增大小
     * @return 次数
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于 0");
        }
        return getRedisTemplate().opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 递减大小
     * @return 次数
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于 0");
        }
        return getRedisTemplate().opsForValue().decrement(key, delta);
    }

    // ======================================== Map =============================

    /**
     * HashGet
     *
     * @param key  键(not null)
     * @param item 项(not null)
     * @return 值
     */
    public Object hget(String key, String item) {
        return getRedisTemplate().opsForHash().get(key, item);
    }


    /**
     * 获取 key 对应的 map
     *
     * @param key 键 (not null)
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return getRedisTemplate().opsForHash().entries(key);
    }

    public List<Object> dataList(String redisKey) {
        List<Object> resultList = new ArrayList<>();
        Map<Object, Object> isRedis = hmget(redisKey);
        if (isRedis.size() > 0) {
            isRedis.forEach((k, v) -> {
                resultList.add(v);
            });
        }
        return resultList;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 值
     * @return true/false
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            getRedisTemplate().opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  值
     * @param time 时间
     * @return true/false
     */
    public boolean hmset(String key, Map<Object, Object> map, long time) {
        try {
            getRedisTemplate().opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据, 如不存在则创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true/false
     */
    public boolean hset(String key, String item, Object value) {
        try {
            getRedisTemplate().opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 Hash表 中放入数据，并设置时间，如不存在则创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间（如果原来的 Hash表 设置了时间，这里会覆盖）
     * @return true / false
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            getRedisTemplate().opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 Hash表 中的值
     *
     * @param key  键
     * @param item 项（可以多个，not null）
     */
    public void hdel(String key, Object... item) {
        getRedisTemplate().opsForHash().delete(key, item);
    }

    /**
     * 判断 Hash表 中是否有该键的值
     *
     * @param key  键（not null）
     * @param item 值（not null）
     * @return true / false
     */
    public boolean hHasKey(String key, String item) {
        return getRedisTemplate().opsForHash().hasKey(key, item);
    }

    /**
     * Hash递增，如果不存在则创建一个，并把新增的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   递增大小 > 0
     * @return 值
     */
    public Double hincr(String key, String item, Double by) {
        return getRedisTemplate().opsForHash().increment(key, item, by);
    }

    /**
     * Hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   递减大小
     * @return 值
     */
    public Double hdecr(String key, String item, Double by) {
        return getRedisTemplate().opsForHash().increment(key, item, -by);
    }

    // ================================== Set =====================================

    /**
     * 根据 key 获取 set 中的所有值
     *
     * @param key 键
     * @return 值
     */
    public Set<Object> sGet(String key) {
        try {
            return getRedisTemplate().opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从键为 key 的 set 中，根据 value 查询是否存在
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return getRedisTemplate().opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入 set缓存
     *
     * @param key    键
     * @param values 值（可以多个）
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return getRedisTemplate().opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将数据放入 set缓存，并设置时间
     *
     * @param key    键
     * @param time   时间
     * @param values 值（可以多个）
     * @return 成功放入个数
     */
    public long sSet(String key, long time, Object... values) {
        try {
            long cnt = getRedisTemplate().opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取 set缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sGetSetSize(String key) {
        try {
            return getRedisTemplate().opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除 set缓存中，值为 value 的
     *
     * @param key    键
     * @param values 值
     * @return 成功移除个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return getRedisTemplate().opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ================================= List ===============================

    /**
     * 获取 list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束（0 到 -1 代表所有值）
     * @return 项
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return getRedisTemplate().opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lGetListSize(String key) {
        try {
            return getRedisTemplate().opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据索引 index 获取键为 key 的 list 中的元素
     *
     * @param key   键
     * @param index 索引
     *              当 index >= 0 时 {0:表头, 1:第二个元素}
     *              当 index < 0 时 {-1:表尾, -2:倒数第二个元素}
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return getRedisTemplate().opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将值 value 插入键为 key 的 list 中，如果 list 不存在则创建空 list
     *
     * @param key   键
     * @param value 值
     * @return true / false
     */
    public boolean lSet(String key, Object value) {
        try {
            getRedisTemplate().opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将值 value 插入键为 key 的 list 中，并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return true / false
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            getRedisTemplate().opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 values 插入键为 key 的 list 中
     *
     * @param key    键
     * @param values 值
     * @return true / false
     */
    public boolean lSetList(String key, List<Object> values) {
        try {
            getRedisTemplate().opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 values 插入键为 key 的 list 中，并设置时间
     *
     * @param key    键
     * @param values 值
     * @param time   时间
     * @return true / false
     */
    public boolean lSetList(String key, List<Object> values, long time) {
        try {
            getRedisTemplate().opsForList().rightPushAll(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引 index 修改键为 key 的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true / false
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            getRedisTemplate().opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在键为 key 的 list 中删除值为 value 的元素
     *
     * @param key   键
     * @param count 如果 count == 0 则删除 list 中所有值为 value 的元素
     *              如果 count > 0 则删除 list 中最左边那个值为 value 的元素
     *              如果 count < 0 则删除 list 中最右边那个值为 value 的元素
     * @param value
     * @return 次数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return getRedisTemplate().opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒），如果time<0则设置无限时间
     * @return true/false
     */
    public Boolean setIfAbsent(String key, Object value, long time) {
        return getRedisTemplate().opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    @Autowired
    private Context context;

    /**
     * 获取redis锁
     *
     * @param key
     * @return
     */
    public IRedisLock getLock(String key) {
        IRedisLock redisLock = context.getRedisLock(key);

        if (ObjectUtils.isNull(redisLock)) {
            RedisLock redisLockInstance = new RedisLock(this, key);
            context.setRedisLock(key, redisLockInstance);
            return redisLockInstance;
        }
        return redisLock;
    }

    public String getServiceRedisKey(String serviceName) {
        return serviceName.replace(".", ":");
    }

    public String getServiceRedisKey(String serviceName, String id) {
        return serviceName.replace(".", ":") + ":" + id;
    }
}
