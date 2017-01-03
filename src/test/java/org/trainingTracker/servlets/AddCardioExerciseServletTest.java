package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;
import org.trainingTracker.database.valueObject.CardioExerciseVO;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;
import static org.trainingTracker.servlets.ServletTestUtils.mocksSetUp;
import static org.trainingTracker.servlets.ServletTestUtils.writerSetUp;

/**
 * Test class to check if the AddCardioExercise servlet works correctly.
 */
public class AddCardioExerciseServletTest extends Mockito{

    private static final int CARDIO_EXERCISE_ID = 1;
    private static final int CARDIO_EXERCISE_ID2 = 2;
    private static final String CARDIO_TYPE = "Carrera";
    private static final String CARDIOVASCULAR_EXERCISE_ADDED_MESSAGE = "Ejercicio cardiovascular añadido";
    private static final String WRONG_TYPE_MESSAGE = "Debe seleccionar un tipo de ejercicio cardiovascular";
    private static final String EXISTING_EXERCISE_MESSAGE = "Este ejercicio ya forma parte de su rutina";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        CardioExercisesDAO.addDefaultExercise(CARDIO_EXERCISE_ID, USERNAME);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to add a new cardiovascular exercise works correctly.
     */
    @Test
    public void addCardiovascularExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+CARDIO_EXERCISE_ID2+"\",\"cardioType\":\""+CARDIO_TYPE+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(CARDIOVASCULAR_EXERCISE_ADDED_MESSAGE));
    }

    /*
    * Checkes if the process to add an existing exercise works correctly.
    * The proccess should end with an error message.
    */
    @Test
    public void addExistingExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+CARDIO_EXERCISE_ID+"\",\"cardioType\":\""+CARDIO_TYPE+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().equals(EXISTING_EXERCISE_MESSAGE));
    }

    /*
     * Checks if there's an error when the client sends a bad request to the server.
     */
    @Test
    public void badRequestTest(){
        String body = "fail";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletcall(bf);
        assertTrue(sWriter.toString().contains(INTERNAL_ERROR_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_EXERCISE_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_TYPE_MESSAGE));
    }

    /*
    * Sets what the mocks must return when they are called from the servlet
    * and makes a call to the servlet that is being tested.
    */
    private static void servletcall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new AddCardioExercise().doPost(request, response);
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
        Iterator<CardioExerciseVO> iter = (CardioExercisesDAO.listUserExercises(USERNAME)).iterator();
        while(iter.hasNext()){
            CardioExercisesDAO.deleteOwnExercise(USERNAME, iter.next().getId());
        }
        UsersDAO.deleteUser(USERNAME);
    }
}
