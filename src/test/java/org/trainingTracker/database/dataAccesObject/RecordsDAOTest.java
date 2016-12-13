package org.trainingTracker.database.dataAccesObject;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trainingTracker.database.valueObject.RecordVO;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by sergio on 13/12/16.
 */
public class RecordsDAOTest {

    public static final String USR = "usr_Records";

    @BeforeClass
    public static void setUp() throws Exception {
        UsersDAO.addUser(USR,"pass","mail");
        for (int i = 0; i < 10 ; i++) {
            RecordsDAO.addRecord(1,USR,0,0,i,""); //Value that changes is repetitions
            Thread.sleep(1000);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        UsersDAO.deleteUser(USR);
    }

    @Test
    public void addRecord() throws Exception {
        //TODO: Implement

    }

    @Test
    public void deleteRecord() throws Exception {
        //TODO: Implement
    }

    @Test
    public void listRecords() throws Exception {
        List<RecordVO> list1 = RecordsDAO.listRecords(USR,1,3,1);
        List<RecordVO> list2 = RecordsDAO.listRecords(USR,1,3,2);
        List<RecordVO> list3 = RecordsDAO.listRecords(USR,1,3,3);
        List<RecordVO> list4 = RecordsDAO.listRecords(USR,1,3,4);

        List<RecordVO> listBulk = RecordsDAO.listRecords(USR,1,0,0);
        List<RecordVO> listAll = new ArrayList<RecordVO>();

        listAll.addAll(list1);
        listAll.addAll(list2);
        listAll.addAll(list3);
        listAll.addAll(list4);

        Assert.assertEquals(10,list1.size()+list2.size()+list3.size()+list4.size());

        for (int i = 0; i < listBulk.size(); i++) {
            Assert.assertEquals(listBulk.get(i).getRepetitions(),listAll.get(i).getRepetitions());

        }

        Assert.assertEquals(0, RecordsDAO.listRecords(null,1,3,1).size()); //Null name won't crash and be empty
        Assert.assertEquals(0, RecordsDAO.listRecords(USR,1,-1,1).size()); //Negate page size must be empty

        Assert.assertEquals(0, RecordsDAO.listRecords(USR,1,3,0).size()); //Page zero must be empty
        Assert.assertEquals(0, RecordsDAO.listRecords(USR,1,3,-1).size()); //Negative pages should not exist



    }

}
