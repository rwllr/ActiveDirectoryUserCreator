package biz.waller.ad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Raphael on 09/03/2017.
 */
@WebServlet(name = "CreateUserServlet", urlPatterns={"/createUser"})
public class CreateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }
    protected void doAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String groups = request.getParameter("data");
        System.out.println(request.getParameter("data"));

        ADUser newUser = new Gson().fromJson(groups, ADUser.class);
        Map<String, String> loginDetails = null;

        try {
            loginDetails = ADDetails.createUser(newUser);
            EmailNotifier.sendMessage(newUser, "raphael@waller.biz", request.getRemoteUser());
        } catch (NameAlreadyBoundException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Error: Account already exists with this username");
            e.printStackTrace();
        } catch (NamingException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Error" + e.getMessage());
            e.printStackTrace();
        }

        if (!loginDetails.equals(null)) {
            String json = new GsonBuilder().create().toJson(loginDetails);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }

    }

}
