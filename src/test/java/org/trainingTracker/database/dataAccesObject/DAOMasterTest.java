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

import static org.junit.Assert.*;

/**
 * Created by sergio on 28/10/16.
 */
public class DAOMasterTest {

    @Test
    public void test() throws Exception {

        System.out.println("--------------USERS----------------");

        UsersDAO.addUser("JohnDoe1","JohnPass1","John1@a.com");
        System.out.println( UsersDAO.addUser("Alfonsa", "zz", "zz"));

        UserVO doe1 = UsersDAO.findUser("JohnDoe1");
        UserVO fault = UsersDAO.findUser("Nobody");

        Assert.assertEquals("JohnDoe1", doe1.getNick());
        Assert.assertEquals("JohnPass1", doe1.getPass());
        Assert.assertEquals("John1@a.com", doe1.getMail());

        Assert.assertEquals("Nickname: JohnDoe1 Mail: John1@a.com Pass: JohnPass1", doe1.toString().substring(0,52));

        Assert.assertEquals(fault, null);


        System.out.println("--------------EXERCISES----------------");

        int pesas = ExercisesDAO.addExercise("Pesas","Brazo","JohnDoe1");
        int dominadas = ExercisesDAO.addExercise("Dominadas","Espalda","JohnDoe1");
        int sentadillas = ExercisesDAO.addExercise("Sentadillas","Pierna","JohnDoe1");
        int andar = ExercisesDAO.addExercise("Andar","Pierna","Alfonsa");

        List<ExerciseVO> exercisesList = ExercisesDAO.listExercises("JohnDoe1");

        for (ExerciseVO exercise:exercisesList ) {
            System.out.println(exercise.toString());
        }


        System.out.println("--------------RECORDS----------------");

        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 42.5, 3, 10);
        RecordsDAO.addRecord(pesas,"JohnDoe1", 45.5, 2, 20);

        System.out.println("----Non limited");
        List<RecordVO> recordsListNon = RecordsDAO.listRecords("JohnDoe1", pesas, 0);

        for (RecordVO exercise:recordsListNon ) {
            System.out.println(exercise.toString());
        }

        System.out.println("-----Limited to 4");
        List<RecordVO> recordsListLim = RecordsDAO.listRecords("JohnDoe1", pesas, 4);

        for (RecordVO exercise:recordsListLim ) {
            System.out.println(exercise.toString());
        }
    }

}
