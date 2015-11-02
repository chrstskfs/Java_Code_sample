package model;

/**
 * Created by PC on 3/2/2015.
 */
public class User {

    private String name;
    private String password;
    private String mail;

    public User() {
    }

    public User(String name, String password, String mail) {
        this.name = name;
        this.password = password;
        this.mail = mail;
    }



    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", password=" + password + ", mail=" + mail + '}';
    }



}
