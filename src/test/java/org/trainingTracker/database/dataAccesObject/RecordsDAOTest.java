package org.trainingTracker.database.dataAccesObject;

import org.junit.Ignore;
import org.junit.Test;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sergio on 31/10/16.
 */
public class RecordsDAOTest {
    @Test
    @Ignore
    public void addRecord() throws Exception {
        RecordsDAO.addRecord(1,"JohnDoe1", 42.5, 3, 10);
        RecordsDAO.addRecord(2,"JohnDoe1", 45.5, 2, 20);
    }

    @Test
    @Ignore
    public void listRecords() throws Exception {

        System.out.println("Non limited");
        List<RecordVO> list = RecordsDAO.listRecords("JohnDoe1", 1, 0);

        for (RecordVO exercise:list ) {
            System.out.println(exercise.toString());
        }

        System.out.println("Limited to 4");
        list = RecordsDAO.listRecords("JohnDoe1", 1, 4);

        for (RecordVO exercise:list ) {
            System.out.println(exercise.toString());
        }
    }

}
