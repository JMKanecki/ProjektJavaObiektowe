package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Samochod extends Pojazd {
    private String tablica;
    private boolean elektryczny;

    public Samochod(String tablica, boolean elektryczny) {
        super();
        this.tablica = tablica;
        this.elektryczny = elektryczny;
    }

    public Samochod(String tablica) { this(tablica, false); }

    public boolean PorownajTablice(String tablica2) { return this.tablica.equalsIgnoreCase(tablica2); }
    public boolean CzyElektryczny() { return elektryczny; }
    public String GetTablica() { return tablica; }
}
