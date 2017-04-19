package biz.waller.ad;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

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
public class ADPropLoader
{
    static String adminUsername;
    static String adminPassword;
    static String adminDN;
    static String baseOU;
    static String ldapString;
    static String searchBase;
    static String defaultUserOU;
    static String domainSuffix;
    static String encryptionKey;
    static String HomeMDB;
    static String msExchHomeServerName;
    static String authAddress;
    static String smtpServer;
    static {
        try {
            Properties prop = new Properties();
            String propFileName = "/config.properties";
            InputStream inputStream = ADPropLoader.class.getResourceAsStream(propFileName);


            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            adminUsername = prop.getProperty("adminUsername");
            adminPassword = prop.getProperty("adminPassword");
            baseOU = prop.getProperty("baseOU");
            ldapString = prop.getProperty("ldapString");
            adminDN = prop.getProperty("adminDN");
            domainSuffix= prop.getProperty("domainSuffix");
            String temp = domainSuffix.replaceAll("(\\w+)\\.?", "DC\\=$1,");
            searchBase = temp.substring(0, temp.length() - 1);
            System.out.println(searchBase);
            //searchBase = prop.getProperty("searchBase");
            defaultUserOU = prop.getProperty("defaultUserOU"); //TODO Dynamically add OU to base path??
            encryptionKey = prop.getProperty("encryptionKey");
            HomeMDB = prop.getProperty("HomeMDB");
            msExchHomeServerName = prop.getProperty("msExchHomeServerName");
            authAddress = prop.getProperty("authAddress");
            smtpServer = prop.getProperty("smtpServer");




        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String args[]){

    }
}
