package org.trainingTracker.database.dataAccesObject;

import java.sql.*;

import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.UserVO;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class UsersDAO {

    //Database Field
    public static final String DBF_TABLE_NAME = "Users";
    public static final String DBF_NICK = "nick";
    public static final String DBF_PASS = "pass";
    public static final String DBF_MAIL = "email";
    public static final String DBF_REGDATE = "registerDate";


    /** Inserta el ususario en la base de datos
	 * 
	 * @param nick
	 * @param pass
	 * @return true si se ha anadido el usuario y false si ya existe o hay error.
	 */
	public static boolean addUser( String nick, String pass, String mail){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();
			if(conn==null){ System.out.printf("****CONN NULO"); }

			//Parte intertesante--------------------------------------------------------

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s ( %s, %s, %s ) VALUES (?, ?, ?);",
                    DBF_TABLE_NAME, DBF_NICK, DBF_PASS, DBF_MAIL));
            stmt.setString(1,nick);
            stmt.setString(2,pass);
            stmt.setString(3,mail);

			stmt.execute();
			
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
			System.err.println("CONSTRAIN VIOLATION: Entrada ya existente");
			//Fin de la parte interesante---------------------------------------------
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			
			if ( conn != null ) ConnectionPool.releaseConnection(conn);
		}
		return false;
	}
	
	/**Busca y devuelve un usuario por nickname
	 * 
	 * @param nick
	 * @return UserVO poblado si existe el ususario y null si no existe
	 */
	public static UserVO findUser(String nick ){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

			//Parte intertesante--------------------------------------------------------

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT  %s, %s, %s, %s  FROM %s WHERE %s=?;",
                    DBF_NICK, DBF_PASS, DBF_MAIL, DBF_REGDATE, DBF_TABLE_NAME, DBF_NICK));
            stmt.setString(1,nick);

            ResultSet rs = stmt.executeQuery();

			if( rs.next() ){
				return new UserVO(rs.getString(DBF_NICK), rs.getString(DBF_PASS), rs.getString(DBF_MAIL), rs.getString(DBF_REGDATE));
			}
			System.out.println("El usuario no existe");
			return null;

			//Fin de la parte interesante---------------------------------------------
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {

			if ( conn != null ) ConnectionPool.releaseConnection(conn);
		}

		return null;
	}


//    /**
//     *
//     * @return
//     * @throws SQLException
//     * @throws ClassNotFoundException
//     */
//	public static ArrayList<UserVO> findAllUsers() throws SQLException, ClassNotFoundException{
//
//		Connection conn = null;
//		try{
//			Class.forName(ConnectionPool.JDBC_DRIVER);
//			conn = ConnectionPool.requestConnection();
//			Statement stmt = conn.createStatement();
//
//			//Parte intertesante--------------------------------------------------------
//
//			String sql = "select _id, nickname, nombre, pass, fecha from usuarios";
//
//			ResultSet rs = stmt.executeQuery(sql);
//
//			ArrayList<UserVO> list = new ArrayList<UserVO>();
//
//			while( rs.next() ){
//				list.add( new UserVO (rs.getInt("_id"), rs.getString("nickname"), rs.getString("nombre"),
//						rs.getString("pass"), rs.getString("fecha")) );
//			}
//
//			return list;
//
//			//Fin de la parte interesante---------------------------------------------
//		} catch (ClassNotFoundException e){
//			throw e;
//		} catch (SQLException e){
//			throw e;
//		} finally {
//
//			if ( conn != null ) ConnectionPool.releaseConnection(conn);
//		}
//	}

//    /**
//     *
//     * @param userID
//     * @param newNickname
//     * @param newNombre
//     * @param newPasswd
//     * @return
//     */
//	public static boolean updateUser( int userID, String newNickname, String newNombre, String newPasswd){
//		Connection conn = null;
//		try{
//			Class.forName(ConnectionPool.JDBC_DRIVER);
//			conn = ConnectionPool.requestConnection();
//			Statement stmt = conn.createStatement();
//
//			//Parte intertesante--------------------------------------------------------
//
//			String sql = String.format("UPDATE `usuarios` SET `nickname`='%s', `nombre`='%s',`pass`='%s' WHERE _id='%s'",
//					newNickname, newNombre, newPasswd, userID);
//			stmt.execute(sql);
//			return true;
//		} catch (MySQLIntegrityConstraintViolationException e) {
//
//			System.out.println("El nickname ya existe");
//			//Fin de la parte interesante---------------------------------------------
//		} catch (ClassNotFoundException e){
//			e.printStackTrace();
//		} catch (SQLException e){
//			e.printStackTrace();
//		} finally {
//
//			if ( conn != null ) ConnectionPool.releaseConnection(conn);
//		}
//		return false;
//	}
	
//	public static boolean deleteUser( String Nickname ){
//
//		Connection conn = null;
//		try{
//			Class.forName(ConnectionPool.JDBC_DRIVER);
//			conn = ConnectionPool.requestConnection();
//			Statement stmt = conn.createStatement();
//
//			//Parte intertesante--------------------------------------------------------
//
//			String sql = String.format("DELETE FROM `usuarios` WHERE `nickname`='%s'", Nickname);
//			stmt.execute(sql);
//			return true;
//		} catch (MySQLIntegrityConstraintViolationException e) {
//
//			System.out.println("El nickname no existe");
//			//Fin de la parte interesante---------------------------------------------
//		} catch (ClassNotFoundException e){
//			e.printStackTrace();
//		} catch (SQLException e){
//			e.printStackTrace();
//		} finally {
//
//			if ( conn != null ) ConnectionPool.releaseConnection(conn);
//		}
//		return false;
//	}
//
}
