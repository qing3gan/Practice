package net.server;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import util.Constants;

/**
 * Created by Agony on 2018/6/29.
 */
public class ZookeeperServer {

    private static final String SERVER_NODE = "/net/server";

    private static final String SERVER_NAME = "server03";

    private ZooKeeper zooKeeper;

    private void connectZookeeper() throws Exception {
        zooKeeper = new ZooKeeper(Constants.SERVERS_URL, Constants.ZOOKEEPER_TIMEOUT, null);
        zooKeeper.create(Constants.SERVERS_NODE + SERVER_NODE, SERVER_NAME.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public static void main(String[] args) throws Exception {
        ZookeeperServer zookeeperServer = new ZookeeperServer();
        zookeeperServer.connectZookeeper();
        Thread.sleep(Long.MAX_VALUE);
    }
}
