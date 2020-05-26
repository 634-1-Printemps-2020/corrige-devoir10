package ch.hesge.cours634.security.db.pool;

public class PoolConfig {

    int size;
    String url;
    String user;
    String credentials;

    public PoolConfig(int size, String url, String user, String credentials) {
        this.size = size;
        this.url = url;
        this.user = user;
        this.credentials = credentials;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }


    @Override
    public String toString() {
        return "PoolConfig{" +
                "size=" + size +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
