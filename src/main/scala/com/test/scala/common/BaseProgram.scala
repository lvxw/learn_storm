package com.test.scala.common

import java.util.Properties

import backtype.storm.generated.StormTopology
import backtype.storm.topology.TopologyBuilder
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
  lazy val builder = new TopologyBuilder()

  def init(): Unit ={
    mainArgsMap = ParamUtils.jsonStrToMap(args.mkString("")).asInstanceOf[Map[String,String]]
    delayedInit(submitTopology())
  }

  def initParams():Unit ={
    runPattern = mainArgsMap.getOrElse("run_pattern","")
    topic = mainArgsMap.getOrElse("topic","")

    fixedParamMap = if(runPattern==runPatternMap.get(1).get || runPattern==runPatternMap.get(2).get){
      ParamUtils.getClassPathFileContent("fixed-params.conf").get(runPatternMap.get(2).get).get.asInstanceOf[Map[String,Any]]
    }else{
      ParamUtils.getClassPathFileContent("fixed-params.conf").get(runPatternMap.get(3).get).get.asInstanceOf[Map[String,Any]]
    }
    zkKafkaStormMap = fixedParamMap.get("zk_kafka_storm").get.asInstanceOf[Map[String,String]]

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
