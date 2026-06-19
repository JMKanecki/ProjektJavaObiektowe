package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Raporty implements IRaporty {
    Parking parking;

    Raporty(Parking parking) {
        this.parking = parking;
    }
    @Override
    public int IloscMiejsc() {
        return parking.miejscaParkingowe.size();
    }
    @Override
    public int IloscWolnychMiejsc() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyDostepne())
                count++;

        return count;
    }
    @Override
    public int IloscWolnychMiejscSamoch() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyDostepne() && miejsce instanceof MiejsceSamochodowe)
                count++;

        return count;
    }
    @Override
    public int IloscWolnychMiejscLadowarka() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyDostepne() && miejsce instanceof MiejsceSamochodowe)
                if (miejsce.CzyPosiadaLadowarke())
                    count++;

        return count;
    }
    @Override
    public int IloscWolnychMiejscRower() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyDostepne() && miejsce instanceof MiejsceRowerowe)
                count++;

        return count;
    }
    @Override
    public int IloscWolnychMiejscBlokada() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyDostepne() && miejsce instanceof MiejsceRowerowe)
                if (miejsce.CzyPosiadaBlokade())
                    count++;

        return count;
    }
    @Override
    public int IloscMiejscZarezerwowanych() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce.CzyZarezerwowane())
                count++;

        return count;
    }
    @Override
    public int IloscMiejscZablokowanych() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (!miejsce.CzyCzynne())
                count++;

        return count;
    }
    @Override
    public int IloscZaparkowanychSamoch() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce instanceof MiejsceSamochodowe && miejsce.CzyZajete())
                count++;

        return count;
    }
    @Override
    public int IloscZaparkowanychRower() {
        int count = 0;
        for (MiejsceParkingowe miejsce : parking.miejscaParkingowe)
            if (miejsce instanceof MiejsceRowerowe && miejsce.CzyZajete())
                count++;

        return count;
    }

    public String GenerujRaportTekstowy() {

        String szablon = """              
        Miejsca:
          - łącznie: %d
          - dostępne:
            - łącznie: %d
            - samochodowe: %d
              - w tym z ładowarką: %d
            - rowerowe: %d
              - w tym z blokadą: %d
            - zarezerwowane: %d
            - zablokowane: %d
        
        Pojazdy:<br>
          - zaparkowane łącznie: %d
            - samochody: %d
            - rowery: %d
       
        """;

        return String.format(szablon,
                IloscMiejsc(),
                IloscWolnychMiejsc(),
                IloscWolnychMiejscSamoch(),
                IloscWolnychMiejscLadowarka(),
                IloscWolnychMiejscRower(),
                IloscWolnychMiejscBlokada(),
                IloscMiejscZarezerwowanych(),
                IloscMiejscZablokowanych(),
                IloscMiejsc() - IloscWolnychMiejsc(),
                IloscZaparkowanychSamoch(),
                IloscZaparkowanychRower()
        );
    }
}