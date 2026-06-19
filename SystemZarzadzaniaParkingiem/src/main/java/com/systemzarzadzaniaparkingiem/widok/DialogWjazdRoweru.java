package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogWjazdRoweru extends JDialog {
    private final Parking parking;
    private JCheckBox checkWlasnaBlokada;
    private JLabel labelWynik;

    public DialogWjazdRoweru(Frame owner, Parking parking) {
        super(owner, "Wjazd roweru", true);
        this.parking = parking;
        buildUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Rowerzysta posiada własną blokadę:"), c);
        c.gridx = 1; checkWlasnaBlokada = new JCheckBox();
        form.add(checkWlasnaBlokada, c);

        add(form, BorderLayout.CENTER);

        labelWynik = new JLabel(" ");
        labelWynik.setForeground(Color.RED);
        add(labelWynik, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOK     = new JButton("Zarejestruj wjazd");
        JButton btnAnuluj = new JButton("Anuluj");
        buttons.add(btnAnuluj);
        buttons.add(btnOK);
        add(buttons, BorderLayout.SOUTH);

        btnOK.addActionListener(e -> zarejestrujWjazd());
        btnAnuluj.addActionListener(e -> dispose());
    }

    private void zarejestrujWjazd() {
        boolean wlasnaBlokada = checkWlasnaBlokada.isSelected();
        Rower rower = new Rower();
        MiejsceParkingowe miejsce = parking.ZnajdzMiejsce(rower, wlasnaBlokada);

        if (miejsce == null) {
            labelWynik.setText("Brak wolnych miejsc rowerowych!");
            return;
        }

        int nrKwitu = 1000 + new Random().nextInt(9000);
        SesjaParkingowa sesja = new SesjaParkingowa(rower, Instant.now(),
            Instant.now().plus(8, ChronoUnit.HOURS), nrKwitu);
        miejsce.ZajmijMiejsce(sesja);
        parking.logi.WjazdRoweru(miejsce.getNrMiejsca());

        JOptionPane.showMessageDialog(this,
            "╔══════════════════════════════╗\n" +
            "║   KWIT PARKINGOWY - ROWER    ║\n" +
            "╠══════════════════════════════╣\n" +
            "║  Nr miejsca: " + String.format("%-17d", miejsce.getNrMiejsca()) + "║\n" +
            "║  Nr kwitu:   " + String.format("%-17d", nrKwitu) + "║\n" +
            (miejsce.CzyPosiadaBlokade() ? "║  [blokada parkingowa]         ║\n" : "║  [własna blokada rowerzysty]  ║\n") +
            "╚══════════════════════════════╝\n" +
            "Wydaj rowerzyście ten kwit!",
            "Kwit parkingowy", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
