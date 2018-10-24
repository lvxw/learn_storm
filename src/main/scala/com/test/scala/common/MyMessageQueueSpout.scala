package com.test.scala.common

import backtype.storm.spout.SchemeAsMultiScheme
import storm.kafka.{KafkaSpout, SpoutConfig, StringScheme, ZkHosts}

object MyMessageQueueSpout {

  def getKafkaSpout(topologyName:String,topic:String,zookeeperKafkaMap:Map[String,String],id:String="kafkaSpout"):KafkaSpout ={
    val brokerHosts = new ZkHosts(zookeeperKafkaMap.get("zk_connect").get)
    val spoutConf = new SpoutConfig(brokerHosts, topic, zookeeperKafkaMap.get("zk_storm").get+topologyName,id)
    spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme)
    spoutConf.forceFromStart = true
    spoutConf.startOffsetTime = -1
    new KafkaSpout(spoutConf)
  }
}
