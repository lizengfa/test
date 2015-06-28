package redis;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class Redis {
	private static JedisPool pool;
	static{
		String ip = "127.0.0.1";
		int port = 6379;
		pool = JedisUtil.getPool(ip, port);
	}
	public static void main(String[] args) {
	   testSet();
		
	}

	
	public static void hello(){
		Jedis jedis = pool.getResource();
		try {
			jedis.set("name", "minxr");
			String value = jedis.get("name");
			System.out.println("firstValue----->"+value);
			
			jedis.append("name", "jintao");
			value = jedis.get("name");
			System.out.println("secondValue---->"+value);
			
			jedis.set("name", "jintao");
			value = jedis.get("name");
			System.out.println("thirdValue----->"+value);
			
			jedis.del("name");
			value = jedis.get("name");
			System.out.println("fourthValue----->"+value);
			
			jedis.mset("name","minxr","jarorwar","aaa");
			System.out.println("mget------>"+jedis.mget("name","jarorwar"));
			System.out.println("getMset1------>"+jedis.get("name"));
			System.out.println("getMset2------>"+jedis.get("jarorwar"));
			
			jedis.flushDB();
			System.out.println("afterFlushDBValue------->"+jedis.get("name"));
			System.out.println(jedis.echo("foo"));
			System.out.println(jedis.exists("foo"));
			jedis.set("key", "values");
			System.out.println(jedis.exists("key"));
			System.out.println(jedis.get("key"));
			
			jedis.setex("foo", 2, "foo not exists");
			System.out.println(jedis.get("foo"));
			Thread.sleep(3000);
			System.out.println(jedis.get("foo"));
			jedis.set("foo", "foo update");  
	        System.out.println(jedis.getSet("foo", "foo modify")); 
	        System.out.println(jedis.get("foo"));
	        System.out.println(jedis.getrange("foo", 1, 3)); 
	        
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			pool.returnResource(jedis);
		}
	}
	
	public static void testList(){
		Jedis jedis = pool.getResource();
		try {
			// 开始前，先移除所有的内容  
            jedis.del("messages");  
            jedis.rpush("messages", "Hello how are you?");  
            jedis.rpush("messages", "Fine thanks. I'm having fun with redis.");  
            jedis.rpush("messages", "I should look into this NOSQL thing ASAP");  
  
            // 再取出所有数据jedis.lrange是按范围取出，  
            // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有 
            List<String> values = jedis.lrange("messages", 0, -1);  
            System.out.println(values);  
		} catch (Exception e) {
			
		}finally{
			pool.returnResource(jedis);
		}
		
		// 清空数据  
        System.out.println(jedis.flushDB());  
        // 添加数据  
        jedis.lpush("lists", "vector");  
        jedis.lpush("lists", "ArrayList");  
        jedis.lpush("lists", "LinkedList");  
        // 数组长度  
        System.out.println(jedis.llen("lists"));  
        // 排序  
        System.out.println(jedis.sort("list", "ALPHA"));  
        // 字串  
        System.out.println(jedis.lrange("lists", 0, 3));  
        // 修改列表中单个值  
        jedis.lset("lists", 0, "hello list!");  
        // 获取列表指定下标的值  
        System.out.println(jedis.lindex("lists", 1));  
        // 删除列表指定下标的值  
        System.out.println(jedis.lrem("lists", 1, "vector"));  
        // 删除区间以外的数据  
        System.out.println(jedis.ltrim("lists", 0, 1));  
        // 列表出栈  
        System.out.println(jedis.lpop("lists"));  
        // 整个列表值  
        System.out.println(jedis.lrange("lists", 0, -1)); 
	}
	
	public static void testSet(){
		Jedis jedis = pool.getResource();
		try {
			 jedis.sadd("myset", "1");  
	            jedis.sadd("myset", "2");  
	            jedis.sadd("myset", "3");  
	            jedis.sadd("myset", "4");  
	            jedis.sadd("myset", "4");  
	            jedis.sadd("myset", "3"); 
	            Set<String> setValues = jedis.smembers("myset");  
	            System.out.println(setValues);  
	  
	            // 移除noname  
	            jedis.srem("myset", "4");  
	            System.out.println(jedis.smembers("myset"));// 获取所有加入的value  
	            System.out.println(jedis.sismember("myset", "4"));// 判断 minxr  
	                                                                // 是否是sname集合的元素 
	            System.out.println(jedis.scard("sname"));// 返回集合的元素个数  
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			pool.returnResource(jedis);
		}
		
		 // 清空数据  
        System.out.println(jedis.flushDB());  
        // 添加数据  
        jedis.sadd("sets", "HashSet");  
        jedis.sadd("sets", "SortedSet");  
        jedis.sadd("sets", "TreeSet");  
        // 判断value是否在列表中  
        System.out.println(jedis.sismember("sets", "TreeSet"));  
        ;  
        // 整个列表值  
        System.out.println(jedis.smembers("sets"));  
        // 删除指定元素  
        System.out.println(jedis.srem("sets", "SortedSet"));  
        // 出栈  
        System.out.println(jedis.spop("sets"));  
        System.out.println(jedis.smembers("sets"));  
        //  
        jedis.sadd("sets1", "HashSet1");  
        jedis.sadd("sets1", "SortedSet1");  
        jedis.sadd("sets1", "TreeSet");  
        jedis.sadd("sets2", "HashSet2");  
        jedis.sadd("sets2", "SortedSet1");  
        jedis.sadd("sets2", "TreeSet1");  
        // 交集  
        System.out.println(jedis.sinter("sets1", "sets2"));  
        // 并集  
        System.out.println(jedis.sunion("sets1", "sets2"));  
        // 差集  
        System.out.println(jedis.sdiff("sets1", "sets2"));
		
	}

}
