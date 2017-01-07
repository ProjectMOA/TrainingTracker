package org.trainingTracker.database.dataAccesObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.CardioRecordVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercises's Data Access Object
 */
public class CardioRecordsDAO {

    //Database Field
    public static final String DBF_CARDIO_RECORD_TABLE_NAME = "CardioRecords";
    public static final String DBF_CARDIO_RECORD_EXERCISE = "exercise";
    public static final String DBF_CARDIO_RECORD_NICK = "user_nick";
    public static final String DBF_CARDIO_RECORD_DISTANCE = "distance";
    public static final String DBF_CARDIO_RECORD_TIME = "time";
    public static final String DBF_CARDIO_RECORD_INTENSITY = "intensity";
    public static final String DBF_CARDIO_RECORD_DATE = "record_date";

    /**
     * Adds the given cardio record to the data base.
     * @param exercise
     * @param user_nick
     * @param time
     * @param intensity
     * @param comment
     * @return
     */
	public static boolean addRecord(int exercise, String user_nick, double distance, Time time, int intensity){
        if(exercise<0 || user_nick==null || user_nick.equals("")  || distance<0  || time==null || intensity<0){
            return false;
        }
		try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);",
                    DBF_CARDIO_RECORD_TABLE_NAME, DBF_CARDIO_RECORD_EXERCISE, DBF_CARDIO_RECORD_NICK,
                    DBF_CARDIO_RECORD_DISTANCE, DBF_CARDIO_RECORD_TIME, DBF_CARDIO_RECORD_INTENSITY));
            stmt.setInt(1,exercise);
            stmt.setString(2,user_nick);
            stmt.setDouble(3,distance);
            stmt.setString(4,time.toString());
            stmt.setInt(5,intensity);

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
     * Deletes the record identified by exercise, user and date
     * @param exercise
     * @param user
     * @param date
     * @return
     */
    public static boolean deleteRecord(int exercise, String user, String date){
        if( user == null || date == null || date.equals("")){
            return false;
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=? AND %S=? AND %S=?;",
                    DBF_CARDIO_RECORD_TABLE_NAME, DBF_CARDIO_RECORD_EXERCISE, DBF_CARDIO_RECORD_NICK, DBF_CARDIO_RECORD_DATE));
            stmt.setInt(1, exercise);
            stmt.setString(2, user);
            stmt.setString(3, date);

            stmt.execute(); //Executes the deletion
            return true;

        } catch (MySQLIntegrityConstraintViolationException e) {

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a list a range of records saved for a given user and exercises.
     * @return
     */
	public static List<CardioRecordVO> listRecords(String user, int exercise, int size, int page){
        if( page<1 || size < 0 ){ //Prevents SQL syntax error
            return new ArrayList<>();
        }
        try (Connection conn = ConnectionPool.requestConnection()) {

            PreparedStatement stmt;

            //If size is zero it wont have a size limit
            if (size > 0) {
                stmt = conn.prepareStatement(
                    String.format( "SELECT * FROM %s WHERE %s=? AND %s=? " +
                        "ORDER BY %s DESC LIMIT ? OFFSET ?;", DBF_CARDIO_RECORD_TABLE_NAME, DBF_CARDIO_RECORD_EXERCISE,
                        DBF_CARDIO_RECORD_NICK, DBF_CARDIO_RECORD_DATE));
                stmt.setInt(3,size);
                stmt.setInt(4,(page-1)*size);
            } else {
                stmt = conn.prepareStatement(
                    String.format( "SELECT * FROM %s WHERE %s=? AND %s=? " +
                        "ORDER BY %s DESC;", DBF_CARDIO_RECORD_TABLE_NAME, DBF_CARDIO_RECORD_EXERCISE,
                        DBF_CARDIO_RECORD_NICK, DBF_CARDIO_RECORD_DATE));
            }

            stmt.setInt(1,exercise);

            stmt.setString(2,user);

            ResultSet rs = stmt.executeQuery();

            ArrayList<CardioRecordVO> list = new ArrayList();

            if( rs.first() ){
                do {
                    list.add(new CardioRecordVO(rs.getInt(DBF_CARDIO_RECORD_EXERCISE), rs.getString(DBF_CARDIO_RECORD_NICK),
                        rs.getString(DBF_CARDIO_RECORD_DISTANCE), rs.getString(DBF_CARDIO_RECORD_TIME),
                        rs.getInt(DBF_CARDIO_RECORD_INTENSITY), rs.getString(DBF_CARDIO_RECORD_DATE)));
                } while (rs.next());
            }
			return list;

		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
}
