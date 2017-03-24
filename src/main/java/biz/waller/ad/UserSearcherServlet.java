package biz.waller.ad;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Raphael on 08/03/2017.
 */
//@WebServlet(fieldname = "UserSearcherServlet")
@WebServlet("/userSearch")
public class UserSearcherServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        generateJSON(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        generateJSON(request, response);
    }
    void generateJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> userMap = ADDetails.getSAMAccountMap(request.getParameter("fieldname"));
        System.out.println(userMap.get(request.getParameter("fieldname")));

        String json = new Gson().toJson(userMap);
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
