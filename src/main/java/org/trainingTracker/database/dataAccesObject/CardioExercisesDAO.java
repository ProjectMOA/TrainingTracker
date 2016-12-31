package org.trainingTracker.database.dataAccesObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.CardioExerciseVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercises's Data Access Object
 */
public class CardioExercisesDAO {

    //Database Field
    public static final String DBF_CARDIO_EXERCISES_TABLE_NAME = "CardioExercises";
    public static final String DBF_CARDIO_EXERCISE_ID = "_id";
    public static final String DBF_CARDIO_EXERCISE_NAME = "name";
    public static final String DBF_CARDIO_EXERCISE_TYPE = "type";
    public static final String DBF_CARDIO_EXERCISE_PREDEFINED = "predefined";
    public static final String DBF_CARDIO_OWN_TABLE_NAME = "CardioOwn";
    public static final String DBF_CARDIO_OWN_NICK = "nick";
    public static final String DBF_CARDIO_OWN_EXERCISE = "exercise";

    /**
     * Binds the existing default cardio exercise with the given owner.
     * @param exercise_id
     * @param owner
     * @return
     */
    public static synchronized int addDefaultExercise(int exercise_id, String owner){
        if( exercise_id<0 || owner==null || owner.equals("")){
            return -1;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_CARDIO_OWN_TABLE_NAME, DBF_CARDIO_OWN_NICK, DBF_CARDIO_OWN_EXERCISE));
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
     * Adds the given cardio exercise to the data base as a custom exercise owned by owner.
     * @param exercise_name
     * @param type
     * @return
     */
	public static synchronized int addCustomExercise(String exercise_name, String type, String owner){
        if ( exercise_name==null || type==null || owner==null ||
            exercise_name.equals("") || type.equals("") || owner.equals("")){
            return -1;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_CARDIO_EXERCISES_TABLE_NAME, DBF_CARDIO_EXERCISE_NAME, DBF_CARDIO_EXERCISE_TYPE));
            stmt.setString(1, exercise_name);
            stmt.setString(2, type);
            stmt.execute(); //Executes the insert

            PreparedStatement stmt2 = conn.prepareStatement( String.format( "SELECT LAST_INSERT_ID();"));
            ResultSet rs = stmt2.executeQuery(); //Executes the select

            rs.first();

            int exercise_id = rs.getInt("LAST_INSERT_ID()");

            stmt = conn.prepareStatement(
                String.format("INSERT INTO %s ( %s, %s ) VALUES (?, ?);",
                    DBF_CARDIO_OWN_TABLE_NAME, DBF_CARDIO_OWN_NICK, DBF_CARDIO_OWN_EXERCISE));
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
     * Deletes the custom cardio exercise from Exercises table identified by id
     * @param id
     * @return
     */
    public static boolean deleteCustomExercise(int id){
        if(id<0){
            return false;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=? AND %s=?;",
                    DBF_CARDIO_EXERCISES_TABLE_NAME, DBF_CARDIO_EXERCISE_ID, DBF_CARDIO_EXERCISE_PREDEFINED));
            stmt.setInt(1, id);
            stmt.setInt(2,0);

            stmt.execute();

            return true;

        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes the CardioExercise-User relation in CardioOwn table but not from the CardioExercises table
     * @param nick
     * @param exercise
     * @return
     */
    public static boolean deleteOwnExercise(String nick, int exercise){
        if(nick==null || nick.equals("") || exercise<0){
            return false;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %s=? AND %s=?;",
                    DBF_CARDIO_OWN_TABLE_NAME, DBF_CARDIO_OWN_NICK, DBF_CARDIO_OWN_EXERCISE));
            stmt.setString(1, nick);
            stmt.setInt(2, exercise);
            stmt.execute();

            return true;
        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates de info about a custom cardio exercise
     * @param exercise
     * @param newExerciseName
     * @param newType
     * @return
     */
    public static boolean updateCustomExercise(int exercise, String newExerciseName, String newType){
        if(exercise<0 || newExerciseName==null || newType==null ||
            newExerciseName.equals("")  || newExerciseName.equals("")){
            return false;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("UPDATE %s SET %s=?, %s=? WHERE %s=? AND predefined=0;",
                    DBF_CARDIO_EXERCISES_TABLE_NAME, DBF_CARDIO_EXERCISE_NAME, DBF_CARDIO_EXERCISE_TYPE, DBF_CARDIO_EXERCISE_ID));
            stmt.setString(1, newExerciseName);
            stmt.setString(2, newType);
            stmt.setInt(3, exercise);
            stmt.execute();
            return true;
        } catch (MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a list of cardio exercises that a user owns
     * @return
     */
	public static List<CardioExerciseVO> listUserExercises(String user){
        if(user==null || user.equals("")){
            return null;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT ex.%2$s, ex.%3$s, ex.%4$s, ex.%5$s " +
                        "FROM %1$s ex, %6$s own WHERE ex.%2$s=own.%7$s AND own.%8$s=?;",
                    DBF_CARDIO_EXERCISES_TABLE_NAME, DBF_CARDIO_EXERCISE_ID, DBF_CARDIO_EXERCISE_NAME,
                    DBF_CARDIO_EXERCISE_TYPE, DBF_CARDIO_EXERCISE_PREDEFINED,
                    DBF_CARDIO_OWN_TABLE_NAME, DBF_CARDIO_OWN_EXERCISE, DBF_CARDIO_OWN_NICK));

            stmt.setString(1,user);

            ResultSet rs = stmt.executeQuery();

            ArrayList<CardioExerciseVO> list = new ArrayList();

            if(rs.first()) {
                do {
                    list.add(new CardioExerciseVO(rs.getInt(DBF_CARDIO_EXERCISE_ID), rs.getString(DBF_CARDIO_EXERCISE_NAME), rs.getString(DBF_CARDIO_EXERCISE_TYPE),
                        rs.getBoolean(DBF_CARDIO_EXERCISE_PREDEFINED)));
                } while (rs.next());
            }

			return list;

		}  catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
    /**
     * Returns a list of default cardio exercises
     * @return
     */
    public static List<CardioExerciseVO> listDefaultExercises(){
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "SELECT * FROM %s WHERE %s=?;",
                    DBF_CARDIO_EXERCISES_TABLE_NAME, DBF_CARDIO_EXERCISE_PREDEFINED));

            stmt.setBoolean(1,true);

            ResultSet rs = stmt.executeQuery();

            ArrayList<CardioExerciseVO> list = new ArrayList();

            if(rs.first()) {
                do {
                    list.add(new CardioExerciseVO(rs.getInt(DBF_CARDIO_EXERCISE_ID), rs.getString(DBF_CARDIO_EXERCISE_NAME), rs.getString(DBF_CARDIO_EXERCISE_TYPE),
                        rs.getBoolean(DBF_CARDIO_EXERCISE_PREDEFINED)));
                } while (rs.next());
            }

            return list;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
