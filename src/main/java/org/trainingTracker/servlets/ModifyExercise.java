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
import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.dataAccesObject.CardioRecordsDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.CardioExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;
import org.trainingTracker.database.valueObject.CardioRecordVO;

/**
 * Servlet implementation class ModifyExercise
 */
@WebServlet("/modifyExercise")
public class ModifyExercise extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifyExercise() {
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
        String exerciseId = "";
        String muscleGroup = "";
        String exerciseName = "";
        
        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            user = json.getString("user");
            exerciseId = json.getString("id");
            muscleGroup = json.getString("muscleGroup");
            exerciseName = json.getString("name");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            error = true;
        }
        
        // Field revision
        if ((muscleGroup==null) || (muscleGroup.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Debe seleccionar un grupo muscular");
            error = true;
        }
        if ((exerciseName==null) || (exerciseName.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Debe seleccionar un ejercicio");
            error = true;
        }
        
        if (!error) {
            try {
                // Modify an exercise in BD
                if (ExercisesDAO.updateCustomExercise(Integer.parseInt(exerciseId), exerciseName, muscleGroup)) {
                    // Search for performed exercises in BD
                    JSONArray jsonExercises = new JSONArray();
                    JSONArray jsonCardioExercises = new JSONArray();
                    JSONObject jExercise, jRecord;
                    List<RecordVO> list;
                    
                    response.setStatus(HttpServletResponse.SC_OK);
                    for (ExerciseVO vo : ExercisesDAO.listUserExercises(user)) {
                        jExercise = JSONObject.fromObject(vo.serialize());
                        if(!(list=RecordsDAO.listRecords(user, vo.getId(), 1, 1)).isEmpty()){
                            jRecord = JSONObject.fromObject(list.get(0).serialize());
                            jRecord.remove("exercise");
                            jRecord.remove("nick");
                            jRecord.remove("commentary");
                            jRecord.remove("date");
                            jExercise.putAll(jRecord);
                        }
                        jsonExercises.add(jExercise);
                    }

                    List<CardioRecordVO> list;
                    for (CardioExerciseVO vo : CardioExercisesDAO.listUserExercises(user)) {
                        jExercise = JSONObject.fromObject(vo.serialize());
                        if(!(list=CardioRecordsDAO.listRecords(user, vo.getId(), 1, 1)).isEmpty()){
                            jRecord = JSONObject.fromObject(list.get(0).serialize());
                            jExercise.putAll(jRecord);
                        }
                        jsonCardioExercises.add(jExercise);
                    }
                    
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(jsonExercises.toString());
                    response.getWriter().write(jsonCardioExercises.toString());
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
