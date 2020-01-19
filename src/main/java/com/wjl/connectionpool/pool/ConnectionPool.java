package com.wjl.connectionpool.pool;

import com.wjl.connectionpool.config.PoolConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
  *1.初始化线程池（初始化空闲线程）
  * 2.调用getConnection方法----获取连接请求
  * 2.1 先去空闲集合获取当前连接，存放在activeConnection(活动连接)
  * 3.调用releaseConnetion方法-----释放掉连接
  * 3.1 获取到活动连接集合连接，转移到freeconnection集合中(当然我们也要判断空闲是否已经到达最大)
  * @throws
  * @data 2018/4/19 13:56
  * @by wjl
*/
public class ConnectionPool implements IConnectionPool{
    //创建这两个集合
    //这里我们使用线程安全的Vector而不是ArrayList
    //空闲池
    private List<Connection> freeConnection = new Vector<Connection>();
    //活动池
    private List<Connection> activeConnection = new Vector<Connection>();
    //实际上这就是我们读取到的配置文件
    private PoolConfig poolConfig;
    //已经创建了的连接数，这个数字不能超过最大值,但是样做效率不高，
    // 如果使用原子类效率会高些也不用锁起来（实际上它本身是个乐观锁，而我们这样操作等同悲观锁效率自然是不如原子类操作）
    private int countConnection = 0;
    //当我们创建了这个连接池的时候
    public ConnectionPool(PoolConfig poolConfig){
        this.poolConfig = poolConfig;
        //初始化连接池
        initPool();
    }

    /**
     * 实际上初始话连接池就是创建初始化连接数个连接
     * 然后存放到空闲池中等待调用getConnection时被使用
     */
    private void initPool(){
       try {
           if (poolConfig == null){
               throw new Exception("Can't find the configuration asking you");
           }
           for (int i = 0; i < poolConfig.getIntiConnections(); i++) {
               //jdbc操作
               Connection connection = newConnection();
               if (connection != null){
                   freeConnection.add(connection);
               }
           }
       }catch (Exception e){

       }
    }

    //jdbc操作创建连接
    //这里锁起来是为了防止线程不安全的情况(实际上是悲观锁）
    //当然你也可以在这里使用原子类
    private synchronized Connection newConnection() {
        try {
            Class.forName(poolConfig.getDriverName());
            Connection connection = DriverManager.getConnection(poolConfig.getUrl(), poolConfig.getUserName(), poolConfig.getPassword());
            countConnection ++;
            return connection;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取连接这一说呢
     * 有几个状态需要注意，
     * 一是达到最大连接的时候让线程进入等待状态（等待时间由用户配置读取）一段时间后重新尝试获取。
     * 二是如果空闲中没有连接了我们应该如何操作,这里我选择了创建新的（实际上应该也是这么操作的毕竟空闲！=最大连接数）
     * 三是创建出来的新的是否需要进行可用校验
     * 四是如何保证线程的安全 锁还是不锁
     * 五如何计数创建了多少个保证不超过最大线程数
     * @return
     */
    public synchronized Connection getConnection(){
            Connection connection = null;
        try{
            if (countConnection < poolConfig.getMaxActiveConnections()){
                if (freeConnection.size() > 0){
                    //如果这一步你看不懂的话可以看一下List接口的源码
                    connection = freeConnection.remove(0);
                    //放入到活跃池中
                    activeConnection.add(connection);
                }else {
                    //如果不存在的话我们高于给新的
                    //这里不用担心会超出最大因为我们已经校验过了
                    connection = newConnection();
                    //这一步校验一下连接是不是可用我觉得是存在必要的
                    if (isAvailable(connection)){
                        activeConnection.add(connection);
                    }else {
                        //如果获取到了一个假的我们就再获取一次
                        countConnection--;
                        //实际上这里就是一个递归
                        connection = getConnection();
                    }
                }
            }else {
                wait(poolConfig.getConnTimeOut());
                connection = getConnection();
            }
        }catch (Exception e){

        }
        return connection;
    }

    /**
     * 其实所谓释放连接就是可复用的操作
     * 需要考虑一下几点
     * 一、空闲池是不是满了
     * 二、这条链接是不是能用，如果不能用肯定不能放到空闲池了
     * 三、
     * @param connection
     */
    public void releaseConnetion(Connection connection) {
       try {
           if(!isAvailable(connection)){
               //我认为这里如果这条连接已经是一条不可用的连接的话
               //我就默认为你已经处理过或者他是不需要处理的了
               //当然你也可以在在这里抛异常或者做其他处理
                return;
           }
           if (freeConnection.size() < poolConfig.getMaxConnections()){
                //没超过的话我就放到空闲池里
               freeConnection.add(connection);
           }else {
               //超过了的话就关掉连接
               connection.close();
           }
           //删除掉活动池里的对应连接
           activeConnection.remove(connection);
           countConnection--;
           notifyAll();
       }catch (Exception e){

       }
    }

    //校验连接是否可用
    private boolean isAvailable(Connection connection) throws SQLException {
        if (connection == null || connection.isClosed()) {
            return false;
        }
        return true;
    }



}
