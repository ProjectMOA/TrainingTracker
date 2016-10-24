package java.org.servlets;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONException;


/**
 * Servlet implementation class Registro
 */
//@WebServlet("/signUp")
public class SignUp extends AbstractServlet {
    
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean error = false;
        String usuario = "";
        String pass = "";
        String repass = "";
        String email = "";
        
        JSONObject json = null;
        try {
            json = readJSON(request.getReader());
            usuario = json.getString("name");
            pass = json.getString("pass");
            repass = json.getString("repass");
            email = json.getString("email");
            
            response.setContentType("text/html; charset=UTF-8");
        }
        catch (Exception e) {
            System.out.printf("Error al leer el JSON");
        }
        
        if ((usuario==null) || (usuario.trim().equals(""))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Usuario incorrecto");
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
        if ((email==null) || (email.trim().equals("")) || !new Pattern().matches(EMAIL_PATTERN, email)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email no valido");
            error = true;
        }
        
        if (!error) {
            try {
                if (UsuariosDAO.addUser(usuario, "", pass, email)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    UsuarioVO vo = UsuariosDAO.findUser(usuario);
                    JSONObject user = JSONObject.fromObject(vo.serialize());
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