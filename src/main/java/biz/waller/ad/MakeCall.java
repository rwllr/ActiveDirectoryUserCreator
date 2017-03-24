package biz.waller.ad;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import java.net.URI;

/**
 * Created by Raphael on 21/02/2017.
 */
public class MakeCall {

    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "ACf4c6a95037f5a69b6e0bc3ebc14574fe";
    public static final String AUTH_TOKEN = "6bb80ae10182dd756a7eedebfc181f09";

    public static String dial(String toNumber) {

        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();

        PhoneNumber to = new PhoneNumber(toNumber); // Replace with your phone number
        PhoneNumber from = new PhoneNumber("447858532173"); // Replace with a Twilio number
        URI uri = URI.create("http://b102ea9b.ngrok.io/callxml.do");

        // Make the call
        Call call = Call.creator(to, from, uri).create(client);
        // Print the call SID (a 32 digit hex like CA123..)
        System.out.println(call.getSid());
        return call.getSid();
    }
}