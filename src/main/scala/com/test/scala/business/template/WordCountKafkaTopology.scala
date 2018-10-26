package com.test.scala.business.template

import backtype.storm.generated.StormTopology
import backtype.storm.tuple.Fields
import com.test.scala.common.{BaseProgram, MyMessageQueueSpout}

/**
  *
  * @param args (IDE 本地测试时，参数为：
        {
          \"topic\":\"test-flink\",
          \"run_pattern\":\"local\"
        }
  */
object WordCountKafkaTopology extends BaseProgram {

  conf.setDebug(true)

  override def getStormTopology():StormTopology = {
    builder.setSpout("spout.word", MyMessageQueueSpout.getKafkaSpout(className,topic,zkKafkaStormMap))
    builder.setBolt("bolt.split", new WordSplitBolt).shuffleGrouping("spout.word")
    builder.setBolt("bolt.count", new WordCountBolt).fieldsGrouping("bolt.split", new Fields("WORD"))
    builder.setBolt("bolt.print", new WordOutputBolt(fixedParamMap)).shuffleGrouping("bolt.count")
    builder.createTopology()
  }
}
