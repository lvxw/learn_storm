package com.test.java.business.template;

import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import com.test.java.common.BaseProgram;
import com.test.java.common.MyMessageQueueSpout;

/**
 * @author lvxw
 * (IDE 本地测试时，参数为)：
    {
        \"topic\":\"test-flink\",
        \"run_pattern\":\"local\",
        \"main_class\":\"com.test.java.business.template.WordCountKafkaTopology\"
    }
 * @throws Exception
 */
public class WordCountKafkaTopology extends BaseProgram {
    private Object neededParam;

    @Override
    public void getConf(){
        neededParam = mainArgsMap.get("");
        if(runPattern.equals(runPatternArr[0])){
            conf.setDebug(true);
        }
    }

    @Override
    public  StormTopology getStormTopology(){
        builder.setSpout("spout.word", MyMessageQueueSpout.getKafkaSpout(className,topic,zkKafkaStormMap));
        builder.setBolt("bolt.split",new WordSplitBolt()).shuffleGrouping("spout.word");
        builder.setBolt("bolt.count",new WordCountBolt()).fieldsGrouping("bolt.split",new Fields("WORD"));
        builder.setBolt("bolt.print",new WordOutputBolt(fixedParamMap)).shuffleGrouping("bolt.count");
        return builder.createTopology();
    }
}
