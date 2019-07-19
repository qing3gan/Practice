package hadoop;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Agony on 2018/6/29.
 */
public class ZookeeperThreadLock {

    private static final String LOCK_NODE = "/lock";

    private ZooKeeper zooKeeper;

    private CountDownLatch latch = new CountDownLatch(1);

    private String thisPath;

    private String waitPath;

    private Watcher watcher = event -> {
        if (Watcher.Event.KeeperState.SyncConnected.equals(event.getState())) {
            latch.countDown();
        }
        if (Watcher.Event.EventType.NodeDeleted.equals(event.getType()) && event.getPath().equals(waitPath)) {
            try {
                getCommonResource();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void connectZookeeper() throws Exception {
        zooKeeper = new ZooKeeper(Constants.SERVERS_URL, Constants.ZOOKEEPER_TIMEOUT, watcher);
        latch.await();

        thisPath = zooKeeper.create(Constants.SERVERS_NODE + LOCK_NODE, LOCK_NODE.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        Thread.sleep(2000);

        List<String> children = zooKeeper.getChildren(Constants.SERVERS_NODE, false);
        if (children.size() == 1) {
            getCommonResource();
        } else {
            String thisNode = thisPath.substring((Constants.SERVERS_NODE + "/").length());
            Collections.sort(children);
            int index = children.indexOf(thisNode);
            if (index == -1) {

            } else if (index == 0) {
                getCommonResource();
            } else {
                waitPath = Constants.SERVERS_NODE + "/" + children.get(index - 1);
                zooKeeper.getData(waitPath, true, new Stat());
            }
        }
    }

    private void getCommonResource() throws Exception {
        try {
            System.out.println(Thread.currentThread() + "get lock" + thisPath);
            Thread.sleep(5000);
        } finally {
            System.out.println(Thread.currentThread() + "release lock" + thisPath);
            zooKeeper.delete(thisPath, -1);
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    ZookeeperThreadLock zookeeperClient = new ZookeeperThreadLock();
                    zookeeperClient.connectZookeeper();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(Long.MAX_VALUE);
    }
}
