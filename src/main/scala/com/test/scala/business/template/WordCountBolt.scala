package com.test.scala.business.template


import backtype.storm.topology.{BasicOutputCollector, OutputFieldsDeclarer}
import backtype.storm.topology.base.BaseBasicBolt
import backtype.storm.tuple.{Fields, Tuple, Values}

import scala.collection.mutable

class WordCountBolt extends BaseBasicBolt{
  val wordCountMap = new mutable.HashMap[String, Long]
  var initTime = System.currentTimeMillis / 1000 / 5 * 5

  override def execute(input: Tuple, collector: BasicOutputCollector): Unit = {
    val word = input.getStringByField("WORD")
    wordCountMap += (word -> (wordCountMap.getOrElse(word,0L)+1L))

    val currentTime = System.currentTimeMillis / 1000 / 5 * 5
    if (currentTime > initTime) {
      for ((word,count) <- wordCountMap) {
        collector.emit(new Values(currentTime.toString, s"${word}->${count}"))
      }
      initTime = currentTime
      wordCountMap.clear()
    }
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("TIME", "VALUE"))
  }
}
