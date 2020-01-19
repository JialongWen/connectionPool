package com.wjl.connectionpool.test;

import com.wjl.connectionpool.config.PoolConfig;
import com.wjl.connectionpool.pool.ConnectionPool;

import java.sql.Connection;

//管理类
public class ConnectionPoolManage {
    private static PoolConfig poolConfig = new PoolConfig();
    private static ConnectionPool connectionPool = new ConnectionPool(poolConfig);

    public Connection getConnection(){
        return connectionPool.getConnection();
    }

    public void releaseConnetion(Connection connection){
        connectionPool.releaseConnetion(connection);
    }

}
