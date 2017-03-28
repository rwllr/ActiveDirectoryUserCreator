package biz.waller.ad;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Raphael on 26/03/2017.
 */
public class EmailNotifier {
    public static void main(String [] args) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "mail.waller.biz");
        Session session = Session.getDefaultInstance(properties);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("newuser@waller.biz"));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress("raphael@waller.biz"));
            message.setSubject("Ping");
            StringBuffer sb = new StringBuffer();
                    sb.append("Hello friend, we have a new user request! \n");
                    sb.append("Next line");

            message.setText(sb.toString());

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException mex)
        {mex.printStackTrace();}

    }
    public static void sendMessage(ADUser userDetails, String toAddress, String requestUser) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "mail.waller.biz"); //TODO Migrate SMTP server to properties file
        Session session = Session.getDefaultInstance(properties);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("newuser@waller.biz")); //TODO Migrate from address to properties file
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toAddress));
            message.setSubject("New user requested!");
            String samAccountName = userDetails.getfName()+"."+userDetails.getsName();
            StringBuffer sb = new StringBuffer();
            sb.append("Hello friend, we have a new user request from "+ requestUser +  "! \n");
            sb.append("First name: " + userDetails.getfName()+"\n");
            sb.append("Last name: " + userDetails.getsName()+"\n");
            sb.append("User name: " + samAccountName +"\n");
            sb.append("Groups requested: \n");
            String[] str = userDetails.getGroups();
            for (int i=0; i < str.length; i++) {
                String temp = str[i].replaceAll("(?>CN=)([^,]+).+", "$1");
                sb.append(temp+"\n");

            }
            sb.append("Please click this link to authorise the request. \n");


            DateTime now = new DateTime().now().plusDays(1);
            String compactJws = Jwts.builder()
                    .setSubject(samAccountName)
                    .signWith(SignatureAlgorithm.HS512, ADPropLoader.encryptionKey)
                    .setExpiration(now.toDate())
                    .compact();

            sb.append("http://localhost:8080/enable?user="+compactJws+"\n");

            message.setText(sb.toString());

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException mex)
        {mex.printStackTrace();}

    }
}
