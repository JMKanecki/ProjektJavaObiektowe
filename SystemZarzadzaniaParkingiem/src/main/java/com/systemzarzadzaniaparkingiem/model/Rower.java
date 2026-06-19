package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Rower extends Pojazd {
    private boolean elektryczny;

    public Rower(boolean elektryczny) {
        super();
        this.elektryczny = elektryczny;
    }

    public Rower() { this(false); }
    public boolean CzyElektryczny() { return elektryczny; }
}
