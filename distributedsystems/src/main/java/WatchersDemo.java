import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class WatchersDemo implements Watcher {

    private static final String ZOOKEEPER_ADDRESS="localhost:2181";
    private static final int SESSION_TIMEOUT=30000;
    private static final String ELECTION_NAMESPACE="/election";
    private ZooKeeper zookeeper;
    private String currentZnodeName;
    private static final String TARGET_ZNODE="/target_znode";


    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        WatchersDemo watchersDemo = new WatchersDemo();
        watchersDemo.connectToZeekper();
        watchersDemo.watchTargetznode();
        watchersDemo.run();
        watchersDemo.close();
    }

    public void volunteerForLeadership() throws InterruptedException, KeeperException {
        String znodePrefix= ELECTION_NAMESPACE+"/c_";
        String znodeFullPath=
                zookeeper.create(znodePrefix,new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("znode name"+znodeFullPath);
        currentZnodeName=znodeFullPath.replace(ELECTION_NAMESPACE+"/","");

    }

    public void electLeader() throws InterruptedException, KeeperException {
        List<String> children= zookeeper.getChildren(ELECTION_NAMESPACE,false);
        Collections.sort(children);
        String smallestChild= children.get(0);
        if(smallestChild.equals(currentZnodeName)){
            System.out.println("Iam the leader");
            return;
        }
        System.out.println("Iam not the leader, "+smallestChild+" is the leader");
    }

    public void run() throws IOException {
        synchronized (zookeeper){
            try {
                zookeeper.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException{
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connectToZeekper() throws IOException {

        this.zookeeper=new ZooKeeper(ZOOKEEPER_ADDRESS,SESSION_TIMEOUT,this);

    }

    public void watchTargetznode() throws InterruptedException, KeeperException {

        Stat stat= zookeeper.exists(TARGET_ZNODE,this);
        if(stat==null){
            return ;
        }

        byte[] data= zookeeper.getData(TARGET_ZNODE,this,stat);
        List<String> children= zookeeper.getChildren(TARGET_ZNODE,this);
        System.out.println("data: "+new String(data)+", children: "+children);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch(watchedEvent.getType()){
            case None:
                if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
                    System.out.println("Successfully connected to zookeeper");
                }else{
                    synchronized (zookeeper){
                        System.out.println("Disconnected from Zookeeper event");
                        zookeeper.notifyAll();
                    }
                }
            case NodeDeleted:
                System.out.println(TARGET_ZNODE + " was deleted");
                break;
            case NodeCreated:
                System.out.println(TARGET_ZNODE + " was created");
                break;
            case NodeDataChanged:
                System.out.println(TARGET_ZNODE + " data changed");
                break;
            case NodeChildrenChanged:
                System.out.println(TARGET_ZNODE + " children changed");
                break;
        }

        try {
            watchTargetznode();
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        }

        }
    }

