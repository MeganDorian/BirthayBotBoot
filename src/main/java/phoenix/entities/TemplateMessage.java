package phoenix.entities;

import javax.persistence.*;

@Entity
@Table(name = "templates", schema = "bdays")
public class TemplateMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private int id;

    public TemplateMessage() {}

    public TemplateMessage (String template) {
//        this.id=id;
        message = template;
    }
    private String message;

//    @OneToMany(mappedBy = "msg_template", fetch = FetchType.LAZY)
//    private Set<Birthday> birthday;

    public int getId () {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemplate(String template) {
        this.message=template;
    }

    public String getTemplate () {
        return message;
    }

    @Override
    public String toString() {
        return id + " " + message;
    }
}