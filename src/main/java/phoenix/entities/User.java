package phoenix.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="users", schema = "bdays")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "user_name")
    private String userName;
    private String login;
    private String password;
    private boolean active;

    @Column(name = "last_activity")
    private LocalDate lastActivity;

    @Column(name="role")
    private String role;


    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id=id;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLastActivity(LocalDate date) {
        this.lastActivity = date;
    }

    public LocalDate getLastActivity() {
        return this.lastActivity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setRole(String role) {
        this.role=role.toUpperCase();
    }

    public String getRole(){
        return role;
    }

    public String getStatusButton(boolean active) {
        if(active)
            return "Block";
        else
            return "Unlock";
    }

    @Override
    public String toString() {
        return "ID: "+id+" User Name: "+userName+" Login: "+login;
    }
}
