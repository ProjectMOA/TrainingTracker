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
    static final int PREDETERMINED_EXERCISE_ID = 1;
    static final String EXERCISE = "My Exercise";
    static final String MG = "Espalda";
    static final String WEIGHT = "10.2";
    static final String SERIES = "4";
    static final String REPETITIONS = "12";
    static final String COMMENT = "Test comment";
    static final String JSON_USER_RESPONSE = "{\"user\":\"test\",\"email\":\"test@prueba.com\"}";
    static final String JSON_EXERCISE_LIST_RESPONSE = "[{\"id\":\"1\",\"name\":\"Press banca\"," +
        "\"muscleGroup\":\"Pecho\",\"predetermined\":true,\"weight\":\"10.2\",\"series\":\"4\",\"repetitions\":\"12\"}]";
    static final String BAD_USERNAME_MESSAGE = "Nombre de usuario no válido";
    static final String BAD_PASS_MESSAGE = "Contraseña no válida";
    static final String INTERNAL_ERROR_MESSAGE = "Error interno en el servidor. Vuelva intentarlo más tarde";
    static final String WRONG_MG_MESSAGE = "Debe seleccionar un grupo muscular";
    static final String WRONG_EXERCISE_MESSAGE = "Debe seleccionar un ejercicio";
    static HttpServletRequest request;
    static HttpServletResponse response;
    static StringWriter sWriter;
    static PrintWriter writer;

    static void mocksSetUp(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    static void writerSetUp(){
        sWriter = new StringWriter();
        writer = new PrintWriter(sWriter);
    }
}
