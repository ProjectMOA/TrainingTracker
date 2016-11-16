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
     * Binds the existing default exercise with the given owner.
     * @param exercise_id
     * @param owner
     * @return
     */
    public static synchronized int addDefaultExercise(int exercise_id, String owner){
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_OWN_TABLE_NAME, DBF_OWN_NICK, DBF_OWN_EXERCISE));
            stmt.setString(1, owner);
            stmt.setInt(2, exercise_id);

            stmt.execute();
            return exercise_id;

        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

	/**
     * Adds the given exercise to the data base as a custom exercise owned by owner.
     * @param exercise_name
     * @param muscleGroup
     * @return
     */
	public static synchronized int addCustomExercise(String exercise_name, String muscleGroup, String owner){
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_NAME, DBF_EXERCISE_MUSCLEGROUP));
            stmt.setString(1, exercise_name);
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
            e.printStackTrace();
        } catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
    /**
     * Deletes the custom exercise from Exercises table identified by id
     * @param id
     * @return
     */
    public static boolean deleteCustomExercise(int id){
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=? AND %s=?;",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_ID, DBF_EXERCISE_PREDEFINED));
            stmt.setInt(1, id);
            stmt.setInt(2,0);

            stmt.execute(); //Executes the deletion

            return true;

        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the Exercise-User relation in Own table but not from the Exercises table
     * @param nick
     * @param exercise
     * @return
     */
    public static boolean deleteOwnExercise(String nick, int exercise){
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=? AND %s=?;",
                    DBF_OWN_TABLE_NAME, DBF_OWN_NICK, DBF_OWN_EXERCISE));
            stmt.setString(1, nick);
            stmt.setInt(2, exercise);
            stmt.execute(); //Executes the deletion
            return true;

        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a list of exercises that a user owns
     * @return
     */
	public static List<ExerciseVO> listUserExercises(String user){
        try (Connection conn = ConnectionPool.requestConnection()) {

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

		}  catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
    /**
     * Returns a list of default exercises
     * @return
     */
    public static List<ExerciseVO> listDefaultExercises(){

        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT * FROM %s WHERE %s=?;",
                    DBF_EXERCISES_TABLE_NAME, DBF_EXERCISE_PREDEFINED));

            stmt.setBoolean(1,true);

            ResultSet rs = stmt.executeQuery();

            ArrayList<ExerciseVO> list = new ArrayList();

            if(rs.first()) {
                do {
                    list.add(new ExerciseVO(rs.getInt(DBF_EXERCISE_ID), rs.getString(DBF_EXERCISE_NAME), rs.getString(DBF_EXERCISE_MUSCLEGROUP),
                        rs.getBoolean(DBF_EXERCISE_PREDEFINED)));
                } while (rs.next());
            }

            return list;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
