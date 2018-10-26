package com.test.java.business.template;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lvxw
 */
public class WordCountBolt extends BaseBasicBolt {
    Map<String,Long> wordCountMap;
    Long initTime;

    public WordCountBolt() {
        wordCountMap = new HashMap<>();
        initTime = System.currentTimeMillis()/1000/5*5;
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = input.getStringByField("WORD");
        if(wordCountMap.get(word) == null){
            wordCountMap.put(word,1L);
        }else{
            wordCountMap.put(word,wordCountMap.get(word)+1);
        }

        Long currentTime = System.currentTimeMillis()/1000/5*5;
        if(currentTime > initTime){
            for(Map.Entry<String,Long> entry:wordCountMap.entrySet()){
                String value = entry.getKey()+"->"+entry.getValue();
                collector.emit(new Values(currentTime,value));
            }

            initTime = currentTime;
            wordCountMap.clear();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("TIME","VALUE"));
    }
}
