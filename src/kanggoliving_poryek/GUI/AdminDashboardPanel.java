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
import kanggoliving_poryek.users.Admin;
import kanggoliving_poryek.users.User;
import kanggoliving_poryek.users.Client;
import kanggoliving_poryek.users.Technician;
import kanggoliving_poryek.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import kanggoliving_poryek.Database.Koneksi;

public class AdminDashboardPanel extends JPanel {
    private MainFrame parentFrame;
    private CardLayout rightLayout;
    private JPanel rightContainer;

    // Sidebar buttons
    private SidebarButton btnMenuOverview;
    private SidebarButton btnMenuUsers;
    private SidebarButton btnMenuApprovals;
    private SidebarButton btnMenuFinancials;
    private SidebarButton btnMenuTickets;
    private SidebarButton btnMenuPortfolio;
    private SidebarButton btnMenuDiscussion;

    // Header sub-role label
    private JLabel lblAdminRole;
    private JLabel lblOverviewWelcome;

    // Overview Stats
    private JLabel lblStatUsers;
    private JLabel lblStatRevenue;
    private JLabel lblStatTickets;

    // Tables
    private CustomTable tblUsers;
    private CustomTable tblApprovals;
    private CustomTable tblPayments;
    private CustomTable tblVendors;
    private CustomTable tblTickets;

    // Portfolio list
    private JList<String> listPortfolio;

    // Chat UI
    private JTextArea taChat;
    private JTextField txtChatMsg;

    private NumberFormat rpFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

    public AdminDashboardPanel(MainFrame parent) {
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

        lblAdminRole = new JLabel("Admin Dashboard");
        lblAdminRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAdminRole.setForeground(ThemeColor.TEXT_MUTED);
        lblAdminRole.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));
        sidebar.add(lblAdminRole);

        btnMenuOverview = new SidebarButton("Overview Metrics");
        btnMenuUsers = new SidebarButton("User Management");
        btnMenuApprovals = new SidebarButton("Approvals");
        btnMenuFinancials = new SidebarButton("Financials");
        btnMenuTickets = new SidebarButton("Ticket Problem Tracker");
        btnMenuPortfolio = new SidebarButton("Portfolio");
        btnMenuDiscussion = new SidebarButton("Ruang Diskusi");

        sidebar.add(btnMenuOverview);
        sidebar.add(btnMenuUsers);
        sidebar.add(btnMenuApprovals);
        sidebar.add(btnMenuFinancials);
        sidebar.add(btnMenuTickets);
        sidebar.add(btnMenuPortfolio);
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

        // Add Screens
        rightContainer.add(createOverviewPanel(), "OVERVIEW");
        rightContainer.add(createUserManagementPanel(), "USERS");
        rightContainer.add(createApprovalsPanel(), "APPROVALS");
        rightContainer.add(createFinancialsPanel(), "FINANCIALS");
        rightContainer.add(createTicketsPanel(), "TICKETS");
        rightContainer.add(createPortfolioPanel(), "PORTFOLIO");
        rightContainer.add(createDiscussionPanel(), "DISCUSSION");

        add(rightContainer, BorderLayout.CENTER);

        // Listeners
        btnMenuOverview.addActionListener(e -> switchSubScreen("OVERVIEW", btnMenuOverview));
        btnMenuUsers.addActionListener(e -> switchSubScreen("USERS", btnMenuUsers));
        btnMenuApprovals.addActionListener(e -> switchSubScreen("APPROVALS", btnMenuApprovals));
        btnMenuFinancials.addActionListener(e -> switchSubScreen("FINANCIALS", btnMenuFinancials));
        btnMenuTickets.addActionListener(e -> switchSubScreen("TICKETS", btnMenuTickets));
        btnMenuPortfolio.addActionListener(e -> switchSubScreen("PORTFOLIO", btnMenuPortfolio));
        btnMenuDiscussion.addActionListener(e -> switchSubScreen("DISCUSSION", btnMenuDiscussion));

        btnLogout.addActionListener(e -> parentFrame.logout());

        btnMenuOverview.setActive(true);
    }

    private void switchSubScreen(String screenName, SidebarButton activeButton) {
        // Enforce sub-role permissions
        String subRole = parentFrame.getLoggedSubRole();
        if (subRole != null && subRole.equalsIgnoreCase("Finance")) {
            if (screenName.equals("APPROVALS") || screenName.equals("TICKETS") || screenName.equals("PORTFOLIO")) {
                JOptionPane.showMessageDialog(this,
                        "Akses Ditolak!\nMenu ini hanya dapat diakses oleh Project Manager.",
                        "Akses Terbatas", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if (subRole != null && subRole.equalsIgnoreCase("Project Manager")) {
            if (screenName.equals("FINANCIALS")) {
                // Let PM view financials but we can restrict payments verification to Finance.
                // We'll handle this restriction inside the Financials screen itself.
            }
        }

        rightLayout.show(rightContainer, screenName);
        btnMenuOverview.setActive(false);
        btnMenuUsers.setActive(false);
        btnMenuApprovals.setActive(false);
        btnMenuFinancials.setActive(false);
        btnMenuTickets.setActive(false);
        btnMenuPortfolio.setActive(false);
        btnMenuDiscussion.setActive(false);
        activeButton.setActive(true);
        refreshUI();
    }

    public void refreshUI() {
        if (parentFrame.getLoggedInUser() == null)
            return;
        Admin admin = (Admin) parentFrame.getLoggedInUser();
        String subRole = parentFrame.getLoggedSubRole();

        lblAdminRole.setText("Admin - " + subRole);
        lblOverviewWelcome.setText("Selamat Datang, " + admin.getName() + " (" + subRole + ")");

        // Calculate Overview Statistics
        int totalUsers = parentFrame.getClientList().size() + parentFrame.getAdminList().size()
                + parentFrame.getTechnicianList().size();
        lblStatUsers.setText(String.valueOf(totalUsers));

        double totalRevenue = 0;
        for (Payment pay : parentFrame.getPaymentList()) {
            if (pay.getStatus().equalsIgnoreCase("Succes")) {
                totalRevenue += pay.getAmount();
            }
        }
        lblStatRevenue.setText(rpFormat.format(totalRevenue));

        int totalTickets = parentFrame.getTicketList().size();
        lblStatTickets.setText(String.valueOf(totalTickets));

        // 1. User Management Screen Table
        DefaultTableModel modelUsers = new DefaultTableModel(
                new String[] { "ID User", "Nama", "Email", "No. Telepon", "Peran / Detail" }, 0);
        for (Client c : parentFrame.getClientList()) {
            modelUsers.addRow(new Object[] { c.getUserId(), c.getName(), c.getEmail(), c.getPhone(),
                    "Client - " + c.getAddress() });
        }
        for (Admin a : parentFrame.getAdminList()) {
            modelUsers.addRow(
                    new Object[] { a.getUserId(), a.getName(), a.getEmail(), a.getPhone(), "Admin - " + a.getRole() });
        }
        for (Technician t : parentFrame.getTechnicianList()) {
            modelUsers.addRow(new Object[] { t.getUserId(), t.getName(), t.getEmail(), t.getPhone(),
                    "Technician - " + t.getSpecialization() });
        }
        tblUsers.setModel(modelUsers);

        // 2. Approvals Screen Table
        DefaultTableModel modelAppr = new DefaultTableModel(new String[] { "ID Desain", "ID Konsultasi", "Konsep Gaya",
                "Estimasi Budget", "Jumlah Revisi", "Status" }, 0);
        for (DesignProject dp : parentFrame.getDesignProjectList()) {
            modelAppr.addRow(new Object[] {
                    dp.getDesignId(),
                    dp.getConsultationId(),
                    dp.getConceptStyle(),
                    rpFormat.format(dp.getEstimatedBudget()),
                    dp.getRevisionCount(),
                    dp.getStatus()
            });

        }
        tblApprovals.setModel(modelAppr);

        // 3. Financials Screen - Payments Table
        DefaultTableModel modelPay = new DefaultTableModel(new String[] { "ID Pembayaran", "ID Invoice",
                "Nominal Pembayaran", "Metode", "Tanggal Bayar", "Status" }, 0);
        for (Payment pay : parentFrame.getPaymentList()) {
            modelPay.addRow(new Object[] {
                    pay.getPaymentId(),
                    pay.getTargetInvoice().getInvoiceId(),
                    rpFormat.format(pay.getAmount()),
                    pay.getPaymentMethod(),
                    pay.getPaymentDate(),
                    pay.getStatus()
            });
        }
        tblPayments.setModel(modelPay);

        // 3b. Financials Screen - Vendors Table
        DefaultTableModel modelVendors = new DefaultTableModel(
                new String[] { "ID Vendor", "Nama Vendor", "Kategori", "Kontak" }, 0);
        for (String[] v : parentFrame.getVendorList()) {
            modelVendors.addRow(v);
        }
        tblVendors.setModel(modelVendors);

        // 4. Tickets Table
        DefaultTableModel modelTickets = new DefaultTableModel(
                new String[] { "ID Tiket", "Deskripsi Keluhan", "Status Tiket", "Tanggal Keluhan" }, 0);
        for (TicketProblem t : parentFrame.getTicketList()) {
            modelTickets.addRow(new Object[] {
                    t.getTicketId() != null ? t.getTicketId() : "Draft",
                    t.getProblemDescription(),
                    t.getStatus(),
                    t.getCreateDate() != null ? t.getCreateDate() : "-"
            });
        }
        tblTickets.setModel(modelTickets);

        // 5. Discussion Screen Text Area
        StringBuilder sb = new StringBuilder();
        for (String[] msg : parentFrame.getDiscussionMessages()) {
            sb.append("[").append(msg[3]).append("] ").append(msg[0]).append(" (").append(msg[1]).append("):\n")
                    .append(msg[2]).append("\n\n");
        }
        taChat.setText(sb.toString());
    }

    private void loadUsersFromDatabase() {

        DefaultTableModel modelUsers = new DefaultTableModel(
                new String[] { "ID User", "Nama", "Email", "No. Telepon", "Peran / Detail" }, 0);

        try {

            Connection conn = Koneksi.getConnection();

            String sql = "SELECT * FROM users";

            PreparedStatement pst = conn.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String detail = "";

                String role = rs.getString("role");

                if (role.equalsIgnoreCase("Client")) {

                    detail = "Client - " + rs.getString("address");

                } else if (role.equalsIgnoreCase("Admin")) {

                    detail = "Admin - " + rs.getString("sub_role");

                } else {

                    detail = "Technician - " + rs.getString("specialization");

                }

                modelUsers.addRow(new Object[] {
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        detail
                });

            }

            tblUsers.setModel(modelUsers);

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);

        }

    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblOverviewWelcome = new JLabel("Selamat Datang, Admin!");
        lblOverviewWelcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblOverviewWelcome.setForeground(ThemeColor.TEXT_DARK);
        panel.add(lblOverviewWelcome, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Card 1: Users
        RoundedPanel card1 = new RoundedPanel(12);
        card1.setBackground(ThemeColor.CARD_BG);
        card1.setLayout(new BorderLayout());
        card1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle1 = new JLabel("Total Pengguna Sistem");
        lblTitle1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle1.setForeground(ThemeColor.TEXT_MUTED);
        lblStatUsers = new JLabel("0");
        lblStatUsers.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblStatUsers.setForeground(ThemeColor.TEXT_DARK);
        card1.add(lblTitle1, BorderLayout.NORTH);
        card1.add(lblStatUsers, BorderLayout.CENTER);
        cardsPanel.add(card1);

        // Card 2: Revenue
        RoundedPanel card2 = new RoundedPanel(12);
        card2.setBackground(ThemeColor.CARD_BG);
        card2.setLayout(new BorderLayout());
        card2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle2 = new JLabel("Pendapatan Keuangan (Lunas)");
        lblTitle2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle2.setForeground(ThemeColor.TEXT_MUTED);
        lblStatRevenue = new JLabel("Rp0");
        lblStatRevenue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblStatRevenue.setForeground(ThemeColor.ACCENT_GREEN);
        card2.add(lblTitle2, BorderLayout.NORTH);
        card2.add(lblStatRevenue, BorderLayout.CENTER);
        cardsPanel.add(card2);

        // Card 3: Tickets
        RoundedPanel card3 = new RoundedPanel(12);
        card3.setBackground(ThemeColor.CARD_BG);
        card3.setLayout(new BorderLayout());
        card3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitle3 = new JLabel("Total Tiket Masalah");
        lblTitle3.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle3.setForeground(ThemeColor.TEXT_MUTED);
        lblStatTickets = new JLabel("0");
        lblStatTickets.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblStatTickets.setForeground(ThemeColor.TEXT_DARK);
        card3.add(lblTitle3, BorderLayout.NORTH);
        card3.add(lblStatTickets, BorderLayout.CENTER);
        cardsPanel.add(card3);

        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Manajemen Data Pengguna (Users)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        panel.add(lblTitle, BorderLayout.NORTH);

        tblUsers = new CustomTable();
        panel.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createApprovalsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel("Persetujuan Proyek & Konsultasi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        top.add(lblTitle, BorderLayout.WEST);

        RoundedButton btnApprove = new RoundedButton("SETUJUI PROYEK (APPROVE)");
        btnApprove.setPreferredSize(new Dimension(210, 32));
        top.add(btnApprove, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        tblApprovals = new CustomTable();
        panel.add(new JScrollPane(tblApprovals), BorderLayout.CENTER);

        // Approval handler (Project Manager)
        btnApprove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subRole = parentFrame.getLoggedSubRole();
                if (!subRole.equalsIgnoreCase("Project Manager") && !subRole.equalsIgnoreCase("Manager")) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                            "Akses Ditolak! Hanya Project Manager yang dapat menyetujui proyek desain.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int row = tblApprovals.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Pilih salah satu baris proyek.");
                    return;
                }

                int designId = (Integer) tblApprovals.getValueAt(row, 0);
                for (DesignProject dp : parentFrame.getDesignProjectList()) {
                    if (dp.getDesignId() == designId) {
                        dp.approve();

                        // Send Chat Alert
                        String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                        parentFrame.getDiscussionMessages().add(new String[] {
                                parentFrame.getLoggedInUser().getName(),
                                "Project Manager",
                                "Telah menyetujui (Approved) Design Project ID #" + designId
                                        + " untuk segera diproduksi.",
                                dateStr
                        });

                        JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                                "Design Project #" + designId + " berhasil disetujui!");
                        refreshUI();
                        return;
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createFinancialsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 20));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Part 1: Payments Validation
        JPanel payPanel = new JPanel(new BorderLayout(0, 10));
        payPanel.setOpaque(false);

        JPanel payHeader = new JPanel(new BorderLayout());
        payHeader.setOpaque(false);
        JLabel lblPayTitle = new JLabel("Verifikasi Pembayaran Transaksi");
        lblPayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPayTitle.setForeground(ThemeColor.TEXT_DARK);
        payHeader.add(lblPayTitle, BorderLayout.WEST);

        RoundedButton btnVerify = new RoundedButton("VERIFIKASI PEMBAYARAN");
        btnVerify.setPreferredSize(new Dimension(200, 28));
        payHeader.add(btnVerify, BorderLayout.EAST);
        payPanel.add(payHeader, BorderLayout.NORTH);

        tblPayments = new CustomTable();
        payPanel.add(new JScrollPane(tblPayments), BorderLayout.CENTER);
        panel.add(payPanel);

        // Part 2: Vendor Management (FR 20)
        JPanel vendorPanel = new JPanel(new BorderLayout(0, 10));
        vendorPanel.setOpaque(false);

        JPanel vendHeader = new JPanel(new BorderLayout());
        vendHeader.setOpaque(false);
        JLabel lblVendTitle = new JLabel("Kelola Data Vendor Supplier");
        lblVendTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblVendTitle.setForeground(ThemeColor.TEXT_DARK);
        vendHeader.add(lblVendTitle, BorderLayout.WEST);

        RoundedButton btnAddVendor = new RoundedButton("TAMBAH VENDOR BARU");
        btnAddVendor.setPreferredSize(new Dimension(180, 28));
        vendHeader.add(btnAddVendor, BorderLayout.EAST);
        vendorPanel.add(vendHeader, BorderLayout.NORTH);

        tblVendors = new CustomTable();
        vendorPanel.add(new JScrollPane(tblVendors), BorderLayout.CENTER);
        panel.add(vendorPanel);

        // Actions
        btnVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subRole = parentFrame.getLoggedSubRole();
                if (!subRole.equalsIgnoreCase("Finance")) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                            "Akses Ditolak! Hanya sub-role Finance yang dapat memverifikasi pembayaran.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int row = tblPayments.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Pilih salah satu baris pembayaran.");
                    return;
                }

                String paymentId = (String) tblPayments.getValueAt(row, 0);
                for (Payment pay : parentFrame.getPaymentList()) {
                    if (pay.getPaymentId().equals(paymentId)) {
                        if (pay.getStatus().equalsIgnoreCase("Succes")) {
                            JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                                    "Pembayaran ini sudah terverifikasi.");
                            return;
                        }

                        Admin admin = (Admin) parentFrame.getLoggedInUser();
                        boolean verified = admin.verifyPayment(pay);
                        if (verified) {
                            // Send chat notification
                            String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                            parentFrame.getDiscussionMessages().add(new String[] {
                                    admin.getName(),
                                    "Finance",
                                    "Telah memverifikasi pembayaran " + paymentId + " sebesar "
                                            + rpFormat.format(pay.getAmount()) + " untuk Invoice "
                                            + pay.getTargetInvoice().getInvoiceId() + " (LUNAS/PARTIAL)",
                                    dateStr
                            });

                            JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                                    "Pembayaran " + paymentId + " berhasil diverifikasi!");
                            refreshUI();
                        }
                        return;
                    }
                }
            }
        });

        btnAddVendor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField txtName = new JTextField();
                JTextField txtCat = new JTextField();
                JTextField txtContact = new JTextField();
                Object[] form = {
                        "Nama Vendor:", txtName,
                        "Kategori:", txtCat,
                        "Kontak/No. Telp:", txtContact
                };
                int option = JOptionPane.showConfirmDialog(AdminDashboardPanel.this, form, "Tambah Vendor Baru",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = txtName.getText().trim();
                    String cat = txtCat.getText().trim();
                    String contact = txtContact.getText().trim();
                    if (name.isEmpty() || cat.isEmpty() || contact.isEmpty()) {
                        JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Semua input vendor wajib diisi.");
                        return;
                    }
                    String nextId = "VND-00" + (parentFrame.getVendorList().size() + 1);
                    parentFrame.getVendorList().add(new String[] { nextId, name, cat, contact });

                    // Log
                    String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                    parentFrame.getDiscussionMessages().add(new String[] {
                            parentFrame.getLoggedInUser().getName(),
                            parentFrame.getLoggedSubRole(),
                            "Menambahkan Vendor baru: " + name + " (" + cat + ")",
                            dateStr
                    });

                    JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Vendor berhasil ditambahkan!");
                    refreshUI();
                }
            }
        });

        return panel;
    }

    private JPanel createTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lblTitle = new JLabel("Keluhan Layanan Pelanggan (Ticket Problem)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        top.add(lblTitle, BorderLayout.WEST);

        RoundedButton btnAssign = new RoundedButton("TANGGAPI / PROSES TIKET");
        btnAssign.setPreferredSize(new Dimension(200, 32));
        top.add(btnAssign, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        tblTickets = new CustomTable();
        panel.add(new JScrollPane(tblTickets), BorderLayout.CENTER);

        btnAssign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblTickets.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Pilih tiket yang ingin ditanggapi.");
                    return;
                }
                String ticketDesc = (String) tblTickets.getValueAt(row, 1);
                String currentStatus = (String) tblTickets.getValueAt(row, 2);

                if (currentStatus.equalsIgnoreCase("Resolved")) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this, "Tiket ini sudah diselesaikan.");
                    return;
                }

                // PM assigns or marks ticket as In Progress
                String[] statusOptions = { "In Progress", "Resolved" };
                String selectedStatus = (String) JOptionPane.showInputDialog(AdminDashboardPanel.this,
                        "Perbarui Status Tiket:", "Update Tiket",
                        JOptionPane.QUESTION_MESSAGE, null, statusOptions, statusOptions[0]);

                if (selectedStatus != null) {
                    for (TicketProblem t : parentFrame.getTicketList()) {
                        if (t.getProblemDescription().equals(ticketDesc)) {
                            t.updateStatus(selectedStatus);

                            // Log
                            String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                            parentFrame.getDiscussionMessages().add(new String[] {
                                    parentFrame.getLoggedInUser().getName(),
                                    "Project Manager",
                                    "Mengubah status tiket keluhan menjadi: " + selectedStatus,
                                    dateStr
                            });

                            JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                                    "Status tiket berhasil diperbarui.");
                            refreshUI();
                            return;
                        }
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPart = new JPanel(new BorderLayout());
        leftPart.setOpaque(false);
        leftPart.setPreferredSize(new Dimension(280, 0));

        JLabel lblListTitle = new JLabel("Daftar Portofolio Desain");
        lblListTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblListTitle.setForeground(ThemeColor.TEXT_DARK);
        lblListTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        leftPart.add(lblListTitle, BorderLayout.NORTH);

        String[] items = {
                "Modern Japandi Kitchen (2025)",
                "Minimalist Nordic Living Room (2025)",
                "Industrial Studio Loft (2026)",
                "Scandinavian Bedroom Comfort (2026)",
                "Classic Royal Dining Room (2026)"
        };
        listPortfolio = new JList<>(items);
        listPortfolio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        listPortfolio.setSelectionBackground(ThemeColor.STATUS_SUCCESS_BG);
        listPortfolio.setSelectionForeground(ThemeColor.TEXT_DARK);
        listPortfolio.setSelectedIndex(0);
        leftPart.add(new JScrollPane(listPortfolio), BorderLayout.CENTER);
        panel.add(leftPart, BorderLayout.WEST);

        // Portfolio Detail Panel
        RoundedPanel detailCard = new RoundedPanel(15);
        detailCard.setBackground(ThemeColor.CARD_BG);
        detailCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        detailCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblDetailHeader = new JLabel("Detail & Spesifikasi Portofolio");
        lblDetailHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDetailHeader.setForeground(ThemeColor.ACCENT_GREEN);
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        detailCard.add(lblDetailHeader, gbc);

        JTextArea taDesc = new JTextArea();
        taDesc.setEditable(false);
        taDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);
        taDesc.setText("Pilih item portofolio di sebelah kiri untuk melihat deskripsi dan perkiraan anggaran desain.");
        JScrollPane spDesc = new JScrollPane(taDesc);
        gbc.gridy = 1;
        gbc.weighty = 0.7;
        detailCard.add(spDesc, gbc);

        // PM Action: Generate PDF Contract (FR 19)
        RoundedButton btnGenContract = new RoundedButton("GENERATE KONTRAK PDF SIMULATION");
        btnGenContract.setPreferredSize(new Dimension(0, 36));
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailCard.add(btnGenContract, gbc);
        panel.add(detailCard, BorderLayout.CENTER);

        // List Selection Handler
        listPortfolio.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String sel = listPortfolio.getSelectedValue();
                if (sel != null) {
                    if (sel.startsWith("Modern Japandi")) {
                        taDesc.setText("Tipe Desain: Modern Japandi\n"
                                + "Kombinasi estetika Jepang yang minimalis dengan fungsionalitas Skandinavia yang hangat.\n"
                                + "Fitur Utama: Furniture kayu terang melengkung, pencahayaan alami maksimal, kabinet fungsional.\n"
                                + "Estimasi Biaya: Rp25.000.000 - Rp40.000.000\n"
                                + "Kesesuaian: Dapur dan Ruang Keluarga.");
                    } else if (sel.startsWith("Minimalist Nordic")) {
                        taDesc.setText("Tipe Desain: Minimalist Nordic\n"
                                + "Menonjolkan warna monokromatik dengan sentuhan aksen pastel lembut.\n"
                                + "Fitur Utama: Lantai kayu parket putih, karpet tebal bertekstur, pencahayaan indirect LED.\n"
                                + "Estimasi Biaya: Rp20.000.000 - Rp30.000.000.");
                    } else if (sel.startsWith("Industrial Studio")) {
                        taDesc.setText("Tipe Desain: Industrial Studio Loft\n"
                                + "Mengusung konsep exposed material dengan dinding semen unpolished dan aksen pipa hitam.\n"
                                + "Fitur Utama: Rak besi hitam, lampu gantung edison, partisi kaca berbingkai logam.\n"
                                + "Estimasi Biaya: Rp35.000.000 - Rp55.000.000.");
                    } else {
                        taDesc.setText("Tipe Desain: Kustom Modern\n"
                                + "Desain kontemporer yang disesuaikan dengan luas denah ruangan dan preferensi warna klien.\n"
                                + "Estimasi Biaya: Fleksibel sesuai anggaran klien.");
                    }
                }
            }
        });

        // Generate contract click
        btnGenContract.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subRole = parentFrame.getLoggedSubRole();
                if (!subRole.equalsIgnoreCase("Project Manager")) {
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                            "Akses Ditolak! Hanya Project Manager yang dapat menyusun dokumen kontrak.",
                            "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Generate simulated contract text
                String clientName = parentFrame.getClientList().get(0).getName();
                String contractText = "====================================================\n"
                        + "                 KONTRAK KERJASAMA DESAIN & INSTALASI\n"
                        + "                       KANGGOLIVING SERVICES\n"
                        + "====================================================\n\n"
                        + "Nomor Kontrak: KGL/KTR/" + System.currentTimeMillis() % 10000 + "\n"
                        + "Tanggal Terbit: " + new Date().toString() + "\n\n"
                        + "PIHAK PERTAMA: PT KanggoLiving Indonesia\n"
                        + "PIHAK KEDUA  : " + clientName + "\n\n"
                        + "PASAL 1: RUANG LINGKUP PEKERJAAN\n"
                        + "Pihak Pertama berkewajiban merancang desain tata ruang interior "
                        + "serta melakukan survey, pengadaan material, inspeksi kelayakan, "
                        + "dan instalasi perangkat sistem kelistrikan / air pada properti Pihak Kedua.\n\n"
                        + "PASAL 2: HARGA DAN CARA PEMBAYARAN\n"
                        + "Nilai Proyek disetujui sebesar Rp25.000.000 (Dua Puluh Lima Juta Rupiah)\n"
                        + "Pembayaran dilakukan bertahap:\n"
                        + "- Down Payment (DP): 40% setelah desain awal disetujui.\n"
                        + "- Pelunasan: 60% setelah instalasi rampung dan lolos inspeksi QC.\n\n"
                        + "Demikian kontrak ini disimulasikan secara sistem otomatis.\n"
                        + "====================================================\n";

                JTextArea taCont = new JTextArea(15, 45);
                taCont.setText(contractText);
                taCont.setFont(new Font("Monospaced", Font.PLAIN, 12));
                taCont.setEditable(false);

                JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                        new JScrollPane(taCont),
                        "Generate PDF Kontrak Proyek Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                // Add to discussion messages
                String dateStr = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                parentFrame.getDiscussionMessages().add(new String[] {
                        parentFrame.getLoggedInUser().getName(),
                        "Project Manager",
                        "Telah berhasil men-generate dokumen PDF Kontrak Proyek untuk " + clientName + ".",
                        dateStr
                });
                refreshUI();
            }
        });

        return panel;
    }

    private JPanel createDiscussionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColor.PASTEL_BROWN_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Ruang Diskusi Kolaborasi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(ThemeColor.TEXT_DARK);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Chat
        taChat = new JTextArea();
        taChat.setEditable(false);
        taChat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        taChat.setLineWrap(true);
        taChat.setWrapStyleWord(true);
        panel.add(new JScrollPane(taChat), BorderLayout.CENTER);

        // Input
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        txtChatMsg = new JTextField();
        txtChatMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtChatMsg.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeColor.DIVIDER, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        inputPanel.add(txtChatMsg, BorderLayout.CENTER);

        RoundedButton btnSend = new RoundedButton("KIRIM");
        btnSend.setPreferredSize(new Dimension(80, 0));
        inputPanel.add(btnSend, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        // Send
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = txtChatMsg.getText().trim();
                if (msg.isEmpty())
                    return;

                String senderName = parentFrame.getLoggedInUser().getName();
                String senderRole = "Admin - " + parentFrame.getLoggedSubRole();
                String timestamp = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());

                parentFrame.getDiscussionMessages().add(new String[] { senderName, senderRole, msg, timestamp });
                txtChatMsg.setText("");
                refreshUI();
            }
        };

        txtChatMsg.addActionListener(sendAction);
        btnSend.addActionListener(sendAction);

        return panel;
    }
}
