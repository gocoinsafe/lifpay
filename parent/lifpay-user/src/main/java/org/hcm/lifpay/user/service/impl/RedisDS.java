package org.hcm.lifpay.user.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;
import org.hcm.lifpay.config.RedisDSConfiguration;
import redis.clients.jedis.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class RedisDS implements Closeable {

    /** 默认配置文件 */
    public final static String REDIS_CONFIG_PATH = "redis.properties";
    public static final String SCRIPT_LOCK = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 1 else return 0 end";
    public static final String SCRIPT_UNLOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    public static final int LOCK_TIME = 300;

    public static final String STOCK_DECR_LUA = "local stock = tonumber(redis.call('get', KEYS[1])) if  stock ~= nil and stock > tonumber(0)  then redis.call('decr', KEYS[1]) return 1 else return 0 end";

    /**
     * redis lock key exist
     */
    public static final String REIDS_LOCK_RESULT_EXIST="0";
    /**
     * redis lock key exist
     */
    public static final String EXECUTE_FAIL="0";

    /** 配置文件 */
    private Setting setting;
    /** Jedis连接池 */
    private JedisPool pool;

    // --------------------------------------------------------------------------------- Static method start
    /**
     * 创建RedisDS，使用默认配置文件，默认分组
     *
     * @return {@link RedisDS}
     */
    public static RedisDS create() {
        return new RedisDS();
    }

    /**
     * 创建RedisDS，使用默认配置文件，默认分组
     *
     * @return {@link RedisDS}
     */
    public static RedisDS create(RedisDSConfiguration configuration) {
        Setting setting = new Setting();
        setting.put("maxIdle", String.valueOf(configuration.getMaxIdle()));
        setting.put("maxWaitMillis", String.valueOf(configuration.getMaxWaitMillis()));
        setting.put("host", configuration.getHost());
        setting.put("port", String.valueOf(configuration.getPort()));
        // 连接时间
        setting.put("connectionTimeout", String.valueOf(configuration.getTimeout()));
        // 读取超时
        setting.put("soTimeout",  String.valueOf(configuration.getTimeout()));
        setting.put("password", configuration.getPassword());
        setting.put("database", String.valueOf(configuration.getDatabase()));
        setting.put("clientName", "dab");
        setting.put("ssl", "false");
        return new RedisDS(setting, null);
    }

    /**
     * 创建RedisDS，使用默认配置文件
     *
     * @param group 配置文件中配置分组
     * @return {@link RedisDS}
     */
    public static RedisDS create(String group) {
        return new RedisDS(group);
    }

    /**
     * 创建RedisDS
     *
     * @param setting 配置文件
     * @param group 配置文件中配置分组
     * @return {@link RedisDS}
     */
    public static RedisDS create(Setting setting, String group) {
        return new RedisDS(setting, group);
    }
    // --------------------------------------------------------------------------------- Static method end

    /**
     * 构造，使用默认配置文件，默认分组
     */
    public RedisDS() {
        this(null, null);
    }

    /**
     * 构造，使用默认配置文件
     *
     * @param group 配置文件中配置分组
     */
    public RedisDS(String group) {
        this(null, group);
    }

    /**
     * 构造
     *
     * @param setting 配置文件
     * @param group 配置文件中配置分组
     */
    public RedisDS(Setting setting, String group) {
        this.setting = setting;
        init(group);
    }

    /**
     * 初始化Jedis客户端
     *
     * @param group Redis服务器信息分组
     * @return this
     */
    public RedisDS init(String group) {
        if (null == setting) {
            setting = new Setting(REDIS_CONFIG_PATH, true);
        }

        final JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        // 共用配置
        setting.toBean(config);
        if (StrUtil.isNotBlank(group)) {
            // 特有配置
            setting.toBean(group, config);
        }
        //# 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        this.pool = new JedisPool(config,
                // 地址
                setting.getStr("host", group, Protocol.DEFAULT_HOST),
                // 端口
                setting.getInt("port", group, Protocol.DEFAULT_PORT),
                // 连接超时
                setting.getInt("connectionTimeout", group, setting.getInt("timeout", group, Protocol.DEFAULT_TIMEOUT)),
                // 读取数据超时
                setting.getInt("soTimeout", group, setting.getInt("timeout", group, Protocol.DEFAULT_TIMEOUT)),
                // 密码
                setting.getStr("password", group, null),
                // 数据库序号
                setting.getInt("database", group, Protocol.DEFAULT_DATABASE),
                // 客户端名
                setting.getStr("clientName", group, "dab"),
                // 是否使用SSL
                setting.getBool("ssl", group, false),
                // SSL相关，使用默认
                null, null, null);

        return this;
    }

    /**
     * 从资源池中获取{@link Jedis}
     *
     * @return {@link Jedis}
     */
    public Jedis getJedis() {
        return this.pool.getResource();
    }

    /**
     * 从Redis中获取值
     *
     * @param key 键
     * @return 值
     */
    public String getStr(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    /**
     * 从Redis中获取值
     *
     * @param key 键
     * @param value 值
     * @return 状态码
     */
    public String setStr(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    public String setex(String key, String value, int time) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, time, value);
        }
    }

    /**
     * 从Redis中删除多个值
     *
     * @param keys 需要删除值对应的键列表
     * @return 删除个数，0表示无key可删除
     */
    public Long del(String... keys) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(keys);
        }
    }

    /**
     * 获取key 对应的失效剩余时间，单位秒
     * @param key
     * @return
     */
    public Long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    /**
     * 设置指定Key的到期时间
     * @param key
     * @param secondTime  单位秒
     * @return
     */
    public Long expire(String key,int secondTime){
        try (Jedis jedis = getJedis()) {
            return jedis.expire(key,secondTime);
        }
    }

    @Override
    public void close() throws IOException {
        IoUtil.close(pool);
    }


    /**
     * 检查锁是否存在，存在则返回true,不存在则创建锁，返回false
     *
     * @param redisKey
     * @param ttlTime 锁时间，单位是ms 空则默认是5分钟
     * @return
     */
    public boolean checkRedisLock(String redisKey,Long ttlTime) {
        try (Jedis jedis = getJedis()) {
            Object  lockResult = jedis.eval(SCRIPT_LOCK,1,redisKey,redisKey,null==ttlTime?String.valueOf(LOCK_TIME):ttlTime.toString());
            if (REIDS_LOCK_RESULT_EXIST.equals(lockResult.toString())){
                return true;
            }else{
                return false;
            }
        }
    }

    public void unLockRedis(String redisKey) {
        try (Jedis jedis = getJedis()) {
            Object  result = jedis.eval(SCRIPT_UNLOCK,1,redisKey,redisKey);
        }
    }


    /**
     * 解决超卖场景，库存扣减
     * @param redisKey
     * @return
     */
    public boolean decrStock(String redisKey) {
        try (Jedis jedis = getJedis()) {
            Object  lockResult = jedis.eval(STOCK_DECR_LUA,1,redisKey);
            // 执行失败返回false
            if (EXECUTE_FAIL.equals(lockResult.toString())){
                return false;
            }
            return true;
        }
    }

    /**
     * 自增1
     * @param redisKey
     * @return
     */
    public Long incr(String redisKey) {
        try (Jedis jedis = getJedis()) {
            return jedis.incr(redisKey);
        }
    }

    /**
     * 自减1
     * @param redisKey
     * @return
     */
    public Long decr(String redisKey) {
        try (Jedis jedis = getJedis()) {
            return jedis.decr(redisKey);
        }
    }

    /**
     * 自减1
     * @param redisKey
     * @return
     */
    public Long incrBy(String redisKey,long increment) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(redisKey,increment);
        }
    }

    public Object evalsha(String script, List<String> keys, List<String> params) {
        try (Jedis jedis = getJedis()) {
            return jedis.evalsha(jedis.scriptLoad(script), keys, params);
        }
    }

    public Object eval(String script, List<String> keys, List<String> params) {
        try (Jedis jedis = getJedis()) {
            return jedis.eval(jedis.scriptLoad(script), keys, params);
        }

    }

    public long setSadd(String key, String[] value) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key,value);
        }
    }

    public String setSpop(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.spop(key);
        }
    }
    public Long setScard(String key){
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        }
    }
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, Integer pageSize) {
        try (Jedis jedis = getJedis()) {
            ScanParams params = new ScanParams();
            params.count(pageSize);
            return jedis.hscan(key, cursor, params);
        }
    }

    public Set<String> keys(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.keys(pattern);
        }
    }



}
