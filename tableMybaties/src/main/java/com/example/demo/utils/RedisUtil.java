package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Jason
 * @Create: 2020/11/22  9:51
 * @Description redis工具
 */

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 给指定的key指定失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time){
        try{
            if (time > 0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 获取到指定的key失效时间
     * @param key
     * @return
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断是否key存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
       return redisTemplate.hasKey(key);

    }

    /**
     * 删除多个key
     * @param key
     */
    public void delete(String... key){
        //对key值进行判断
        if (key != null && key.length > 0){
            if (key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }


    /**
     * 获取到key值对应的值大小
     * @param key
     * @return
     */
    public Object get(String key){
        return key==null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 存放key,value值
     * @param key
     * @param value
     * @return
     */
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 对key 存放一个有效值
     * @param key
     * @param value
     * @param time
     */
    public void set(String key, Object value, long time){
        if (time > 0){
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
    }


    /**
     * 对key递增dalta因素
     * @param key
     * @param dalta
     * @return
     */
    public long incr(String key, long dalta ){
        if (dalta < 0){
            throw  new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, dalta);
    }


    /**
     * 对key进行递减多少个元素
     * @param key
     * @param delta
     * @return
     */
    public long decr(String key, long delta){
        if (delta < 0){
            throw new RuntimeException("递减因子必须大于0");
        }

        return redisTemplate.opsForValue().decrement(key, delta);
    }


    /**
     * hash取值
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key, String item){
        return redisTemplate.opsForHash().get(key, item);
    }


    /**
     * 获取key下面的所有值
     * @param key
     * @return
     */
    public Map<Object, Object> hmget(String key){
       return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 将对象存储进hash中去
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * 对其中的key进行设置时效时间
     * @param key
     * @param map
     * @param time
     */
    public void hmset(String key, Map<String, Object> map, long time){
        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0){
            expire(key, time);
        }

    }


    /**
     * 往一张表中注入一调数据
     * @param key
     * @param item
     * @param value
     */
    public void hset(String key, String item, Object value){
        redisTemplate.opsForHash().put(key, item,  value);
    }


    /**
     * 对key设置一个过期时间
     * @param key
     * @param item
     * @param value
     * @param time
     */
    public void hset(String key, String item, Object value, long time){
        redisTemplate.opsForHash().put(key, item,value);
        if (time > 0){
            expire(key, time);
        }
    }


    /**
     * 删除hash中的值
     * @param key
     * @param item
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断hash表中是否存在
     * @param key
     * @param item
     */
    public void hHashKey(String key, String item){
        redisTemplate.opsForHash().hasKey(key, item);
    }


    /**
     * 给存在的可以一个值，并存在则会创建并且给它增加值
     * @param key
     * @param item
     * @param by
     */
    public void hincr(String key, String item, double by){
        redisTemplate.opsForHash().increment(key, item, by);
    }


    /**
     * 给存在的key减少一个值
     * @param key
     * @param item
     * @param by
     */
    public void hdecr(String key, String item, double by){
        redisTemplate.opsForHash().increment(key, item, -by);
    }


    /**
     * 从set中获取值
     * @param key
     * @return
     */
    public Set<Object> sGet(String key){
       return redisTemplate.opsForSet().members(key);
    }


    /////////////////////////////////////////////Zset集合中的常用方法
    /**
     * 将元素添加进指定的key,并且指定元素的分值
     * @param key
     * @param obj
     * @param score
     * @return
     */
    public boolean hZSet(String key, Object obj, double score){
        return  redisTemplate.opsForZSet().add(key, obj, score);
    }


    /**
     * 对数据库中的元素进行分页处理
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set hZget(String key, double start, double end){
       return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * 根据指定的数据来删除掉数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long hZDel(String key, double start, double end){
        return  redisTemplate.opsForZSet().removeRangeByScore(key, start, end);
    }

    // List

    /**
     * 从list中取值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
       return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 获取到list的长度
     * @param key
     * @return
     */
    public long lGetLilstSize(String key){
        return  redisTemplate.opsForList().size(key);
    }


    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index){
        return  redisTemplate.opsForList().index(key, index);
    }


    /**
     * list中数据存放进缓存
     * @param key
     * @param value
     */
    public void lSet(String key, Object value){
        redisTemplate.opsForList().rightPush(key,value);
    }


    /**
     * 对list中的key设置失效时间
     * @param key
     * @param value
     * @param time
     */
    public void lSet(String key, Object value, long time){
        redisTemplate.opsForList().rightPush(key, value);
        if (time > 0){
            expire(key, time);
        }
    }


    /**
     * 将一整个List集合存进缓存
     * @param key
     * @param value
     */
    public void lSet(String key, List<Object> value){
        redisTemplate.opsForList().rightPushAll(key, value);
    }


    /**
     * 对key值设置一个失效时间
     * @param key
     * @param value
     * @param time
     */
    public void lSet(String key, List<Object> value, long time){
        redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0){
            expire(key, time);
        }
    }


    /**
     * 将一个value值存进到对应的index中去
     * @param key
     * @param index
     * @param value
     */
    public void lUpdateIndex(String key, long index, Object value){
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 删除对应的index位置的值
     * @param key
     * @param count
     * @param value
     * @return
     */
    public void lRemove(String key, long count, Object value){
        redisTemplate.opsForList().remove(key, count, value);
    }


}
