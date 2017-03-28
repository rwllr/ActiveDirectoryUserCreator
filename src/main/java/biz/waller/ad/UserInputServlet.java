package biz.waller.ad;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
@WebServlet(name = "UserInputServlet")
public class UserInputServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doCheck(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doCheck(request, response);
    }

    private void doCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mailaddress= request.getParameter("mailaddress");
        String enteredNumber = request.getParameter("number");
        request.setAttribute("param.mail", mailaddress);
        request.setAttribute("param.number", enteredNumber);
        List<String> phoneList = ADDetails.getPhone(mailaddress);
        for (int i=0; i<phoneList.size(); i++) {
            System.out.println(phoneList.get(i));
        }
        if (phoneList.contains(enteredNumber)) {
            String callSID = MakeCall.dial(enteredNumber);
            SessionHandler.addCall(callSID);
            SessionHandler.setSAMAccountName(callSID, ADDetails.getSAMAccount(mailaddress));
            request.setAttribute("authstatus", "Request successful. You will receive a call shortly."); }
        else {
            request.setAttribute("authstatus", "User not found. Please check and try again.");
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);



    }
}
