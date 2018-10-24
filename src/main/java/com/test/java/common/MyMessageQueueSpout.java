package com.test.java.common;

import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Map;

/**
 * @author lvxw
 */
public class MyMessageQueueSpout {
    public static KafkaSpout getKafkaSpout(String id, String topic, Map<String,String> zookeeperKafkaMap,String topologyName){
        ZkHosts brokerHosts = new ZkHosts(zookeeperKafkaMap.get("zk_connect"));
        SpoutConfig  spoutConf = new SpoutConfig(brokerHosts, topic, zookeeperKafkaMap.get("zk_storm")+topologyName,id);
        spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
        spoutConf.forceFromStart = true;
        spoutConf.startOffsetTime = -1;
        return new KafkaSpout(spoutConf);
    }

    public static KafkaSpout getKafkaSpout(String id, String topic, Map<String,String> zookeeperKafkaMap){
        String topologyName = "kafkaSpout";
        return getKafkaSpout(id,topic,zookeeperKafkaMap,topologyName);
    }
}
