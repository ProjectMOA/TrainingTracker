package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.sql.Time;

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
 * Servlet implementation class SaveCardiovascularRecord
 */
@WebServlet("/saveCardiovascularRecord")
public class SaveCardiovascularRecord extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveCardiovascularRecord() {
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
        String distance = "";
        String time = "";
        String intensity = "";
        
        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            user = json.getString("user");
            exercise = json.getString("id");
            distance = json.getString("distance").replace(",", ".");
            time = json.getString("time");
            intensity = json.getString("intensity");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            error = true;
        }
        
        // Field revision
        if (!isValidDistance(distance, response) |
            !isValidTime(time, response) |
            !isValidIntensity(intensity, response)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            error = true;
        }
        
        if (!error) {
            try {
                Double parsedDistance = 0.0;
                if(!distance.equals("")){
                    parsedDistance = Double.parseDouble(distance);
                }
                // Creates an record in BD
                if (CardioRecordsDAO.addRecord(Integer.parseInt(exercise), user, parsedDistance,
                                   new Time(Long.parseLong(time)), ServletCommon.getIntensidades().get(intensity))) {
                    // Search for performed exercises in BD
                    JSONObject jResponse = new JSONObject();
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

                    List<CardioRecordVO> list2;
                    Iterator<Map.Entry<String, Integer>> it;
                    Map.Entry<String, Integer> entry;
                    for (CardioExerciseVO vo2 : CardioExercisesDAO.listUserExercises(user)) {
                        jExercise = JSONObject.fromObject(vo2.serialize());
                        jExercise.remove("predetermined");
                        if(!(list2=CardioRecordsDAO.listRecords(user, vo2.getId(), 1, 1)).isEmpty()){
                            jRecord = JSONObject.fromObject(list2.get(0).serialize());
                            jRecord.remove("exercise");
                            jRecord.remove("nick");
                            jRecord.remove("date");
                            it = ServletCommon.getIntensidades().entrySet().iterator();
                            entry = it.next();
                            while (entry.getValue() != jRecord.getInt("intensity")) {
                                entry = it.next();
                            }
                            jRecord.remove("intensity");
                            jRecord.put("intensity", entry.getKey());
                            jExercise.putAll(jRecord);
                        }
                        jsonCardioExercises.add(jExercise);
                    }
                    jResponse.put("listPerformed", jsonExercises);
                    jResponse.put("listCardioPerformed", jsonCardioExercises);
                    
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(jResponse.toString());
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
     * @returns true if str confirms distance specifications
     */
    static boolean isValidDistance (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        if(!str.equals("")){
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
                response.getWriter().println("Distancia no válida");
            }

        }
        return !error;
    }
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms time specifications
     */
    static boolean isValidTime (String str, HttpServletResponse response) throws IOException {
        boolean error = false;

        try {
            if (!(Long.parseLong(str) >= 0)) {
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
            response.getWriter().println("Tiempo no válido");
        }

        return !error;
    }
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms intensity specifications
     */
    static boolean isValidIntensity (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        
        try {
            if (!ServletCommon.getIntensidades().containsKey(str)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Intensidad no válida");
        }
        
        return !error;
    }
    
}
