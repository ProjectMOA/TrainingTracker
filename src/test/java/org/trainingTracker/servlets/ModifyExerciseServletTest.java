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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;
import static org.trainingTracker.servlets.ServletTestUtils.mocksSetUp;

public class ModifyExerciseServletTest extends Mockito{

    private static int ExerciseID;

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
    public void modifyExerciseTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"id\":\""+ExerciseID+"\",\"muscleGroup\":\""+MG+"\"," +
            "\"name\":\""+EXERCISE+"\"}\n";
        String responseMessage = "[{\"id\":\""+ExerciseID+"\",\"name\":\"My Exercise\",\"muscleGroup\":\"Espalda\"," +
            "\"predetermined\":false,\"weight\":\"10.2\",\"series\":\"4\",\"repetitions\":\"12\"}]";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().equals(responseMessage));
    }

    @Test
    public void badRequestTest(){
        String body = "fail";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(INTERNAL_ERROR_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_EXERCISE_MESSAGE));
        assertTrue(sWriter.toString().contains(WRONG_MG_MESSAGE));
    }

    private static void servletCall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new ModifyExercise().doPost(request, response);
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
        ExercisesDAO.deleteCustomExercise(ExerciseID);
        UsersDAO.deleteUser(USERNAME);
    }
}
