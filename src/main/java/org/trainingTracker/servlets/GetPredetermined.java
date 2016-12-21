package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;

/**
 * Servlet implementation class GetPredetermined
 */
@WebServlet("/getPredetermined")
public class GetPredetermined extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPredetermined() {
        super();
    }

    @Override
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            // Search for predefined exercises in BD
            JSONObject jsonResponse = new JSONObject();
            JSONArray jsonExercises = new JSONArray();
            JSONObject exercise;
            Set<String> groupsSorted = new TreeSet<String>();
            
            response.setStatus(HttpServletResponse.SC_OK);
            for (ExerciseVO vo : ExercisesDAO.listDefaultExercises()) {
                exercise = JSONObject.fromObject(vo.serialize());
                exercise.remove("predefines");
                jsonExercises.add(exercise);
                exercise.remove("id");
                exercise.remove("name");
                // add muscleGroups in a Collection that don´t repeat elements and sorts it
                groupsSorted.add(exercise.getString("muscleGroup"));
            }
            response.setContentType("application/json; charset=UTF-8");
            jsonResponse.put("muscleGroups", groupsSorted);
            jsonResponse.put("predeterminedExercises", jsonExercises.toString());
            response.getWriter().write(jsonResponse.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
        }
    }
    
    @Override
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
}
