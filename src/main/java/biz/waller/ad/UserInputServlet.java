package biz.waller.ad;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Raphael on 24/02/2017.
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
