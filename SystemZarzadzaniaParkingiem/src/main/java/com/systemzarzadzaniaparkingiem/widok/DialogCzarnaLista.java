package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogCzarnaLista extends JDialog {
    private final Parking parking;
    private DefaultTableModel tableModel;
    private JTable tabela;
    private JTextField fieldSzukaj;

    public DialogCzarnaLista(Frame owner, Parking parking) {
        super(owner, "Czarna lista tablic", true);
        this.parking = parking;
        buildUI();
        odswiezTabele("");
        setSize(450, 380);
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout(6, 4));
        JPanel addPanel = new JPanel(new BorderLayout(4, 0));
        JTextField fieldNowa = new JTextField(12);
        JButton btnDodaj = new JButton("Dodaj do czarnej listy");
        addPanel.add(new JLabel("Tablica: "), BorderLayout.WEST);
        addPanel.add(fieldNowa, BorderLayout.CENTER);
        addPanel.add(btnDodaj, BorderLayout.EAST);

        JPanel szukajPanel = new JPanel(new BorderLayout(4, 0));
        fieldSzukaj = new JTextField(12);
        JButton btnSzukaj = new JButton("Szukaj");
        szukajPanel.add(new JLabel("Szukaj: "), BorderLayout.WEST);
        szukajPanel.add(fieldSzukaj, BorderLayout.CENTER);
        szukajPanel.add(btnSzukaj, BorderLayout.EAST);

        top.add(addPanel, BorderLayout.NORTH);
        top.add(szukajPanel, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Zablokowana tablica"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnUsun    = new JButton("Usuń z czarnej listy");
        JButton btnZamknij = new JButton("Zamknij");
        buttons.add(btnUsun); buttons.add(btnZamknij);
        add(buttons, BorderLayout.SOUTH);

        btnDodaj.addActionListener(e -> {
            String tab = fieldNowa.getText().trim().toUpperCase();
            if (!tab.isEmpty()) {
                // ZablokujTablice automatycznie usuwa pojazd z parkingu jeśli jest zaparkowany
                parking.zarejestrowaneTablice.ZablokujTablice(tab);
                fieldNowa.setText("");
                odswiezTabele(fieldSzukaj.getText());
            }
        });
        fieldNowa.addActionListener(e -> btnDodaj.doClick());
        btnSzukaj.addActionListener(e -> odswiezTabele(fieldSzukaj.getText()));
        fieldSzukaj.addActionListener(e -> odswiezTabele(fieldSzukaj.getText()));
        btnUsun.addActionListener(e -> usunWybrany());
        btnZamknij.addActionListener(e -> dispose());
    }

    private void odswiezTabele(String fraza) {
        tableModel.setRowCount(0);
        String f = fraza.trim().toLowerCase();
        for (String tab : parking.zarejestrowaneTablice.GetZablokowaneTablice()) {
            if (f.isEmpty() || tab.toLowerCase().contains(f))
                tableModel.addRow(new Object[]{tab});
        }
    }

    private void usunWybrany() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Wybierz tablicę z listy."); return; }
        String tab = (String) tableModel.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this,
            "Usunąć tablicę " + tab + " z czarnej listy?",
            "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            parking.zarejestrowaneTablice.OdblokujTablice(tab);
            odswiezTabele(fieldSzukaj.getText());
        }
    }
}
