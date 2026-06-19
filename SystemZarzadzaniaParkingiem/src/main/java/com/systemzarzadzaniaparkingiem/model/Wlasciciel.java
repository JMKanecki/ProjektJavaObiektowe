package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Wlasciciel {
    private String imieNazwisko;
    private String eMail;
    private String nrTelefonu;

    public Wlasciciel(String imieNazwisko, String eMail, String nrTelefonu) {
        this.imieNazwisko = imieNazwisko;
        this.eMail = eMail;
        this.nrTelefonu = nrTelefonu;
    }

    public Wlasciciel() { this("Jan Kowalski", "example@example.com", "123456789"); }

    // Odczyt danych – konieczne do wyświetlania w GUI i wyszukiwania
    public String GetImieNazwisko() { return imieNazwisko; }
    public String GeteMail() { return eMail; }
    public String GetNrTelefonu() { return nrTelefonu; }

    public void Aktualizuj(String imieNazwisko, String eMail, String nrTelefonu) {
        this.imieNazwisko = imieNazwisko;
        this.eMail = eMail;
        this.nrTelefonu = nrTelefonu;
    }

    public String Dane() {
        return imieNazwisko + " | " + eMail + " | " + nrTelefonu;
    }
}
