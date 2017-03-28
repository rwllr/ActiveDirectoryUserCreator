package biz.waller.ad;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import java.net.URI;

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