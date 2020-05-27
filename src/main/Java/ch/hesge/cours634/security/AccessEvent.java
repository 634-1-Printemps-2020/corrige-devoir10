package ch.hesge.cours634.security;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AccessEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Status status;
    private LocalDateTime date;
    private String message;
    @ManyToOne
    @JoinColumn(name="user_name")
    private UserAccount user;

    public AccessEvent() {
    }

    public AccessEvent(int id, Status status, LocalDateTime date, String message) {
        this(status, date, message);
        this.id = id;
    }

    public AccessEvent(Status status, LocalDateTime date, String message) {
        this.status = status;
        this.date = date;
        this.message = message;
    }

    public AccessEvent(Status status, String message) {
        this.status = status;
        this.date = LocalDateTime.now();
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccessEvent that = (AccessEvent) o;
        return id == that.id && status == that.status && Objects.equals(date, that.date) && Objects.equals(message, that.message) && Objects.equals(user, that.user);
    }

    @Override public int hashCode() {
        return Objects.hash(id, status, date, message, user);
    }

    @Override public String toString() {
        return "AccessEvent{" + "id=" + id + ", status=" + status + ", date=" + date + ", message='" + message + '\'' + ", user=" + user + '}';
    }
}
