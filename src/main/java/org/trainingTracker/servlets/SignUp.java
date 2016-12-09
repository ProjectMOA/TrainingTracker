package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import org.trainingTracker.servlets.ServletCommon;
import org.trainingTracker.database.dataAccesObject.UsersDAO;
import org.trainingTracker.database.valueObject.UserVO;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/signUp")
public class SignUp extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
        String name = "";
        String pass = "";
        String repass = "";
        String email = "";
        boolean emailValid = false;
        
        // Reads a JSON Object from request and captures his fields
        JSONObject json = null;
        try {
            json = ServletCommon.readJSON(request.getReader());
            name = json.getString("user");
            pass = json.getString("pass");
            repass = json.getString("repass");
            email = json.getString("email");
            emailValid = Pattern.matches(EMAIL_PATTERN, email); // Compares the email with his pattern
            
            response.setContentType("text/html; charset=UTF-8");
        }
        catch (Exception e) {
            System.out.println("Error al leer el JSON");
            error = true;
        }
        
        // Field revision
        if ((name==null) || (name.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Nombre de usuario no válido");
            error = true;
        }
        if ((pass==null) || (pass.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Contraseña no válida");
            error = true;
        }
        if ((repass==null) || (repass.trim().equals("")) || !(repass.equals(pass))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Las contraseñas no coinciden");
            error = true;
        }
        if ((email==null) || (email.trim().equals("")) || !emailValid) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email no válido");
            error = true;
        }
        
        if (!error) {
            try {
                // Creates an user in BD
                if (UsersDAO.addUser(name, pass, email)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    UserVO vo = UsersDAO.findUser(name);
                    JSONObject user = JSONObject.fromObject(vo.serialize());
                    user.remove("pass");
                    user.remove("date");
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(user.toString());
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("El nombre de usuario o el email ya está en uso");
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
