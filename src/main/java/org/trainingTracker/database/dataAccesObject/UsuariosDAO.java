package org.trainingTracker.database.dataAccesObject;


import java.sql.*;

import org.trainingTracker.database.conection.ConnectionPool;


//Hay dos versiones de este paquete, puede dar fallos.
//		...exceptions.jdbc4.MySQLIntegr...
//		...exceptions.MySQLIntegr...


public class UsuariosDAO {

    //Database Field
    public static final String DBF_USER = "user";
    public static final String DBF_PASS = "pass";


    /** Inserta el ususario en la base de datos
	 * 
	 * @param user
	 * @param pass
	 * @return true si se ha anadido el usuario y false si ya existe o hay error.
	 */
	public static boolean addUser( String user, String pass){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();
			if(conn==null){ System.out.printf("****CONN NULO"); }

			//Parte intertesante--------------------------------------------------------

            PreparedStatement stmt = conn.prepareStatement(
                "INSERT into usuarios ("+ DBF_USER +", "+ DBF_PASS +") values (?, ?, ?);");

            stmt.setString(1,user);
            stmt.setString(2,pass);
			stmt.execute();
			
			return true;
		} catch (com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException e) {
			
			//System.out.println("Entrada ya existente");
			//Fin de la parte interesante---------------------------------------------
		}
		catch (ClassNotFoundException e){
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
	 * @param nickname
	 * @return UsuarioVO poblado si existe el ususario y null si no existe
	 */
//	public static UsuarioVO findUser( String nickname ){
//		Connection conn = null;
//		try{
//			Class.forName(ConnectionPool.JDBC_DRIVER);
//			conn = ConnectionPool.requestConnection();
//			Statement stmt = conn.createStatement();
//
//			//Parte intertesante--------------------------------------------------------
//
//			String sql = String.format("SELECT _id, nickname, nombre, pass, fecha from usuarios where nickname='%s'", nickname);
//			ResultSet rs = stmt.executeQuery(sql);
//
//			if( rs.next() ){
//				return new UsuarioVO (rs.getInt("_id"), rs.getString("nickname"), rs.getString("nombre"), rs.getString("pass"), rs.getString("fecha"));
//			}
//			System.out.println("El usuario no existe");
//			return null;
//
//			//Fin de la parte interesante---------------------------------------------
//		} catch (ClassNotFoundException e){
//			e.printStackTrace();
//		} catch (SQLException e){
//			e.printStackTrace();
//		} finally {
//
//			if ( conn != null ) ConnectionPool.releaseConnection(conn);
//		}
//
//		return null;
//	}
//
	/**Busca y devuelve un usuario por id
	 * 
	 * @param nickname
	 * @return UsuarioVO poblado si existe el ususario y null si no existe
	 */
//	public static UsuarioVO findUser( int nickname ){
//		Connection conn = null;
//		try{
//			Class.forName(ConnectionPool.JDBC_DRIVER);
//			conn = ConnectionPool.requestConnection();
//			Statement stmt = conn.createStatement();
//
//			//Parte intertesante--------------------------------------------------------
//
//			String sql = String.format("SELECT _id, nickname, nombre, pass, fecha from usuarios where _id='%s'", id);
//			ResultSet rs = stmt.executeQuery(sql);
//
//			if( rs.next() ){
//				return new UsuarioVO (rs.getInt("_id"), rs.getString("nickname"), rs.getString("nombre"), rs.getString("pass"), rs.getString("fecha"));
//			}
//			System.out.println("El usuario no existe");
//			return null;
//
//			//Fin de la parte interesante---------------------------------------------
//		} catch (ClassNotFoundException e){
//			e.printStackTrace();
//		} catch (SQLException e){
//			e.printStackTrace();
//		} finally {
//
//			if ( conn != null ) ConnectionPool.releaseConnection(conn);
//		}
//
//		return null;
//	}

    /**
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
//	public static ArrayList<UsuarioVO> findAllUsers() throws SQLException, ClassNotFoundException{
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
//			ArrayList<UsuarioVO> list = new ArrayList<UsuarioVO>();
//
//			while( rs.next() ){
//				list.add( new UsuarioVO (rs.getInt("_id"), rs.getString("nickname"), rs.getString("nombre"),
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

    /**
     *
     * @param userID
     * @param newNickname
     * @param newNombre
     * @param newPasswd
     * @return
     */
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
