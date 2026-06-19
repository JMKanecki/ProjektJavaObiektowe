package com.systemzarzadzaniaparkingiem.model;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public abstract class MiejsceParkingowe {
    protected static int iloscMiejsc = 0;
    protected int nrMiejsca;
    protected STATUS_MIEJSCA status;
    protected SesjaParkingowa aktywnaSesja;

    public MiejsceParkingowe() {
        this.iloscMiejsc++;
        this.nrMiejsca = this.iloscMiejsc;
        this.status = STATUS_MIEJSCA.DOSTEPNE;
        this.aktywnaSesja = null;
    }

    public static void resetLicznik() { iloscMiejsc = 0; }

    public void ZablokujMiejsce() { this.status = STATUS_MIEJSCA.ZABLOKOWANE; }
    public void ZarezerwujMiejsce() { this.status = STATUS_MIEJSCA.ZAREZERWOWANE; }
    public void OdblokujMiejsce() { this.status = STATUS_MIEJSCA.DOSTEPNE; }

    public void ZajmijMiejsce(SesjaParkingowa sesja) {
        this.aktywnaSesja = sesja;
        this.status = STATUS_MIEJSCA.ZAJETE;
    }

    public void ZwolnijMiejsce() {
        this.aktywnaSesja = null;
        this.OdblokujMiejsce();
    }

    public boolean CzyDostepne() { return this.status == STATUS_MIEJSCA.DOSTEPNE; }
    public boolean CzyCzynne() { return this.status != STATUS_MIEJSCA.ZABLOKOWANE; }
    public boolean CzyZarezerwowane() { return this.status == STATUS_MIEJSCA.ZAREZERWOWANE; }
    public boolean CzyZajete() { return this.status == STATUS_MIEJSCA.ZAJETE; }
    public boolean CzyPosiadaLadowarke() { return false; }
    public boolean CzyPosiadaBlokade() { return false; }
    public boolean PorownajId(int id) { return this.nrMiejsca == id; }

    public int getNrMiejsca() { return nrMiejsca; }
    public STATUS_MIEJSCA getStatus() { return status; }
    public SesjaParkingowa getAktywnaSesja() { return aktywnaSesja; }
}
