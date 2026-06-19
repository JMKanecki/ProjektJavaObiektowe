package com.systemzarzadzaniaparkingiem.model;

/**
 * @author Jakub Kanecki, Dawid Lazarski
 */
public interface IRaporty { // do zmian w UML
    // miejsca
    public int IloscMiejsc();
    public int IloscWolnychMiejsc();
    public int IloscWolnychMiejscSamoch();
    public int IloscWolnychMiejscLadowarka();
    public int IloscWolnychMiejscRower();
    public int IloscWolnychMiejscBlokada();
    public int IloscMiejscZarezerwowanych();
    public int IloscMiejscZablokowanych();

    // pojazdy
    // IloscZaparkowanychPojazdow do wyliczenia przez IloscMiejsc() - IloscWolnychMiejsc()
    public int IloscZaparkowanychSamoch();
    public int IloscZaparkowanychRower();
    // raport tekstowy
    
    
}
