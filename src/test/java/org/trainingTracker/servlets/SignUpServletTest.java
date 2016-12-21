package org.trainingTracker.servlets;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.Assert.assertTrue;
import static org.trainingTracker.servlets.ServletTestUtils.*;


public class SignUpServletTest extends Mockito {

    private static final String BAD_EMAIL_MESSAGE = "Email no válido";
    private static final String BAD_REPASS_MESSAGE = "Las contraseñas no coinciden";
    private static final String INTERNAL_ERROR_MESSAGE = "Error interno en el servidor. Vuelva intentarlo más tarde";
    private static final String EXISTING_USERNAME_MESSAGE = "El nombre de usuario o el email ya está en uso";


    @BeforeClass
    public static void setUp(){
        testSetUp();
    }

    @Test
    public void signUpTest(){
        String body = "{\"user\":\""+USERNAME+"\",\"email\":\""+EMAIL+"\",\"pass\":\""+PASS+"\",\"repass\":\""+PASS+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().equals(JSON_USER_RESPONSE));
    }

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

    @Test
    public void existingUserTest(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        String body = "{\"user\":\""+USERNAME+"\",\"email\":\""+EMAIL+"\",\"pass\":\""+PASS+"\",\"repass\":\""+PASS+"\"}\n";
        BufferedReader bf = new BufferedReader(new StringReader(body));
        servletCall(bf);
        assertTrue(sWriter.toString().contains(EXISTING_USERNAME_MESSAGE));
    }

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
