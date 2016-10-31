package org.trainingTracker.database.dataAccesObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.ExerciseVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercises's Data Access Object
 */
public class ExercisesDAO {

    //Database Field
    public static final String DBF_EXERCISES_TABLE_NAME = "Exercises";
    public static final String DBF_EXERCISE_ID = "_id";
    public static final String DBF_EXERCISE_NAME = "name";
    public static final String DBF_EXERCISE_MUSCLEGROUP = "muscle_group";
    public static final String DBF_EXERCISE_PREDEFINED = "predefined";
    public static final String DBF_OWN_TABLE_NAME = "Own";
    public static final String DBF_OWN_NICK = "nick";
    public static final String DBF_OWN_EXERCISE = "exercise";



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
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_NAME, DBF_EXERCISE_MUSCLEGROUP));
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
     * Returns a list with all exercises
     * @return
     */
	public static List<ExerciseVO> listAllExercises(String user){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT ex.%2$s, ex.%3$s, ex.%4$s, ex.%5$s " +
                        "FROM %1$s ex, %6$s own WHERE ex.%2$s=own.%7$s AND own.%8$s=?;",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_ID, DBF_EXERCISE_NAME,
                    DBF_EXERCISE_MUSCLEGROUP, DBF_EXERCISE_PREDEFINED,
                    DBF_OWN_TABLE_NAME, DBF_OWN_EXERCISE, DBF_OWN_NICK));

            stmt.setString(1,user);

            ResultSet rs = stmt.executeQuery();

            ArrayList<ExerciseVO> list = new ArrayList();

            rs.first();

            do{
				list.add( new ExerciseVO(rs.getInt(DBF_EXERCISE_ID), rs.getString(DBF_EXERCISE_NAME), rs.getString(DBF_EXERCISE_MUSCLEGROUP),
                    rs.getBoolean(DBF_EXERCISE_PREDEFINED)) );
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
