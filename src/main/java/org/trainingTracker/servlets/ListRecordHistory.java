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

import org.trainingTracker.database.dataAccesObject.ExercisesDAO;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.valueObject.ExerciseVO;
import org.trainingTracker.database.valueObject.RecordVO;

/**
 * Servlet implementation class ListRecordHistory
 */
@WebServlet("/listRecordHistory")
public class ListRecordHistory extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListRecordHistory() {
        super();
    }

    @Override
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Reads headers and substracts user, exercise and numPage
        String user = request.getHeader("user");
        String exercise = request.getHeader("id");
        String numPage = request.getHeader("numPage");
        
        try {
            // Search for exercise records in BD
            JSONArray jsonRecords = new JSONArray();
            JSONObject jRecord;
            List<RecordVO> list;
            
            response.setStatus(HttpServletResponse.SC_OK);
            for (RecordVO vo : RecordsDAO.listRecords(user, Integer.parseInt(exercise), 5, Integer.parseInt(numPage))) {
                jRecord = JSONObject.fromObject(vo.serialize());
                jRecord.remove("exercise");
                jRecord.remove("nick");
                jsonRecords.add(jRecord);
            }
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(jsonRecords.toString());
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
