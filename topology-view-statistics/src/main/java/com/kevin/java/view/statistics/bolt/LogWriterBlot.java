package com.kevin.java.view.statistics.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.com.google.common.collect.Maps;

public class LogWriterBlot extends BaseRichBolt {
  private static final Logger logger = LoggerFactory.getLogger(LogWriterBlot.class);

  private OutputCollector collector;

  private Map<String, Integer> pvMap = Maps.newHashMap();

  public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
    collector = outputCollector;
  }

  public void execute(Tuple tuple) {
    logger.info(String.format("result --- %s - %s", tuple.getStringByField("user"),
        tuple.getIntegerByField("pv")));
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {

  }
}
