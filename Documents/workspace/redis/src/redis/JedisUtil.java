package redis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

	private JedisUtil() {

	}

	private static Map<String, JedisPool> maps = new HashMap<String, JedisPool>();

	/**
	 * 获取连接池
	 * 
	 * @param ip
	 * @param port
	 * @return 连接池实例
	 */
	public static JedisPool getPool(String ip, int port) {
		String key = ip + ":" + port;
		JedisPool pool = null;
		if (!maps.containsKey(key)) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(10);
			config.setMaxWait(5);
			config.setMaxWait(1000);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			/**
			 * 如果你遇到 java.net.SocketTimeoutException: Read timed out
			 * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
			 * JedisPool默认的超时时间是2秒(单位毫秒)
			 */
			pool = new JedisPool(config, ip, port, 3000);
			maps.put(key, pool);
		} else {
			pool = maps.get(key);
		}
		return pool;
	}

	/**
	 * 
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
	 * 
	 * @author lizengfa
	 *
	 */
	private static class RedisUtilHolder {
		/**
		 * 静态初始化，由JVM来保证线程安全
		 */
		private static JedisUtil instance = new JedisUtil();
	}

	public static JedisUtil getInstance() {
		return RedisUtilHolder.instance;
	}

	/**
	 * 获取jedis实例
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	public Jedis getJedis(String ip, int port) {
		Jedis jedis = null;
		int count = 0;
		do {
			jedis = getPool(ip, port).getResource();
			count++;
		} while (jedis == null && count < 10);
		return jedis;
	}

	/**
	 * 释放jedis实例到连接池
	 * 
	 * @param jedis
	 * @param ip
	 * @param port
	 */
	public void closeJedis(Jedis jedis, String ip, int port) {
		if (jedis != null) {
			getPool(ip, port).returnResource(jedis);
		}
	}

}
