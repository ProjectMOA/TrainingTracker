package org.trainingTracker.database.dataAccesObject;

import org.apache.xpath.SourceTree;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;
import org.trainingTracker.database.valueObject.UserVO;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class DAOMasterTest {

    /**
     * This test implements a simple script that verifies that every single functionality works.
     * Not trying to cover all cases.
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        System.out.println("--------------USERS----------------");

        //Add
        UsersDAO.addUser("JohnDoe1","JohnPass1","John1@a.com");
        UsersDAO.addUser("Alfonsa", "zz", "zz");
        UsersDAO.addUser("Nobody", "yy", "xx");

        //Delete
        UsersDAO.deleteUser("Nobody");

        //Find
        UserVO doe1 = UsersDAO.findUser("JohnDoe1");
        UserVO fault = UsersDAO.findUser("Nobody");

        Assert.assertEquals("JohnDoe1", doe1.getNick());
        Assert.assertEquals("JohnPass1", doe1.getPass());
        Assert.assertEquals("John1@a.com", doe1.getMail());

        Assert.assertEquals("Nickname: JohnDoe1 Mail: John1@a.com Pass: JohnPass1", doe1.toString().substring(0,52));

        Assert.assertEquals(fault, null);


        System.out.println("--------------EXERCISES----------------");

        //Add
        int pesas = ExercisesDAO.addExercise("Pesas","Brazo","JohnDoe1");
        int dominadas = ExercisesDAO.addExercise("Dominadas","Espalda","JohnDoe1");
        int sentadillas = ExercisesDAO.addExercise("Sentadillas","Pierna","JohnDoe1");
        int andar = ExercisesDAO.addExercise("Andar","Pierna","JohnDoe1");

        //List
        List<ExerciseVO> exercisesList = ExercisesDAO.listExercises("JohnDoe1");

        for (ExerciseVO exercise:exercisesList ) {
            System.out.println(exercise.toString());
        }

        System.out.println("Delete exercise");

        //Delete
        ExercisesDAO.deleteExercise(andar);

        //List Deleted
        exercisesList = ExercisesDAO.listExercises("JohnDoe1");

        for (ExerciseVO exercise:exercisesList ) {
            System.out.println(exercise.toString());
        }

        System.out.println("--------------RECORDS----------------");

        //Add
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 42.5, 3, 15);
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 45.5, 2, 10);
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 50, 1, 7);
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 52, 1, 5);

        //List
        System.out.println("----Non limited");
        List<RecordVO> recordsListNon = RecordsDAO.listRecords("JohnDoe1", sentadillas, 0);

        for (RecordVO exercise:recordsListNon ) {
            System.out.println(exercise.toString());
        }

        System.out.println("-----Limited to 4");
        List<RecordVO> recordsListLim = RecordsDAO.listRecords("JohnDoe1", sentadillas, 1);

        for (RecordVO exercise:recordsListLim ) {
            System.out.println(exercise.toString());
        }

        //Delete
        for (RecordVO ex:recordsListNon ) {
            RecordsDAO.deleteExercise(ex.getExercise(), ex.getUserNick(),ex.getRecordDate());
        }

        //ListDeleted
        System.out.println("----PrintDeleted");
        recordsListNon = RecordsDAO.listRecords("JohnDoe1", sentadillas, 0);

        for (RecordVO exercise:recordsListNon ) {
            System.out.println(exercise.toString());
        }

    }

}
