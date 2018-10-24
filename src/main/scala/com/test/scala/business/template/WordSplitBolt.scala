package com.test.scala.business.template

import backtype.storm.topology.{BasicOutputCollector, OutputFieldsDeclarer}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.{Fields, Tuple, Values}

class WordSplitBolt extends BaseBasicBolt{
  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    val lineStr = input.getString(0)
    val wordArr = lineStr.split(" ")

    for(word <- wordArr){
      collector.emit(new Values(word))
    }
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("WORD"))
  }
}
