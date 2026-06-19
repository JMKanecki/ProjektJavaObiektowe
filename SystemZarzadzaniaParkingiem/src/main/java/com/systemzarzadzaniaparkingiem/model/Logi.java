package com.systemzarzadzaniaparkingiem.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class Logi {
    private ArrayList<String> logi;
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public Logi() { this.logi = new ArrayList<>(); }

    private String Czas() { return FMT.format(Instant.now()); }

    public void WjazdSamochodu(String tab, int miejsce) {
        logi.add(Czas() + "  [+] Wjazd samochodu " + tab + " → miejsce nr " + miejsce);
    }

    public void WjazdRoweru(int miejsce) {
        logi.add(Czas() + "  [+] Wjazd roweru → miejsce nr " + miejsce);
    }

    public void WyjazdSamochodu(String tab, int miejsce) {
        logi.add(Czas() + "  [-] Wyjazd samochodu " + tab + " z miejsca nr " + miejsce);
    }

    public void WyjazdRoweru(int miejsce) {
        logi.add(Czas() + "  [-] Wyjazd roweru z miejsca nr " + miejsce);
    }

    public void ProbaWjazduZablokowanegoPojazdu(String tab) {
        logi.add(Czas() + "  [!] Próba wjazdu ZABLOKOWANEGO pojazdu " + tab);
    }

    public void DodanoKlienta(String tab, String dane) {
        logi.add(Czas() + "  [K] Dodano klienta: " + tab + " (" + dane + ")");
    }

    public void UsunietKlient(String tab) {
        logi.add(Czas() + "  [K] Usunięto klienta: " + tab);
    }

    public void ZablokowanaTablica(String tab) {
        logi.add(Czas() + "  [B] Dodano do czarnej listy: " + tab);
    }

    public void OdblokowanaTablica(String tab) {
        logi.add(Czas() + "  [B] Usunięto z czarnej listy: " + tab);
    }

    public void EksportLogow(String sciezka) {
        logi.add(Czas() + "  [E] Eksport logów do pliku: " + sciezka);
    }

    public List<String> GetLogi() { return Collections.unmodifiableList(logi); }

    public String Serializuj() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LOGI SYSTEMU ZARZĄDZANIA PARKINGIEM ===\n");
        sb.append("Eksport: ").append(Czas()).append("\n");
        sb.append("Liczba wpisów: ").append(logi.size()).append("\n");
        sb.append("==========================================\n\n");
        for (String log : logi) {
            sb.append(log).append("\n");
        }
        return sb.toString();
    }
}
