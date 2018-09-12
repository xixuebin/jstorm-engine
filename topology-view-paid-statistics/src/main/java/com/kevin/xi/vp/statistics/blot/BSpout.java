package com.kevin.xi.vp.statistics.blot;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import java.util.stream.IntStream;

public class BSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;

  private String[] users = {
      "user1","user2","user3","user4","user5"
  };

  private String[] pays = {
      "100","200","300","400","200"
  };

  private int count = 5;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector =collector;
  }

  @Override
  public void nextTuple() {
    IntStream.range(0, count).forEach(i -> {
      try {
        Thread.sleep(1500);
        collector.emit("business", new Values(
            System.currentTimeMillis(),
            users[i],
            pays[i]
        ));
      }catch (InterruptedException e){
        e.printStackTrace();
      }
    });

  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("business", new Fields("time", "user", "pay"));
  }
}
