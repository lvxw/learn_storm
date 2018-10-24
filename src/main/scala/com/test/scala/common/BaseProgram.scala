package com.test.scala.common

import java.util.Properties

import backtype.storm.generated.StormTopology
import backtype.storm.{Config, LocalCluster, StormSubmitter}
import com.test.scala.util.ParamUtils

class BaseProgram extends App {

  val kafkaProps:Properties = new Properties()
  val className = this.getClass.getSimpleName.replace("$", "")

  val runPatternMap = Map(1->"local",2->"test",3->"public")
  var mainArgsMap:Map[String,String] = _
  var fixedParamMap:Map[String,Any] = _
  var runPattern:String = _

  var topic:String = _
  var zkKafkaStormMap:Map[String,String] = _

  val conf:Config = new Config()

  def init(): Unit ={
    mainArgsMap = ParamUtils.jsonStrToMap(args.mkString("")).asInstanceOf[Map[String,String]]
    fixedParamMap = ParamUtils.getClassPathFileContent("fixed-params.conf")
    delayedInit(submitTopology())
  }

  def initParams():Unit ={
    runPattern = mainArgsMap.getOrElse("run_pattern","")
    topic = mainArgsMap.getOrElse("topic","")

    val testOrProductParamMap = if(runPattern==runPatternMap.get(1).get || runPattern==runPatternMap.get(2).get){
      fixedParamMap.get(runPatternMap.get(2).get).get.asInstanceOf[Map[String,Any]]
    }else{
      fixedParamMap.get(runPatternMap.get(3).get).get.asInstanceOf[Map[String,Any]]
    }
    zkKafkaStormMap = testOrProductParamMap.get("zk_kafka_storm").get.asInstanceOf[Map[String,String]]

    getConfig()
  }

  def getConfig():Unit ={
    if(runPattern == runPatternMap.get(1).get){
      conf.setDebug(false)
    }
  }

  def getStormTopology() : StormTopology ={
    return null
  }

  def submitTopology(): Unit ={
    if(runPattern == runPatternMap.get(1).get){
      val cluster = new LocalCluster()
      cluster.submitTopology(className, conf, getStormTopology)
    }else {
      StormSubmitter.submitTopology(className, conf, getStormTopology)
    }
  }

  init()
  initParams()
}
