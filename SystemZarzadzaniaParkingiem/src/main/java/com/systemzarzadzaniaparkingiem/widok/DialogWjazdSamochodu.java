package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogWjazdSamochodu extends JDialog {
    private final Parking parking;
    private JTextField fieldTablica;
    private JCheckBox checkElektryczny;
    private JSpinner spinnerGodziny;
    private JLabel labelWynik;

    public DialogWjazdSamochodu(Frame owner, Parking parking) {
        super(owner, "Wjazd pojazdu", true);
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
        form.add(new JLabel("Nr rejestracyjny:"), c);
        c.gridx = 1; fieldTablica = new JTextField(12);
        form.add(fieldTablica, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Pojazd elektryczny:"), c);
        c.gridx = 1; checkElektryczny = new JCheckBox();
        form.add(checkElektryczny, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Planowany postój (godz.):"), c);
        c.gridx = 1;
        spinnerGodziny = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        form.add(spinnerGodziny, c);

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
        String tablica = fieldTablica.getText().trim().toUpperCase();
        if (tablica.isEmpty()) {
            labelWynik.setText("Podaj numer rejestracyjny!");
            return;
        }

        int sprawdzenie = parking.sprawdzTabliceWjazd(tablica);
        if (sprawdzenie == -1) {
            parking.logi.ProbaWjazduZablokowanegoPojazdu(tablica);
            JOptionPane.showMessageDialog(this,
                "ODMOWA WJAZDU!\nTablica " + tablica + " jest na czarnej liście.",
                "Zablokowany pojazd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean elektryczny = checkElektryczny.isSelected();
        Samochod samochod = new Samochod(tablica, elektryczny);
        MiejsceParkingowe miejsce = parking.ZnajdzMiejsce(samochod);

        if (miejsce == null) {
            labelWynik.setText("Brak wolnych miejsc samochodowych!");
            return;
        }

        int godziny = (int) spinnerGodziny.getValue();
        Instant wyjazd = Instant.now().plus(godziny, ChronoUnit.HOURS);
        parking.ZarejestrujWjazd(samochod, miejsce, wyjazd);

        String info = sprawdzenie == 1 ? " (klient zarejestrowany)" : " (klient niezarejestrowany)";
        JOptionPane.showMessageDialog(this,
            "Wjazd zarejestrowany!\nNr rejestracyjny: " + tablica + info +
            "\nPrzydzielone miejsce nr: " + miejsce.getNrMiejsca() +
            (miejsce.CzyPosiadaLadowarke() ? " [ładowarka]" : ""),
            "Wjazd", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
