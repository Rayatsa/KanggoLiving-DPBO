package kanggoliving_poryek.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import kanggoliving_poryek.users.*;
import kanggoliving_poryek.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import kanggoliving_poryek.Database.Koneksi;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Simulation Shared State
    private ArrayList<Client> clientList = new ArrayList<>();
    private ArrayList<Admin> adminList = new ArrayList<>();
    private ArrayList<Technician> technicianList = new ArrayList<>();

    private ArrayList<TicketProblem> ticketList = new ArrayList<>();
    private ArrayList<Consultation> consultationList = new ArrayList<>();
    private ArrayList<DesignProject> designProjectList = new ArrayList<>();
    private ArrayList<Invoice> invoiceList = new ArrayList<>();
    private ArrayList<Payment> paymentList = new ArrayList<>();
    private ArrayList<Material> materialList = new ArrayList<>();
    private ArrayList<SystemUnit> systemUnitList = new ArrayList<>();
    private ArrayList<Schedule> scheduleList = new ArrayList<>();
    private ArrayList<Report> reportList = new ArrayList<>();
    private ArrayList<String[]> discussionMessages = new ArrayList<>(); // {senderName, senderRole, message, timestamp}
    private ArrayList<String[]> vendorList = new ArrayList<>(); // {vendorId, name, category, contact}

    // Active Session
    private User loggedInUser;
    private String loggedSubRole; // e.g., "Finance", "Project Manager", "Workshop", "Surveyor", etc.

    // Screen Panels
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ClientDashboardPanel clientDashboard;
    private AdminDashboardPanel adminDashboard;
    private TechnicianDashboardPanel technicianDashboard;

    public MainFrame() {
        setTitle("KanggoLiving Services & Project Management System");
        setSize(1024, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Populate initial mock/dummy data
        initializeMockData();

        // Layout setting
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Panels initialization
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        clientDashboard = new ClientDashboardPanel(this);
        adminDashboard = new AdminDashboardPanel(this);
        technicianDashboard = new TechnicianDashboardPanel(this);

        // Add to card layout container
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(registerPanel, "REGISTER");
        mainContainer.add(clientDashboard, "CLIENT_DASHBOARD");
        mainContainer.add(adminDashboard, "ADMIN_DASHBOARD");
        mainContainer.add(technicianDashboard, "TECHNICIAN_DASHBOARD");

        add(mainContainer);

        // Start with LOGIN screen
        showScreen("LOGIN");
    }

    private void initializeMockData() {
        // Users
        clientList.add(new Client(101, "Sidqi Maan", "sidqi@client.com", "sidqi123", "0812345", "Client", "Bandung"));

        // Admins
        adminList.add(new Admin(1, "Meliana", "meli@kanggo.com", "pass123", "08112233", "Finance"));
        adminList.add(new Admin(2, "Budi PM", "budi@kanggo.com", "pass123", "08112244", "Project Manager"));

        // Technicians
        technicianList.add(new Technician(201, "Interior Design", 301, "Raihan Yassar", "raihan@tech.com", "raihan123",
                "089999", "Technician"));
        technicianList.add(new Technician(202, "Surveyor", 302, "Denny Surveyor", "denny@tech.com", "denny123",
                "088888", "Technician"));
        technicianList.add(new Technician(203, "Workshop", 303, "Wawan Workshop", "wawan@tech.com", "wawan123",
                "087777", "Technician"));
        technicianList.add(
                new Technician(204, "Staf QC", 304, "Qori QC", "qori@tech.com", "qori123", "086666", "Technician"));
        technicianList.add(new Technician(205, "Installer", 305, "Iwan Installer", "iwan@tech.com", "iwan123", "085555",
                "Technician"));

        // Materials
        materialList.add(new Material(1, "Cat Dinding Jotun (Pastel)", 50, 150000, "In Stock"));
        materialList.add(new Material(2, "Kabel Listrik Supreme 2x1.5", 8, 45000, "Low"));
        materialList.add(new Material(3, "Lampu LED Phillips 9W", 100, 35000, "In Stock"));
        materialList.add(new Material(4, "Pipa PVC Wavin 3/4 inch", 0, 20000, "Empty"));
        materialList.add(new Material(5, "Papan MDF 18mm", 20, 180000, "In Stock"));

        // System Units
        systemUnitList.add(new SystemUnit(801, "Instalasi Listrik Kamar Utama", "Perlu Pengecekan", "SN-LSTRK-099"));
        systemUnitList.add(new SystemUnit(802, "Instalasi Air Kamar Mandi", "Rusak", "SN-WATER-102"));
        systemUnitList.add(new SystemUnit(803, "Kitchen Cabinet Fitting", "Baik", "SN-KITCH-004"));

        // Vendors
        vendorList.add(new String[] { "VND-001", "Jotun Paints Indonesia", "Cat & Pelapis", "021-555123" });
        vendorList.add(new String[] { "VND-002", "Phillips Lighting Store", "Kelistrikan & Lampu", "021-555456" });
        vendorList.add(new String[] { "VND-003", "Depo Bangunan Jaya", "Material Bangunan", "022-777888" });

        // Initial Discussion Messages
        discussionMessages
                .add(new String[] { "System", "System", "Ruang diskusi proyek dimulai.", "21-06-2026 10:00" });
        discussionMessages.add(new String[] { "Budi PM", "Project Manager",
                "Selamat datang di proyek baru. Silakan Interior Design mengunggah desain awal.", "21-06-2026 10:15" });

        // Prepopulate a basic ticket and project
        TicketProblem t = new TicketProblem("Instalasi listrik kamar utama sering korsleting dan perlu desain ulang.");
        t.createTicket();
        ticketList.add(t);

        Schedule s = new Schedule(501, new Date(System.currentTimeMillis() + 86400000), "10:00 WIB", "Scheduled");
        scheduleList.add(s);

        Consultation c = new Consultation(601, s.getDate(), "Disetujui");
        consultationList.add(c);

        DesignProject dp = new DesignProject(701, 601, "Modern Japandi", 25000000.0);
        designProjectList.add(dp);

        Invoice inv = new Invoice("INV-JAPANDI-01", 10000000.0);
        invoiceList.add(inv);
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
        if (screenName.equals("CLIENT_DASHBOARD")) {
            clientDashboard.refreshUI();
        } else if (screenName.equals("ADMIN_DASHBOARD")) {
            adminDashboard.refreshUI();
        } else if (screenName.equals("TECHNICIAN_DASHBOARD")) {
            technicianDashboard.refreshUI();
        }
    }

    public boolean attemptLogin(String email, String password, String selectedRole) {
        try {
            Connection conn = Koneksi.getConnection();

            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String dbRole = rs.getString("role");

                System.out.println("Role Database = " + dbRole);
                System.out.println("Role Dipilih = " + selectedRole);

                if (!dbRole.equalsIgnoreCase(selectedRole)) {

                    rs.close();
                    pst.close();
                    conn.close();

                    JOptionPane.showMessageDialog(
                            this,
                            "Role yang dipilih tidak sesuai dengan data akun!",
                            "Login Gagal",
                            JOptionPane.ERROR_MESSAGE);

                    return false;
                }

                loggedSubRole = rs.getString("sub_role");
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String specialization = rs.getString("specialization");

                if (selectedRole.equals("Client")) {

                    loggedInUser = new Client(
                            userId,
                            name,
                            email,
                            password,
                            phone,
                            "Client",
                            address);

                    showScreen("CLIENT_DASHBOARD");

                } else if (selectedRole.equals("Admin")) {

                    loggedInUser = new Admin(
                            userId,
                            name,
                            email,
                            password,
                            phone,
                            loggedSubRole);

                    showScreen("ADMIN_DASHBOARD");

                } else {

                    loggedInUser = new Technician(
                            userId, // technicianId
                            specialization,
                            userId, // userId
                            name,
                            email,
                            password,
                            phone,
                            "Technician");

                    showScreen("TECHNICIAN_DASHBOARD");
                }

                rs.close();
                pst.close();
                conn.close();

                return true;
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public boolean attemptRegister(String name, String email, String password, String phone, String info,
            String selectedRole) {
        // Prevent duplicate emails
        for (Client c : clientList) {
            if (c.getEmail().equalsIgnoreCase(email))
                return false;
        }
        for (Admin a : adminList) {
            if (a.getEmail().equalsIgnoreCase(email))
                return false;
        }
        for (Technician t : technicianList) {
            if (t.getEmail().equalsIgnoreCase(email))
                return false;
        }

        int newUserId = (int) (System.currentTimeMillis() % 100000);

        if (selectedRole.equals("Client")) {
            Client c = new Client(newUserId, name, email, password, phone, "Client", info);
            clientList.add(c);
            return true;
        } else if (selectedRole.startsWith("Technician")) {
            String spec = selectedRole.substring(selectedRole.indexOf("-") + 2).trim();
            int newTechId = newUserId + 100;
            Technician t = new Technician(newTechId, spec, newUserId, name, email, password, phone, "Technician");
            technicianList.add(t);
            return true;
        }
        return false;
    }

    public void logout() {
        loggedInUser = null;
        loggedSubRole = null;
        showScreen("LOGIN");
    }

    // Getters and Setters for Simulator state
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public String getLoggedSubRole() {
        return loggedSubRole;
    }

    public ArrayList<Client> getClientList() {
        return clientList;
    }

    public ArrayList<Admin> getAdminList() {
        return adminList;
    }

    public ArrayList<Technician> getTechnicianList() {
        return technicianList;
    }

    public ArrayList<TicketProblem> getTicketList() {
        return ticketList;
    }

    public ArrayList<Consultation> getConsultationList() {
        return consultationList;
    }

    public ArrayList<DesignProject> getDesignProjectList() {
        return designProjectList;
    }

    public ArrayList<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public ArrayList<Payment> getPaymentList() {
        return paymentList;
    }

    public ArrayList<Material> getMaterialList() {
        return materialList;
    }

    public ArrayList<SystemUnit> getSystemUnitList() {
        return systemUnitList;
    }

    public ArrayList<Schedule> getScheduleList() {
        return scheduleList;
    }

    public ArrayList<Report> getReportList() {
        return reportList;
    }

    public ArrayList<String[]> getDiscussionMessages() {
        return discussionMessages;
    }

    public ArrayList<String[]> getVendorList() {
        return vendorList;
    }
}
