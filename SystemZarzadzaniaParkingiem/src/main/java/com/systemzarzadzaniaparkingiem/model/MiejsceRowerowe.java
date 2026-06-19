package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class MiejsceRowerowe extends MiejsceParkingowe {
    private boolean blokada;

    public MiejsceRowerowe(boolean blokada) { super(); this.blokada = blokada; }
    public MiejsceRowerowe() { super(); this.blokada = false; }

    @Override public boolean CzyPosiadaBlokade() { return this.blokada; }
}
