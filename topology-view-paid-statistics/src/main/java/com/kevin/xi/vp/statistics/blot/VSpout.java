package com.kevin.xi.vp.statistics.blot;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import java.util.stream.IntStream;

public class VSpout extends BaseRichSpout {

  private SpoutOutputCollector collector;
  private String[] users = {
      "user1","user2","user3","user4","user5"
  };

  private String[] srcIds = {
      "s1","s2","s3","s4","s5"
  };
  private int count = 5;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
  }

  @Override
  public void nextTuple() {

    IntStream.range(0, count).forEach(i -> {
      try {
        Thread.sleep(1000);
        collector.emit("visit", new Values(
            System.currentTimeMillis(),
            users[i],
            srcIds[i]
        ));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("visit", new Fields("time", "user", "srcid"));
  }
}
