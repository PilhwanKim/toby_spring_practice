package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 인터페이스로 db connection 관심사를 분리, 추상화 함.
 */
@Deprecated
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
