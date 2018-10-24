package com.test.java.common;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import com.test.java.util.ParamUtils;

import java.util.Map;

/**
 * @author lvxw
 */
public abstract class BaseProgram {
    public String className;
    public TopologyBuilder builder;
    public static Config conf = new Config();
    public static String[] runPatternArr = {"local","test","public"};
    public static String runPattern;
    public String topic;
    public Map<String,String> zkKafkaStormMap;
    public static Map<String,Object> mainArgsMap;
    public static Map<String,Object> fixedParamMap;


    public BaseProgram() {
        this.builder = new TopologyBuilder();
        this.className = this.getClass().getSimpleName();
    }

    public static void main(String[] args) throws Exception {
        init(args);
        BaseProgram program = (BaseProgram) Class.forName((String) mainArgsMap.get("main_class")).newInstance();
        program.initParams(program);
        program.submitTopology(conf);
    }

    public static void init(String[] args){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<args.length;i++){
            sb.append(args[i]);
        }
        mainArgsMap = ParamUtils.jsonStrToMap(sb.toString());
        fixedParamMap = ParamUtils.getClassPathFileContent("fixed-params.conf");
    }

    public void initParams(BaseProgram program){
        runPattern = (String) mainArgsMap.get("run_pattern");
        topic = (String) mainArgsMap.get("topic");

        Map<String,Object> testOrProductParamMap;
        if(runPattern.equals(runPatternArr[0]) || runPattern.equals(runPatternArr[1])){
            testOrProductParamMap = (Map<String, Object>) fixedParamMap.get(runPatternArr[1]);
        }else{
            testOrProductParamMap = (Map<String, Object>) fixedParamMap.get(runPatternArr[2]);
        }
        zkKafkaStormMap = (Map<String, String>) testOrProductParamMap.get("zk_kafka_storm");

        program.getConf();
    }

    public void getConf(){
        if(runPattern.equals(runPatternArr[0])){
            conf.setDebug(true);
        }
    }

    public void submitTopology(Config conf){
        try {
            if(runPattern.equals(runPatternArr[0])){
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology(className, conf, getStormTopology());
            }else {
                StormSubmitter.submitTopology(className, conf, getStormTopology());
            }
        } catch (AlreadyAliveException e) {
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            e.printStackTrace();
        }
    }

    public abstract StormTopology getStormTopology();
}
