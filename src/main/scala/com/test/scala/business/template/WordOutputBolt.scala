package com.test.scala.business.template

import backtype.storm.topology.{BasicOutputCollector, OutputFieldsDeclarer}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.Tuple

class WordOutputBolt extends BaseBasicBolt{
  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    println(input.getStringByField("WORD"))
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {

  }
}
