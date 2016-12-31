package org.trainingTracker.database.dataAccesObject;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.trainingTracker.database.valueObject.CardioExerciseVO;

import java.util.List;

/**
 * Created by sergio on 7/12/16.
 */
public class CardioExercisesDAOTest {

    public static final String USR = "usr_Exercises";
    private String carrera;

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
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);

        Assert.assertEquals(0, list.size());

        CardioExercisesDAO.addDefaultExercise(1,USR);
        CardioExercisesDAO.addDefaultExercise(2,USR);
        CardioExercisesDAO.addDefaultExercise(5,USR);

        list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1,list.get(0).getId()); //Be careful with order
        Assert.assertEquals(2,list.get(1).getId()); //Be careful with order
        Assert.assertEquals(5,list.get(2).getId()); //Be careful with order

        CardioExercisesDAO.deleteOwnExercise(USR,1);
        CardioExercisesDAO.deleteOwnExercise(USR,2);
        CardioExercisesDAO.deleteOwnExercise(USR,5);

        list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void addCustomExercise() throws Exception {
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0,list.size());

        int exerciseNum = CardioExercisesDAO.addCustomExercise("ex_1","carrera",USR);

        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise("","carrera",USR) );
        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise("ex_1","",USR) );
        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise("ex_1","carrera","") );

        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise(null,"carrera",USR) );
        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise("ex_1",null,USR) );
        Assert.assertEquals( -1, CardioExercisesDAO.addCustomExercise("ex_1","carrera",null) );

        list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1,list.size());

        CardioExercisesDAO.deleteCustomExercise(exerciseNum);
    }

    @Test
    public void deleteCustomExercise() throws Exception {
        int exerciseNum = CardioExercisesDAO.addCustomExercise("ex_1","carrera",USR);
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1,list.size());

        Assert.assertFalse( CardioExercisesDAO.deleteCustomExercise(-1) );
        Assert.assertTrue( CardioExercisesDAO.deleteCustomExercise(9999999) );
        list = CardioExercisesDAO.listUserExercises(USR); //Not deleted yet
        Assert.assertEquals(1,list.size());

        Assert.assertTrue( CardioExercisesDAO.deleteCustomExercise(exerciseNum) );
        list = CardioExercisesDAO.listUserExercises(USR); //Already deleted
        Assert.assertEquals(0,list.size());
    }

    @Test
    public void deleteOwnExercise() throws Exception {
        int customExerciseNum = CardioExercisesDAO.addCustomExercise("ex_1","carrera",USR);
        int defaultExerciseNum = CardioExercisesDAO.addDefaultExercise(1,USR);
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(2,list.size());

        Assert.assertFalse( CardioExercisesDAO.deleteOwnExercise(null, customExerciseNum) );
        Assert.assertFalse( CardioExercisesDAO.deleteOwnExercise("", customExerciseNum) );
        Assert.assertFalse( CardioExercisesDAO.deleteOwnExercise(USR, -1) );

        list = CardioExercisesDAO.listUserExercises(USR); //Not yet deleted
        Assert.assertEquals(2,list.size());

        Assert.assertTrue( CardioExercisesDAO.deleteOwnExercise(USR, customExerciseNum) );
        Assert.assertTrue( CardioExercisesDAO.deleteOwnExercise(USR, defaultExerciseNum) );

        Assert.assertTrue( CardioExercisesDAO.deleteCustomExercise(customExerciseNum) );
        list = CardioExercisesDAO.listUserExercises(USR); //Already deleted
        Assert.assertEquals(0,list.size());

        Assert.assertTrue( CardioExercisesDAO.deleteCustomExercise(customExerciseNum) ); //Clean up orphan exercise
    }

    @Test
    public void updateCustomExercise() throws Exception {
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());

        CardioExercisesDAO.addCustomExercise("test_1","carrera", USR);
        list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("test_1",list.get(0).getName());

        CardioExercisesDAO.updateCustomExercise(list.get(0).getId(),"test_5", "kayak");

        list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("test_5",list.get(0).getName());

        CardioExercisesDAO.deleteOwnExercise(USR, list.get(0).getId());
    }

    @Test
    public void listUserExercises() throws Exception {
        List<CardioExerciseVO> list = CardioExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void listDefaultExercises() throws Exception {
        //TODO Implement
    }

}
