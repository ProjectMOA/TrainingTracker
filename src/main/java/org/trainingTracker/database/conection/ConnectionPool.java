package org.trainingTracker.database.conection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by sergio on 18/10/16.
 */
public class ConnectionPool {


    private static HikariDataSource ds;
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/trainingTracker");
        config.setUsername("travis");
        config.setPassword("");
        ds = new HikariDataSource(config);
    }

        //Condition
        private static Lock lock = new ReentrantLock();
        private static Condition notEmpty = lock.newCondition();

        private static Stack<Connection> poolDeConexiones = null;
        private static final int POOL_SIZE = 20;

        public static Connection requestConnection(){
            try {
                return ds.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }



}
