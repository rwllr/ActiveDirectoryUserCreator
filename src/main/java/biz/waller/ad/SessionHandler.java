package biz.waller.ad;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;
import java.util.HashMap;
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
public class SessionHandler {
    private static HashMap<String, String[]> people = new HashMap<String, String[]>();
    public SessionHandler() {
        people = new HashMap<String, String[]>();
        people.put("TestSID", new String[]{"test.reset", "password"});
    }
    static {
        people.put("TestSID", new String[]{"test.reset", "password"});
        //Initialise this value so can test without generating calls
    }
    public static void addCall(String callSID) {
        String[] unused = people.put(callSID, new String[2]);
    }

    public static void setSAMAccountName(String callSID, String samAccountName) {
        String[] unused = people.put(callSID, new String[] {samAccountName, ""});
    }
    public static String getSAMAccountName(String callSID) {
        String temp[] = people.get(callSID);
        return temp[0];
    }
    public static String getADPassword(String callSID) {
        String details[] = people.get(callSID);
        return details[1];
    }

    public static String createADPassword(String callSID) {
        List<CharacterRule> rules = Arrays.asList(
                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1) );

        PasswordGenerator generator = new PasswordGenerator();
        // Generated password is 12 characters long, which complies with policy
        String password = generator.generatePassword(10, rules);
        System.out.println(password);

        //put password into hashmap
        if (people.get(callSID)==null) { System.out.println("Null callid return"); }
        String details[] = people.get(callSID);
        if (details[0]==null) { System.out.println("Null samaccountname"); }

        String samAccountName = details[0];
        details[1] = password;
        people.put(callSID, details);

        //reset the password in AD
        ADDetails.setPassword(samAccountName, password);

        return password;
    }
}
