package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.dataAccesObject.CardioExercisesDAO;
import org.trainingTracker.database.dataAccesObject.CardioRecordsDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.CardioExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;
import org.trainingTracker.database.valueObject.CardioRecordVO;

/**
 * Servlet implementation class ListPerformed
 */
@WebServlet("/listPerformed")
public class ListPerformed extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListPerformed() {
        super();
    }

    @Override
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Reads User header and substracts user
        String user = request.getHeader("user");
        
        try {
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
            Iterator<Map.Entry<String, int>> it;
            Map.Entry<String, int> entry;
            for (CardioExerciseVO vo : CardioExercisesDAO.listUserExercises(user)) {
                jExercise = JSONObject.fromObject(vo.serialize());
                jExercise.remove("predetermined");
                if(!(list=CardioRecordsDAO.listRecords(user, vo.getId(), 1, 1)).isEmpty()){
                    jRecord = JSONObject.fromObject(list.get(0).serialize());
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
            
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(jsonExercises.toString());
            response.getWriter().write(jsonCardioExercises.toString());
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
