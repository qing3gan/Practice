package hadoop;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agony on 2018/6/29.
 */
public class ZookeeperClient {

    private ZooKeeper zooKeeper;

    private volatile List<String> servers;

    private Watcher watcher = event -> {
        if (Watcher.Event.EventType.NodeChildrenChanged.equals(event.getType())
                && Constants.SERVERS_NODE.equals(event.getPath())) {
            updateServers();
        }
    };

    private void connectZookeeper() throws Exception {
        zooKeeper = new ZooKeeper(Constants.SERVERS_URL, Constants.ZOOKEEPER_TIMEOUT, watcher);
        updateServers();
    }

    private void updateServers() {
        List<String> tempServers = new ArrayList<>();
        try {
            List<String> children = zooKeeper.getChildren(Constants.SERVERS_NODE, true);
            tempServers.addAll(children);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        servers = tempServers;
        System.out.println(servers);
    }

    public static void main(String[] args) throws Exception {
        ZookeeperClient zookeeperClient = new ZookeeperClient();
        zookeeperClient.connectZookeeper();
        Thread.sleep(Long.MAX_VALUE);
    }
}
