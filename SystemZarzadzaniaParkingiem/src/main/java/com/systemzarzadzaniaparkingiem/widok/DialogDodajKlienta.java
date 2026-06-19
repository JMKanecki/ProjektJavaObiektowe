package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogDodajKlienta extends JDialog {
    private final Parking parking;
    private JTextField fieldTablica, fieldImie, fieldEmail, fieldTelefon;
    private JLabel labelWynik;

    public DialogDodajKlienta(Frame owner, Parking parking) {
        super(owner, "Dodaj klienta (rejestracja tablicy)", true);
        this.parking = parking;
        buildUI();
        pack();
        setMinimumSize(new Dimension(380, 0));
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Nr rejestracyjny:", "Imię i nazwisko:", "Adres e-mail:", "Nr telefonu:"};
        JTextField[] fields = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            c.gridx = 0; c.gridy = i; c.weightx = 0;
            form.add(new JLabel(labels[i]), c);
            c.gridx = 1; c.weightx = 1;
            fields[i] = new JTextField(16);
            form.add(fields[i], c);
        }
        fieldTablica = fields[0];
        fieldImie    = fields[1];
        fieldEmail   = fields[2];
        fieldTelefon = fields[3];

        add(form, BorderLayout.CENTER);

        labelWynik = new JLabel(" ");
        labelWynik.setForeground(Color.RED);
        add(labelWynik, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOK    = new JButton("Dodaj");
        JButton btnAnuluj = new JButton("Anuluj");
        buttons.add(btnAnuluj); buttons.add(btnOK);
        add(buttons, BorderLayout.SOUTH);

        btnOK.addActionListener(e -> dodaj());
        btnAnuluj.addActionListener(e -> dispose());
    }

    private void dodaj() {
        String tablica = fieldTablica.getText().trim().toUpperCase();
        String imie    = fieldImie.getText().trim();
        String email   = fieldEmail.getText().trim();
        String tel     = fieldTelefon.getText().trim();

        if (tablica.isEmpty() || imie.isEmpty()) {
            labelWynik.setText("Tablica i imię/nazwisko są wymagane!");
            return;
        }
        if (tel.length() != 9 || !tel.matches("\\d+")){
            labelWynik.setText("Niepoprawny numer telefonu");
            return;           
        }
        if (!email.contains("@")){
            labelWynik.setText("Niepoprawny email!");
            return;           
        }
        
        if (parking.zarejestrowaneTablice.CzyZablokowana(tablica)) {
            JOptionPane.showMessageDialog(this,
                "Nie można dodać klienta — tablica " + tablica + " jest na czarnej liście!",
                "Tablica zablokowana", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Wlasciciel w = new Wlasciciel(imie, email, tel);
        parking.DodajKlienta(tablica, w);

        JOptionPane.showMessageDialog(this,
            "Klient zarejestrowany!\nTablica: " + tablica + "\n" + w.Dane(),
            "Sukces", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
