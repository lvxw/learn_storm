package com.test.java.business.template;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * @author lvxw
 */
public class WordOutputBolt extends BaseBasicBolt {

    private Map<String,String> redisParamMap;
    private static JedisPool pool;

    public WordOutputBolt(Map<String,Object> fixedParamMap) {
        this.redisParamMap = (Map<String, String>) fixedParamMap.get("redis");
        pool = getPool();
    }

    public JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setMaxWaitMillis(1000 * 100);
            config.setTestOnBorrow(true);
            pool = new JedisPool(
                    config,
                    redisParamMap.get("redis_host"),
                    Integer.parseInt(redisParamMap.get("redis_port")),
                    Integer.parseInt(redisParamMap.get("timeout")),
                    null,
                    Integer.parseInt(redisParamMap.get("redis_db"))
            );
        }
        return pool;
    }


    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Long key = input.getLongByField("TIME");
        String value = input.getStringByField("VALUE");

        pool.getResource().rpush(key.toString(),value);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
