package com.wjl.connectionpool.config;

/**
  *
  * 这里说明一下为什么要写这个配置的类
  * 原本我们在配置一个数据库连接池的时候都是在配置文件中设置他的属性
  * 又或者是xml形式的获取dataSource直接依赖注入属性到类中
  * 而springboot中则是采用的配置类的形式来配置一个dataSource(实际效果一样）
  * 但是在这里三种形式我都没有去采取,主要原因是这些都不是重点内容所以就略过不去做这些没有必要的编码了
  * 节约时间,直接将配置写死在封装类中
  *
  * @throws
  * @data 2018/4/19 13:46
  * @by wjl
*/
public class PoolConfig {

    private String driverName = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://localhost:3306/test";

    private String userName = "root";

    private String password = "1234";
    //连接池的名称
    private String poolName = "testPool_1";
    //空闲池最小连接数
    private int minConnections =1;
    //空闲池最大连接数
    private int maxConnections =10;
    //初始化连接数
    private int intiConnections =5;
    //重复获得连接的频率
    private long connTimeOut = 1000;
    //最大允许的连接数,和数据库对应
    private int maxActiveConnections = 100;
    //连接超时时间 , 默认二十分钟
    private long connectionTimeOut = 1000*60*20;


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(int minConnections) {
        this.minConnections = minConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getIntiConnections() {
        return intiConnections;
    }

    public void setIntiConnections(int intiConnections) {
        this.intiConnections = intiConnections;
    }

    public long getConnTimeOut() {
        return connTimeOut;
    }

    public void setConnTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public void setMaxActiveConnections(int maxActiveConnections) {
        this.maxActiveConnections = maxActiveConnections;
    }

    public long getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(long connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }
}

