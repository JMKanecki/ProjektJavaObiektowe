package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class MiejsceSamochodowe extends MiejsceParkingowe {
    private boolean ladowarka;

    public MiejsceSamochodowe(boolean ladowarka) { super(); this.ladowarka = ladowarka; }
    public MiejsceSamochodowe() { super(); this.ladowarka = false; }

    @Override public boolean CzyPosiadaLadowarke() { return this.ladowarka; }
}
