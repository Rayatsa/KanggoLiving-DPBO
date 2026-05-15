/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek;

/**
 *
 * @author nadiya
 */
public class Main {
    public static void main(String[] args) {
        User user1 = new Client(102938, "Mahmud", "Mahmd.gmail.com", "Se49(01)", "08999888777", "Client", "Jl.Sukapura");
        
        
        TicketProblem[] ticketList = {((Client) user1).submitProblem("Ingin merenov dapur saya")};
        String ticketStatus = ticketList[0].getStatus();
        
        System.out.println(ticketStatus);
    }
}
