package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;

/**
 * Test class to check if the SaveRecord servlet works correctly.
 */
public class SaveRecordServletTest extends Mockito{

    private static final String BAD_WEIGHT_MESSAGE = "Peso no válido";
    private static final String BAD_SERIES_MESSAGE = "Número de series no válido";
    private static final String BAD_REPETITIONS_MESSAGE = "Número de repeticiones no válido";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to save a new record works correctly.
     */
    @Test
    public void saveRecordTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+PREDETERMINED_EXERCISE_ID+"\",\"weight\":\""+WEIGHT+"\"," +
            "\"series\":\""+SERIES+"\"," + "\"repetitions\":\""+REPETITIONS+"\",\"commentary\":\""+COMMENT+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().equals(JSON_EXERCISE_LIST_RESPONSE));
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
        assertTrue(sWriter.toString().contains(BAD_WEIGHT_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_SERIES_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_REPETITIONS_MESSAGE));
    }

    /*
     * Checks if there's an error message when the client sends wrong inputs
     * in the process of saving a new record.
     */
    @Test
    public void wrongInputsTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+PREDETERMINED_EXERCISE_ID+"\",\"weight\":\"0\"," +
            "\"series\":\"0\"," + "\"repetitions\":\"0\",\"commentary\":\""+COMMENT+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(BAD_WEIGHT_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_SERIES_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_REPETITIONS_MESSAGE));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
    private static void servletCall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new SaveRecord().doPost(request, response);
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
        UsersDAO.deleteUser(USERNAME);
    }
}
