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

/**
 * Test class to check if the SignIn servlet works correctly.
 */
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

    /*
     * Checks if the process of signing in a user works correctly.
     */
    @Test
    public void signInTest(){
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASS).getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().equals(JSON_USER_RESPONSE));
    }

    /*
     * Checks if there's an error message if the client sends a request with the required
     * fields blank.
     */
    @Test
    public void blankFieldsTest() {
        String header = "Basic " + Base64.getEncoder().encodeToString((":").getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(BAD_USERNAME_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_PASS_MESSAGE));
    }

    /*
     * Checks if there's an erroe message if the user is trying to sign in with
     * a non existing username.
     */
    @Test
    public void notExistingUsername(){
        String header = "Basic " + Base64.getEncoder().encodeToString(("doesNotExists:" + PASS).getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(NOT_EXISTING_USERNAME_MESSAGE));
    }

    /*
     * Checks if there's an error message if the password doesn't match with the username.
     */
    @Test
    public void wrongPassword() {
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":wrongPass").getBytes());
        servletCall(header);
        assertTrue(sWriter.toString().contains(WRONG_PASS_MESSAGE));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
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
