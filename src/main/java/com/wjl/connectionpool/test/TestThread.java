package com.wjl.connectionpool.test;

import java.sql.Connection;

public class TestThread implements Runnable {
    public void run() {
        ConnectionPoolManage connectionPoolManagea = new ConnectionPoolManage();
        for (int i = 0; i < 10; i++) {
            Connection connection = connectionPoolManagea.getConnection();
            System.out.println(Thread.currentThread().getName()+",connection:"+connection);
            connectionPoolManagea.releaseConnetion(connection);
        }
    }
}
