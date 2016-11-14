package org.trainingTracker.database.conection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sergio on 18/10/16.
 */
public class ConnectionPool {

        // JDBC driver name and database URL
        public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        public static final String DB_URL = "jdbc:mysql://localhost:3306/trainingTracker";

        // Database credentials (DO NOT SEE PLEASE)
        static final String USER = "travis";
        static final String PASS = "";

        //Condition
        private static Lock lock = new ReentrantLock();
        private static Condition notEmpty = lock.newCondition();

        private static Stack<Connection> poolDeConexiones = null;
        private static final int POOL_SIZE = 20;

        private static void createConnections(){
            Connection conn = null;

            try {
                poolDeConexiones = new Stack<Connection>();
                Class.forName(JDBC_DRIVER);

                for(int i=0; i<POOL_SIZE; i++){
                    conn = DriverManager.getConnection(DB_URL,USER,PASS);
                    poolDeConexiones.push(conn);
                }

            } catch (SQLException e) {

                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        public static Connection requestConnection(){
            lock.lock();
            try {
                if (poolDeConexiones == null) {
                    createConnections();
                }

                if (poolDeConexiones.empty()) {

                    //AWAIT: It returns false if timeout ends and true if it return because of a signal
                    if (!notEmpty.await(10, TimeUnit.SECONDS)) {
                        System.out.println("Conection to DB timed out");
                        return null;
                    }
                }
                return poolDeConexiones.pop();

            } catch (InterruptedException ex) {
                System.err.println("Error: Interupted exception");
                return null;
            }finally {
                lock.unlock();
            }
        }

        public static void releaseConnection(Connection conn){
            lock.lock();
            try {
                poolDeConexiones.push(conn);
                //SIGNAL
                notEmpty.signal();
            } finally {
                lock.unlock();
            }


        }


}
