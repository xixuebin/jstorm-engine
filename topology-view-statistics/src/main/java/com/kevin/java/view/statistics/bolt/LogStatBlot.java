package com.kevin.java.view.statistics.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.Map;
import shade.storm.com.google.common.collect.Maps;

public class LogStatBlot extends BaseRichBolt {

  private OutputCollector collector;

  private Map<String, Integer> pvMap = Maps.newHashMap();

  public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
    collector = outputCollector;
  }

  public void execute(Tuple tuple) {
    String user = tuple.getStringByField("user");

    if (pvMap.containsKey(user)){
      pvMap.put(user, pvMap.get(user) + 1);
    }else {
      pvMap.put(user, 1);
    }

    collector.emit(new Values(user, pvMap.get(user)));
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("user", "pv"));
  }
}
