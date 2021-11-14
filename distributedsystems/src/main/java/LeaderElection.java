import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LeaderElection implements Watcher {

    private static final String ZOOKEEPER_ADDRESS="localhost:2181";
    private static final int SESSION_TIMEOUT=30000;
    private static final String ELECTION_NAMESPACE="/election";
    private ZooKeeper zookeeper;
    private String currentZnodeName;
    public static void main(String[] args){
        LeaderElection leaderelection= new LeaderElection();
        try {
            leaderelection.connectToZeekper();
            leaderelection.volunteerForLeadership();
            leaderelection.reelectLeader();
            leaderelection.run();
            leaderelection.close();
            System.out.println("Disconnected from zookeper, exiting application");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void volunteerForLeadership() throws InterruptedException, KeeperException {
             String znodePrefix= ELECTION_NAMESPACE+"/c_";
             String znodeFullPath=
                     zookeeper.create(znodePrefix,new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("znode name"+znodeFullPath);
            currentZnodeName=znodeFullPath.replace(ELECTION_NAMESPACE+"/","");

    }

    public void reelectLeader() throws InterruptedException, KeeperException {
        String predecessorName="";
        Stat predecessorStat=null;
        while (predecessorStat == null) {
            List<String> children = zookeeper.getChildren(ELECTION_NAMESPACE, false);
            Collections.sort(children);
            String smallestChild = children.get(0);

            if (smallestChild.equals(currentZnodeName)) {
                System.out.println("Iam the leader");
                return;
            } else {
                System.out.println("Iam not the leader");
                int predecessorIdx = Collections.binarySearch(children, currentZnodeName) - 1;
                predecessorName = children.get(predecessorIdx);
                predecessorStat = zookeeper.exists(ELECTION_NAMESPACE + "/" + predecessorName, this);
            }
        }
        System.out.println("Watching znode " + predecessorName);
        System.out.println();
    }

    public void run() throws IOException{
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
                try {
                    reelectLeader();
                } catch (InterruptedException e) {
                } catch (KeeperException e) {
                }
        }
    }
}
