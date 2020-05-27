package ch.hesge.cours634.security;

import javax.persistence.*;
import java.time.DateTimeException;
import java.time.LocalDate;

@Entity
public class AccessEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Status status;
    private LocalDate date;
    private String message;
    @ManyToOne
    @JoinColumn(name="user_name")
    private UserAccount user;


    public AccessEvent(int id, Status status, LocalDate date, String message) {
        this(status, date, message);
        this.id = id;
    }

    public AccessEvent(Status status, LocalDate date, String message) {
        this.status = status;
        this.date = date;
        this.message = message;
    }

    public AccessEvent(Status status, String message) {
        this.status = status;
        this.date = LocalDate.now();
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
