package kanggoliving_poryek.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import kanggoliving_poryek.GUI.CustomComponents.*;

public class LoginPanel extends JPanel {
    private MainFrame parentFrame;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private RoundedButton btnLogin;
    private JButton btnGoRegister;

    public LoginPanel(MainFrame parent) {
        this.parentFrame = parent;
        setBackground(ThemeColor.PASTEL_BROWN_BG);
        setLayout(new GridBagLayout());

        // Center card container
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(ThemeColor.CARD_BG);
        card.setPreferredSize(new Dimension(420, 520));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Title
        JLabel lblTitle = new JLabel("KanggoLiving", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        card.add(lblTitle, gbc);

        // Subtitle
        JLabel lblSub = new JLabel("Sistem Layanan, Desain & Perbaikan", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(ThemeColor.TEXT_MUTED);
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(0, 30, 20, 30);
        card.add(lblSub, gbc);

        // Email Label & Input
        gbc.insets = new java.awt.Insets(5, 30, 5, 30);
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 2;
        card.add(lblEmail, gbc);

        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridy = 3;
        card.add(txtEmail, gbc);

        // Password Label & Input
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 4;
        card.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        gbc.gridy = 5;
        card.add(txtPassword, gbc);

        // Role Label & Dropdown
        JLabel lblRole = new JLabel("Pilih Peran (Role)");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRole.setForeground(ThemeColor.TEXT_DARK);
        gbc.gridy = 6;
        card.add(lblRole, gbc);

        String[] roles = {
            "Client",
            "Admin - Finance",
            "Admin - Project Manager",
            "Technician - Workshop",
            "Technician - Surveyor",
            "Technician - Staf QC",
            "Technician - Installer",
            "Technician - Interior Design"
        };
        cbRole = new JComboBox<>(roles);
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbRole.setBackground(Color.WHITE);
        gbc.gridy = 7;
        gbc.insets = new java.awt.Insets(5, 30, 15, 30);
        card.add(cbRole, gbc);

        // Login Button
        btnLogin = new RoundedButton("MASUK");
        btnLogin.setPreferredSize(new Dimension(100, 38));
        gbc.gridy = 8;
        gbc.insets = new java.awt.Insets(10, 30, 10, 30);
        card.add(btnLogin, gbc);

        // Register Link
        btnGoRegister = new JButton("Belum punya akun? Daftar Sekarang");
        btnGoRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnGoRegister.setForeground(ThemeColor.ACCENT_GREEN);
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setFocusPainted(false);
        btnGoRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 9;
        gbc.insets = new java.awt.Insets(5, 30, 10, 30);
        card.add(btnGoRegister, gbc);

        add(card);

        // Action Listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                String role = (String) cbRole.getSelectedItem();

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                            "Email dan password tidak boleh kosong!", 
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean success = parentFrame.attemptLogin(email, password, role);
                if (success) {
                    // Clear inputs
                    txtEmail.setText("");
                    txtPassword.setText("");
                    cbRole.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, 
                            "Login Gagal! Email, Password, atau Role salah.", 
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnGoRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.showScreen("REGISTER");
            }
        });
    }
}
