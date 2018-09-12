package com.kevin.xi.vp.statistics.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.kevin.xi.vp.statistics.blot.BSpout;
import com.kevin.xi.vp.statistics.blot.LogMergeBlot;
import com.kevin.xi.vp.statistics.blot.LogStatBlot;
import com.kevin.xi.vp.statistics.blot.VSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VPStatisticsTopology {

  private static final Logger logger = LoggerFactory.getLogger(VPStatisticsTopology.class);

  public static void main(String[] args) {

    String topologyName = "vp-statistics-topology";
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("log-vspout", new VSpout(), 1);
    builder.setSpout("log-bspout", new BSpout(), 1);

    builder.setBolt("log-merge", new LogMergeBlot(), 2).
        fieldsGrouping("log-vspout", "visit", new Fields("user"))
        .fieldsGrouping("log-bspout", "business", new Fields("user"));

    builder.setBolt("log-stat", new LogStatBlot(), 2)
        .fieldsGrouping("log-merge", new Fields("srcid"));

    Config config = new Config();
    config.setNumAckers(7);


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
