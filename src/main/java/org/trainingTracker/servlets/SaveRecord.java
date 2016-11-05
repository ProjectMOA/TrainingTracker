package org.trainingTracker.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import org.trainingTracker.servlets.ServletCommon;
import org.trainingTracker.database.dataAccesObject.RecordsDAO;
import org.trainingTracker.database.valueObject.RecordVO;

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
            weight = json.getString("weight");
            series = json.getString("series");
            repetitions = json.getString("repetitions");
            commentary = json.getString("commentary");
            
            response.setContentType("text/html; charset=UTF-8");
        }
        catch (Exception e) {
            System.out.println("Error al leer el JSON");
        }
        
        // Field revision
        if (!ServletCommon.isValidWeight(weight, response) |
            !ServletCommon.isValidSeries(series, response) |
            !ServletCommon.isValidRepetitions(repetitions, response)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            error = true;
        }
        
        if (!error) {
            try {
                // Creates an record in BD
                if (RecordsDAO.addRecord(Integer.parseInt(exercise), user, Double.parseDouble(weight),
                                         Integer.parseInt(series), Integer.parseInt(repetitions))) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.sendRedirect("listPerformed");
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
