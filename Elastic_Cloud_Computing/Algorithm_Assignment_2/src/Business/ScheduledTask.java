/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimerTask;

/**
 *
 * @author PeaceFull
 */
public class ScheduledTask extends TimerTask {

    int count = 0;
    int serverCount = 1;
    int flag = 1;
    int size = 0;

    String data;
    Elasticity elasticity;

    ArrayList<Server> serverPool = new ArrayList<Server>();

    Queue<String> q = new LinkedList<>();

    static int requestNumber = 0;

    public ScheduledTask(Elasticity e) {
        this.elasticity = e;
    }

    public void run() {

        requestNumber++;
        System.out.println("Entry queue is :" + String.valueOf("Request" + requestNumber));
        q.add(String.valueOf("Request" + requestNumber));
        this.dispatchServer(q);

    }

    public void dispatchServer(Queue<String> queue) {
        if (flag == 1) {
            Server serveri = new Server();

            serverPool.add(serveri);
            serveri.start();
            flag = 0;
        }
        for (Server server : serverPool) {

            int size = server.queueSize();
            if (size < elasticity.getCapacity()) {
                try {
                    String data = queue.poll();

                    server.addQueue(data, elasticity.getTime());

                } catch (Exception e) {
                    System.out.println(e);
                }
                return;
            }
        }

        serverCount++;
        Server serveri = new Server();

        String data = queue.poll();
        serveri.addQueue(data, elasticity.getTime());
        serverPool.add(serveri);
        serveri.start();

    }

}
