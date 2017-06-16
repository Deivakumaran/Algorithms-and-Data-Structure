/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

/**
 *
 * @author PeaceFull
 */
public class SchedulerMain {
   
    Timer timer = new Timer(); 
    int count = 0;// Instantiate Timer Object
    Elasticity elasticity ;
  
    public SchedulerMain(Elasticity e) {
    this.elasticity =e;
    }

    public void scheduleTask(String status) {

        if (status.equals("true")) {
            ScheduledTask st = new ScheduledTask(elasticity); // Instantiate SheduledTask class
            timer.schedule(st, 0,elasticity.getRate() * 1000); // Create Repetitively task for every 1 secs
        } else {
            timer.cancel();  //Terminates this timer,discarding any currently scheduled tasks.
            timer.purge();
            System.exit(0) ; 

        }
    }

}
