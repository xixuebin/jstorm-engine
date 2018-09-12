package com.kevin.xi.vp.statistics.blot;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import java.util.HashMap;
import java.util.Map;
import shade.storm.com.google.common.collect.Maps;
import shade.storm.org.apache.commons.lang.StringUtils;

public class LogMergeBlot extends BaseRichBolt {

  private HashMap<String, String> srcMap;
  private transient OutputCollector collector;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    this.collector = collector;

    if (srcMap == null){
      srcMap = Maps.newHashMap();
    }
  }

  @Override
  public void execute(Tuple input) {
    String streamID = input.getSourceStreamId();

    if (StringUtils.equals("visit", streamID)){
      String user = input.getStringByField("user");
      String srcId = input.getStringByField("srcid");
      srcMap.put(user, srcId);
    }else if (StringUtils.equals("business", streamID)){
      String user = input.getStringByField("user");
      String pay = input.getStringByField("pay");
      String srcId = srcMap.get("user");
      if (srcId != null){
        collector.emit(new Values(user, pay, srcId));
        srcMap.remove(user);
      }else {
        //TODO
      }
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("user", "srcid", "pay"));
  }
}
