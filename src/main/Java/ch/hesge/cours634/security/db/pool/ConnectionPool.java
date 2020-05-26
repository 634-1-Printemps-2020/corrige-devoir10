package ch.hesge.cours634.security.db.pool;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    Logger logger = Logger.getLogger(ConnectionPool.class);
    private static PoolConfig config;

    private List<Connection> available = new ArrayList<>();
    private List<Connection> inUse=new ArrayList<>();


    public ConnectionPool(PoolConfig config)  throws  ConnectionPoolException {
        this.config = config;
        init(config);
    }

    private void init(PoolConfig config) throws ConnectionPoolException {
        for ( int i=0; i< config.size; i++) {
            try {
                available.add(DriverManager.getConnection(config.getUrl(), config.getUser(), config.getCredentials()));
            } catch (SQLException e) {
                throw new ConnectionPoolException("Failed to initialize cnnection pool");
            }
        }
    }


    public  void shutdown(){
        close(available);
        close(inUse);
        available.clear();
        inUse.clear();
    }

    private void close(List<Connection> connections) {
        for (Connection c : connections){
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    synchronized public Connection obtainConnection() throws ConnectionPoolException {
        if (available.size() > 0) {
            Connection connection = available.get(0);
            available.remove(connection);
            inUse.add(connection);
            return connection;
        }else{
            throw new ConnectionPoolException("No more available connections");
        }

    }
    synchronized public void releaseConnection(Connection connection){
        available.add(connection);
        inUse.remove(connection);
    }


    public int getAvailableConnectionsSize(){
        return available.size();
    }

    public int getInuseConnectionsSize(){
        return inUse.size();
    }

}
