package com.RedisDemo01;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * redis的应用场景分析
 *  主要是用来做缓存
 *      缓存主要有两个作用:
 *          1.快速访问
 *          2.减少IO频率
 * 减少IO的频率就是等缓存积累一定的量(大小)之后写到磁盘中进行持久化
 *
 * 一般的设计就是客户端往数据库里更新或者写读数据,redis作为经常需要被读取的数据或者被修改数据的缓存,提高操作效率,
 * 一般的操作应该是客户端要修改数据说话,先去缓存redis去找,找不到的话去数据库读取,替换不热的缓存,不热的缓存刷回数据,能找到的话直接修改,这不存在一致性的问题
 * 如果要并发访问redis和SQL.这样要保持一致性问题的话,进行读操作的时候就不能进行写操作了,就是客户端更新redis,然后redis回写数据库,这是一个事务.
 * 这个事务如果有一步不成功,那么整个事务就是失败的.
 *
 */
public class RedisConnectionTest {

    private RedisConnection redisConnection;

    @Before
    public void before() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置 redis 连接池最大连接数量
        jedisPoolConfig.setMaxTotal(50);
        //设置 redis 连接池最大空闲连接数量
        jedisPoolConfig.setMaxIdle(10);
        //设置 redis 连接池最小空闲连接数量
        jedisPoolConfig.setMinIdle(1);
        redisConnection = new RedisConnection();
        redisConnection.setIp("192.168.136.200");
        redisConnection.setPort(6379);
//        redisConnection.setPwd("test123");
        redisConnection.setClientName(Thread.currentThread().getName());
        redisConnection.setTimeout(600);
        redisConnection.setJedisPoolConfig(jedisPoolConfig);

    }

    @Test
    public void testPutGet() {

        // 通过连接池 获取redis
        Jedis jedis = redisConnection.getJedis();

        try {
            //删除库里的所有数据
            System.out.println(jedis.flushDB());

            //选择数据库
//            jedis.select(5);
            jedis.set("name", "grade");

            for (int i = 0; i <= 3000; i++) {
                jedis.set(i + "hello", i + "word.." + UUID.randomUUID());
            }

            // 一个list集合
            for (int i = 0; i < 10; i++) {
                jedis.lpush("list_alice", "" + UUID.randomUUID());
            }

            jedis.set("fallback", "helloworld");
            //设置有效期10秒
            jedis.expire("fallback",10);

            //断言语句
            Assert.assertTrue("grade".equals(jedis.get("name")));

            //获取所有的keys
            Set<String> keys = jedis.keys("*");
            System.out.println("redis已经save keys.length==> " + keys.size());

            //再根据这些key获取所有的value
            for (String key : keys) {
                if (jedis.type(key).equalsIgnoreCase("string")) {
                    System.out.println(jedis.get(key));
                }

                if (jedis.type(key).equalsIgnoreCase("set")) {
                    System.out.println(jedis.get(key));
                }

                //获取list集合所有的value
                if (jedis.type(key).equalsIgnoreCase("list")) {
                    List<String> list_alice1 = jedis.lrange(key, 0, -1);
                    for (String str : list_alice1) {
                        System.out.println(str);
                    }
                }

            }

            ScanResult<String> scan = jedis.scan("1");
            System.err.println(scan.getResult());

            //线程睡眠10秒后 获取已经过期的key
            Thread.sleep(10000);
            System.out.println(jedis.get("fallback"));

            //or 可以使用这种方式  设置key的时候顺便设置过期时间
            jedis.setex("fallback",10,"heloo");

//            System.out.println(jedis.lpop("list_alice").length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
