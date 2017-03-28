package biz.waller.ad;

import io.jsonwebtoken.Jwts;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Raphael on 08/03/2017.
 */
@WebServlet(name = "EnableUserServlet", urlPatterns={"/enable"})
public class EnableUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doAll(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doAll(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    private void doAll(HttpServletRequest request, HttpServletResponse response) throws IOException, NamingException {
        System.out.println("Enable servlet reached");
        String encryptedString = request.getParameter("user");
        String samAccountName = Jwts.parser().setSigningKey(ADPropLoader.encryptionKey).parseClaimsJws(encryptedString).getBody().getSubject();
        ADDetails.enableUser(samAccountName);

        response.setContentType("text/html;charset=UTF-8"); //TODO send response nicely in a themed JSP for future expansion
        PrintWriter writer = response.getWriter();
        writer.println("Account successfully unlocked.");
        writer.flush();
        writer.close();


    }
}
