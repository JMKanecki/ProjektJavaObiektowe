package com.systemzarzadzaniaparkingiem.widok;

import com.systemzarzadzaniaparkingiem.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jakub Kanecki, Dawid Lazarski 
 */
public class MainWindow extends JFrame {

    private final Parking parking;
    private JPanel panelSamochodowe;
    private JPanel panelRowerowe;
    private JLabel labelRaport;
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());

    // Kolory
    private static final Color CLR_WOLNE      = new Color(180, 230, 180);
    private static final Color CLR_ZAJETE      = new Color(230, 100, 100);
    private static final Color CLR_ZABLOK      = new Color(200, 200, 200);
    private static final Color CLR_LADOW       = new Color(180, 210, 255);
    private static final Color CLR_ROWER_BL    = new Color(255, 220, 140);
    private static final Color CLR_TLO_SEKCJI  = new Color(240, 240, 245);
    private static final Color CLR_NAGLOWEK    = new Color(50, 60, 80);

    public MainWindow() {
        this.parking = new Parking();
        buildUI();
        odswiezWidok();
        setTitle("System Zarządzania Parkingiem");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setMaximumSize(new Dimension(1280, 720));
        pack();
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        // Pasek menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuKlienci = new JMenu("Klienci");
        JMenuItem miDodajKlienta = new JMenuItem("Dodaj klienta...");
        JMenuItem miWyszukajKlienta = new JMenuItem("Wyszukaj / edytuj klientów...");
        menuKlienci.add(miDodajKlienta);
        menuKlienci.add(miWyszukajKlienta);

        JMenu menuCzarnaLista = new JMenu("Czarna lista");
        JMenuItem miCzarnaLista = new JMenuItem("Zarządzaj czarną listą...");
        menuCzarnaLista.add(miCzarnaLista);

        JMenu menuLogi = new JMenu("Logi");
        JMenuItem miEksport = new JMenuItem("Podgląd i eksport logów...");
        menuLogi.add(miEksport);

        menuBar.add(menuKlienci);
        menuBar.add(menuCzarnaLista);
        menuBar.add(menuLogi);
        setJMenuBar(menuBar);

        miDodajKlienta.addActionListener(e -> { new DialogDodajKlienta(this, parking).setVisible(true); odswiezWidok(); });
        miWyszukajKlienta.addActionListener(e -> { new DialogWyszukajKlienta(this, parking).setVisible(true); odswiezWidok(); });
        miCzarnaLista.addActionListener(e -> { new DialogCzarnaLista(this, parking).setVisible(true); odswiezWidok(); });
        miEksport.addActionListener(e -> new DialogEksportLogow(this, parking).setVisible(true));

        // Główny layout
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        root.setBackground(Color.WHITE);
        setContentPane(root);

        // Nagłówek
        JLabel title = new JLabel(parking.GetNazwa(), JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(CLR_NAGLOWEK);
        title.setBorder(new EmptyBorder(0, 0, 6, 0));
        root.add(title, BorderLayout.NORTH);

        // Środek: widok + boczny panel
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.78);
        split.setDividerSize(6);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);

        // Widok parkingu
        JPanel widok = new JPanel(new BorderLayout(6, 6));
        widok.setBackground(Color.WHITE);

        // Sekcja samochodowa
        JPanel sek1 = new JPanel(new BorderLayout(4, 4));
        sek1.setBackground(CLR_TLO_SEKCJI);
        sek1.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_NAGLOWEK, 1), new EmptyBorder(6, 6, 6, 6)));
        JLabel lab1 = naglowekSekcji("  Miejsca samochodowe");
        sek1.add(lab1, BorderLayout.NORTH);
        panelSamochodowe = new JPanel(new WrapLayout(FlowLayout.LEFT, 6, 6));
        panelSamochodowe.setBackground(CLR_TLO_SEKCJI);
        sek1.add(panelSamochodowe, BorderLayout.CENTER);

        // Sekcja rowerowa
        JPanel sek2 = new JPanel(new BorderLayout(4, 4));
        sek2.setBackground(CLR_TLO_SEKCJI);
        sek2.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(CLR_NAGLOWEK, 1), new EmptyBorder(6, 6, 6, 6)));
        JLabel lab2 = naglowekSekcji(" Miejsca rowerowe");
        sek2.add(lab2, BorderLayout.NORTH);
        panelRowerowe = new JPanel(new WrapLayout(FlowLayout.LEFT, 6, 6));
        panelRowerowe.setBackground(CLR_TLO_SEKCJI);
        sek2.add(panelRowerowe, BorderLayout.CENTER);

        JPanel sekcje = new JPanel(new GridLayout(2, 1, 0, 8));
        sekcje.setBackground(Color.WHITE);
        sekcje.add(sek1);
        sekcje.add(sek2);

        widok.add(sekcje, BorderLayout.CENTER);

        // Legenda
        widok.add(buildLegenda(), BorderLayout.SOUTH);
        split.setLeftComponent(new JScrollPane(widok));

        // Panel boczny: przyciski + raport
        JPanel boczny = new JPanel(new BorderLayout(0, 10));
        boczny.setBackground(Color.WHITE);

        JPanel przyciski = buildPanelPrzyciskow();
        boczny.add(przyciski, BorderLayout.NORTH);

        JPanel panelRaport = new JPanel(new BorderLayout());
        panelRaport.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(CLR_NAGLOWEK, 1), " Raport "),
            new EmptyBorder(6, 6, 6, 6)));
        panelRaport.setBackground(Color.WHITE);
        labelRaport = new JLabel();
        labelRaport.setVerticalAlignment(JLabel.TOP);
        labelRaport.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        panelRaport.add(labelRaport, BorderLayout.CENTER);
        boczny.add(panelRaport, BorderLayout.CENTER);

        split.setRightComponent(boczny);
    }

    private JLabel naglowekSekcji(String tekst) {
        JLabel l = new JLabel(tekst);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(CLR_NAGLOWEK);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

    private JPanel buildPanelPrzyciskow() {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(CLR_NAGLOWEK, 1), " Operacje "),
            new EmptyBorder(6, 6, 6, 6)));

        JButton btnWjSam = new JButton("Wjazd samochodu");
        JButton btnWjRow = new JButton("Wjazd roweru");
        JButton btnWyj   = new JButton("Wyjazd pojazdu");
        JButton btnOdswiez = new JButton("Odśwież widok");

        for (JButton b : new JButton[]{btnWjSam, btnWjRow, btnWyj, btnOdswiez}) {
            b.setFont(new Font("SansSerif", Font.PLAIN, 13));
            b.setFocusPainted(false);
            p.add(b);
        }

        btnWjSam.addActionListener(e -> { new DialogWjazdSamochodu(this, parking).setVisible(true); odswiezWidok(); });
        btnWjRow.addActionListener(e -> { new DialogWjazdRoweru(this, parking).setVisible(true); odswiezWidok(); });
        btnWyj.addActionListener(e -> { new DialogWyjazd(this, parking).setVisible(true); odswiezWidok(); });
        btnOdswiez.addActionListener(e -> odswiezWidok());

        return p;
    }

    private JPanel buildLegenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(4, 0, 0, 0));
        p.add(legendaKlocek(CLR_WOLNE, "Wolne"));
        p.add(legendaKlocek(CLR_ZAJETE, "Zajęte"));
        p.add(legendaKlocek(CLR_ZABLOK, "Zablokowane"));
        p.add(legendaKlocek(CLR_LADOW, "Ładowarka EV"));
        p.add(legendaKlocek(CLR_ROWER_BL, "Blokada roweru"));
        return p;
    }

    private JPanel legendaKlocek(Color kolor, String opis) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Color.WHITE);
        JPanel kwadrat = new JPanel();
        kwadrat.setPreferredSize(new Dimension(14, 14));
        kwadrat.setBackground(kolor);
        kwadrat.setBorder(new LineBorder(Color.GRAY, 1));
        p.add(kwadrat);
        JLabel l = new JLabel(opis);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(l);
        return p;
    }

    public void odswiezWidok() {
        odswiezSekcje();
        odswiezRaport();
    }

    private void odswiezSekcje() {
        panelSamochodowe.removeAll();
        for (MiejsceParkingowe m : parking.GetMiejscaSamochodowe()) {
            panelSamochodowe.add(buildKafelekSamochod(m));
        }
        panelSamochodowe.revalidate();
        panelSamochodowe.repaint();

        panelRowerowe.removeAll();
        for (MiejsceParkingowe m : parking.GetMiejscaRowerowe()) {
            panelRowerowe.add(buildKafelekRower(m));
        }
        panelRowerowe.revalidate();
        panelRowerowe.repaint();
    }

    private JPanel buildKafelekSamochod(MiejsceParkingowe m) {
        JPanel k = new JPanel();
        k.setLayout(new BoxLayout(k, BoxLayout.Y_AXIS));
        k.setPreferredSize(new Dimension(118, 90));
        k.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        Color tlo;
        if (!m.CzyCzynne()) tlo = CLR_ZABLOK;
        else if (m.CzyZajete()) tlo = CLR_ZAJETE;
        else if (m.CzyPosiadaLadowarke()) tlo = CLR_LADOW;
        else tlo = CLR_WOLNE;
        k.setBackground(tlo);

        String typ = m.CzyPosiadaLadowarke() ? "⚡ EV" : "Silnik";
        JLabel lNr = boldLabel("Nr " + m.getNrMiejsca() + "  " + typ, 12);
        JLabel lStatus;
        JLabel lInfo1 = new JLabel(" ");
        JLabel lInfo2 = new JLabel(" ");
        lInfo1.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lInfo2.setFont(new Font("SansSerif", Font.PLAIN, 10));

        if (m.CzyZajete() && m.getAktywnaSesja() != null) {
            SesjaParkingowa s = m.getAktywnaSesja();
            lStatus = new JLabel("ZAJĘTE");
            lStatus.setForeground(new Color(140, 0, 0));
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
            if (s.pojazd instanceof Samochod samochod) {
                lInfo1 = new JLabel(samochod.GetTablica());
                lInfo1.setFont(new Font("SansSerif", Font.PLAIN, 10));
            }
            if (s.getZaplanowanyCzasWyjazdu() != null) {
                lInfo2 = new JLabel("Do: " + FMT.format(s.getZaplanowanyCzasWyjazdu()));
                lInfo2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            }
        } else if (!m.CzyCzynne()) {
            lStatus = new JLabel("ZABLOKOWANE");
            lStatus.setForeground(Color.DARK_GRAY);
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
        } else {
            lStatus = new JLabel("wolne");
            lStatus.setForeground(new Color(0, 100, 0));
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
        }

        for (JLabel l : new JLabel[]{lNr, lStatus, lInfo1, lInfo2}) {
            l.setAlignmentX(CENTER_ALIGNMENT);
            k.add(l);
        }
        k.add(Box.createVerticalGlue());
        return k;
    }

    private JPanel buildKafelekRower(MiejsceParkingowe m) {
        JPanel k = new JPanel();
        k.setLayout(new BoxLayout(k, BoxLayout.Y_AXIS));
        k.setPreferredSize(new Dimension(100, 70));
        k.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        Color tlo;
        if (!m.CzyCzynne()) tlo = CLR_ZABLOK;
        else if (m.CzyZajete()) tlo = CLR_ZAJETE;
        else if (m.CzyPosiadaBlokade()) tlo = CLR_ROWER_BL;
        else tlo = CLR_WOLNE;
        k.setBackground(tlo);

        String typ = m.CzyPosiadaBlokade() ? "Rower z blokadą" : "Rower";
        JLabel lNr = boldLabel("Nr " + m.getNrMiejsca() + " " + typ, 11);

        JLabel lStatus;
        JLabel lKwit = new JLabel(" ");
        lKwit.setFont(new Font("SansSerif", Font.PLAIN, 10));

        if (m.CzyZajete() && m.getAktywnaSesja() != null) {
            lStatus = new JLabel("ZAJĘTE");
            lStatus.setForeground(new Color(140, 0, 0));
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
            if (m.getAktywnaSesja().getNumerKwitu() > 0) {
                lKwit = new JLabel("Kwit: " + m.getAktywnaSesja().getNumerKwitu());
                lKwit.setFont(new Font("SansSerif", Font.PLAIN, 10));
            }
        } else if (!m.CzyCzynne()) {
            lStatus = new JLabel("ZABLOK.");
            lStatus.setForeground(Color.DARK_GRAY);
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
        } else {
            lStatus = new JLabel("wolne");
            lStatus.setForeground(new Color(0, 100, 0));
            lStatus.setFont(new Font("SansSerif", Font.BOLD, 11));
        }

        for (JLabel l : new JLabel[]{lNr, lStatus, lKwit}) {
            l.setAlignmentX(CENTER_ALIGNMENT);
            k.add(l);
        }
        k.add(Box.createVerticalGlue());
        return k;
    }

    private JLabel boldLabel(String text, int size) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, size));
        return l;
    }

    private void odswiezRaport() {
        Raporty r = parking.raporty;
        String html = "<html>" +
            "Miejsca łącznie:    " + r.IloscMiejsc() + "\n" +
            "Wolne łącznie:      " + r.IloscWolnychMiejsc() + "\n" +
            " – samochodowe:     " + r.IloscWolnychMiejscSamoch() + "\n" +
            " – z ładowarką:     " + r.IloscWolnychMiejscLadowarka() + "\n" +
            " – rowerowe:        " + r.IloscWolnychMiejscRower() + "\n" +
            " – z blokadą:       " + r.IloscWolnychMiejscBlokada() + "\n" +
            "Zarezerwowane:      " + r.IloscMiejscZarezerwowanych() + "\n" +
            "Zablokowane:        " + r.IloscMiejscZablokowanych() + "\n" +
            "─────────────────────\n" +
            "Zaparkowane łącznie:" + (r.IloscMiejsc() - r.IloscWolnychMiejsc()) + "\n" +
            " – samochody:       " + r.IloscZaparkowanychSamoch() + "\n" +
            " – rowery:          " + r.IloscZaparkowanychRower() +
            "</pre></html>";
        labelRaport.setText(html);
    }

    /**
     * FlowLayout z zawijaniem — miejsca parkingowe układają się w wiele rzędów
     * przy zmianie szerokości okna.
     */
    static class WrapLayout extends FlowLayout {
        WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
                int width = 0, height = 0, rowWidth = 0, rowHeight = 0;
                int count = target.getComponentCount();
                for (int i = 0; i < count; i++) {
                    Component c = target.getComponent(i);
                    if (c.isVisible()) {
                        Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                        if (rowWidth + d.width > maxWidth && rowWidth > 0) {
                            width = Math.max(width, rowWidth);
                            height += rowHeight + vgap;
                            rowWidth = 0; rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }
                width = Math.max(width, rowWidth);
                height += rowHeight;
                return new Dimension(width + insets.left + insets.right + hgap * 2,
                    height + insets.top + insets.bottom + vgap * 2);
            }
        }
    }
}
