/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author PeaceFull
 */
public class Server extends Thread {

    int time = 1;

    Queue<String> q = new LinkedList<>();
    String name = "";
    int flag = 1;
    int serverCount = 0;

    public Server() {

    }

    public void addQueue(String element, int time) {

        try {

            this.time = time;
            q.add(element);
        
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public int queueSize() {

        int sizeOfQueue = q.size();
        return sizeOfQueue;
    }

    public void run() {

         // while(true){ 
        String data = q.poll();

        while (data != null) {

            try {
                Thread.sleep(this.time * 1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            name = currentThread().getName();
            System.out.println("The request processed in " + name + " is :" + data);
            data = q.poll();
        }

    }

}
    