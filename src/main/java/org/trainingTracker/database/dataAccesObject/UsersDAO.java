package org.trainingTracker.database.dataAccesObject;

import java.sql.*;

import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.UserVO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * User's Data Access Object
 */
public class UsersDAO {

    //Database Field
    public static final String DBF_TABLE_NAME = "Users";
    public static final String DBF_NICK = "nick";
    public static final String DBF_PASS = "pass";
    public static final String DBF_MAIL = "email";
    public static final String DBF_REGDATE = "registerDate";

    /**
     * Inserts user into database
     * @param nick User's name
     * @param pass User's pass
     * @param mail User's mail
     * @return true if insertion goes well or false if it fails
     */
	public static boolean addUser( String nick, String pass, String mail){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();
            System.out.println("Add User: " + conn);

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s ( %s, %s, %s ) VALUES (?, ?, ?);",
                    DBF_TABLE_NAME, DBF_NICK, DBF_PASS, DBF_MAIL));
            stmt.setString(1,nick);
            stmt.setString(2,pass);
            stmt.setString(3,mail);

			stmt.execute();
			
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			
			if ( conn != null ) ConnectionPool.releaseConnection(conn);
		}
		return false;
	}

    /**
     * Deletes the user identified by nick
     * @param nick
     * @return
     */
    public static boolean deleteUser(String nick){
        Connection conn = null;

        try{
            Class.forName(ConnectionPool.JDBC_DRIVER);
            conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=?;",
                    DBF_TABLE_NAME, DBF_NICK));
            stmt.setString(1, nick);

            stmt.execute(); //Executes the deletion
            return true;

        } catch (MySQLIntegrityConstraintViolationException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if ( conn != null ) ConnectionPool.releaseConnection(conn);
        }
        return false;
    }

    /**
     * Finds user identified by nick
     * @param nick User's identifier
     * @return User's value object
     */
	public static UserVO findUser(String nick ){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT  %s, %s, %s, %s  FROM %s WHERE %s=?;",
                    DBF_NICK, DBF_PASS, DBF_MAIL, DBF_REGDATE, DBF_TABLE_NAME, DBF_NICK));
            stmt.setString(1,nick);

            ResultSet rs = stmt.executeQuery();

			if( rs.next() ){
				return new UserVO(rs.getString(DBF_NICK), rs.getString(DBF_PASS), rs.getString(DBF_MAIL), rs.getString(DBF_REGDATE));
			}
			return null;
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {

			if ( conn != null ) ConnectionPool.releaseConnection(conn);
		}
		return null;
	}
}
