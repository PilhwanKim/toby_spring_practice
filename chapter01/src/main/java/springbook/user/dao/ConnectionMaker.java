package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by pilhwankim on 15/12/2017.
 */
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
