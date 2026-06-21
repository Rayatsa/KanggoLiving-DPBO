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
import kanggoliving_poryek.users.Client;
import kanggoliving_poryek.model.*;

public class ClientDashboardPanel extends JPanel {
    private MainFrame parentFrame;
    private CardLayout rightLayout;
    private JPanel rightContainer;

    // Sidebar buttons
    private SidebarButton btnMenuOverview;
    private SidebarButton btnMenuConsultations;
    private SidebarButton btnMenuProjects;
    private SidebarButton btnMenuInvoices;
    private SidebarButton btnMenuTickets;
    private SidebarButton btnMenuDiscussion;

    // UI elements to update dynamically
    private JLabel lblWelcome;
    private JLabel lblProjectStatusVal;
    private JLabel lblInvoiceBalanceVal;
    private JLabel lblOpenTicketsVal;

    // Tables
    private CustomTable tblConsultations;
    private CustomTable tblInvoices;
    private CustomTable tblTickets;

    // Discussion UI
    private JTextArea taChat;
    private JTextField txtChatMsg;

    // Active Project view elements
    private JLabel lblProjId;
    private JLabel lblProjStyle;
    private JLabel lblProjBudget;
    private JLabel lblProjRevisions;
    private JLabel lblProjStatus;
    private RoundedButton btnApproveProject;
    private RoundedButton btnRequestRevision;

    private NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public ClientDashboardPanel(MainFrame parent) {
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

        JLabel lblRoleText = new JLabel("Client Dashboard");
        lblRoleText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRoleText.setForeground(ThemeColor.TEXT_MUTED);
        lblRoleText.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));
        sidebar.add(lblRoleText);

        btnMenuOverview = new SidebarButton("Overview");
        btnMenuConsultations = new SidebarButton("Konsultasi & Jadwal");
        btnMenuProjects = new SidebarButton("Proyek Saya");
        btnMenuInvoices = new SidebarButton("Tagihan & Invoices");
        btnMenuTickets = new SidebarButton("Tiket Masalah");
        btnMenuDiscussion = new SidebarButton("Ruang Diskusi");

        sidebar.add(btnMenuOverview);
        sidebar.add(btnMenuConsultations);
        sidebar.add(btnMenuProjects);
        sidebar.add(btnMenuInvoices);
        sidebar.add(btnMenuTickets);
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

        // Right Content Area Container
        rightLayout = new CardLayout();
        rightContainer = new JPanel(rightLayout);
        rightContainer.setBackground(ThemeColor.PASTEL_BROWN_BG);

        // Build screens
        rightContainer.add(createOverviewPanel(), "OVERVIEW");
        rightContainer.add(createConsultationPanel(), "CONSULTATIONS");
        rightContainer.add(createProjectsPanel(), "PROJECTS");
        rightContainer.add(createInvoicesPanel(), "INVOICES");
        rightContainer.add(createTicketsPanel(), "TICKETS");
        rightContainer.add(createDiscussionPanel(), "DISCUSSION");

        add(rightContainer, BorderLayout.CENTER);

        // Sidebar Navigation Action Listeners
        btnMenuOverview.addActionListener(e -> switchSubScreen("OVERVIEW", btnMenuOverview));
        btnMenuConsultations.addActionListener(e -> switchSubScreen("CONSULTATIONS", btnMenuConsultations));
        btnMenuProjects.addActionListener(e -> switchSubScreen("PROJECTS", btnMenuProjects));
        btnMenuInvoices.addActionListener(e -> switchSubScreen("INVOICES", btnMenuInvoices));
        btnMenuTickets.addActionListener(e -> switchSubScreen("TICKETS", btnMenuTickets));
        btnMenuDiscussion.addActionListener(e -> switchSubScreen("DISCUSSION", btnMenuDiscussion));

        btnLogout.addActionListener(e -> parentFrame.logout());

        // Default Active Tab
        btnMenuOverview.setActive(true);
    }

    private void switchSubScreen(String screenName, SidebarButton activeButton) {
        rightLayout.show(rightContainer, screenName);
        btnMenuOverview.setActive(false);
        btnMenuConsultations.setActive(false);
        btnMenuProjects.setActive(false);
        btnMenuInvoices.setActive(false);
        btnMenuTickets.setActive(false);
        btnMenuDiscussion.setActive(false);
        activeButton.setActive(true);
        refreshUI();
    }

    public void refreshUI() {
        if (parentFrame.getLoggedInUser() == null) return;
        Client client = (Client) parentFrame.getLoggedInUser();

        // 1. Overview Screen
        lblWelcome.setText("Selamat Datang Kembali, " + client.getName() + "!");

        // Metrics calculations
        String projectStatus = "Tidak Ada Proyek Aktif";
        if (!parentFrame.getDesignProjectList().isEmpty()) {
            DesignProject dp = parentFrame.getDesignProjectList().get(parentFrame.getDesignProjectList().size() - 1);
            projectStatus = dp.getStatus();
        }
        lblProjectStatusVal.setText(projectStatus);

        double balance = 0;
        for (Invoice inv : parentFrame.getInvoiceList()) {
            if (!inv.getStatus().equalsIgnoreCase("Paid")) {
                balance += (inv.calculateTotal() - inv.getAmountPaid());
            }
        }
        lblInvoiceBalanceVal.setText(rpFormat.format(balance));

        int openTickets = 0;
        for (TicketProblem t : parentFrame.getTicketList()) {
            if (t.getStatus().equalsIgnoreCase("Open") || t.getStatus().equalsIgnoreCase("Draft")) {
                openTickets++;
            }
        }
        lblOpenTicketsVal.setText(String.valueOf(openTickets));

        // 2. Consultation Screen Table
        DefaultTableModel modelConsult = new DefaultTableModel(new String[]{"ID Konsultasi", "Tanggal Sesi", "Status"}, 0);
        for (Consultation c : parentFrame.getConsultationList()) {
            modelConsult.addRow(new Object[]{
                    c.getConsultationId(),
                    c.getConsultationDate().toString(),
                    c.getStatus()
            });
        }
        tblConsultations.setModel(modelConsult);

        // 3. Project Detail Screen
        if (!parentFrame.getDesignProjectList().isEmpty()) {
            DesignProject dp = parentFrame.getDesignProjectList().get(parentFrame.getDesignProjectList().size() - 1);
            lblProjId.setText("PROJ-" + dp.getDesignId());
            lblProjStyle.setText(dp.getConceptStyle());
            lblProjBudget.setText(rpFormat.format(dp.getEstimatedBudget()));
            lblProjRevisions.setText(String.valueOf(dp.getRevisionCount()));
            lblProjStatus.setText(dp.getStatus());
            
            btnApproveProject.setEnabled(dp.getStatus().equalsIgnoreCase("Pending"));
            btnRequestRevision.setEnabled(true);
        } else {
            lblProjId.setText("-");
            lblProjStyle.setText("-");
            lblProjBudget.setText("-");
            lblProjRevisions.setText("-");
            lblProjStatus.setText("-");
            btnApproveProject.setEnabled(false);
            btnRequestRevision.setEnabled(false);
        }

        // 4. Invoices Screen Table
        DefaultTableModel modelInvoice = new DefaultTableModel(new String[]{"ID Invoice", "Tagihan Dasar", "Pajak (PPN 12%)", "Total Tagihan", "Telah Dibayar", "Status"}, 0);
        for (Invoice inv : parentFrame.getInvoiceList()) {
            modelInvoice.addRow(new Object[]{
                    inv.getInvoiceId(),
                    rpFormat.format(inv.getAmount()),
                    rpFormat.format(inv.getAmount() * 0.12),
                    rpFormat.format(inv.calculateTotal()),
                    rpFormat.format(inv.getAmountPaid()),
                    inv.getStatus()
            });
        }
        tblInvoices.setModel(modelInvoice);

        // 5. Tickets Screen Table
        DefaultTableModel modelTicket = new DefaultTableModel(new String[]{"ID Tiket", "Deskripsi Masalah", "Status Masalah", "Tanggal Dibuat"}, 0);
        for (TicketProblem t : parentFrame.getTicketList()) {
            modelTicket.addRow(new Object[]{
                    t.getTicketId() != null ? t.getTicketId() : "Draft/Processing",
                    t.getProblemDescription(),
                    t.getStatus(),
                    t.getCreateDate() != null ? t.getCreateDate() : "-"
            });
        }
        tblTickets.setModel(modelTicket);

        // 6. Discussion Screen Text Area
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

        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        lblWelcome = new JLabel("Selamat Datang Kembali,!");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblWelcome.setForeground(ThemeColor.TEXT_DARK);
        header.add(lblWelcome, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        // Center Area (Metrics & Feedback Form)
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.gridx = 0;

        // Stat cards container
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);

        // Card 1: Project Status
        RoundedPanel cardProj = new RoundedPanel(12);
        cardProj.setBackground(ThemeColor.CARD_BG);
        cardProj.setLayout(new BorderLayout());
        cardProj.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblProjTitle = new JLabel("Status Proyek Aktif");
        lblProjTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProjTitle.setForeground(ThemeColor.TEXT_MUTED);
        lblProjectStatusVal = new JLabel("Pending");
        lblProjectStatusVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblProjectStatusVal.setForeground(ThemeColor.ACCENT_GREEN);
        cardProj.add(lblProjTitle, BorderLayout.NORTH);
        cardProj.add(lblProjectStatusVal, BorderLayout.CENTER);
        statsPanel.add(cardProj);

        // Card 2: Invoice Balance
        RoundedPanel cardInv = new RoundedPanel(12);
        cardInv.setBackground(ThemeColor.CARD_BG);
        cardInv.setLayout(new BorderLayout());
        cardInv.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblInvTitle = new JLabel("Sisa Tagihan");
        lblInvTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblInvTitle.setForeground(ThemeColor.TEXT_MUTED);
        lblInvoiceBalanceVal = new JLabel("Rp0");
        lblInvoiceBalanceVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInvoiceBalanceVal.setForeground(ThemeColor.TEXT_DARK);
        cardInv.add(lblInvTitle, BorderLayout.NORTH);
        cardInv.add(lblInvoiceBalanceVal, BorderLayout.CENTER);
        statsPanel.add(cardInv);

        // Card 3: Open Tickets
        RoundedPanel cardTickets = new RoundedPanel(12);
        cardTickets.setBackground(ThemeColor.CARD_BG);
        cardTickets.setLayout(new BorderLayout());
        cardTickets.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTicketsTitle = new JLabel("Tiket Masalah Aktif");
        lblTicketsTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTicketsTitle.setForeground(ThemeColor.TEXT_MUTED);
        lblOpenTicketsVal = new JLabel("0");
        lblOpenTicketsVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblOpenTicketsVal.setForeground(ThemeColor.TEXT_DARK);
        cardTickets.add(lblTicketsTitle, BorderLayout.NORTH);
        cardTickets.add(lblOpenTicketsVal, BorderLayout.CENTER);
        statsPanel.add(cardTickets);

        gbc.gridy = 0;
        center.add(statsPanel, gbc);

        // Feedback Card (FR 17)
        RoundedPanel cardFeedback = new RoundedPanel(15);
        cardFeedback.setBackground(ThemeColor.CARD_BG);
        cardFeedback.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        cardFeedback.setLayout(new GridBagLayout());
        GridBagConstraints gbcF = new GridBagConstraints();
        gbcF.fill = GridBagConstraints.HORIZONTAL;
        gbcF.gridx = 0;
        gbcF.insets = new Insets(5, 5, 5, 5);

        JLabel lblFeedbackTitle = new JLabel("Feedback Layanan & Kepuasan Proyek");
        lblFeedbackTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFeedbackTitle.setForeground(ThemeColor.ACCENT_GREEN);
        gbcF.gridy = 0;
        cardFeedback.add(lblFeedbackTitle, gbcF);

        JLabel lblFeedbackSub = new JLabel("Jika proyek Anda selesai, berikan feedback dan ulasan mengenai kinerja kami.");
        lblFeedbackSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFeedbackSub.setForeground(ThemeColor.TEXT_MUTED);
        gbcF.gridy = 1;
        cardFeedback.add(lblFeedbackSub, gbcF);

        // Rating Star Simulation Dropdown
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        ratingPanel.setOpaque(false);
        ratingPanel.add(new JLabel("Rating Kepuasan: "));
        JComboBox<String> cbRating = new JComboBox<>(new String[]{
            "\u2605\u2605\u2605\u2605\u2605 (Sangat Puas)",
            "\u2605\u2605\u2605\u2605\u2606 (Puas)",
            "\u2605\u2605\u2605\u2606\u2606 (Cukup)",
            "\u2605\u2605\u2606\u2606\u2606 (Kurang)",
            "\u2605\u2606\u2606\u2606\u2606 (Kecewa)"
        });
        cbRating.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ratingPanel.add(cbRating);
        gbcF.gridy = 2;
        cardFeedback.add(ratingPanel, gbcF);

        JTextArea taFeedback = new JTextArea(3, 30);
        taFeedback.setLineWrap(true);
        taFeedback.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taFeedback.setBorder(BorderFactory.createLineBorder(ThemeColor.DIVIDER));
        JScrollPane spFeedback = new JScrollPane(taFeedback);
        gbcF.gridy = 3;
        gbcF.weightx = 1.0;
        gbcF.fill = GridBagConstraints.BOTH;
        gbcF.insets = new Insets(5, 5, 10, 5);
        cardFeedback.add(spFeedback, gbcF);

        RoundedButton btnSendFeedback = new RoundedButton("KIRIM FEEDBACK");
        btnSendFeedback.setPreferredSize(new Dimension(140, 32));
        gbcF.gridy = 4;
        gbcF.weightx = 0;
        gbcF.fill = GridBagConstraints.NONE;
        cardFeedback.add(btnSendFeedback, gbcF);

        btnSendFeedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String review = taFeedback.getText().trim();
                if (review.isEmpty()) {
                    JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Tulis ulasan Anda terlebih dahulu.");
                    return;
                }
                String rate = (String) cbRating.getSelectedItem();
                // Add to discussion simulation
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Client (Feedback)",
                        "Memberikan Rating: " + rate + "\nUlasan: " + review,
                        dateStr
                });
                JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Terima kasih atas feedback Anda! Ulasan Anda telah diterbitkan ke tim.");
                taFeedback.setText("");
                cbRating.setSelectedIndex(0);
                refreshUI();
            }
        });

        gbc.gridy = 1;
        gbc.weighty = 0.6;
        center.add(cardFeedback, gbc);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConsultationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Pendaftaran Preferensi & Jadwal Konsultasi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        panel.add(lblTitle, BorderLayout.NORTH);

        // Main content
        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Form Preferensi JDialog/Panel
        RoundedPanel formPanel = new RoundedPanel(12);
        formPanel.setBackground(ThemeColor.CARD_BG);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.gridx = 0;
        gbcForm.insets = new Insets(5, 5, 5, 5);

        JLabel lblFormHeader = new JLabel("Buat Jadwal & Isi Preferensi Desain Ruangan");
        lblFormHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFormHeader.setForeground(ThemeColor.ACCENT_GREEN);
        gbcForm.gridy = 0;
        formPanel.add(lblFormHeader, gbcForm);

        // Preferensi gaya
        JPanel prefStylePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        prefStylePanel.setOpaque(false);
        prefStylePanel.add(new JLabel("Gaya Ruangan: "));
        JComboBox<String> cbPrefStyle = new JComboBox<>(new String[]{"Modern Japandi", "Minimalist Modern", "Industrial Chic", "Scandinavian Comfort", "Classic Elegant"});
        cbPrefStyle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prefStylePanel.add(cbPrefStyle);
        gbcForm.gridy = 1;
        formPanel.add(prefStylePanel, gbcForm);

        // Jam Konsultasi
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timePanel.setOpaque(false);
        timePanel.add(new JLabel("Pilih Jam Konsultasi: "));
        JComboBox<String> cbPrefTime = new JComboBox<>(new String[]{"09:00 WIB", "11:00 WIB", "13:30 WIB", "15:30 WIB"});
        cbPrefTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timePanel.add(cbPrefTime);
        gbcForm.gridy = 2;
        formPanel.add(timePanel, gbcForm);

        RoundedButton btnSubmitPref = new RoundedButton("KIRIM PREFERENSI & BOOKING");
        btnSubmitPref.setPreferredSize(new Dimension(200, 32));
        gbcForm.gridy = 3;
        gbcForm.insets = new Insets(10, 5, 5, 5);
        formPanel.add(btnSubmitPref, gbcForm);

        btnSubmitPref.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String style = (String) cbPrefStyle.getSelectedItem();
                String timeVal = (String) cbPrefTime.getSelectedItem();
                int nextSchedId = 500 + parentFrame.getScheduleList().size() + 1;
                int nextConsultId = 600 + parentFrame.getConsultationList().size() + 1;
                
                // Add schedule & consultation
                Schedule newSched = new Schedule(nextSchedId, new Date(System.currentTimeMillis() + 86400000), timeVal, "Scheduled");
                parentFrame.getScheduleList().add(newSched);
                
                Consultation newConsult = new Consultation(nextConsultId, newSched.getDate(), "Pending");
                parentFrame.getConsultationList().add(newConsult);

                // Add message to chat discussion
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Client",
                        "Telah booking konsultasi #" + nextConsultId + " dengan preferensi gaya: " + style + " pada " + timeVal + ". Mohon konfirmasi jadwal oleh Interior Design.",
                        dateStr
                });

                JOptionPane.showMessageDialog(ClientDashboardPanel.this, 
                        "Sukses booking konsultasi!\nID Konsultasi: " + nextConsultId + "\nGaya Preferensi: " + style, 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshUI();
            }
        });

        gbc.gridy = 0;
        gbc.weighty = 0.4;
        body.add(formPanel, gbc);

        // Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel lblTableTitle = new JLabel("Riwayat Konsultasi Aktif");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTableTitle.setForeground(ThemeColor.TEXT_DARK);
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);

        tblConsultations = new CustomTable();
        JScrollPane sp = new JScrollPane(tblConsultations);
        tablePanel.add(sp, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.weighty = 0.6;
        body.add(tablePanel, gbc);

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Detail Proyek Desain & Persetujuan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        panel.add(lblTitle, BorderLayout.NORTH);

        RoundedPanel cardProj = new RoundedPanel(15);
        cardProj.setBackground(ThemeColor.CARD_BG);
        cardProj.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        cardProj.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel lblHeader = new JLabel("Proyek Desain Ruangan Aktif");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeader.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        cardProj.add(lblHeader, gbc);

        JPanel details = new JPanel(new GridLayout(5, 2, 10, 10));
        details.setOpaque(false);

        details.add(new JLabel("ID Proyek Desain:"));
        lblProjId = new JLabel("-");
        lblProjId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        details.add(lblProjId);

        details.add(new JLabel("Konsep / Gaya:"));
        lblProjStyle = new JLabel("-");
        lblProjStyle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        details.add(lblProjStyle);

        details.add(new JLabel("Estimasi Anggaran (Budget):"));
        lblProjBudget = new JLabel("-");
        lblProjBudget.setFont(new Font("Segoe UI", Font.BOLD, 12));
        details.add(lblProjBudget);

        details.add(new JLabel("Jumlah Revisi Diajukan:"));
        lblProjRevisions = new JLabel("-");
        details.add(lblProjRevisions);

        details.add(new JLabel("Status Persetujuan:"));
        lblProjStatus = new JLabel("-");
        lblProjStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        details.add(lblProjStatus);

        gbc.gridy = 1;
        cardProj.add(details, gbc);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actions.setOpaque(false);

        btnApproveProject = new RoundedButton("SETUJUI DESAIN (APPROVE)");
        btnApproveProject.setPreferredSize(new Dimension(190, 36));
        btnRequestRevision = new RoundedButton("AJUKAN REVISI");
        btnRequestRevision.setColors(new Color(130, 115, 105), new Color(150, 135, 125));
        btnRequestRevision.setPreferredSize(new Dimension(140, 36));

        actions.add(btnApproveProject);
        actions.add(btnRequestRevision);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        cardProj.add(actions, gbc);

        // Action Handlers
        btnApproveProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentFrame.getDesignProjectList().isEmpty()) return;
                DesignProject dp = parentFrame.getDesignProjectList().get(parentFrame.getDesignProjectList().size() - 1);
                dp.approve();
                
                // Add discussion log
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[]{
                        parentFrame.getLoggedInUser().getName(),
                        "Client",
                        "Telah MENYETUJUI desain proyek #" + dp.getDesignId() + " (" + dp.getConceptStyle() + ")",
                        dateStr
                });

                JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Desain proyek berhasil disetujui! Status berubah menjadi Approved.");
                refreshUI();
            }
        });

        btnRequestRevision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentFrame.getDesignProjectList().isEmpty()) return;
                DesignProject dp = parentFrame.getDesignProjectList().get(parentFrame.getDesignProjectList().size() - 1);

                String revisionNote = JOptionPane.showInputDialog(ClientDashboardPanel.this, 
                        "Masukkan instruksi/catatan revisi desain:", 
                        "Form Revisi Desain", JOptionPane.QUESTION_MESSAGE);
                if (revisionNote != null && !revisionNote.trim().isEmpty()) {
                    dp.updateDesign(revisionNote);
                    
                    // Log
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[]{
                            parentFrame.getLoggedInUser().getName(),
                            "Client",
                            "Mengajukan Revisi #" + dp.getRevisionCount() + " dengan catatan: " + revisionNote,
                            dateStr
                    });

                    JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Revisi berhasil diajukan!");
                    refreshUI();
                }
            }
        });

        panel.add(cardProj, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInvoicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel("Rincian Billing & Pembayaran");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        top.add(lblTitle, BorderLayout.WEST);

        RoundedButton btnPay = new RoundedButton("BAYAR TAGIHAN (MAKE PAYMENT)");
        btnPay.setPreferredSize(new Dimension(240, 32));
        top.add(btnPay, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        tblInvoices = new CustomTable();
        JScrollPane sp = new JScrollPane(tblInvoices);
        panel.add(sp, BorderLayout.CENTER);

        btnPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblInvoices.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ClientDashboardPanel.this, 
                            "Pilih salah satu baris tagihan pada tabel untuk dibayar.", 
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String invoiceId = (String) tblInvoices.getValueAt(selectedRow, 0);
                Invoice targetInv = null;
                for (Invoice inv : parentFrame.getInvoiceList()) {
                    if (inv.getInvoiceId().equals(invoiceId)) {
                        targetInv = inv;
                        break;
                    }
                }

                if (targetInv == null) return;
                if (targetInv.getStatus().equalsIgnoreCase("Paid")) {
                    JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Faktur ini sudah Lunas.");
                    return;
                }

                // Payment popup dialog
                String[] methods = {"Transfer Bank BCA", "Transfer Bank Mandiri", "Kartu Kredit", "E-Wallet (OVO/GoPay)"};
                String method = (String) JOptionPane.showInputDialog(ClientDashboardPanel.this,
                        "Pilih Metode Pembayaran:", "Pembayaran",
                        JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);

                if (method != null) {
                    double outstandingAmount = targetInv.calculateTotal() - targetInv.getAmountPaid();
                    String amtStr = JOptionPane.showInputDialog(ClientDashboardPanel.this, 
                            "Masukkan nominal pembayaran (Sisa tagihan: " + rpFormat.format(outstandingAmount) + "):", 
                            outstandingAmount);
                    if (amtStr != null) {
                        try {
                            double amountToPay = Double.parseDouble(amtStr);
                            if (amountToPay <= 0) {
                                JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Nominal tidak valid.");
                                return;
                            }
                            
                            Client client = (Client) parentFrame.getLoggedInUser();
                            Payment payment = client.makePayment(targetInv, amountToPay, method);
                            
                            // Add payment to simulator list
                            parentFrame.getPaymentList().add(payment);

                            // Add chat status
                            String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                            parentFrame.getDiscussionMessages().add(new String[]{
                                    client.getName(),
                                    "Client (Billing)",
                                    "Telah mengirim Pembayaran (" + payment.getPaymentId() + ") sebesar " + rpFormat.format(amountToPay) + " via " + method + ". Menunggu verifikasi Finance.",
                                    dateStr
                            });

                            JOptionPane.showMessageDialog(ClientDashboardPanel.this, 
                                    "Pembayaran dikirim!\nID Pembayaran: " + payment.getPaymentId() + "\nStatus: Menunggu Verifikasi", 
                                    "Pembayaran Berhasil", JOptionPane.INFORMATION_MESSAGE);
                            refreshUI();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(ClientDashboardPanel.this, "Nominal harus berupa angka.");
                        }
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel("Layanan Keluhan & Tiket Masalah");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        top.add(lblTitle, BorderLayout.WEST);

        RoundedButton btnCreateTicket = new RoundedButton("PENGAJUAN MASALAH BARU");
        btnCreateTicket.setPreferredSize(new Dimension(200, 32));
        top.add(btnCreateTicket, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        tblTickets = new CustomTable();
        JScrollPane sp = new JScrollPane(tblTickets);
        panel.add(sp, BorderLayout.CENTER);

        btnCreateTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String complaint = JOptionPane.showInputDialog(ClientDashboardPanel.this, 
                        "Jelaskan detail masalah/keluhan ruangan Anda secara lengkap:", 
                        "Ajukan Keluhan Baru", JOptionPane.QUESTION_MESSAGE);
                if (complaint != null && !complaint.trim().isEmpty()) {
                    Client client = (Client) parentFrame.getLoggedInUser();
                    TicketProblem newTicket = client.submitProblem(complaint);
                    newTicket.createTicket();
                    
                    // Add to system state
                    parentFrame.getTicketList().add(newTicket);
                    
                    // Log to chat
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[]{
                            client.getName(),
                            "Client",
                            "Telah mengajukan tiket keluhan baru (" + newTicket.getTicketId() + "): \"" + complaint + "\"",
                            dateStr
                    });

                    JOptionPane.showMessageDialog(ClientDashboardPanel.this, 
                            "Tiket berhasil dibuat!\nID Tiket: " + newTicket.getTicketId(), 
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    refreshUI();
                }
            }
        });

        return panel;
    }

    private JPanel createDiscussionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Ruang Diskusi & Kolaborasi Proyek");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Chat Area
        taChat = new JTextArea();
        taChat.setEditable(false);
        taChat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taChat.setLineWrap(true);
        taChat.setWrapStyleWord(true);
        JScrollPane spChat = new JScrollPane(taChat);
        panel.add(spChat, BorderLayout.CENTER);

        // Input Area
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

        // Send logic
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtChatMsg.getText().trim();
                if (msg.isEmpty()) return;

                String senderName = parentFrame.getLoggedInUser().getName();
                String senderRole = "Client";
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
