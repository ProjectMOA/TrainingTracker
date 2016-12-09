package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.List;

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
            user = json.getString("user");
            exerciseId = json.getString("id");
            
            response.setContentType("text/html; charset=UTF-8");
        }
        catch (Exception e) {
            System.out.println("Error al leer el JSON");
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
                        if (exercise.equals(vo.getId())) {
                            encontrado = true;
                            // Deletes a user's performed exercise in BD
                            if (vo.isPredefined()) {
                                if (!ExercisesDAO.deleteOwnExercise(user, vo.getName())) {
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
                                for (ExerciseVO vo : ExercisesDAO.listUserExercises(name)) {
                                    exercise = JSONObject.fromObject(vo.serialize());
                                    if(!(list=RecordsDAO.listRecords(name, vo.getId(), 1)).isEmpty()){
                                        record = JSONObject.fromObject(list.get(0).serialize());
                                        record.remove("exercise");
                                        record.remove("nick");
                                        record.remove("commentary");
                                        record.remove("date");
                                        exercise.putAll(record);
                                    }
                                    jsonExercises.add(exercise);
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
                
                if (ExercisessDAO.modifyExercise(Integer.parseInt(exerciseId), user)) {
                    // Search for performed exercises in BD
                    JSONArray jsonExercises = new JSONArray();
                    JSONObject jExercise, jRecord;
                    List<RecordVO> list;
                    
                    response.setStatus(HttpServletResponse.SC_OK);
                    for (ExerciseVO vo : ExercisesDAO.listUserExercises(name)) {
                        exercise = JSONObject.fromObject(vo.serialize());
                        if(!(list=RecordsDAO.listRecords(name, vo.getId(), 1)).isEmpty()){
                            record = JSONObject.fromObject(list.get(0).serialize());
                            record.remove("exercise");
                            record.remove("nick");
                            record.remove("commentary");
                            record.remove("date");
                            exercise.putAll(record);
                        }
                        jsonExercises.add(exercise);
                    }
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(jsonExercises.toString());
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
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
