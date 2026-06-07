/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author nadiya
 */
public class Client extends User {

    private String address;

    public Client(int userId, String name, String email, String password, String phone, String role, String address) {
        super(userId, name, email, password, phone, role);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TicketProblem submitProblem(String description) {
        System.out.println("Klien " + super.getName() + " mengajukan masalah : " + description);

        TicketProblem newTicket = new TicketProblem(description);

        boolean isSuccess = newTicket.createTicket();

        if (isSuccess) {
            System.out.println("Berhasil memproses pengajuan Klien");
        } else {
            System.out.println("Gagal memproses pengajuan Klien");
        }

        return newTicket;
    }
}