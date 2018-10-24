package com.test.java.business.template;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * @author lvxw
 */
public class WordOutputBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        System.out.println(input.getStringByField("WORD"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
