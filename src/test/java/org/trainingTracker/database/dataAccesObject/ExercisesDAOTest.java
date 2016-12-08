package org.trainingTracker.database.dataAccesObject;

import org.junit.*;
import org.trainingTracker.database.valueObject.ExerciseVO;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sergio on 7/12/16.
 */
public class ExercisesDAOTest {

    public static final String USR = "usr_Exercises";

    @BeforeClass
    public static void setUp() throws Exception {
        UsersDAO.addUser(USR,"pass","mail");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        UsersDAO.deleteUser(USR);
    }

    @Test
    public void addDefaultExercise() throws Exception {
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);

        Assert.assertEquals(0, list.size());

        ExercisesDAO.addDefaultExercise(1,USR);
        ExercisesDAO.addDefaultExercise(2,USR);
        ExercisesDAO.addDefaultExercise(5,USR);

        list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1,list.get(0).getId()); //Be careful with order
        Assert.assertEquals(2,list.get(1).getId()); //Be careful with order
        Assert.assertEquals(5,list.get(2).getId()); //Be careful with order

        ExercisesDAO.deleteOwnExercise(USR,1);
        ExercisesDAO.deleteOwnExercise(USR,2);
        ExercisesDAO.deleteOwnExercise(USR,5);

        list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void addCustomExercise() throws Exception {
        //TODO: Implement
    }

    @Test
    public void deleteCustomExercise() throws Exception {
        //TODO: Implement
    }

    @Test
    public void deleteOwnExercise() throws Exception {
        //TODO: Implement
    }

    @Test
    public void updateCustomExercise() throws Exception {
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());

        ExercisesDAO.addCustomExercise("test_1","Pierna", USR);
        list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("test_1",list.get(0).getName());

        ExercisesDAO.updateCustomExercise(list.get(0).getId(),"test_5", "Bíceps");

        list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("test_5",list.get(0).getName());

        ExercisesDAO.deleteOwnExercise(USR, list.get(0).getId());
    }

    @Test
    public void listUserExercises() throws Exception {
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void listDefaultExercises() throws Exception {
        //TODO: Implement
    }

}
