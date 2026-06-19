package com.systemzarzadzaniaparkingiem.model;
import java.util.UUID;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public abstract class Pojazd {
    protected UUID uuid;
    public Pojazd() { uuid = UUID.randomUUID(); }
}
