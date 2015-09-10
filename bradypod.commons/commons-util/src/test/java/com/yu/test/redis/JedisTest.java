package com.yu.test.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yu.util.date.DateUtils;
import com.yu.util.redis.RedisLock;
import com.yu.util.redis.RedisPool;
import com.yu.util.redis.RedisUtil;
import com.yu.util.thread.ThreadPool;
import com.yu.util.thread.ThreadWorker;

public class JedisTest {

	static final String HOST = "redis.bradypod.com";
	static final int PORT = 6379;

	Jedis jedis;
	ShardedJedis shardedJedis;

	@Before
	public void init() {
		jedis = RedisUtil.createJedis(HOST, PORT);
		shardedJedis = RedisUtil.createShardJedis(new String[] { HOST },
				new int[] { PORT }, new String[] { "" });
	}

	@Test
	public void testGet() throws JsonParseException, JsonMappingException,
			IOException {
		System.out.println(System.currentTimeMillis());
		byte[] bytes = jedis.get("articles".getBytes());
		SerializationUtils.deserialize(bytes);
		System.out.println(System.currentTimeMillis());
	}

	@Test
	public void testShardedGetSet() {
		System.out.println(shardedJedis.set("haha", "1"));
		System.out.println(shardedJedis.get("haha"));
	}

	@Test
	public void testJedisPool() throws InterruptedException {
		// System.out.println(RedisPool.getJedis().get("haha"));
		// System.out.println(RedisPool.getShardedJedis().del("key.lock"));
		// System.out.println(RedisPool.getShardedJedis().get("key.lock"));
		long startTime = System.currentTimeMillis();
		int count = 100;
		final CountDownLatch latch = new CountDownLatch(count);
		ExecutorService ex = Executors.newFixedThreadPool(count);
		for (int i = 0; i < count; i++) {
			ex.execute(new Runnable() {
				@Override
				public void run() {
					try (Jedis redis = RedisPool.getJedis()) {
						redis.get("key_1");

						System.out.println("active:"
								+ RedisPool.jedisPool.getNumActive() + ";idle:"
								+ RedisPool.jedisPool.getNumIdle() + ";wait:"
								+ RedisPool.jedisPool.getNumWaiters());

					}
					latch.countDown();
				}
			});
		}
		latch.await();
		long endTime = System.currentTimeMillis();
		System.out.println("------------finish at:" + (endTime - startTime));
	}

	@Test
	public void testJedisRock() {
		int count = 10;
		ThreadPool pool = new ThreadPool(count);
		pool.executeThread(new ThreadWorker() {

			@Override
			public void execute() {
				try (RedisLock redisLock = new RedisLock("key.lock")) {
					System.out.println(Thread.currentThread().getName() + ","
							+ DateUtils.getDateStr("yyyy-MM-dd HH:mm:ss") + ";"
							+ redisLock.lock());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * redis操作Map
	 */
	@Test
	public void testMap() {
		// -----添加数据----------
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "xinxin");
		map.put("age", "22");
		map.put("qq", "123456");
		jedis.hmset("user", map);
		// 取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
		// 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
		List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
		System.out.println(rsmap);

		// 删除map中的某个键值
		jedis.hdel("user", "age");
		System.out.println(jedis.hmget("user", "age")); // 因为删除了，所以返回的是null
		System.out.println(jedis.hlen("user")); // 返回key为user的键中存放的值的个数2
		System.out.println(jedis.exists("user"));// 是否存在key为user的记录 返回true
		System.out.println(jedis.hkeys("user"));// 返回map对象中的所有key
		System.out.println(jedis.hvals("user"));// 返回map对象中的所有value

		Iterator<String> iter = jedis.hkeys("user").iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + ":" + jedis.hmget("user", key));
		}
	}

	/**
	 * jedis操作List
	 */
	@Test
	public void testList() {
		// 开始前，先移除所有的内容
		jedis.del("java framework");
		System.out.println(jedis.lrange("java framework", 0, -1));
		// 先向key java framework中存放三条数据
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "hibernate");
		// 再取出所有数据jedis.lrange是按范围取出，
		// 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
		System.out.println(jedis.lrange("java framework", 0, -1));

		jedis.del("java framework");
		jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "struts");
		jedis.rpush("java framework", "hibernate");
		System.out.println(jedis.lrange("java framework", 0, -1));
	}

	/**
	 * jedis操作Set
	 */
	@Test
	public void testSet() {
		// 添加
		jedis.sadd("user", "liuling");
		jedis.sadd("user", "xinxin");
		jedis.sadd("user", "ling");
		jedis.sadd("user", "zhangxinxin");
		jedis.sadd("user", "who");
		// 移除noname
		jedis.srem("user", "who");
		System.out.println(jedis.smembers("user"));// 获取所有加入的value
		System.out.println(jedis.sismember("user", "who"));// 判断 who
															// 是否是user集合的元素
		System.out.println(jedis.srandmember("user"));
		System.out.println(jedis.scard("user"));// 返回集合的元素个数
	}

	@Test
	public void test() throws InterruptedException {
		// jedis 排序
		// 注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
		jedis.del("a");// 先清除数据，再加入数据进行测试
		jedis.rpush("a", "1");
		jedis.lpush("a", "6");
		jedis.lpush("a", "3");
		jedis.lpush("a", "9");
		System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
		System.out.println(jedis.sort("a")); // [1, 3, 6, 9] //输入排序后结果
		System.out.println(jedis.lrange("a", 0, -1));
	}

	@Test
	public void testLock() {
		try (RedisLock lock = new RedisLock("sign")) {
			if (lock.lock()) {
				System.out.println("获得锁");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}