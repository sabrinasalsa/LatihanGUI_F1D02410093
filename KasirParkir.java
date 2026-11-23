import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KasirParkir extends JFrame { 

    private final JTextField txtPlat = new JTextField(15);
    private final JComboBox<String> cmbJenis = new JComboBox<>(new String[]{"Motor", "Mobil", "Truk"});
    private final JSpinner spnDurasi = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
    private final JLabel lblTarif = new JLabel("Tarif: Rp2.000 / jam");
    private final JButton btnHitung = new JButton("Hitung & Cetak Struk");
    private final JButton btnReset = new JButton("Reset");
    private final JTextArea txtStruk = new JTextArea(12, 30);

    private final NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public KasirParkir() {
        super("Kasir Parkir Kampus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));               
        add(buatHeader(), BorderLayout.NORTH);
        add(buatFormPanel(), BorderLayout.WEST);
        add(buatStrukPanel(), BorderLayout.CENTER);
        add(buatTombolPanel(), BorderLayout.SOUTH);

        pasangEventListener();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buatHeader() {
        JPanel p = new JPanel();
        p.setBackground(new Color(30, 90, 160));
        JLabel judul = new JLabel("SISTEM KASIR PARKIR");
        judul.setForeground(Color.WHITE);
        judul.setFont(new Font("SansSerif", Font.BOLD, 18));
        p.add(judul);
        return p;
    }

    private JPanel buatFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());        
        p.setBorder(BorderFactory.createTitledBorder("Data Kendaraan"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Plat Nomor"), g);
        g.gridx = 1;              p.add(txtPlat, g);

        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Jenis Kendaraan"), g);
        g.gridx = 1;              p.add(cmbJenis, g);

        g.gridx = 0; g.gridy = 2; p.add(new JLabel("Lama Parkir (jam)"), g);
        g.gridx = 1;              p.add(spnDurasi, g);

        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        p.add(lblTarif, g);
        return p;
    }

    private JScrollPane buatStrukPanel() {
        txtStruk.setEditable(false);
        txtStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(txtStruk);
        sp.setBorder(BorderFactory.createTitledBorder("Struk Parkir"));
        return sp;
    }

    private JPanel buatTombolPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8)); // Layout 3
        p.add(btnHitung);
        p.add(btnReset);
        return p;
    }

    private void pasangEventListener() {

        btnHitung.addActionListener(e -> hitungDanCetak());

        btnReset.addActionListener(e -> reset());

        cmbJenis.addActionListener(e -> {
            lblTarif.setText("Tarif: " + rupiah.format(getTarif()) + " / jam");
        });

        txtPlat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int pos = txtPlat.getCaretPosition();
                txtPlat.setText(txtPlat.getText().toUpperCase());
                txtPlat.setCaretPosition(Math.min(pos, txtPlat.getText().length()));
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    hitungDanCetak();
                }
            }
        });
    }

    private int getTarif() {
        switch (cmbJenis.getSelectedIndex()) {
            case 1:  return 5000;
            case 2:  return 10000;
            default: return 2000;
        }
    }

    private void hitungDanCetak() {
        String plat = txtPlat.getText().trim();
        if (plat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Plat nomor wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtPlat.requestFocus();
            return;
        }
        int durasi = (Integer) spnDurasi.getValue();
        int total = getTarif() * durasi;
        String waktu = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        txtStruk.setText(
            "================================\n" +
            "        STRUK PARKIR \n" +
            "================================\n" +
            "Waktu    : " + waktu + "\n" +
            "Plat     : " + plat + "\n" +
            "Jenis    : " + cmbJenis.getSelectedItem() + "\n" +
            "Durasi   : " + durasi + " jam\n" +
            "Tarif    : " + rupiah.format(getTarif()) + " / jam\n" +
            "--------------------------------\n" +
            "TOTAL    : " + rupiah.format(total) + "\n" +
            "================================\n" +
            "    Terima kasih, hati-hati!\n");
    }

    private void reset() {
        txtPlat.setText("");
        cmbJenis.setSelectedIndex(0);
        spnDurasi.setValue(1);
        txtStruk.setText("");
        txtPlat.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KasirParkir().setVisible(true));
    }
}
