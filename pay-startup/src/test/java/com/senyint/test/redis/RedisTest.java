package com.senyint.test.redis;

import com.senyint.common.redis.JedisTemplate;
import com.senyint.common.redis.RedisClient;
import com.senyint.common.util.RandomStringUtil;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
public class RedisTest {

    RedisClient redisClient;

    String host = "192.168.20.159";
    int port = 6379;

    @Setup
    public void setup() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisTemplate jedisTemplate = new JedisTemplate(jedisPoolConfig, host, port);
        redisClient = new RedisClient(jedisTemplate);
    }

    @Benchmark
    public void testSet() throws Exception {
        redisClient.set("lhm", "hello world");
    }

    @Benchmark
    public void testGet() throws Exception {
        redisClient.get("lhm");
    }

    @Benchmark
    public void testKeys() throws Exception {
        redisClient.keys("mr:*");
    }

    @Benchmark
    public void testDel() throws Exception {
        redisClient.del("mr:id:NA5Xq-29", "mr:id:V9DBL-37");
    }

    @Test
    public void testSETNX() throws Exception {
        setup();

        long now = System.currentTimeMillis();
        long timeout = 10 * 1000;
        long lockTimeout = now + timeout + 1;
        long resultCode = redisClient.setnx("lock.foo", "" + lockTimeout);

        System.out.println(resultCode);
    }

    @Test
    public void testSETEX() throws Exception {
        setup();
        String resultCode = redisClient.setex("lhm", 20, "i see you");
        System.out.println(resultCode);
    }

    @Benchmark
    public void testMget() throws Exception {
        List<String> values = redisClient.mget("mr:id:V9DBL-28", "mr:id:V9DBL-47");
//        values.forEach(value -> {
//            System.out.println(value);
//        });
    }

    @Benchmark
    public void testMset() throws Exception {
        String[] keyvalues = new String[10];
        for (int i = 0, j = 0, size = 2; i < keyvalues.length / size; i++) {
            j = i * size;
            keyvalues[j] = RandomStringUtil.randomNumeric(5) + "_mset";
            keyvalues[j + 1] = "mset" + i;
            if (j >= keyvalues.length) {
                break;
            }
        }
        redisClient.mset(keyvalues);
    }


}
