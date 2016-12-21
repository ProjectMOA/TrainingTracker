package org.trainingTracker.servlets;

import static org.junit.Assert.*;
import static org.trainingTracker.servlets.ServletTestUtils.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import java.io.*;
import javax.servlet.ServletException;
import org.junit.Test;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.Base64;


public class SignInServletTest extends Mockito {

    private static final String NOT_EXISTING_USERNAME_MESSAGE = "El nombre de usuario no existe";
    private static final String WRONG_PASS_MESSAGE = "Contraseña incorrecta";

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    @Test
    public void signInTest(){
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASS).getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().equals(JSON_USER_RESPONSE));
    }

    @Test
    public void blankFieldsTest() {
        String header = "Basic " + Base64.getEncoder().encodeToString((":").getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(BAD_USERNAME_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_PASS_MESSAGE));
    }

    @Test
    public void notExistingUsername(){
        String header = "Basic " + Base64.getEncoder().encodeToString(("doesNotExists:" + PASS).getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(NOT_EXISTING_USERNAME_MESSAGE));
    }

    @Test
    public void wrongPassword() {
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":wrongPass").getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(WRONG_PASS_MESSAGE));
    }

    private static void servletCall(String header){
        try{
            when(request.getHeader("Authorization")).thenReturn(header);
            when(response.getWriter()).thenReturn(writer);
            new SignIn().doGet(request, response);
            verify(request, atLeast(1)).getHeader("Authorization");
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
