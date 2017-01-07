package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;
import static org.trainingTracker.servlets.ServletTestUtils.mocksSetUp;

/**
 * Test class to check if the RemoveExercise servlet works correctly.
 */
public class RemoveExerciseServletTest extends Mockito{

    private static int ExerciseID;
    private static final String NON_EXISTING_EXERCISE_MESSAGE = "Este ejercicio no forma parte de su rutina";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
        RecordsDAO.addRecord(PREDETERMINED_EXERCISE_ID, USERNAME, Double.parseDouble(WEIGHT),
            Integer.parseInt(SERIES), Integer.parseInt(REPETITIONS), COMMENT);
        ExerciseID = ExercisesDAO.addCustomExercise(EXERCISE, MG, USERNAME);
        RecordsDAO.addRecord(ExerciseID, USERNAME, Double.parseDouble(WEIGHT),
            Integer.parseInt(SERIES), Integer.parseInt(REPETITIONS), COMMENT);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to remove an existing predetermined exercise works correctly.
     */
    @Test
    public void removePredeterminedExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+PREDETERMINED_EXERCISE_ID+"\"}";
        String responseMessage = "[{\"id\":\""+ExerciseID+"\",\"name\":\"My Exercise\",\"muscleGroup\":\"Espalda\"," +
            "\"predetermined\":false,\"weight\":\"10.2\",\"series\":\"4\",\"repetitions\":\"12\"}]";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        System.out.println(sWriter.toString());
        assertTrue(sWriter.toString().equals(responseMessage) || sWriter.toString().equals("{\"listPerformed\":[]," +
            "\"listCardioPerformed\":[]}"));
    }

    /*
     * Checks if the process to remove an existing custom exercise works correctly.
     */
    @Test
    public void removeCustomExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+ExerciseID+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().equals(JSON_EXERCISE_LIST_RESPONSE) || sWriter.toString().equals("[]"));
    }

    /*
     * Checks if there's an error when the client sends a bad request to the server.
     */
    @Test
    public void badRequestTest(){
        String body = "fail";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(INTERNAL_ERROR_MESSAGE));
    }

    /*
     * Checks if the process to remove a non existing exercise works correctly.
     * It should end with an error message.
     */
    @Test
    public void nonExistingExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+2+"\"}";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(NON_EXISTING_EXERCISE_MESSAGE));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
    private static void servletCall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new RemoveExercise().doPost(request, response);
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
        ExercisesDAO.deleteCustomExercise(ExerciseID);
        UsersDAO.deleteUser(USERNAME);
    }
}
