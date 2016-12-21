package org.trainingTracker.servlets;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;

public class ServletTestUtils {

    static final String USERNAME = "test";
    static final String EMAIL= "test@prueba.com";
    static final String PASS = "pass";
    static final String JSON_USER_RESPONSE = "{\"user\":\"test\",\"email\":\"test@prueba.com\"}";
    static final String BAD_USERNAME_MESSAGE = "Nombre de usuario no válido";
    static final String BAD_PASS_MESSAGE = "Contraseña no válida";
    static HttpServletRequest request;
    static HttpServletResponse response;
    static StringWriter sWriter;
    static PrintWriter writer;

    static void testSetUp(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        sWriter = new StringWriter();
        writer = new PrintWriter(sWriter);
    }
}
