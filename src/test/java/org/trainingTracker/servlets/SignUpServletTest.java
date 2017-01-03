package org.trainingTracker.servlets;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import java.io.*;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;

/**
 * Test class to check if the SignUp servlet works correctly.
 */
public class SignUpServletTest extends Mockito {

    private static final String BAD_EMAIL_MESSAGE = "Email no válido";
    private static final String BAD_REPASS_MESSAGE = "Las contraseñas no coinciden";
    private static final String EXISTING_USERNAME_MESSAGE = "El nombre de usuario o el email ya está en uso";


    @BeforeClass
    public static void setUp(){
        mocksSetUp();
    }

    @Before
    public void initializeWriter(){
        writerSetUp();
    }

    /*
     * Checks if the process to sign up a new user works correctly.
     */
    @Test
    public void signUpTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"email\":\""+EMAIL+"\",\"pass\":\""+PASS+"\",\"repass\":\""+PASS+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().equals(JSON_USER_RESPONSE));
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
        assertTrue(sWriter.toString().contains(BAD_USERNAME_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_PASS_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_REPASS_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_EMAIL_MESSAGE));
    }

    /*
     * Checks if there's an error message when trying to sign up a new user
     * with an existing username.
     */
    @Test
    public void existingUserTest(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        String body = "{\"user\":\""+USERNAME+"\",\"email\":\""+EMAIL+"\",\"pass\":\""+PASS+"\",\"repass\":\""+PASS+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(EXISTING_USERNAME_MESSAGE));
    }

    /*
     * Sets what the mocks must return when they are called from the servlet
     * and makes a call to the servlet that is being tested.
     */
    private static void servletCall(BufferedReader bf){
        try{
            when(request.getReader()).thenReturn(bf);
            when(response.getWriter()).thenReturn(writer);
            new SignUp().doPost(request, response);
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
        UsersDAO.deleteUser(USERNAME);
    }
}
