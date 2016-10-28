package org.trainingTracker.database.dataAccesObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.UserVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.trainingTracker.database.dataAccesObject.UsersDAO.DBF_NICK;

/**
 * Exercises's Data Access Object
 */
public class ExercisesDAO {

    //Database Field
    public static final String DBF_TABLE_NAME = "Exercises";
    public static final String DBF_ID = "_id";
    public static final String DBF_NAME = "name";
    public static final String DBF_MUSCLEGROUP = "muscle_group";
    public static final String DBF_PREDEFINED = "predefined";

    /**
     * Adds the given exercise to the data base.
     * @param name
     * @param muscleGroup
     * @return
     */
	public static boolean addExercise( String name, String muscleGroup){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_TABLE_NAME, DBF_NAME, DBF_MUSCLEGROUP));
            stmt.setString(1,name);
            stmt.setString(2,muscleGroup);

			stmt.execute();
			
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
     *
     * @return
     */
	public static List<ExerciseVO> listAllExercises(){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT  %s, %s, %s, %s FROM %s;",
                    DBF_ID, DBF_NAME, DBF_MUSCLEGROUP, DBF_PREDEFINED, DBF_TABLE_NAME));

            ResultSet rs = stmt.executeQuery();

            ArrayList<ExerciseVO> list = new ArrayList();

            rs.first();

            do{
				list.add( new ExerciseVO(rs.getInt(DBF_ID), rs.getString(DBF_NAME), rs.getString(DBF_MUSCLEGROUP),
                    rs.getBoolean(DBF_PREDEFINED)) );
			} while( rs.next() );

			return list;
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
