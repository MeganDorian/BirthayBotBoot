package phoenix.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table (name = "birthdays", schema = "bdays")
public class Birthday {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;


    @Column (name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column (name = "user_name")
    private String userName;

    public void setId(int id) {
        this.id= id;
    }

    public Birthday() {

    }

    public Birthday(int id, String userName, LocalDate localDate) {
        setDateOfBirth(localDate);
        setId(id);
        setUserName(userName);
    }

//    public void setMessage(int id_message, String message) {
//        TemplateMessage templateMessage = new TemplateMessage();
//        templateMessage.setId(id_message);
//        templateMessage.setTemplate(message);
//        //this.msg_template=templateMessage;
//    }

//    @ManyToOne
//    @JoinColumn(name="id_template")
//    private TemplateMessage msg_template;

    public int getId () {
        return id;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth () {
        return dateOfBirth;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }



    @Override
    public String toString() {
        return id+" "+ userName+" "+dateOfBirth+" "; //+ msg_template.getTemplate();
    }
}
