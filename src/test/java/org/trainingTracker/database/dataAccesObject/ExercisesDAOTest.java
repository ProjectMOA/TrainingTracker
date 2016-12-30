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
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(0,list.size());

        int exerciseNum = ExercisesDAO.addCustomExercise("ex_1","pierna",USR);

        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise("","pierna",USR) );
        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise("ex_1","",USR) );
        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise("ex_1","pierna","") );

        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise(null,"pierna",USR) );
        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise("ex_1",null,USR) );
        Assert.assertEquals( -1, ExercisesDAO.addCustomExercise("ex_1","pierna",null) );

        list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1,list.size());

        ExercisesDAO.deleteCustomExercise(exerciseNum);
    }

    @Test
    public void deleteCustomExercise() throws Exception {
        int exerciseNum = ExercisesDAO.addCustomExercise("ex_1","pierna",USR);
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(1,list.size());

        Assert.assertFalse( ExercisesDAO.deleteCustomExercise(-1) );
        Assert.assertTrue( ExercisesDAO.deleteCustomExercise(9999999) );
        list = ExercisesDAO.listUserExercises(USR); //Not deleted yet
        Assert.assertEquals(1,list.size());

        Assert.assertTrue( ExercisesDAO.deleteCustomExercise(exerciseNum) );
        list = ExercisesDAO.listUserExercises(USR); //Already deleted
        Assert.assertEquals(0,list.size());
    }

    @Test
    public void deleteOwnExercise() throws Exception {
        int customExerciseNum = ExercisesDAO.addCustomExercise("ex_1","pierna",USR);
        int defaultExerciseNum = ExercisesDAO.addDefaultExercise(1,USR);
        List<ExerciseVO> list = ExercisesDAO.listUserExercises(USR);
        Assert.assertEquals(2,list.size());

        Assert.assertFalse( ExercisesDAO.deleteOwnExercise(null, customExerciseNum) );
        Assert.assertFalse( ExercisesDAO.deleteOwnExercise("", customExerciseNum) );
        Assert.assertFalse( ExercisesDAO.deleteOwnExercise(USR, -1) );

        list = ExercisesDAO.listUserExercises(USR); //Not yet deleted
        Assert.assertEquals(2,list.size());

        Assert.assertTrue( ExercisesDAO.deleteOwnExercise(USR, customExerciseNum) );
        Assert.assertTrue( ExercisesDAO.deleteOwnExercise(USR, defaultExerciseNum) );

        Assert.assertTrue( ExercisesDAO.deleteCustomExercise(customExerciseNum) );
        list = ExercisesDAO.listUserExercises(USR); //Already deleted
        Assert.assertEquals(0,list.size());

        Assert.assertTrue( ExercisesDAO.deleteCustomExercise(customExerciseNum) ); //Clean up orphan exercise
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
        List<ExerciseVO> list = ExercisesDAO.listDefaultExercises();
        Assert.assertTrue( list!=null && list.size() > 0);
    }

}
