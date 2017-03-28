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

/*
MIT License

Copyright (c) 2017 Raphael Waller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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
