package biz.waller.ad;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Raphael on 08/03/2017.
 */
@WebServlet("/groupList")
public class GroupListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }
    private void doAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] receivedData = request.getParameterValues("grouplist[]");
        for (int i=0; i < receivedData.length; i++ ) {
        System.out.println(receivedData[i]); }
        List<String> adGroupList = ADDetails.getDistinctGroups(receivedData);
        String json = new Gson().toJson(adGroupList);
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
