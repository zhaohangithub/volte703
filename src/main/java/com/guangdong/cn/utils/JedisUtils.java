package com.guangdong.cn.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {
    private static JedisPool jedisPool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);//最大连接数
        config.setMaxIdle(30);//闲时最大连接
        config.setMinIdle(5);//闲时最小数
        jedisPool = new JedisPool(config, GlobalConfUtils.RedisHost,Integer.parseInt(GlobalConfUtils.RedisPort),5000);
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}
