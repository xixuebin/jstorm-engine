package com.kevin.xi.vp.statistics.blot;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shade.storm.com.google.common.collect.Maps;
import shade.storm.org.apache.commons.lang.math.NumberUtils;

public class LogStatBlot extends BaseRichBolt {

  private static final Logger logger = LoggerFactory.getLogger(LogStatBlot.class);

  private HashMap<String, Long> srcPayMap;
  private transient OutputCollector collector;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;

    if (srcPayMap == null){
      srcPayMap = Maps.newHashMap();
    }
  }

  @Override
  public void execute(Tuple input) {
      String pay = input.getStringByField("pay");
      String srcId = input.getStringByField("srcid");

      if (srcPayMap.containsKey(srcId)) {
        srcPayMap.put(srcId, NumberUtils.toLong(pay) + srcPayMap.get(srcId));
      }else {
        srcPayMap.put(srcId, NumberUtils.toLong(pay));
      }

    logger.info("result --- {} -- {}", srcId, pay);

  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("user", "srcid", "pay"));
  }
}
