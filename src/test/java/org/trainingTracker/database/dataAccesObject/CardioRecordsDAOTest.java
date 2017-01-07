package org.trainingTracker.database.dataAccesObject;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.trainingTracker.database.valueObject.CardioRecordVO;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergio on 13/12/16.
 */
public class CardioRecordsDAOTest {

    public static final String USR = "usr_Records";

    @BeforeClass
    public static void setUp() throws Exception {
        UsersDAO.addUser(USR,"pass","mail");
        for (int i = 0; i < 10 ; i++) {
            //CardioRecordsDAO.addRecord(1,USR,Time.valueOf("8:15:30"),i,""); //Value that changes is repetitions
            //Thread.sleep(1000);
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        UsersDAO.deleteUser(USR);
    }

    @Ignore
    @Test
    public void addRecord() throws Exception {
        List<CardioRecordVO> list = CardioRecordsDAO.listRecords(USR,2,0,1); //List all
        Assert.assertEquals(0,list.size());

        //Assert.assertTrue( CardioRecordsDAO.addRecord(2,USR,Time.valueOf("8:15:30"),0,"") );
        Thread.sleep(1000);
        //Assert.assertFalse( CardioRecordsDAO.addRecord(2,USR,Time.valueOf("8:15:30"),0,null) );
        Thread.sleep(1000);
        Thread.sleep(1000);
        //Assert.assertFalse( CardioRecordsDAO.addRecord(0,USR,Time.valueOf("8:15:30"),0,"") );
        Thread.sleep(1000);
        //Assert.assertFalse( CardioRecordsDAO.addRecord(2,"",Time.valueOf("8:15:30"),0,"") );
        Thread.sleep(1000);
        //Assert.assertFalse( CardioRecordsDAO.addRecord(2,"NONEXISTINGUSER",Time.valueOf("8:15:30"),0,"") );

        list = CardioRecordsDAO.listRecords(USR,2,0,1); //List all
        Assert.assertEquals(1,list.size());

    }

    @Ignore
    @Test
    public void deleteRecord() throws Exception {
        //Assert.assertTrue( CardioRecordsDAO.addRecord(2,USR,Time.valueOf("8:15:30"),0,"") );
        List<CardioRecordVO> list = CardioRecordsDAO.listRecords(USR,2,0,1);
        Assert.assertEquals(1,list.size());

        Assert.assertFalse( CardioRecordsDAO.deleteRecord(2,null, list.get(0).getRecordDate()) );
        Assert.assertFalse( CardioRecordsDAO.deleteRecord(2,USR, "") );
        Assert.assertFalse( CardioRecordsDAO.deleteRecord(2,USR, null) );

        list = CardioRecordsDAO.listRecords(USR,2,0,1);
        Assert.assertEquals(1,list.size());

        CardioRecordsDAO.deleteRecord(2,USR,list.get(0).getRecordDate());
        list = CardioRecordsDAO.listRecords(USR,2,0,1);
        Assert.assertEquals(0,list.size());

    }

    @Ignore
    @Test
    public void listRecords() throws Exception {
        List<CardioRecordVO> list1 = CardioRecordsDAO.listRecords(USR,1,3,1);
        List<CardioRecordVO> list2 = CardioRecordsDAO.listRecords(USR,1,3,2);
        List<CardioRecordVO> list3 = CardioRecordsDAO.listRecords(USR,1,3,3);
        List<CardioRecordVO> list4 = CardioRecordsDAO.listRecords(USR,1,3,4);

        List<CardioRecordVO> listBulk = CardioRecordsDAO.listRecords(USR,1,0,1);
        List<CardioRecordVO> listAll = new ArrayList<>();

        listAll.addAll(list1);
        listAll.addAll(list2);
        listAll.addAll(list3);
        listAll.addAll(list4);

        Assert.assertEquals(10,list1.size()+list2.size()+list3.size()+list4.size());

        for (int i = 0; i < listBulk.size(); i++) {
            Assert.assertEquals(listBulk.get(i).toString(),listAll.get(i).toString());

        }

        Assert.assertEquals(0, CardioRecordsDAO.listRecords(null,1,3,1).size()); //Null name won't crash and be empty
        Assert.assertEquals(0, CardioRecordsDAO.listRecords(USR,1,-1,1).size()); //Negate page size must be empty

        Assert.assertEquals(0, CardioRecordsDAO.listRecords(USR,1,3,0).size()); //Page zero must be empty
        Assert.assertEquals(0, CardioRecordsDAO.listRecords(USR,1,3,-1).size()); //Negative pages should not exist

    }

}
