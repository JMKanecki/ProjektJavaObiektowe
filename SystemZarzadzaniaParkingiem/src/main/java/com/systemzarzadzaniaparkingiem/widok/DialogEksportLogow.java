package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class DialogEksportLogow extends JDialog {
    private final Parking parking;
    private JTextArea areaLogi;

    public DialogEksportLogow(Frame owner, Parking parking) {
        super(owner, "Logi systemu – eksport", true);
        this.parking = parking;
        buildUI();
        setSize(620, 480);
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        areaLogi = new JTextArea();
        areaLogi.setEditable(false);
        areaLogi.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaLogi.setText(parking.logi.Serializuj());
        add(new JScrollPane(areaLogi), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOdswiez = new JButton("Odśwież");
        JButton btnEksport = new JButton("Eksportuj do TXT...");
        JButton btnZamknij = new JButton("Zamknij");
        buttons.add(btnOdswiez); buttons.add(btnEksport); buttons.add(btnZamknij);
        add(buttons, BorderLayout.SOUTH);

        btnOdswiez.addActionListener(e -> areaLogi.setText(parking.logi.Serializuj()));
        btnEksport.addActionListener(e -> eksportuj());
        btnZamknij.addActionListener(e -> dispose());
    }

    private void eksportuj() {
        String domyslna = "logi_parkingu_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(domyslna));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File plik = fc.getSelectedFile();
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(plik), StandardCharsets.UTF_8))) {
            pw.print(parking.logi.Serializuj());
            parking.logi.EksportLogow(plik.getAbsolutePath());
            areaLogi.setText(parking.logi.Serializuj());
            JOptionPane.showMessageDialog(this,
                "Logi zapisane do:\n" + plik.getAbsolutePath(),
                "Eksport zakończony", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Błąd zapisu pliku:\n" + ex.getMessage(),
                "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
