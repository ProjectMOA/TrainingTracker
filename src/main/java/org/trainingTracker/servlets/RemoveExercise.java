package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import org.trainingTracker.servlets.ServletCommon;
import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;

/**
 * Servlet implementation class RemoveExercise
 */
@WebServlet("/removeExercise")
public class RemoveExercise extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveExercise() {
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
        String exercise = "";
        
        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            System.out.println(json.toString());
            user = json.getString("user");
            exercise = json.getString("id");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            error = true;
        }
        
        if (!error) {
            try {
                List<ExerciseVO> exercisesList;
                Iterator<ExerciseVO> it;
                ExerciseVO vo;
                boolean encontrado = false;
                
                // Search for user exercises in BD
                exercisesList = ExercisesDAO.listUserExercises(user);
                if (exercisesList == null) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
                }
                else {
                    it = exercisesList.iterator();
                    while (!encontrado && it.hasNext()) {
                        vo = it.next();
                        // if the new exercise doesn't exists in user's routine
                        if (Integer.parseInt(exercise) == vo.getId()) {
                            encontrado = true;
                            // Deletes a user's performed exercise in BD
                            if (vo.isPredefined()) {
                                if (!ExercisesDAO.deleteOwnExercise(user, Integer.parseInt(exercise))) {
                                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
                                    error = true;
                                }
                            }
                            else {
                                if (!ExercisesDAO.deleteCustomExercise(Integer.parseInt(exercise))) {
                                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                    response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
                                    error = true;
                                }
                            }
                            
                            if (!error) {
                                // Search for performed exercises in BD
                                JSONArray jsonExercises = new JSONArray();
                                JSONObject jExercise, jRecord;
                                List<RecordVO> list;
                                
                                response.setStatus(HttpServletResponse.SC_OK);
                                for (ExerciseVO vo2 : ExercisesDAO.listUserExercises(user)) {
                                    jExercise = JSONObject.fromObject(vo2.serialize());
                                    if(!(list=RecordsDAO.listRecords(user, vo2.getId(), 1)).isEmpty()){
                                        jRecord = JSONObject.fromObject(list.get(0).serialize());
                                        jRecord.remove("exercise");
                                        jRecord.remove("nick");
                                        jRecord.remove("commentary");
                                        jRecord.remove("date");
                                        jExercise.putAll(jRecord);
                                    }
                                    jsonExercises.add(jExercise);
                                }
                                response.setContentType("application/json; charset=UTF-8");
                                response.getWriter().write(jsonExercises.toString());
                            }
                        }
                    }
                    if (!encontrado) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Este ejercicio no forma parte de su rutina");
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            }
        }
    }
    
}
