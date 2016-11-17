package org.trainingTracker.database.dataAccesObject;

import org.junit.Assert;
import org.junit.Test;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;
import org.trainingTracker.database.valueObject.UserVO;

import java.util.List;

import static java.lang.Thread.sleep;

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

        //List default exercises
        System.out.println("---Default exercises");
        List<ExerciseVO> exercisesListDefault = ExercisesDAO.listDefaultExercises();
        for (ExerciseVO exercise:exercisesListDefault ) {
            System.out.println(exercise.toString());
        }

        //Add
        int pesas = ExercisesDAO.addCustomExercise("Pesas","Biceps","JohnDoe1");
        int sentadillas = ExercisesDAO.addCustomExercise("Sentadillas","Pierna","JohnDoe1");
        int andar = ExercisesDAO.addCustomExercise("Andar","Pierna","JohnDoe1");
        int dominadas = ExercisesDAO.addDefaultExercise( exercisesListDefault.get(0).getId(), "JohnDoe1" );


        //List User exercises
        System.out.println("---JohnDoe1 exercises");
        List<ExerciseVO> exercisesList = ExercisesDAO.listUserExercises("JohnDoe1");
        for (ExerciseVO exercise:exercisesList ) {
            System.out.println(exercise.toString());
        }

        //Delete
        System.out.println("Delete exercise");
        System.out.println("This two should be the same");
        System.out.println( ExercisesDAO.listDefaultExercises() );
        ExercisesDAO.deleteCustomExercise(1);
        System.out.println( ExercisesDAO.listDefaultExercises() );
        System.out.println("This two should be different");


        //List Deleted
        exercisesList = ExercisesDAO.listUserExercises("JohnDoe1");

        for (ExerciseVO exercise:exercisesList ) {
            System.out.println(exercise.toString());
        }

        System.out.println("--------------RECORDS----------------");

        //Add
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 42.5, 3, 15, "A bit exhausted");
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 45.5, 2, 10, "Much better");
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 50, 1, 7, "Dammit my ass");
        sleep(1000);
        RecordsDAO.addRecord(sentadillas,"JohnDoe1", 52, 1, 5, "Icnt even write");

        //List
        System.out.println("----Non limited");
        List<RecordVO> recordsListNon = RecordsDAO.listRecords("JohnDoe1", sentadillas, 0);

        for (RecordVO exercise:recordsListNon ) {
            System.out.println(exercise.toString());
        }

        System.out.println("-----Limited to 1");
        List<RecordVO> recordsListLim = RecordsDAO.listRecords("JohnDoe1", sentadillas, 1);

        for (RecordVO exercise:recordsListLim ) {
            System.out.println(exercise.toString());
        }

        //Delete
        for (RecordVO ex:recordsListNon ) {
            RecordsDAO.deleteRecord(ex.getExercise(), ex.getUserNick(), ex.getRecordDate());
        }

        //ListDeleted
        System.out.println("----PrintDeleted");
        recordsListNon = RecordsDAO.listRecords("JohnDoe1", sentadillas, 0);

        for (RecordVO exercise:recordsListNon ) {
            System.out.println(exercise.toString());
        }

    }

}
