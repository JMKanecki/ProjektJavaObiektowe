package com.systemzarzadzaniaparkingiem.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class ZarejestrowaneTablice {
    private HashSet<String> tablice;
    private HashMap<String, Wlasciciel> wlasciciele; 
    private HashSet<String> zablokowaneTablice;

    public ZarejestrowaneTablice() {
        this.wlasciciele = new HashMap<>();
        this.zablokowaneTablice = new HashSet<>();
        this.tablice = new HashSet<>();
    }

    public void DodajTablice(String tablica, Wlasciciel wlasciciel) {
        this.wlasciciele.put(tablica.toUpperCase(), wlasciciel);
        this.tablice.add(tablica.toUpperCase());
    }

    public void UsunTablice(String tablica) {
        this.wlasciciele.remove(tablica.toUpperCase());
        this.tablice.remove(tablica.toUpperCase());
    }

    public void ZablokujTablice(String tablica) {
        this.zablokowaneTablice.add(tablica.toUpperCase());
        this.tablice.remove(tablica.toUpperCase());
    }

    public void OdblokujTablice(String tablica) {
        this.zablokowaneTablice.remove(tablica.toUpperCase());
        this.tablice.add(tablica.toUpperCase());
    }

    public boolean CzyZarejestrowana(String tablica) {
        return this.tablice.contains(tablica);
    }

    public boolean CzyZablokowana(String tablica) {
        return this.zablokowaneTablice.contains(tablica.toUpperCase());
    }

    public Wlasciciel Wlasciciel(String tablica) {
        return this.wlasciciele.get(tablica.toUpperCase());
    }

    public Map<String, Wlasciciel> GetWszystkieTablice() { return wlasciciele; }
    public Set<String> GetZablokowaneTablice() { return zablokowaneTablice; }
}
