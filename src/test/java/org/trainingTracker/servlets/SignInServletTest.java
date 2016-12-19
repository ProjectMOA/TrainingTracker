package org.trainingTracker.servlets;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.trainingTracker.database.dataAccesObject.UsersDAO;

import java.util.Base64;


public class SignInServletTest extends Mockito {

    private static final String USERNAME = "test";
    private static final String EMAIL= "test@prueba.com";
    private static final String PASS = "pass";
    private static final String JSON_RESPONSE = "{\"user\":\"test\",\"email\":\"test@prueba.com\"}";
    private static final String BAD_USERNAME_MESSAGE = "Nombre de usuario no válido";
    private static final String BAD_PASS_MESSAGE = "Contraseña no válida";
    private static final String NOT_EXISTING_USERNAME_MESSAGE = "El nombre de usuario no existe";
    private static final String WRONG_PASS_MESSAGE = "Contraseña incorrecta";
    private static HttpServletRequest request;
    private static HttpServletResponse response;
    private static StringWriter sWriter;
    private static PrintWriter writer;

    @BeforeClass
    public static void setUp(){
        UsersDAO.addUser(USERNAME, PASS, EMAIL);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        sWriter = new StringWriter();
        writer = new PrintWriter(sWriter);
    }

    @Test
    public void signInTest(){
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + PASS).getBytes());
        servletCall(request, response, header, writer);
        assertTrue(sWriter.toString().equals(JSON_RESPONSE));
    }

    @Test
    public void blankFieldsTest() {
        String header = "Basic " + Base64.getEncoder().encodeToString((":").getBytes());
        servletCall(request, response, header, writer);
        assertTrue(sWriter.toString().contains(BAD_USERNAME_MESSAGE));
        assertTrue(sWriter.toString().contains(BAD_PASS_MESSAGE));
    }

    @Test
    public void notExistingUsername(){
        String header = "Basic " + Base64.getEncoder().encodeToString(("doesNotExists:" + PASS).getBytes());
        servletCall(request, response, header, writer);
        assertTrue(sWriter.toString().contains(NOT_EXISTING_USERNAME_MESSAGE));
    }

    @Test
    public void wrongPassword() {
        String header = "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":wrongPass").getBytes());
        servletCall(request, response, header, writer);
        assertTrue(sWriter.toString().contains(WRONG_PASS_MESSAGE));
    }

    private static void servletCall(HttpServletRequest request, HttpServletResponse response, String header,
                                    PrintWriter writer){
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
