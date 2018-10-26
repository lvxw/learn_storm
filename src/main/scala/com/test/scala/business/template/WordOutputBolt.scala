package com.test.scala.business.template

import backtype.storm.topology.{BasicOutputCollector, OutputFieldsDeclarer}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

class WordOutputBolt(val fixedParamMap:Map[String, Any]) extends BaseBasicBolt{
  lazy val pool:JedisPool = getPool()
  val redisParamMap = fixedParamMap.get("redis").get.asInstanceOf[Map[String,String]]


  def getPool(): JedisPool = {
    val config = new JedisPoolConfig
    config.setMaxTotal(500)
    config.setMaxIdle(5)
    config.setMaxWaitMillis(1000 * 100)
    config.setTestOnBorrow(true)
    new JedisPool(
      config,
      redisParamMap.getOrElse("redis_host","artemis-02"),
      redisParamMap.getOrElse("redis_port","6379").toInt,
      redisParamMap.getOrElse("timeout", "3000").toInt,
      null,
      redisParamMap.getOrElse("redis_db", "0").toInt
    )
  }


  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    val key = input.getStringByField("TIME")
    val value = input.getStringByField("VALUE")
    pool.getResource.rpush(key, value)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {

  }
}
