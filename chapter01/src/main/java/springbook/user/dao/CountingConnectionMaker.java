package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionMaker 인터페이스 도입으로 인해 확장이 가능해졌다.
 */
@Deprecated
public class CountingConnectionMaker implements ConnectionMaker{
    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        counter++;
        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return counter;
    }
}
