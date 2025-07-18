package org.example.courier;


import java.util.concurrent.ThreadLocalRandom;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public Courier() {

    }

    public static Courier random() {
        int suffix = ThreadLocalRandom.current().nextInt(100, 100_000);
        return new Courier("Petfed" + suffix, "pass1234", "Petfed");
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}


