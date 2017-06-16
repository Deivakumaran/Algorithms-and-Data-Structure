/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author PeaceFull
 */
public class Elasticity {

    int rate;
    int capacity;
    int time;

    public Elasticity(int rate, int capacity, int time) {
        this.rate = rate;
        this.capacity = capacity;
        this.time = time;
    }

    public Elasticity() {
     
    }

    
    
    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
