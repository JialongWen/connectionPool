package com.wjl.connectionpool.pool;

import java.sql.Connection;

public interface IConnectionPool {
    public Connection getConnection();

    public void releaseConnetion(Connection connection);
}
