package org.trainingTracker.servlets;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONException;

import org.trainingTracker.database.dataAccesObject.UsersDAO;
import org.trainingTracker.database.valueObject.UserVO;

/**
 * Servlet implementation class SignIn
 */
@WebServlet("/signIn")
public class SignIn extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignIn() {
        super();
    }

    @Override
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean error = false;
        
        //Reads Authorization header and substracts user and password
        String header = request.getHeader("Authorization");
        header = header.substring(6);
        byte [] decoded = Base64.getDecoder().decode(header);
        String info = new String (decoded, "UTF-8");
        int i = info.indexOf(":");
        String name  = info.substring(0, i);
        String pass = info.substring(i+1);

        // Field revision
        if((name==null) || (name.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Nombre de usuario no válido");
            error = true;
        }
        if((pass==null) || (pass.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Contraseña no válida");
            error = true;
        }
        
        if(!error) {
            try {
                // Search for the user in BD
                UserVO vo = UsersDAO.findUser(name);
                // If user don't exists
                if (vo==null) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("El usuario \"" + name + "\" no existe");
                }
                // If pass don't match
                else if(!(vo.getPass()).equals(pass)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Contraseña incorrecta");
                }
                // User found
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    JSONObject user = JSONObject.fromObject(vo.serialize());
                    user.remove("pass");
                    user.remove("date");
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write(user.toString());
                }
            }
            catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error interno en el servidor. Vuelva intentarlo más tarde");
            }
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
