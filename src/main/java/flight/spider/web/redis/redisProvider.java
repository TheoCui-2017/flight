package flight.spider.web.redis;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class redisProvider {  
	  
    protected static final Logger LOG = Logger.getLogger(redisProvider.class);
    protected static JedisPool jedispool;
    protected static int EXPIRE = 130;
    protected int expire = 130;
	protected int maxTotal;
    protected int maxIdle;
    protected long maxWaitMillis;
    protected boolean testOnBorrow;
    protected boolean testOnReturn;
    protected String ip;
    protected int port;
    
    public redisProvider(){
    	super();
    }
    
    @PostConstruct
    public void init(){  
        JedisPoolConfig jedisconfig = new JedisPoolConfig();  
        jedisconfig.setMaxTotal(Integer.valueOf(maxTotal));  
        jedisconfig.setMaxIdle(maxIdle);  
        jedisconfig.setMaxWaitMillis(maxWaitMillis);  
        jedisconfig.setTestOnBorrow(testOnBorrow);  
        jedisconfig.setTestOnReturn(testOnReturn);  
        jedispool = new JedisPool(jedisconfig, ip, port, 100000);
    }  
      
    public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
		EXPIRE = this.expire;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

    public static Jedis getJedis() {  
        Jedis jedis = null;  
        try {  
            jedis = jedispool.getResource();  
        } catch (JedisConnectionException jce) {  
        	LOG.error("get reids error:",jce); 
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
            	LOG.error(e); 
            }  
            jedis = jedispool.getResource();  
        }  
        return jedis;  
    }  
  
    public static void returnResource(JedisPool pool, Jedis jedis) {  
        if (jedis != null) {
        	jedis.close();
            //pool.returnResource(jedis);  
        }  
    }  
}  