package com.systemzarzadzaniaparkingiem.model;

import java.time.Duration;
import java.time.Instant;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class SesjaParkingowa {
    public Pojazd pojazd;
    private Instant czasWjazdu;
    private Instant zaplanowanyCzasWyjazdu;
    private Instant rzeczywistyCzasWyjazdu;
    private int numerKwitu; // dla rowerów

    public SesjaParkingowa(Pojazd pojazd, Instant czasWjazdu, Instant zaplanowanyCzasWyjazdu) {
        this.pojazd = pojazd;
        this.czasWjazdu = czasWjazdu;
        this.zaplanowanyCzasWyjazdu = zaplanowanyCzasWyjazdu;
        this.numerKwitu = -1;
    }

    public SesjaParkingowa(Pojazd pojazd, Instant czasWjazdu, Instant zaplanowanyCzasWyjazdu, int numerKwitu) {
        this(pojazd, czasWjazdu, zaplanowanyCzasWyjazdu);
        this.numerKwitu = numerKwitu;
    }

    public Duration ObliczZaplanowanyCzas() {
        return Duration.between(this.czasWjazdu, this.zaplanowanyCzasWyjazdu);
    }

    public Duration ObliczRzeczywistyCzas() {
        if (rzeczywistyCzasWyjazdu == null) return Duration.between(this.czasWjazdu, Instant.now());
        return Duration.between(this.czasWjazdu, this.rzeczywistyCzasWyjazdu);
    }

    public void Zamknij() { this.rzeczywistyCzasWyjazdu = Instant.now(); }

    public boolean CzyWyjazdPunktualny() {
        if (rzeczywistyCzasWyjazdu == null) return false;
        return this.zaplanowanyCzasWyjazdu.isAfter(this.rzeczywistyCzasWyjazdu);
    }

    public Instant getCzasWjazdu() { return czasWjazdu; }
    public Instant getZaplanowanyCzasWyjazdu() { return zaplanowanyCzasWyjazdu; }
    public int getNumerKwitu() { return numerKwitu; }
}
