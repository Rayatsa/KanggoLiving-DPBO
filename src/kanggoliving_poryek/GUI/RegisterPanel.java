package kanggoliving_poryek.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import kanggoliving_poryek.GUI.CustomComponents.*;

public class RegisterPanel extends JPanel {
    private MainFrame parentFrame;

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtPhone;
    private JComboBox<String> cbRole;
    private JLabel lblDynamicInfo;
    private JTextField txtDynamicInfo;
    private RoundedButton btnRegister;
    private JButton btnGoLogin;

    public RegisterPanel(MainFrame parent) {
        this.parentFrame = parent;
        setBackground(ThemeColor.PASTEL_BROWN_BG);
        setLayout(new GridBagLayout());

        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(ThemeColor.CARD_BG);
        card.setPreferredSize(new Dimension(460, 600));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(6, 30, 6, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Title
        JLabel lblTitle = new JLabel("Daftar Akun Baru", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        card.add(lblTitle, gbc);

        // Subtitle
        JLabel lblSub = new JLabel("Bergabunglah dengan Layanan KanggoLiving", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(ThemeColor.TEXT_MUTED);
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 30, 15, 30);
        card.add(lblSub, gbc);

        gbc.insets = new java.awt.Insets(4, 30, 4, 30);

        // Name
        JLabel lblName = new JLabel("Nama Lengkap");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblName.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 2;
        card.add(lblName, gbc);

        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        gbc.gridy = 3;
        card.add(txtName, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEmail.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 4;
        card.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        gbc.gridy = 5;
        card.add(txtEmail, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPassword.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 6;
        card.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        gbc.gridy = 7;
        card.add(txtPassword, gbc);

        // Phone
        JLabel lblPhone = new JLabel("Nomor Telepon");
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPhone.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 8;
        card.add(lblPhone, gbc);

        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        gbc.gridy = 9;
        card.add(txtPhone, gbc);

        // Role Dropdown
        JLabel lblRole = new JLabel("Peran Pendaftaran");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRole.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 10;
        card.add(lblRole, gbc);

        String[] roles = {
            "Client",
            "Technician - Workshop",
            "Technician - Surveyor",
            "Technician - Staf QC",
            "Technician - Installer",
            "Technician - Interior Design"
        };
        cbRole = new JComboBox<>(roles);
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbRole.setBackground(Color.WHITE);
        gbc.gridy = 11;
        card.add(cbRole, gbc);

        // Dynamic Info (Address for Client, Specialization for Technician)
        lblDynamicInfo = new JLabel("Alamat Rumah");
        lblDynamicInfo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDynamicInfo.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 12;
        card.add(lblDynamicInfo, gbc);

        txtDynamicInfo = new JTextField();
        txtDynamicInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDynamicInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        gbc.gridy = 13;
        card.add(txtDynamicInfo, gbc);

        // Register Button
        btnRegister = new RoundedButton("DAFTAR SEKARANG");
        btnRegister.setPreferredSize(new Dimension(100, 36));
        gbc.gridy = 14;
        gbc.insets = new java.awt.Insets(15, 30, 5, 30);
        card.add(btnRegister, gbc);

        // Back to Login Link
        btnGoLogin = new JButton("Sudah punya akun? Masuk");
        btnGoLogin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnGoLogin.setForeground(ThemeColor.ACCENT_GREEN);
        btnGoLogin.setContentAreaFilled(false);
        btnGoLogin.setBorderPainted(false);
        btnGoLogin.setFocusPainted(false);
        btnGoLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 15;
        gbc.insets = new java.awt.Insets(2, 30, 10, 30);
        card.add(btnGoLogin, gbc);

        add(card);

        // Item listener for dynamic label and pre-fills
        cbRole.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selected = (String) cbRole.getSelectedItem();
                    if (selected.equals("Client")) {
                        lblDynamicInfo.setText("Alamat Rumah");
                        txtDynamicInfo.setText("");
                    } else {
                        lblDynamicInfo.setText("Spesialisasi Keahlian");
                        // prefill specialization name
                        String spec = selected.substring(selected.indexOf("-") + 2).trim();
                        txtDynamicInfo.setText(spec);
                    }
                }
            }
        });

        // Action Listeners
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText().trim();
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                String phone = txtPhone.getText().trim();
                String role = (String) cbRole.getSelectedItem();
                String info = txtDynamicInfo.getText().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || info.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, 
                            "Semua kolom data wajib diisi!", 
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean success = parentFrame.attemptRegister(name, email, password, phone, info, role);
                if (success) {
                    JOptionPane.showMessageDialog(RegisterPanel.this, 
                            "Pendaftaran berhasil! Silakan masuk dengan akun Anda.", 
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    // Clear inputs
                    txtName.setText("");
                    txtEmail.setText("");
                    txtPassword.setText("");
                    txtPhone.setText("");
                    txtDynamicInfo.setText("");
                    cbRole.setSelectedIndex(0);
                    
                    parentFrame.showScreen("LOGIN");
                } else {
                    JOptionPane.showMessageDialog(RegisterPanel.this, 
                            "Pendaftaran gagal! Email mungkin sudah terdaftar.", 
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnGoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showScreen("LOGIN");
            }
        });
    }
}
