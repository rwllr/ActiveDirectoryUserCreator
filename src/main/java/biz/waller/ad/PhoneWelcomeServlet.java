package biz.waller.ad;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Raphael on 21/02/2017.
 */
@WebServlet(name = "PhoneWelcomeServlet")
public class PhoneWelcomeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGeneric(response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGeneric(response);
    }

    protected void doGeneric(HttpServletResponse response) throws IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter writer = response.getWriter();



        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<Response>");
        writer.println("<Gather timeout=\"10\" numDigits=\"1\" action=\"/passGenerator.do\">");
        writer.println("<Say>Hello and welcome to the password reset tool. If you requested this, please press 1. Otherwise, press 2 to report this.</Say>");
        writer.println("</Gather>");
        writer.println("</Response>");
        writer.flush();
        writer.close();

    }
}
