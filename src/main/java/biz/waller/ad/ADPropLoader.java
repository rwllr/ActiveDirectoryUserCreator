package biz.waller.ad;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Raphael on 22/03/2017.
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

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String args[]){

    }
}
