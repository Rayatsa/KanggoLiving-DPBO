/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kanggoliving_poryek.users;

/**
 *
 * @author terzy
 */
public abstract class User {
    private int userId;
    private String name, email, password, phone, role;

    public User(int userId, String name, String email, String password, String phone, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean login (String email, String password){
        if(this.email.equals(email) && this.password.equals(password)){
            System.out.println("Login Berhasil, Selamat Datang: "+ this.name);
            return true;
        }else{
            System.out.println("Login Gagal, Cek Kembali Email dan Password Anda");
            return false;
        }
    }
    public void logout(){
        System.out.println(this.name+" Berhasil LogOut");
    }
    
    public boolean updateProfile(String name,String phone,String email, String password){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        System.out.println("Berhasil Mengganti Profil");
        return true;
    }
    
    public boolean changePassword(String oldPassword, String newPassword){
        if(this.password.equals(oldPassword)){
            this.password = newPassword;
            System.out.println("Berhasil Mengganti Password");
            return true;
        }else{
            System.out.println("Gagal Mengganti Password");
            return false;
        }
    }
}