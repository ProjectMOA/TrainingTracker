package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.valueObject.CardioExerciseVO;

/**
 * Servlet implementation class AddCardiovascular
 */
@WebServlet("/addCardiovascular")
public class AddCardiovascular extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCardiovascular() {
        super();
    }

    @Override
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean error = false;
        String user = "";
        String cardioType = "";
        String id = "";

        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            user = json.getString("user");
            id = json.getString("id");
            cardioType = json.getString("cardioType");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            error = true;
        }

        // Field revision
        if ((cardioType == null) || (cardioType.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Debe seleccionar un tipo de ejercicio cardiovascular");
            error = true;
        }
        if ((id == null) || (id.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Debe seleccionar un ejercicio");
            error = true;
        }

        if (!error) {
            try{
                Iterator <CardioExerciseVO> userCardioExercisesList = (CardioExercisesDAO.listUserExercises(user)).iterator();
                CardioExerciseVO vo;
                while (userCardioExercisesList.hasNext() && !error){
                    vo = userCardioExercisesList.next();
                    // if the new exercise doesn't exists in user's routine
                    if (cardioType.equals(vo.getType()) && (Integer.parseInt(id)==vo.getId())) {
                        error = true;
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Este ejercicio ya forma parte de su rutina");
                    }
                }
                if(!error){
                    if (CardioExercisesDAO.addDefaultExercise(Integer.parseInt(id), user) == -1) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("Ejercicio cardiovascular añadido");
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            }
        }
    }
    
}
