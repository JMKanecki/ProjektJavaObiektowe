package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogWyjazd extends JDialog {
    private final Parking parking;
    private JTextField fieldTablicaLubKwit;
    private JRadioButton rbSamochod, rbRower;
    private JLabel labelWynik;

    public DialogWyjazd(Frame owner, Parking parking) {
        super(owner, "Wyjazd pojazdu", true);
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

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        rbSamochod = new JRadioButton("Samochód", true);
        rbRower = new JRadioButton("Rower");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbSamochod); bg.add(rbRower);
        radioPanel.add(rbSamochod); radioPanel.add(rbRower);
        form.add(radioPanel, c);

        c.gridwidth = 1; c.gridy = 1; c.gridx = 0;
        JLabel labelPole = new JLabel("Nr rejestracyjny:");
        form.add(labelPole, c);
        c.gridx = 1;
        fieldTablicaLubKwit = new JTextField(14);
        form.add(fieldTablicaLubKwit, c);

        rbSamochod.addActionListener(e -> labelPole.setText("Nr rejestracyjny:"));
        rbRower.addActionListener(e -> labelPole.setText("Nr kwitu:"));

        add(form, BorderLayout.CENTER);

        labelWynik = new JLabel(" ");
        labelWynik.setForeground(Color.RED);
        add(labelWynik, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOK = new JButton("Zarejestruj wyjazd");
        JButton btnAnuluj = new JButton("Anuluj");
        buttons.add(btnAnuluj); buttons.add(btnOK);
        add(buttons, BorderLayout.SOUTH);

        btnOK.addActionListener(e -> zarejestrujWyjazd());
        btnAnuluj.addActionListener(e -> dispose());
    }

    private void zarejestrujWyjazd() {
        String wartoscPola = fieldTablicaLubKwit.getText().trim();
        if (wartoscPola.isEmpty()) {
            labelWynik.setText("Uzupełnij pole!");
            return;
        }

        if (rbSamochod.isSelected()) {
            String tablica = wartoscPola.toUpperCase();
            MiejsceParkingowe miejsce = parking.ZnajdzMiejscePojazdaZTabl(tablica);
            if (miejsce == null) {
                labelWynik.setText("Nie znaleziono samochodu o tablicy " + tablica);
                return;
            }
            int nrMiejsca = miejsce.getNrMiejsca();
            parking.ZarejestrujWyjazd(miejsce);
            JOptionPane.showMessageDialog(this,
                "Wyjazd zarejestrowany.\nSamochód " + tablica + " opuścił miejsce nr " + nrMiejsca,
                "Wyjazd", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            // Rower - szukaj po numerze kwitu
            int nrKwitu;
            try { nrKwitu = Integer.parseInt(wartoscPola); }
            catch (NumberFormatException ex) { labelWynik.setText("Nr kwitu musi być liczbą!"); return; }

            MiejsceParkingowe znalezione = null;
            for (MiejsceParkingowe m : parking.GetMiejscaRowerowe()) {
                if (m.CzyZajete() && m.getAktywnaSesja() != null
                        && m.getAktywnaSesja().getNumerKwitu() == nrKwitu) {
                    znalezione = m;
                    break;
                }
            }
            if (znalezione == null) {
                labelWynik.setText("Nie znaleziono roweru z kwitem nr " + nrKwitu);
                return;
            }
            int nrMiejsca = znalezione.getNrMiejsca();
            parking.ZarejestrujWyjazd(znalezione);
            JOptionPane.showMessageDialog(this,
                "Wyjazd roweru zarejestrowany.\nMiejsce nr " + nrMiejsca + " zwolnione.",
                "Wyjazd roweru", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
