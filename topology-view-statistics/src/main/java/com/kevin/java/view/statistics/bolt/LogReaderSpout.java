package com.kevin.java.view.statistics.bolt;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import java.util.Map;
import java.util.Random;

public class LogReaderSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;

  private Random random = new Random();
  private int count = 100;
  private String[] users = {"user1","user2","user3","user4", "user5"};
  private String[] views = {"view1","view2","view3","view4", "view5"};


  public void open(Map map, TopologyContext topologyContext,
      SpoutOutputCollector spoutOutputCollector) {
    collector = spoutOutputCollector;

  }

  public void nextTuple() {
    try {
      Thread.sleep(1000);

      while (count-- > 0){
        collector.emit(new Values(System.currentTimeMillis(), users[random.nextInt(5)],
            views[random.nextInt(5)]));
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("time","user","url"));
  }
}
