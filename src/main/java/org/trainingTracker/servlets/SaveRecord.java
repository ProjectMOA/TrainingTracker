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
import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.dataAccesObject.CardioRecordsDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.CardioExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;
import org.trainingTracker.database.valueObject.CardioRecordVO;

/**
 * Servlet implementation class SaveRecord
 */
@WebServlet("/saveRecord")
public class SaveRecord extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveRecord() {
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
        String weight = "";
        String series = "";
        String repetitions = "";
        String commentary = "";
        
        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            user = json.getString("user");
            exercise = json.getString("id");
            weight = json.getString("weight").replace(",", ".");
            series = json.getString("series");
            repetitions = json.getString("repetitions");
            commentary = json.getString("commentary");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            error = true;
        }
        
        // Field revision
        if (!isValidWeight(weight, response) |
            !isValidSeries(series, response) |
            !isValidRepetitions(repetitions, response)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            error = true;
        }
        
        if (!error) {
            try {
                // Creates an record in BD
                if (RecordsDAO.addRecord(Integer.parseInt(exercise), user, Double.parseDouble(weight),
                                         Integer.parseInt(series), Integer.parseInt(repetitions), commentary)) {
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
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms weight specifications
     */
    static boolean isValidWeight (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        try {
            if (!(Double.parseDouble(str) > 0)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        catch (NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Peso no válido");
        }
        
        return !error;
    }
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms series specifications
     */
    static boolean isValidSeries (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        
        try {
            if (!(Integer.parseInt(str) > 0)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        catch (NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Número de series no válido");
        }
        
        return !error;
    }
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms repetitions specifications
     */
    static boolean isValidRepetitions (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        
        try {
            if (!(Integer.parseInt(str) > 0)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        catch (NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Número de repeticiones no válido");
        }
        
        return !error;
    }
    
}
