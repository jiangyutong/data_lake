package com.boyoi.base.tools.redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.Objects;

public class JedisUtil {

    private static final Long UNLOCK_SUCCESS = 1L;

    protected static void release(Jedis jedis) {
        jedis.close();
    }

    public static boolean setnx(JedisPool jedisPool, String key, String value, Long expireMillis) {
        Jedis jedis = null;
        boolean flag = false;
        /*try {
            jedis = jedisPool.getResource();

            // nx = not exist, px= 单位是毫秒
            String result = jedis.setnx(key,value);
            if (result != null && result.equalsIgnoreCase("OK")) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(jedis);
        }*/
        return flag;
    }

    @Deprecated
    public static long setnx(JedisPool jedisPool, String key, String value) {
        Jedis jedis = null;
        long result = 0L;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setnx(key, value);
            jedis.expire(key, 1);
        } catch (Exception e) {
            System.out.println("JedisDao::setnxOld: key: " + key + ",value:" + value + " message: " + e.getMessage());
        } finally {
            release(jedis);
        }
        return result;
    }

    public static boolean unlock(JedisPool jedisPool, String fullKey, String value) {
//    return unlockV1(fullKey, value);
        return unlockV2(jedisPool, fullKey, value);

    }

    private static boolean unlockV2(JedisPool jedisPool, String fullKey, String value) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = jedisPool.getResource();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(fullKey), Collections.singletonList(value));
            if (Objects.equals(UNLOCK_SUCCESS, result)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.unwatch();
            release(jedis);
        }
        return flag;
    }

    private static boolean unlockV1(JedisPool jedisPool, String fullKey, String value) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = jedisPool.getResource();
            jedis.watch(fullKey);
            String existValue = jedis.get(fullKey);
            if (Objects.equals(value, existValue)) {
                jedis.del(fullKey);
                flag = true;
            } else {
                System.out.println("unlock failed ; key:" + fullKey + ",value:" + value + ",existValue:" + existValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.unwatch();
            release(jedis);
        }
        return flag;
    }
}
