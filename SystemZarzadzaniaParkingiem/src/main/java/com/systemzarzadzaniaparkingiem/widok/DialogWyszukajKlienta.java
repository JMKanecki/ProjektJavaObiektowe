package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogWyszukajKlienta extends JDialog {
    private final Parking parking;
    private JTextField fieldSzukaj;
    private DefaultTableModel tableModel;
    private JTable tabela;

    public DialogWyszukajKlienta(Frame owner, Parking parking) {
        super(owner, "Wyszukiwanie klientów", true);
        this.parking = parking;
        buildUI();
        odswiezTabele("");
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(6, 0));
        top.add(new JLabel("Szukaj (tablica/imię/email/tel): "), BorderLayout.WEST);
        fieldSzukaj = new JTextField();
        top.add(fieldSzukaj, BorderLayout.CENTER);
        JButton btnSzukaj = new JButton("Szukaj");
        top.add(btnSzukaj, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Tablica", "Imię i nazwisko", "E-mail", "Telefon"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEdytuj  = new JButton("Edytuj");
        JButton btnUsun    = new JButton("Usuń");
        JButton btnZamknij = new JButton("Zamknij");
        buttons.add(btnEdytuj); buttons.add(btnUsun); buttons.add(btnZamknij);
        add(buttons, BorderLayout.SOUTH);

        fieldSzukaj.addActionListener(e -> odswiezTabele(fieldSzukaj.getText()));
        btnSzukaj.addActionListener(e -> odswiezTabele(fieldSzukaj.getText()));
        btnEdytuj.addActionListener(e -> edytujWybrany());
        btnUsun.addActionListener(e -> usunWybrany());
        btnZamknij.addActionListener(e -> dispose());
    }

    private void odswiezTabele(String fraza) {
        tableModel.setRowCount(0);
        String f = fraza.trim().toLowerCase();
        for (Map.Entry<String, Wlasciciel> e : parking.zarejestrowaneTablice.GetWszystkieTablice().entrySet()) {
            String tab = e.getKey();
            Wlasciciel w = e.getValue();
            if (f.isEmpty() || tab.toLowerCase().contains(f)
                    || w.GetImieNazwisko().toLowerCase().contains(f)
                    || w.GeteMail().toLowerCase().contains(f)
                    || w.GetNrTelefonu().contains(f)) {
                tableModel.addRow(new Object[]{tab, w.GetImieNazwisko(), w.GeteMail(), w.GetNrTelefonu()});
            }
        }
    }

    private void edytujWybrany() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Wybierz klienta z listy."); return; }
        String tablica = (String) tableModel.getValueAt(row, 0);
        Wlasciciel w = parking.ZnajdzWlasciciela(tablica);
        if (w == null) return;

        JTextField fImie  = new JTextField(w.GetImieNazwisko(), 18);
        JTextField fEmail = new JTextField(w.GeteMail(), 18);
        JTextField fTel   = new JTextField(w.GetNrTelefonu(), 18);
        JPanel p = new JPanel(new GridLayout(3, 2, 4, 4));
        p.add(new JLabel("Imię i nazwisko:")); p.add(fImie);
        p.add(new JLabel("E-mail:"));          p.add(fEmail);
        p.add(new JLabel("Telefon:"));         p.add(fTel);

        int res = JOptionPane.showConfirmDialog(this, p, "Edycja klienta " + tablica,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            // aktualizuj() zamiast trzech setterów
            w.Aktualizuj(fImie.getText().trim(), fEmail.getText().trim(), fTel.getText().trim());
            odswiezTabele(fieldSzukaj.getText());
        }
    }

    private void usunWybrany() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Wybierz klienta z listy."); return; }
        String tablica = (String) tableModel.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this,
            "Czy na pewno usunąć klienta z tablicą " + tablica + "?",
            "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            parking.UsunKlienta(tablica);
            odswiezTabele(fieldSzukaj.getText());
        }
    }
}
