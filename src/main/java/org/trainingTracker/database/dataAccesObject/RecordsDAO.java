package org.trainingTracker.database.dataAccesObject;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.trainingTracker.database.conection.ConnectionPool;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercises's Data Access Object
 */
public class RecordsDAO {

    //Database Field
    public static final String DBF_RECORD_TABLE_NAME = "Records";
    public static final String DBF_RECORD_EXERCISE = "exercise";
    public static final String DBF_RECORD_NICK = "user_nick";
    public static final String DBF_RECORD_WEIGHT = "weigth";
    public static final String DBF_RECORD_SERIES = "series";
    public static final String DBF_RECORD_REPETITIONS = "repetitions";
    public static final String DBF_RECORD_COMMENT = "comment";
    public static final String DBF_RECORD_DATE = "record_date";


    /**
     * Adds the given record to the data base.
     * @param exercise
     * @param user_nick
     * @param weight
     * @param series
     * @param repetitions
     * @return
     */
	public static boolean addRecord( int exercise, String user_nick, double weight, int series,
                                     int repetitions, String comment){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format( "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?);",
                    DBF_RECORD_TABLE_NAME, DBF_RECORD_EXERCISE, DBF_RECORD_NICK, DBF_RECORD_WEIGHT,
                    DBF_RECORD_SERIES, DBF_RECORD_REPETITIONS, DBF_RECORD_COMMENT));
            stmt.setInt(1,exercise);
            stmt.setString(2,user_nick);
            stmt.setDouble(3,weight);
            stmt.setInt(4,series);
            stmt.setInt(5,repetitions);
            stmt.setString(6,comment);

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
     * Deletes the record identified by exercise, user and date
     * @param exercise
     * @param user
     * @param date
     * @return
     */
    public static boolean deleteRecord(int exercise, String user, String date){
        Connection conn = null;

        try{
            Class.forName(ConnectionPool.JDBC_DRIVER);
            conn = ConnectionPool.requestConnection();

            PreparedStatement stmt = conn.prepareStatement(
                String.format("DELETE FROM %s WHERE %S=? AND %S=? AND %S=?;",
                    DBF_RECORD_TABLE_NAME,DBF_RECORD_EXERCISE, DBF_RECORD_NICK, DBF_RECORD_DATE));
            stmt.setInt(1, exercise);
            stmt.setString(2, user);
            stmt.setString(3, date);

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
     * Returns a list a range of records saved for a given user and exercises.
     * @return
     */
	public static List<RecordVO> listRecords(String user, int exercise, int size){
		Connection conn = null;
		try{
			Class.forName(ConnectionPool.JDBC_DRIVER);
			conn = ConnectionPool.requestConnection();

            PreparedStatement stmt;

            //If size is lower than 0 it wont have a size limit
            if (size > 0) {
                stmt = conn.prepareStatement(
                    String.format( "SELECT * FROM %s WHERE %s=? AND %s=? " +
                        "ORDER BY %s DESC LIMIT ?;",DBF_RECORD_TABLE_NAME, DBF_RECORD_EXERCISE,
                        DBF_RECORD_NICK, DBF_RECORD_DATE));
                stmt.setInt(3,size);
            } else {
                stmt = conn.prepareStatement(
                    String.format( "SELECT * FROM %s WHERE %s=? AND %s=? " +
                        "ORDER BY %s DESC;", DBF_RECORD_TABLE_NAME, DBF_RECORD_EXERCISE,
                        DBF_RECORD_NICK, DBF_RECORD_DATE));
            }

            stmt.setInt(1,exercise);

            stmt.setString(2,user);

            ResultSet rs = stmt.executeQuery();

            ArrayList<RecordVO> list = new ArrayList();

            if( rs.first() ){
                do {
                    list.add(new RecordVO(rs.getInt(DBF_RECORD_EXERCISE), rs.getString(DBF_RECORD_NICK),
                        rs.getDouble(DBF_RECORD_WEIGHT), rs.getInt(DBF_RECORD_SERIES),
                        rs.getInt(DBF_RECORD_REPETITIONS), rs.getString(DBF_RECORD_COMMENT), rs.getString(DBF_RECORD_DATE)));
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
