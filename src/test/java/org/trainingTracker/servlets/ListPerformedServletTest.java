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
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;

/**
 * Test class to check if the ListPerformed servlet works correctly.
 */
public class ListPerformedServletTest extends Mockito {

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
        RecordsDAO.addRecord(PREDETERMINED_EXERCISE_ID, USERNAME, Double.parseDouble(WEIGHT),
            Integer.parseInt(SERIES), Integer.parseInt(REPETITIONS), COMMENT);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to list all the performed exercises by the user works correctly.
     */
    @Test
    public void listExercisesTest(){
        String header = USERNAME;
        servletCall(header);
        assertTrue(sWriter.toString().equals(JSON_EXERCISE_LIST_RESPONSE));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
    private void servletCall(String header){
        try{
            when(request.getHeader("user")).thenReturn(header);
            when(response.getWriter()).thenReturn(writer);
            new ListPerformed().doGet(request, response);
            verify(request, atLeast(1)).getHeader("user");
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
