package com.kevin.java.view.statistics.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.alibaba.fastjson.JSONObject;
import com.kevin.java.engine.core.utils.ConfigUtil;
import com.kevin.java.view.statistics.bolt.LogReaderSpout;
import com.kevin.java.view.statistics.bolt.LogStatBlot;
import com.kevin.java.view.statistics.bolt.LogWriterBlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewStatisticsTopology {

  private static final Logger logger = LoggerFactory.getLogger(ViewStatisticsTopology.class);

  private static final String PROP_FILE_NAME = "config.prop";

  private static JSONObject config = null;

  public static void main(String[] args){

    config = new JSONObject(ConfigUtil.getMapFromConfig(PROP_FILE_NAME));

    String topologyName = String.valueOf(config.get("topology.name"));

    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("log-reader", new LogReaderSpout(), 1);
    builder.setBolt("log-stat", new LogStatBlot(), 2)
        .fieldsGrouping("log-reader", new Fields("user"));

    builder.setBolt("log-writer", new LogWriterBlot(), 1)
        .localOrShuffleGrouping("log-stat");

    Config config = new Config();

    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology(topologyName, config, builder.createTopology());

    logger.info("cluster is running");
    //StormSubmitter.submitTopology("log-topology", config, builder.createTopology());

    try {
      Thread.sleep(20 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    cluster.killTopology(topologyName);
    cluster.shutdown();

  }
}
