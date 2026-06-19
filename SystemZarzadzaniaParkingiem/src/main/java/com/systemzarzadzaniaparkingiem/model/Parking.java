package com.systemzarzadzaniaparkingiem.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Parking {
    private String nazwa;
    public ZarejestrowaneTablice zarejestrowaneTablice;
    public Raporty raporty;
    public ArrayList<MiejsceParkingowe> miejscaParkingowe;
    public Logi logi;

    public Parking(String nazwa) {
        this.nazwa = nazwa;
        this.zarejestrowaneTablice = new ZarejestrowaneTablice();
        this.raporty = new Raporty(this);
        this.miejscaParkingowe = new ArrayList<>();
        this.logi = new Logi();
    }

    public Parking() {
        this("Parking Miejski");
        DodajMiejsca();
    }

    private void DodajMiejsca() {
        for (int i = 0; i < 4; i++)
            this.miejscaParkingowe.add(new MiejsceSamochodowe(true));
        for (int i = 0; i < 10; i++)
            this.miejscaParkingowe.add(new MiejsceSamochodowe());
        for (int i = 0; i < 3; i++)
            this.miejscaParkingowe.add(new MiejsceRowerowe(true));
        for (int i = 0; i < 8; i++)
            this.miejscaParkingowe.add(new MiejsceRowerowe());
    }

    public String GetNazwa() { return nazwa; }

    // ── operacje parkingowe ────────────────────────────────────────────────

    public void ZarejestrujWjazd(Pojazd pojazd, MiejsceParkingowe miejsce, Instant zakonczeniePostoju) {
        miejsce.ZajmijMiejsce(new SesjaParkingowa(pojazd, Instant.now(), zakonczeniePostoju));
        if (pojazd instanceof Samochod) {
            logi.WjazdSamochodu(((Samochod) pojazd).GetTablica(), miejsce.getNrMiejsca());
        } else if (pojazd instanceof Rower) {
            logi.WjazdRoweru(miejsce.getNrMiejsca());
        }
    }

    public void ZarejestrujWyjazd(MiejsceParkingowe miejsce) {
        if (miejsce != null && miejsce.CzyZajete()) {
            Pojazd pojazd = miejsce.getAktywnaSesja() != null ? miejsce.getAktywnaSesja().pojazd : null;
            miejsce.getAktywnaSesja().Zamknij();
            miejsce.ZwolnijMiejsce();
            if (pojazd instanceof Samochod) {
                logi.WyjazdSamochodu(((Samochod) pojazd).GetTablica(), miejsce.getNrMiejsca());
            } else if (pojazd instanceof Rower) {
                logi.WyjazdRoweru(miejsce.getNrMiejsca());
            }
        }
    }

    public MiejsceParkingowe ZnajdzMiejsce(Samochod pojazd) {
        if (pojazd.CzyElektryczny()) {
            for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
                if (miejsce.CzyDostepne() && miejsce instanceof MiejsceSamochodowe && miejsce.CzyPosiadaLadowarke())
                    return miejsce;
            }
        } else {
            for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
                if (miejsce.CzyDostepne() && miejsce instanceof MiejsceSamochodowe && !miejsce.CzyPosiadaLadowarke())
                    return miejsce;
            }
        }
        for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
            if (miejsce.CzyDostepne() && miejsce instanceof MiejsceSamochodowe)
                return miejsce;
        }
        return null;
    }

    public MiejsceParkingowe ZnajdzMiejsce(Rower pojazd, boolean wlasnaBlokada) {
        if (!wlasnaBlokada) {
            for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
                if (miejsce.CzyDostepne() && miejsce instanceof MiejsceRowerowe && miejsce.CzyPosiadaBlokade())
                    return miejsce;
            }
        } else {
            for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
                if (miejsce.CzyDostepne() && miejsce instanceof MiejsceRowerowe && !miejsce.CzyPosiadaBlokade())
                    return miejsce;
            }
            for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
                if (miejsce.CzyDostepne() && miejsce instanceof MiejsceRowerowe)
                    return miejsce;
            }
        }
        return null;
    }

    /** Zwraca -1 jeśli zablokowana, 1 jeśli zarejestrowana, 0 jeśli nieznana */
    public int sprawdzTabliceWjazd(String tablica) {
        if (this.zarejestrowaneTablice.CzyZablokowana(tablica)) return -1;
        if (this.zarejestrowaneTablice.CzyZarejestrowana(tablica)) return 1;
        return 0;
    }

    public MiejsceParkingowe WyszukajMiejsce(int id) {
        for (MiejsceParkingowe miejsce : this.miejscaParkingowe) {
            if (miejsce.PorownajId(id)) return miejsce;
        }
        return null;
    }

    public boolean CzyIstniejeSesjaNaMiejscuNr(int id) {
        MiejsceParkingowe miejsce = WyszukajMiejsce(id);
        return miejsce != null && miejsce.CzyZajete();
    }

    public List<MiejsceParkingowe> GetMiejscaSamochodowe() {
        List<MiejsceParkingowe> lista = new ArrayList<>();
        for (MiejsceParkingowe m : miejscaParkingowe)
            if (m instanceof MiejsceSamochodowe) lista.add(m);
        return lista;
    }

    public List<MiejsceParkingowe> GetMiejscaRowerowe() {
        List<MiejsceParkingowe> lista = new ArrayList<>();
        for (MiejsceParkingowe m : miejscaParkingowe)
            if (m instanceof MiejsceRowerowe) lista.add(m);
        return lista;
    }

    public MiejsceParkingowe ZnajdzMiejscePojazdaZTabl(String tablica) {
        for (MiejsceParkingowe m : miejscaParkingowe) {
            if (m.CzyZajete() && m.getAktywnaSesja() != null) {
                Pojazd p = m.getAktywnaSesja().pojazd;
                if (p instanceof Samochod && ((Samochod) p).GetTablica().equalsIgnoreCase(tablica))
                    return m;
            }
        }
        return null;
    }

    public void DodajKlienta(String tablica, Wlasciciel wlasciciel) {
        zarejestrowaneTablice.DodajTablice(tablica, wlasciciel);
        logi.DodanoKlienta(tablica, wlasciciel.Dane());
    }

    public void UsunKlienta(String tablica) {
        zarejestrowaneTablice.UsunTablice(tablica);
        logi.UsunietKlient(tablica);
    }

    public Wlasciciel ZnajdzWlasciciela(String tablica) {
        return zarejestrowaneTablice.Wlasciciel(tablica);
    }

}
