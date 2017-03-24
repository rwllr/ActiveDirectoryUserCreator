package biz.waller.ad;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    protected void doAll(HttpServletRequest request, HttpServletResponse response) {
        String groups = request.getParameter("data");
        System.out.println("1" + request.getParameter("data[]"));
        System.out.println("2" + request.getParameter("data"));

        ADUser newUser = new Gson().fromJson(groups, ADUser.class);
        ADDetails.createUser(newUser);

    }

}
