package com.yu.test.pool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;

import com.bradypod.util.redis.RedisImpl;

/**
 * 测试Redis
 *
 * @author zengxm<github.com/JumperYu>
 *
 * @date 2015年9月21日 下午12:04:15
 */
public class RedisTest {

	private RedisImpl redis;

	@Before
	public void init() {
		redis = new RedisImpl();
		JedisPool pool = new JedisPool("redis.bradypod.com", 6379);
		redis.setPool(pool);
	}

	@Test
	public void testSet() {
		// 设置key的value, 返回状态码
		String ret = redis.set("zxm", "qwerty");
		LOGGER.info(ret);
		// 设置key, 如果已不存在, 20秒后失效
		ret = redis.set("zxm", "newValue-1", "nx", "ex", 200);
		LOGGER.info(ret);
		// 设置key, 如果已存在, 20000毫秒后失效
		ret = redis.set("zxm", "newValue-2", "xx", "px", 20000);
		LOGGER.info(ret);
		// 简化版
		LOGGER.info(redis.setnx("not-exist", "value") + "");
		LOGGER.info(redis.setex("not-exist", 100, "value"));
	}

	@Test
	public void testGet() {
		String ret = redis.get("zxm");
		LOGGER.info(ret);
	}

	@Test
	public void testExists() {
		LOGGER.info(redis.exists("zxm").toString());
	}

	@Test
	public void testPersit() {
		// 持久化
		LOGGER.info(redis.persist("zxm") + "");
	}

	@Test
	public void testType() {
		LOGGER.info(redis.type("zzzz"));
	}

	@Test
	public void testExpire() {
		LOGGER.info(redis.expire("zxm", 100) + "");
	}

	@Test
	public void testExpireAt() throws ParseException {
		// 设置一个时间点失效
		LOGGER.info(redis.pexpireAt("zxm",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-09-21 14:40:00").getTime())
				+ "");
	}

	@Test
	public void testTTL() {
		LOGGER.info(redis.ttl("zxm") + "");
	}

	@Test
	public void testSetBit() {
		redis.setbit("zxm", 1, true);
	}

	// TODO set/getBit

	@Test
	public void testSetRange() {
		redis.set("zxm", "hello world");
		LOGGER.info(redis.setrange("zxm", 6, "dear redis!") + "");
		LOGGER.info(redis.get("zxm"));
	}

	@Test
	public void testGetRange() {
		LOGGER.info(redis.getrange("zxm", 0, -1));
	}

	@Test
	public void testGetSet() {
		LOGGER.info(redis.getSet("not-exist", "123"));
	}

	@Test
	public void testIncrAndDecr() {
		redis.set("math", "100");
		LOGGER.info(redis.decrBy("math", 20) + "");
		LOGGER.info(redis.decr("math") + "");
		LOGGER.info(redis.incr("math") + "");
		LOGGER.info(redis.incrBy("math", 20) + "");
		LOGGER.info(redis.incrByFloat("math", 10.0) + "");
	}

	@Test
	public void tesStr() {
		redis.set("string", "The fox jump over a cat");
		// setrange是截取覆盖掉
		redis.setrange("string", 0, "one");
		LOGGER.info(redis.get("string"));
		// 截取但不覆盖
		LOGGER.info(redis.substr("string", 0, 3));
		LOGGER.info(redis.get("string"));
		// 拼接
		LOGGER.info(redis.append("string", "!") + "");
	}

	@Test
	public void testHsetAndGet() {
		// 设置map
		LOGGER.info(redis.hset("myMap", "key-1", "value-1") + "");
		LOGGER.info(redis.hset("myMap", "key-1", "value-1") + "");
		// 获取
		LOGGER.info(redis.hget("myMap", "key-1"));
		// 设置多个
		Map<String, String> map = new HashMap<>();
		map.put("key-1", "value-1");
		map.put("key-2", "value-2");
		map.put("key-3", "value-3");
		map.put("key-4", "1");
		redis.hmset("map", map);
		for (String value : redis.hmget("map", "key-1", "key-2", "key-3")) {
			LOGGER.info(value);
		}
		LOGGER.info(redis.hincrBy("map", "key-4", 1) + "");
		LOGGER.info(redis.hlen("map") + "");
		// keys && values
		LOGGER.info(redis.hkeys("map").toString());
		LOGGER.info(redis.hvals("map").toString());
	}

	@Test
	public void testList() {
		redis.rpush("myList", "one");
		redis.rpush("myList", "two");
		redis.rpush("myList", "three");
		redis.rpush("myList", "four");
		redis.rpush("myList", "five");
		// 获取全部
		LOGGER.info(redis.lrange("myList", 0, -1).toString());
		// 保留第一个到最后一个
		redis.ltrim("myList", 1, -1).toString();
		LOGGER.info(redis.lrange("myList", 0, -1).toString());
		LOGGER.info(redis.lpop("myList"));
		LOGGER.info(redis.rpop("myList"));
		LOGGER.info(redis.lrange("myList", 0, -1).toString());
		redis.del("myList");
	}

	@Test
	public void testListRemove() {
		redis.rpush("remove", "hello");
		redis.rpush("remove", "hello");
		redis.rpush("remove", "hi");
		redis.rpush("remove", "nihao");
		redis.rpush("remove", "hello");
		LOGGER.info(redis.lrem("remove", -2, "hello") + "");
		LOGGER.info(redis.lrange("remove", 0, -1).toString());
		redis.del("remove");
	}

	@Test
	public void testHashSet() {
		redis.sadd("hashset", "one");
		redis.sadd("hashset", "one");
		redis.sadd("hashset", "two");
		redis.sadd("hashset", "three");
		redis.sadd("hashset", "four");
		redis.sadd("hashset", "five");
		LOGGER.info(redis.smembers("hashset").toString());
		// LOGGER.info(redis.spop("hashset", 1).toString());
		// 删除
		redis.srem("hashset", "one");
		LOGGER.info(redis.scard("hashset") + "");
		LOGGER.info(redis.srandmember("hashset"));
		LOGGER.info(redis.srandmember("hashset", 3).toString());
		redis.strlen("hashset");
		redis.del("hashset");
	}

	@Test
	public void testSortedSet() {
		redis.del("zset");
		redis.zadd("zset", 1, "three");
		redis.zadd("zset", 1, "one");
		redis.zadd("zset", 1, "two");
		LOGGER.info(redis.zrange("zset", 0, -1).toString());
		redis.del("zset");
	}

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
}
