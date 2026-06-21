package kanggoliving_poryek.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Date;
import kanggoliving_poryek.GUI.CustomComponents.*;
import kanggoliving_poryek.users.Technician;
import kanggoliving_poryek.model.*;

public class TechnicianDashboardPanel extends JPanel {
    private MainFrame parentFrame;
    private CardLayout rightLayout;
    private JPanel rightContainer;

    // Sidebar buttons
    private SidebarButton btnMenuOverview;
    private SidebarButton btnMenuAction; // Dynamic based on specialization
    private SidebarButton btnMenuMaterials; // Workshop only
    private SidebarButton btnMenuDiscussion;

    // Dynamic labels
    private JLabel lblTechRole;
    private JLabel lblOverviewWelcome;
    
    // Overview Stats
    private JLabel lblTaskStatusVal;
    private JLabel lblActiveProjectVal;

    // Custom views components
    // 1. Interior Design: Consultations & Design creation
    private CustomTable tblIDConsultations;
    // 2. Surveyor: Measurement Upload
    private JTextField txtLength;
    private JTextField txtWidth;
    private JTextField txtHeight;
    private JTextArea taSurveyNotes;
    // 3. Workshop: Stock & Production
    private CustomTable tblMaterials;
    private JComboBox<String> cbProductionProj;
    private JLabel lblProdStatus;
    private RoundedButton btnUpdateProd;
    // 4. Staf QC: Quality Control Checks
    private JComboBox<SystemUnit> cbQCUnits;
    private JTextArea taQCDiagnosis;
    // 5. Installer: Checklist
    private JCheckBox chk1, chk2, chk3, chk4;

    // Discussion Chat
    private JTextArea taChat;
    private JTextField txtChatMsg;

    private NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public TechnicianDashboardPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(new BorderLayout());

        // Left Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setBackground(ThemeColor.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel lblBrand = new JLabel("KanggoLiving");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setBorder(BorderFactory.createEmptyBorder(25, 20, 5, 20));
        sidebar.add(lblBrand);

        lblTechRole = new JLabel("Technician Dashboard");
        lblTechRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTechRole.setForeground(ThemeColor.TEXT_MUTED);
        lblTechRole.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));
        sidebar.add(lblTechRole);

        btnMenuOverview = new SidebarButton("Overview Task");
        btnMenuAction = new SidebarButton("Tugas Spesialisasi");
        btnMenuMaterials = new SidebarButton("Material Stock");
        btnMenuDiscussion = new SidebarButton("Ruang Diskusi");

        sidebar.add(btnMenuOverview);
        sidebar.add(btnMenuAction);
        sidebar.add(btnMenuMaterials);
        sidebar.add(btnMenuDiscussion);

        sidebar.add(Box.createVerticalGlue());

        JButton btnLogout = new JButton("Keluar (Logout)");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setForeground(new Color(239, 83, 80));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(20, 20, 25, 20));
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // Right Content Area
        rightLayout = new CardLayout();
        rightContainer = new JPanel(rightLayout);
        rightContainer.setBackground(ThemeColor.PASTEL_BROWN_BG);

        // Screens
        rightContainer.add(createOverviewPanel(), "OVERVIEW");
        rightContainer.add(createInteriorDesignPanel(), "INTERIOR_DESIGN");
        rightContainer.add(createSurveyorPanel(), "SURVEYOR");
        rightContainer.add(createWorkshopPanel(), "WORKSHOP");
        rightContainer.add(createQCPanel(), "STAF_QC");
        rightContainer.add(createInstallerPanel(), "INSTALLER");
        rightContainer.add(createDiscussionPanel(), "DISCUSSION");

        add(rightContainer, BorderLayout.CENTER);

        // Listeners
        btnMenuOverview.addActionListener(e -> switchSubScreen("OVERVIEW", btnMenuOverview));
        btnMenuAction.addActionListener(e -> {
            String role = parentFrame.getLoggedSubRole();
            if (role != null) {
                if (role.equalsIgnoreCase("Interior Design")) {
                    switchSubScreen("INTERIOR_DESIGN", btnMenuAction);
                } else if (role.equalsIgnoreCase("Surveyor")) {
                    switchSubScreen("SURVEYOR", btnMenuAction);
                } else if (role.equalsIgnoreCase("Workshop")) {
                    switchSubScreen("WORKSHOP", btnMenuAction);
                } else if (role.equalsIgnoreCase("Staf QC")) {
                    switchSubScreen("STAF_QC", btnMenuAction);
                } else if (role.equalsIgnoreCase("Installer")) {
                    switchSubScreen("INSTALLER", btnMenuAction);
                }
            }
        });
        btnMenuMaterials.addActionListener(e -> {
            String role = parentFrame.getLoggedSubRole();
            if (role != null && role.equalsIgnoreCase("Workshop")) {
                switchSubScreen("WORKSHOP_MATERIALS", btnMenuMaterials);
            } else {
                JOptionPane.showMessageDialog(this, "Akses Terbatas untuk bagian Workshop.");
            }
        });
        btnMenuDiscussion.addActionListener(e -> switchSubScreen("DISCUSSION", btnMenuDiscussion));

        btnLogout.addActionListener(e -> parentFrame.logout());

        btnMenuOverview.setActive(true);
    }

    private void switchSubScreen(String screenName, SidebarButton activeButton) {
        if (screenName.equals("WORKSHOP_MATERIALS")) {
            rightLayout.show(rightContainer, "WORKSHOP"); // We integrate material management in the Workshop tab
        } else {
            rightLayout.show(rightContainer, screenName);
        }
        btnMenuOverview.setActive(false);
        btnMenuAction.setActive(false);
        btnMenuMaterials.setActive(false);
        btnMenuDiscussion.setActive(false);
        activeButton.setActive(true);
        refreshUI();
    }

    public void refreshUI() {
        if (parentFrame.getLoggedInUser() == null) return;
        Technician tech = (Technician) parentFrame.getLoggedInUser();
        String spec = parentFrame.getLoggedSubRole();

        lblTechRole.setText("Technician - " + spec);
        lblOverviewWelcome.setText("Selamat Datang, " + tech.getName() + " (" + spec + ")");

        btnMenuAction.setText("Tugas: " + spec);

        // Adjust sidebar options
        if (spec.equalsIgnoreCase("Workshop")) {
            btnMenuMaterials.setVisible(true);
        } else {
            btnMenuMaterials.setVisible(false);
        }

        // Metrics
        lblActiveProjectVal.setText(parentFrame.getDesignProjectList().isEmpty() ? "-" : "Modern Japandi");
        lblTaskStatusVal.setText("1 Aktif");

        // 1. Interior Design: load consultations
        if (spec.equalsIgnoreCase("Interior Design")) {
            DefaultTableModel modelConsult = new DefaultTableModel(new String[]{"ID Konsultasi", "Tanggal Sesi", "Status"}, 0);
            for (Consultation c : parentFrame.getConsultationList()) {
                modelConsult.addRow(new Object[]{c.getConsultationId(), c.getConsultationDate().toString(), c.getStatus()});
            }
            tblIDConsultations.setModel(modelConsult);
        }

        // 2. Workshop: Load materials and production list
        if (spec.equalsIgnoreCase("Workshop")) {
            DefaultTableModel modelMat = new DefaultTableModel(new String[]{"ID Material", "Nama Barang", "Stok", "Harga Satuan", "Status Ketersediaan"}, 0);
            for (Material m : parentFrame.getMaterialList()) {
                modelMat.addRow(new Object[]{m.getMaterialId(), m.getMaterialName(), m.getQuantity(), rpFormat.format(m.getUnitPrice()), m.getStatus()});
            }
            tblMaterials.setModel(modelMat);

            cbProductionProj.removeAllItems();
            cbProductionProj.addItem("Modern Japandi Dapur - PROJ-701");
        }

        // 3. Staf QC: Load System Units
        if (spec.equalsIgnoreCase("Staf QC")) {
            cbQCUnits.removeAllItems();
            for (SystemUnit su : parentFrame.getSystemUnitList()) {
                cbQCUnits.addItem(su);
            }
        }

        // 4. Discussion Screen
        StringBuilder sb = new StringBuilder();
        for (String[] msg : parentFrame.getDiscussionMessages()) {
            sb.append("[").append(msg[3]).append("] ").append(msg[0]).append(" (").append(msg[1]).append("):\n")
              .append(msg[2]).append("\n\n");
        }
        taChat.setText(sb.toString());
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblOverviewWelcome = new JLabel("Selamat Datang, Teknisi!");
        lblOverviewWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblOverviewWelcome.setForeground(ThemeColor.TEXT_DARK);
        panel.add(lblOverviewWelcome, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        RoundedPanel card1 = new RoundedPanel(12);
        card1.setBackground(ThemeColor.CARD_BG);
        card1.setLayout(new BorderLayout());
        card1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle1 = new JLabel("Status Tugas Anda");
        lblTitle1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle1.setForeground(ThemeColor.TEXT_MUTED);
        lblTaskStatusVal = new JLabel("Belum Dimulai");
        lblTaskStatusVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTaskStatusVal.setForeground(ThemeColor.TEXT_DARK);
        card1.add(lblTitle1, BorderLayout.NORTH);
        card1.add(lblTaskStatusVal, BorderLayout.CENTER);
        cardsPanel.add(card1);

        RoundedPanel card2 = new RoundedPanel(12);
        card2.setBackground(ThemeColor.CARD_BG);
        card2.setLayout(new BorderLayout());
        card2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle2 = new JLabel("Proyek Desain Terkait");
        lblTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle2.setForeground(ThemeColor.TEXT_MUTED);
        lblActiveProjectVal = new JLabel("-");
        lblActiveProjectVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblActiveProjectVal.setForeground(ThemeColor.ACCENT_GREEN);
        card2.add(lblTitle2, BorderLayout.NORTH);
        card2.add(lblActiveProjectVal, BorderLayout.CENTER);
        cardsPanel.add(card2);

        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInteriorDesignPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Part 1: Consultation Scheduling Confirmation (FR 1 & 2)
        JPanel consultPanel = new JPanel(new BorderLayout(0, 5));
        consultPanel.setOpaque(false);
        
        JPanel consultHeader = new JPanel(new BorderLayout());
        consultHeader.setOpaque(false);
        JLabel lblConsult = new JLabel("Konfirmasi Sesi Konsultasi Klien");
        lblConsult.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblConsult.setForeground(ThemeColor.TEXT_DARK);
        consultHeader.add(lblConsult, BorderLayout.WEST);

        RoundedButton btnConfirmConsult = new RoundedButton("KONFIRMASI JADWAL");
        btnConfirmConsult.setPreferredSize(new Dimension(180, 28));
        consultHeader.add(btnConfirmConsult, BorderLayout.EAST);
        consultPanel.add(consultHeader, BorderLayout.NORTH);

        tblIDConsultations = new CustomTable();
        consultPanel.add(new JScrollPane(tblIDConsultations), BorderLayout.CENTER);
        panel.add(consultPanel);

        // Part 2: Upload Initial Design (FR 3 & 13)
        RoundedPanel designUpload = new RoundedPanel(12);
        designUpload.setBackground(ThemeColor.CARD_BG);
        designUpload.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        designUpload.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        JLabel lblDesignTitle = new JLabel("Buat & Unggah Draf Desain Ruangan Awal");
        lblDesignTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesignTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        designUpload.add(lblDesignTitle, gbc);

        JPanel formFields = new JPanel(new GridLayout(2, 2, 10, 10));
        formFields.setOpaque(false);
        formFields.add(new JLabel("Gaya Konsep:"));
        JTextField txtDesignStyle = new JTextField("Modern Japandi");
        formFields.add(txtDesignStyle);

        formFields.add(new JLabel("Estimasi Budget Awal (Rp):"));
        JTextField txtDesignBudget = new JTextField("25000000");
        formFields.add(txtDesignBudget);

        gbc.gridy = 1;
        designUpload.add(formFields, gbc);

        RoundedButton btnUploadDesign = new RoundedButton("UNGGAH DESAIN & AJUKAN");
        btnUploadDesign.setPreferredSize(new Dimension(190, 32));
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        designUpload.add(btnUploadDesign, gbc);
        panel.add(designUpload);

        // Actions
        btnConfirmConsult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblIDConsultations.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Pilih salah satu jadwal konsultasi.");
                    return;
                }
                int consultId = (Integer) tblIDConsultations.getValueAt(row, 0);
                for (Consultation c : parentFrame.getConsultationList()) {
                    if (c.getConsultationId() == consultId) {
                        c.approve(); // Set status to Disetujui
                        
                        // Send Chat Status
                        String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                        parentFrame.getDiscussionMessages().add(new String[]{
                                parentFrame.getLoggedInUser().getName(),
                                "Interior Design",
                                "Telah mengonfirmasi persetujuan untuk Sesi Konsultasi ID #" + consultId,
                                dateStr
                        });

                        JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Konsultasi #" + consultId + " disetujui!");
                        refreshUI();
                        return;
                    }
                }
            }
        });

        btnUploadDesign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String style = txtDesignStyle.getText().trim();
                String budgetStr = txtDesignBudget.getText().trim();
                if (style.isEmpty() || budgetStr.isEmpty()) {
                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Input gaya dan budget tidak boleh kosong.");
                    return;
                }
                try {
                    double budget = Double.parseDouble(budgetStr);
                    int nextDesignId = 700 + parentFrame.getDesignProjectList().size() + 1;
                    
                    // Create design project
                    DesignProject dp = new DesignProject(nextDesignId, 601, style, budget);
                    parentFrame.getDesignProjectList().add(dp);

                    // Add log
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[]{
                            parentFrame.getLoggedInUser().getName(),
                            "Interior Design",
                            "Mengunggah Draf Desain Baru: \"" + style + "\" dengan estimasi budget " + rpFormat.format(budget) + ". Silakan Klien melakukan persetujuan.",
                            dateStr
                    });

                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Draf Desain baru berhasil diunggah!");
                    refreshUI();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Budget harus diisi dengan angka.");
                }
            }
        });

        return panel;
    }

    private JPanel createSurveyorPanel() {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setBackground(ThemeColor.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Unggah Pengukuran Dimensi Ruangan Lapangan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        JPanel fields = new JPanel(new GridLayout(3, 2, 10, 10));
        fields.setOpaque(false);

        fields.add(new JLabel("Panjang Ruangan (meter):"));
        txtLength = new JTextField("5.5");
        fields.add(txtLength);

        fields.add(new JLabel("Lebar Ruangan (meter):"));
        txtWidth = new JTextField("4.0");
        fields.add(txtWidth);

        fields.add(new JLabel("Tinggi Ruangan (meter):"));
        txtHeight = new JTextField("3.2");
        fields.add(txtHeight);

        gbc.gridy = 1;
        panel.add(fields, gbc);

        gbc.gridy = 2;
        panel.add(new JLabel("Catatan Tambahan Survey Lapangan:"), gbc);

        taSurveyNotes = new JTextArea(4, 30);
        taSurveyNotes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taSurveyNotes.setLineWrap(true);
        taSurveyNotes.setBorder(BorderFactory.createLineBorder(ThemeColor.DIVIDER));
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(taSurveyNotes), gbc);

        RoundedButton btnUploadSurvey = new RoundedButton("KIRIM DATA PENGUKURAN");
        btnUploadSurvey.setPreferredSize(new Dimension(200, 36));
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 10, 10, 10);
        panel.add(btnUploadSurvey, gbc);

        btnUploadSurvey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String len = txtLength.getText().trim();
                String wid = txtWidth.getText().trim();
                String hei = txtHeight.getText().trim();
                String notes = taSurveyNotes.getText().trim();

                if (len.isEmpty() || wid.isEmpty() || hei.isEmpty()) {
                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Lengkapi ukuran dimensi ruangan.");
                    return;
                }

                // Add message
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Surveyor",
                        "Telah berhasil mengunggah data pengukuran dimensi: "
                        + len + "m x " + wid + "m x " + hei + "m.\nCatatan: " + (notes.isEmpty() ? "Tidak ada" : notes),
                        dateStr
                });

                JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Data pengukuran lapangan berhasil dikirim ke sistem!");
                txtLength.setText("");
                txtWidth.setText("");
                txtHeight.setText("");
                taSurveyNotes.setText("");
                refreshUI();
            }
        });

        return panel;
    }

    private JPanel createWorkshopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Part 1: Production Update (FR 7)
        RoundedPanel prodPanel = new RoundedPanel(12);
        prodPanel.setBackground(ThemeColor.CARD_BG);
        prodPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        prodPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Update Proses Produksi Item Kustom");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        prodPanel.add(lblTitle, gbc);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row1.setOpaque(false);
        row1.add(new JLabel("Pilih Item Proyek: "));
        cbProductionProj = new JComboBox<>();
        row1.add(cbProductionProj);
        gbc.gridy = 1;
        prodPanel.add(row1, gbc);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row2.setOpaque(false);
        row2.add(new JLabel("Status Produksi Saat Ini: "));
        lblProdStatus = new JLabel("Cutting & Assembly");
        lblProdStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProdStatus.setForeground(ThemeColor.TEXT_DARK);
        row2.add(lblProdStatus);
        gbc.gridy = 2;
        prodPanel.add(row2, gbc);

        btnUpdateProd = new RoundedButton("UPDATE STATUS KE FINISHING/READY");
        btnUpdateProd.setPreferredSize(new Dimension(260, 32));
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 5, 5, 5);
        prodPanel.add(btnUpdateProd, gbc);
        panel.add(prodPanel);

        // Part 2: Material Stock View & Request Procurement (FR 11 & 12)
        JPanel matPanel = new JPanel(new BorderLayout(0, 5));
        matPanel.setOpaque(false);

        JPanel matHeader = new JPanel(new BorderLayout());
        matHeader.setOpaque(false);
        JLabel lblMatTitle = new JLabel("Ketersediaan & Pengadaan Material Gudang");
        lblMatTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMatTitle.setForeground(ThemeColor.TEXT_DARK);
        matHeader.add(lblMatTitle, BorderLayout.WEST);

        RoundedButton btnProcure = new RoundedButton("TINTA/AJUKAN PENGADAAN");
        btnProcure.setPreferredSize(new Dimension(200, 28));
        matHeader.add(btnProcure, BorderLayout.EAST);
        matPanel.add(matHeader, BorderLayout.NORTH);

        tblMaterials = new CustomTable();
        matPanel.add(new JScrollPane(tblMaterials), BorderLayout.CENTER);
        panel.add(matPanel);

        // Listeners
        btnUpdateProd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblProdStatus.setText("Finishing / Ready for QC");
                
                // Add chat log
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Workshop",
                        "Telah memperbarui proses produksi item kustom untuk PROJ-701 menjadi: Ready for QC.",
                        dateStr
                });
                
                JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Status produksi berhasil diperbarui!");
                refreshUI();
            }
        });

        btnProcure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblMaterials.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Pilih material dari tabel gudang.");
                    return;
                }
                int matId = (Integer) tblMaterials.getValueAt(row, 0);
                String matName = (String) tblMaterials.getValueAt(row, 1);

                String qtyStr = JOptionPane.showInputDialog(TechnicianDashboardPanel.this, "Masukkan jumlah kuantitas pengadaan material baru:", "25");
                if (qtyStr != null) {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        if (qty <= 0) return;
                        
                        for (Material m : parentFrame.getMaterialList()) {
                            if (m.getMaterialId() == matId) {
                                m.setQuantity(m.getQuantity() + qty);
                                
                                // Send chat alert
                                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                                parentFrame.getDiscussionMessages().add(new String[]{
                                        parentFrame.getLoggedInUser().getName(),
                                        "Workshop",
                                        "Telah melakukan pengadaan material: " + matName + " sejumlah " + qty + " pcs.",
                                        dateStr
                                });
                                
                                JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Stok material berhasil ditambahkan!");
                                refreshUI();
                                return;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Input kuantitas harus berupa angka.");
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createQCPanel() {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setBackground(ThemeColor.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Uji Kelayakan Quality Control (QC)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        JPanel rowUnit = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rowUnit.setOpaque(false);
        rowUnit.add(new JLabel("Pilih Unit Sistem Ruangan:"));
        cbQCUnits = new JComboBox<>();
        rowUnit.add(cbQCUnits);
        gbc.gridy = 1;
        panel.add(rowUnit, gbc);

        RoundedButton btnDiagnose = new RoundedButton("JALANKAN DIAGNOSA SISTEM");
        btnDiagnose.setPreferredSize(new Dimension(200, 32));
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnDiagnose, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Hasil Analisis & Uji Kelayakan QC:"), gbc);

        taQCDiagnosis = new JTextArea(5, 30);
        taQCDiagnosis.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taQCDiagnosis.setEditable(false);
        taQCDiagnosis.setBorder(BorderFactory.createLineBorder(ThemeColor.DIVIDER));
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(taQCDiagnosis), gbc);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionRow.setOpaque(false);
        RoundedButton btnPass = new RoundedButton("LOLOS UJI KELAYAKAN (QC PASSED)");
        btnPass.setPreferredSize(new Dimension(240, 36));
        RoundedButton btnFail = new RoundedButton("GAGAL QC (PERLU PERBAIKAN)");
        btnFail.setColors(new Color(198, 40, 40), new Color(211, 47, 47));
        btnFail.setPreferredSize(new Dimension(240, 36));

        actionRow.add(btnPass);
        actionRow.add(btnFail);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 10, 10);
        panel.add(actionRow, gbc);

        // Listeners
        btnDiagnose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemUnit selectedUnit = (SystemUnit) cbQCUnits.getSelectedItem();
                if (selectedUnit != null) {
                    String result = selectedUnit.diagnose();
                    taQCDiagnosis.setText("ID Unit: " + selectedUnit.getSystemId() + "\n"
                            + "Nama Unit: " + selectedUnit.getSystemName() + "\n"
                            + "Kondisi Terdeteksi: " + selectedUnit.getCondition() + "\n"
                            + "Diagnosa: " + result);
                }
            }
        });

        btnPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemUnit selectedUnit = (SystemUnit) cbQCUnits.getSelectedItem();
                if (selectedUnit != null) {
                    selectedUnit.updateCondition("Baik");
                    
                    // Chat Alert
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[]{
                            parentFrame.getLoggedInUser().getName(),
                            "Staf QC",
                            "Telah memverifikasi LOLOS QC untuk Unit: " + selectedUnit.getSystemName() + " (Kondisi: Baik)",
                            dateStr
                    });

                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Unit berhasil dikonfirmasi Lolos Uji Kelayakan QC!");
                    taQCDiagnosis.setText("");
                    refreshUI();
                }
            }
        });

        btnFail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemUnit selectedUnit = (SystemUnit) cbQCUnits.getSelectedItem();
                if (selectedUnit != null) {
                    selectedUnit.updateCondition("Rusak / Perlu Perbaikan");
                    
                    // Chat Alert
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[]{
                            parentFrame.getLoggedInUser().getName(),
                            "Staf QC",
                            "UJI KELAYAKAN GAGAL untuk Unit: " + selectedUnit.getSystemName() + ". Memerlukan perbaikan segera.",
                            dateStr
                    });

                    JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Kondisi unit dirubah menjadi Perlu Perbaikan.");
                    taQCDiagnosis.setText("");
                    refreshUI();
                }
            }
        });

        return panel;
    }

    private JPanel createInstallerPanel() {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setBackground(ThemeColor.CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblTitle = new JLabel("Checklist Instalasi Rakitan Lapangan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        JLabel lblSub = new JLabel("Pastikan seluruh checklist instalasi terpasang dengan baik.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(ThemeColor.TEXT_MUTED);
        gbc.gridy = 1;
        panel.add(lblSub, gbc);

        JPanel chkPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        chkPanel.setOpaque(false);
        chkPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        chk1 = new JCheckBox("1. Pasang struktur dasar & kayu penopang (Kabinet / Rangka)");
        chk1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chk2 = new JCheckBox("2. Lakukan perakitan kabel kelistrikan & sambungan fitting saklar");
        chk2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chk3 = new JCheckBox("3. Pemasangan aksesoris, engsel pintu kabinet, dan panel finishing");
        chk3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chk4 = new JCheckBox("4. Uji kelayakan mekanikal dasar instalasi di ruangan properti");
        chk4.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        chkPanel.add(chk1);
        chkPanel.add(chk2);
        chkPanel.add(chk3);
        chkPanel.add(chk4);
        gbc.gridy = 2;
        panel.add(chkPanel, gbc);

        RoundedButton btnSubmitChecklist = new RoundedButton("KIRIM STATUS INSTALASI");
        btnSubmitChecklist.setPreferredSize(new Dimension(200, 36));
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(btnSubmitChecklist, gbc);

        btnSubmitChecklist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = 0;
                if (chk1.isSelected()) count++;
                if (chk2.isSelected()) count++;
                if (chk3.isSelected()) count++;
                if (chk4.isSelected()) count++;

                String statusMsg = "Telah menyelesaikan checklist instalasi: " + count + "/4 langkah berhasil diselesaikan.";
                
                // Add chat log
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Installer",
                        statusMsg,
                        dateStr
                });

                JOptionPane.showMessageDialog(TechnicianDashboardPanel.this, "Laporan status checklist instalasi dikirim!");
                refreshUI();
            }
        });

        return panel;
    }

    private JPanel createDiscussionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Ruang Diskusi Proyek Bersama");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        taChat = new JTextArea();
        taChat.setEditable(false);
        taChat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taChat.setLineWrap(true);
        taChat.setWrapStyleWord(true);
        panel.add(new JScrollPane(taChat), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        txtChatMsg = new JTextField();
        txtChatMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtChatMsg.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        inputPanel.add(txtChatMsg, BorderLayout.CENTER);

        RoundedButton btnSend = new RoundedButton("KIRIM");
        btnSend.setPreferredSize(new Dimension(80, 0));
        inputPanel.add(btnSend, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtChatMsg.getText().trim();
                if (msg.isEmpty()) return;

                String senderName = parentFrame.getLoggedInUser().getName();
                String senderRole = "Technician - " + parentFrame.getLoggedSubRole();
                String timestamp = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

                parentFrame.getDiscussionMessages().add(new String[]{senderName, senderRole, msg, timestamp});
                txtChatMsg.setText("");
                refreshUI();
            }
        };

        txtChatMsg.addActionListener(sendAction);
        btnSend.addActionListener(sendAction);

        return panel;
    }
}
