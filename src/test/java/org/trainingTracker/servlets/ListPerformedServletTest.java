package org.trainingTracker.servlets;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;

public class ListPerformedServletTest extends Mockito {

    private static final int PREDETERMINED_EXERCISE_ID = 1;
    private static final String JSON_EXERCISE_LIST_RESPONSE =
        "[{\"id\":\"1\",\"name\":\"Press banca\",\"muscleGroup\":\"Pecho\",\"predetermined\":true}]";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        ExercisesDAO.addDefaultExercise(PREDETERMINED_EXERCISE_ID, USERNAME);
        testSetUp();
    }

    @Test
    public void listExercisesTest(){
        String header = USERNAME;
        servletCall(header);
        assertTrue(sWriter.toString().equals(JSON_EXERCISE_LIST_RESPONSE));
    }

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
