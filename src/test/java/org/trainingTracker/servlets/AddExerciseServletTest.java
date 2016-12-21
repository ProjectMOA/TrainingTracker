package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;
import static org.trainingTracker.servlets.ServletTestUtils.mocksSetUp;

public class AddExerciseServletTest extends Mockito{

    private static final String PREDETERMINED_EXERCISE_ADDED_MESSAGE = "Ejercicio predefinido añadido";
    private static final String CUSTOM_EXERCISE_ADDED_MESSAGE = "Ejercicio personalizado añadido";
    private static final String EXISTING_EXERCISE_MESSAGE = "Este ejercicio ya forma parte de su rutina";
    private static final String EXISTING_PREDETERMINED_EXERCISE_MESSAGE = "Este ejercicio forma parte de los predefinidos";
    private static int ExerciseID;
    private static final String EXERCISE2 = "My Exercise2";
    private static final String MG2 = "Hombro";
    private static final String PREDETERMINED_EXERCISE_NAME = "Press disco";
    private static final String PREDETERMINED_EXERCISE_MG = "Pecho";


    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExerciseID = ExercisesDAO.addCustomExercise(EXERCISE, MG, USERNAME);
        RecordsDAO.addRecord(ExerciseID, USERNAME, Double.parseDouble(WEIGHT),
            Integer.parseInt(SERIES), Integer.parseInt(REPETITIONS), COMMENT);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    @Test
    public void addPredeterminedExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":"+PREDETERMINED_EXERCISE_ID+",\"name\":\""+EXERCISE2+"\"," +
            "\"muscleGroup\":\""+MG2+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(PREDETERMINED_EXERCISE_ADDED_MESSAGE));
    }

    @Test
    public void addCustomExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":0,\"name\":\""+EXERCISE2+"\"," +
            "\"muscleGroup\":\""+MG2+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(CUSTOM_EXERCISE_ADDED_MESSAGE));
    }

    @Test
    public void addExistingExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":0,\"name\":\""+EXERCISE+"\"," +
            "\"muscleGroup\":\""+MG+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(EXISTING_EXERCISE_MESSAGE));
    }

    @Test
    public void addCustomExerciseEqualToPredetermined(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":0,\"name\":\""+PREDETERMINED_EXERCISE_NAME+"\"," +
            "\"muscleGroup\":\""+PREDETERMINED_EXERCISE_MG+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(EXISTING_PREDETERMINED_EXERCISE_MESSAGE));
    }


    @Test
    public void badRequestTest(){
        String body = "fail";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().contains(INTERNAL_ERROR_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_EXERCISE_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_MG_MESSAGE));
    }

    private static void servletcall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new AddExercise().doPost(request, response);
            verify(request, atLeast(1)).getReader();
            writer.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ServletException e){
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown(){
        ExercisesDAO.deleteOwnExercise(USERNAME, PREDETERMINED_EXERCISE_ID);
        Iterator<ExerciseVO> iter = (ExercisesDAO.listUserExercises(USERNAME)).iterator();
        while(iter.hasNext()){
            ExercisesDAO.deleteCustomExercise(iter.next().getId());
        }
        UsersDAO.deleteUser(USERNAME);
    }
}
