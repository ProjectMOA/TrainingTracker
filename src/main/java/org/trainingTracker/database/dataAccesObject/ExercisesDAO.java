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
     * Adds the given exercise to the data base as deffault.
     * @param name
     * @param muscleGroup
     * @return
     */
    @Deprecated
	public static boolean addExercise( String name, String muscleGroup){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s ( %s, %s, %s ) VALUES (?, ?, ?);",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_NAME, DBF_EXERCISE_MUSCLEGROUP, DBF_EXERCISE_PREDEFINED));
            stmt.setString(1,name);
            stmt.setString(2,muscleGroup);
            stmt.setBoolean(3,true);

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
     * Adds the given exercise to the data base as a custom exercise owned by owner.
     * @param name
     * @param muscleGroup
     * @return
     */
	public static synchronized int addExercise(String name, String muscleGroup, String owner){
		Connection conn = null;

		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_NAME, DBF_EXERCISE_MUSCLEGROUP));
            stmt.setString(1, name);
            stmt.setString(2, muscleGroup);
            stmt.execute(); //Executes the insert


            PreparedStatement stmt2 = conn.prepareStatement( String.format( "SELECT LAST_INSERT_ID();"));
            ResultSet rs = stmt2.executeQuery(); //Executes the select

            rs.first();

            int exercise_id = rs.getInt("LAST_INSERT_ID()");

            stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_OWN_TABLE_NAME, DBF_OWN_NICK, DBF_OWN_EXERCISE));
            stmt.setString(1, owner);
            stmt.setInt(2, exercise_id);

            stmt.execute();
			return exercise_id;
		} catch (MySQLIntegrityConstraintViolationException e) {

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			if ( conn != null ) ConnectionPool.releaseConnection(conn);
		}
		return -1;
	}


    /**
     * Returns a list of exercises that a user owns
     * @return
     */
	public static List<ExerciseVO> listExercises(String user){
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

            if(rs.first()) {
                do {
                    list.add(new ExerciseVO(rs.getInt(DBF_EXERCISE_ID), rs.getString(DBF_EXERCISE_NAME), rs.getString(DBF_EXERCISE_MUSCLEGROUP),
                        rs.getBoolean(DBF_EXERCISE_PREDEFINED)));
                } while (rs.next());
            }

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
