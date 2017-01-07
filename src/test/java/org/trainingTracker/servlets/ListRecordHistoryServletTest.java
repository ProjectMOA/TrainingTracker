package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;
import static org.trainingTracker.servlets.ServletTestUtils.writerSetUp;

/**
 * Test class to check if the ListRecordHistory servlet works correctly.
 */
public class ListRecordHistoryServletTest extends Mockito{

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
        RecordsDAO.addRecord(PREDETERMINED_EXERCISE_ID, USERNAME, Double.parseDouble(WEIGHT), Integer.parseInt(SERIES),
            Integer.parseInt(REPETITIONS), COMMENT);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to list the record history of an exercise works correctly.
     */
    @Test
    public void listRecordTest(){
        // The response wil also include the date of the record, but since it will be always
        // different for every test, it is omitted from the 'responseMessage' string.
        String responseMessage = "[{\"weight\":\"10.2\",\"series\":\"4\",\"repetitions\":\"12\",\"commentary\":" +
            "\"Test comment\"";
        servletCall(USERNAME, PREDETERMINED_EXERCISE_ID+"", "1");
        assertTrue(sWriter.toString().contains(responseMessage));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
    private static void servletCall(String user, String id, String numPage){
        try{
            when(request.getHeader("user")).thenReturn(user);
            when(request.getHeader("id")).thenReturn(id);
            when(request.getHeader("numPage")).thenReturn(numPage);
            when(response.getWriter()).thenReturn(writer);
            new ListRecordHistory().doGet(request, response);
            verify(request, atLeast(1)).getHeader("user");
            verify(request, atLeast(1)).getHeader("id");
            verify(request, atLeast(1)).getHeader("numPage");
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
        UsersDAO.deleteUser(USERNAME);
    }
}
