package biz.waller.ad;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.*;
import java.util.stream.Collectors;

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
public class ADDetails {
    public ADDetails() {
        super();
    }

    protected static String getDN(String samAccountName) throws NamingException {
        LdapContext cnx = getADConnection();
        SearchControls searchCtls = new SearchControls();
        String returnedAtts[] = {};
        searchCtls.setReturningAttributes(returnedAtts);
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "samaccountname=" + samAccountName;
        //Specify the Base for the search
        String searchBase = ADPropLoader.searchBase;
        NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
        cnx.close();
        return answer.next().getNameInNamespace();
    }
    protected static byte[] createUnicodePassword(String password) {
        String quotedPassword = "\"" + password + "\"";
        char unicodePwd[] = quotedPassword.toCharArray();
        byte pwdArray[] = new byte[unicodePwd.length * 2];
        for (int i = 0; i < unicodePwd.length; i++)
        {
            pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
            pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
        }
        System.out.print("encoded password: ");
        for (int i = 0; i < pwdArray.length; i++)
        {
            System.out.print(pwdArray[i] + " ");
        }
        return pwdArray;
    }

    protected static LdapContext getADConnection() {

        try {
            System.out.println("Début du test Active Directory");

            Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL, ADPropLoader.ldapString);
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapEnv.put(Context.SECURITY_PRINCIPAL, ADPropLoader.adminDN);
            ldapEnv.put(Context.SECURITY_CREDENTIALS, ADPropLoader.adminPassword);
            ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
            ldapEnv.put("com.sun.jndi.ldap.connect.pool", "true");
            ldapEnv.put("com.sun.jndi.ldap.connect.pool.protocol", "plain ssl");
            System.out.println(System.currentTimeMillis() + " <- Pre LDAP Context creation");
            LdapContext ldapContext = new InitialLdapContext(ldapEnv, null);
            System.out.println(System.currentTimeMillis() + " <- Post LDAP Context creation");
            return ldapContext;
        } catch (Exception e) {
            System.out.println(" Search error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
        return null;

    }

    public static List<ADGroup> getGroupDetails(String[] samAcountList) throws NamingException {
        List<String> groupList = getDistinctGroups(samAcountList);
        List<ADGroup> detailedADGroupList = new ArrayList<ADGroup>();
        //String samquerystring = "";
        String[] attributes = {"samaccountname", "description"};
        LdapContext cnx = getADConnection();
        for (int i = 0; i < groupList.size(); i++) {
            Attributes result = cnx.getAttributes(groupList.get(i), attributes);
            String groupDescr;
            if (result.get("description") == null) { groupDescr = ""; }
            else { groupDescr = result.get("description").get().toString(); }

            detailedADGroupList.add(new ADGroup(groupList.get(i),
                    result.get("samaccountname").get().toString(),
                    groupDescr));
        //samquerystring = samquerystring + "(sAMAccountName="+ groupList.get(i) +")";
        }


        //TODO get ready the object to return for table creation
        return detailedADGroupList;
    }

    public static List<String> getDistinctGroups(String[] samAcountList) {
        List<String> groupList = new ArrayList<String>();
        String samquerystring = "";
        for (int i=0; i<samAcountList.length; i++) {
            samquerystring = samquerystring + "(sAMAccountName="+ samAcountList[i] +")";
        }


        try {
        LdapContext ldapContext = getADConnection();
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchBase = ADPropLoader.searchBase;
        String[] attributes = {"memberOf"};
        searchCtls.setReturningAttributes(attributes);
        NamingEnumeration<?> answer = ldapContext.search(searchBase, "(&(objectclass=user)(|"+ samquerystring + "))", searchCtls);

        //Loop through the search results
        while (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();

            String dn = sr.getName() + ", " + searchBase;
            System.out.println(">>>" + sr.getName());

            Attributes attrs = sr.getAttributes();

            Attribute memberOf = attrs.get("memberOf");
            if (memberOf != null) {
                for (Enumeration e1 = memberOf.getAll(); e1.hasMoreElements(); ) {
                    String unprocessedGroupDN = e1.nextElement().toString();
                    System.out.println(unprocessedGroupDN);

                    groupList.add(unprocessedGroupDN);
                }
            }

        }
        groupList = groupList.stream().distinct().collect(Collectors.toList());
        System.out.println("Entering distinct list...");
        for (int i = 0; i < groupList.size(); i++) {
            System.out.println(groupList.get(i));
        }
        return groupList;

    } catch(
    NamingException e)    {
        e.printStackTrace();
        System.exit(-1);
    }
    return null;
}

    public static List<String> getPhone(String mailaddress) {
        List<String> phone = new ArrayList<String>();
        try {
        LdapContext cnx = getADConnection();
        SearchControls searchCtls = new SearchControls();
        String returnedAtts[]={"mail", "displayName", "description", "homePhone", "telephoneNumber", "mobile"};
        searchCtls.setReturningAttributes(returnedAtts);
        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //specify the LDAP search filter
        String searchFilter = "mail=" + mailaddress;
        //Specify the Base for the search
        String searchBase = ADPropLoader.searchBase;
        //initialize counter to total the results
        int totalResults = 0;

        // Search for objects using the filter

            NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();

                totalResults++;
                String dn = sr.getName() + ", " + searchBase;

                Attributes searchAtt = cnx.getAttributes(dn, returnedAtts);
                Attributes attrs = sr.getAttributes();
                if (attrs.get("description") != null) { System.out.println(attrs.get("description").get());
                    }
                if (attrs.get("mail") != null) { System.out.println(attrs.get("mail").get()); }
                if (attrs.get("telephoneNumber") != null) { System.out.println(attrs.get("telephoneNumber").get());
                    phone.add(attrs.get("telephoneNumber").get().toString()); }
                if (attrs.get("homePhone") != null) { System.out.println(attrs.get("homePhone").get());
                    phone.add(attrs.get("homePhone").get().toString()); }
                if (attrs.get("mobile") != null) { System.out.println(attrs.get("mobile").get());
                    phone.add(attrs.get("mobile").get().toString()); }

        }
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        return phone;
    }

    public static Map<String, String> getSAMAccountMap(String displayName) {
        System.out.println(displayName);
        Map<String, String> results = new HashMap();
        try {
            LdapContext cnx = getADConnection();
            SearchControls searchCtls = new SearchControls();
            String returnedAtts[]={"displayName", "samAccountName"};
            searchCtls.setReturningAttributes(returnedAtts);
            //Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //specify the LDAP search filter
            String searchFilter = "displayName=*" + displayName + "*";
            //Specify the Base for the search
            String searchBase = ADPropLoader.searchBase;
            //initialize counter to total the results
            int totalResults = 0;

            // Search for objects using the filter

            NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();

                totalResults++;
                String dn = sr.getName() + ", " + searchBase;


                Attributes searchAtt = cnx.getAttributes(dn, returnedAtts);
                Attributes attrs = sr.getAttributes();
                System.out.println(attrs.get("samaccountname").get().toString());
                System.out.println(attrs.get("displayName").get().toString());
                results.put(attrs.get("samaccountname").get().toString(), attrs.get("displayName").get().toString());
                System.out.println(attrs.get("displayName").get().toString());
                System.out.println(results.get(attrs.get("samaccountname").get().toString()));

            }
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println(results.get("Sarah"));
        return results;
    }

    public static String getSAMAccount(String mailaddress) {
        String samAccountName = new String();
        try {
            LdapContext cnx = getADConnection();
            SearchControls searchCtls = new SearchControls();
            String returnedAtts[]={"samAccountName"};
            searchCtls.setReturningAttributes(returnedAtts);
            //Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //specify the LDAP search filter
            String searchFilter = "mail=" + mailaddress;
            //Specify the Base for the search
            String searchBase = ADPropLoader.searchBase;
            //initialize counter to total the results
            int totalResults = 0;

            // Search for objects using the filter

            NamingEnumeration<SearchResult> answer = cnx.search(searchBase, searchFilter, searchCtls);
            //Loop through the search results
            while (answer.hasMoreElements())
            {
                SearchResult sr = (SearchResult)answer.next();

                totalResults++;
                String dn = sr.getName() + ", " + searchBase;

                Attributes searchAtt = cnx.getAttributes(dn, returnedAtts);
                Attributes attrs = sr.getAttributes();
                samAccountName = attrs.get("samaccountname").get().toString();
                System.out.println(samAccountName);
            }
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        return samAccountName;
    }
    public static Boolean setPassword(String samAccountName, String password) {

        try
        {

            ModificationItem[] mods = new ModificationItem[1];

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("employeeNumber","18"));
            String fullDN = getDN(samAccountName);
            LdapContext cnx = getADConnection();

            System.out.println(System.currentTimeMillis() + " <- Pre Create Attribute");
            BasicAttribute attr = new BasicAttribute("UnicodePwd", createUnicodePassword(password));
            System.out.println(System.currentTimeMillis() + " <- Pre Make modification item");
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
            System.out.println(System.currentTimeMillis() + " <- Pre Modify Context (Password)");
            cnx.modifyAttributes(fullDN, mods);
            System.out.println(System.currentTimeMillis() + " <- Post Modify Context (Password)");
            cnx.close();
            System.out.println(System.currentTimeMillis() + " <- Post Connection Close");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("update password error: " + e);
            return false;
        }
    }
    public static Map<String, String> createUser(ADUser newUser) throws NamingException {
        String userName = newUser.getfName()  + "."  + newUser.getsName(); //TODO Add . or create naming scheme
        String userCN = newUser.getfName() + " " + newUser.getsName();
        String userDN = "CN=" + userCN + "," + ADPropLoader.defaultUserOU;
        String newPassword = ADPassword.generate();

        Map<String, String> returnedDetails = new HashMap();
        returnedDetails.put("samAccountName", userName);
        returnedDetails.put("password", newPassword);

        Attributes container = new BasicAttributes();

        Attribute objClasses = new BasicAttribute("objectClass");
        objClasses.add("top");
        objClasses.add("person");
        objClasses.add("organizationalPerson");
        objClasses.add("user");

        String cnValue = /* userName; */ userCN;
        Attribute cn = new BasicAttribute("cn", cnValue);
        Attribute sAMAccountName = new BasicAttribute("sAMAccountName", userName);
        Attribute principalName = new BasicAttribute("userPrincipalName", userName
                + "@" + ADPropLoader.domainSuffix);
        Attribute givenName = new BasicAttribute("givenName", newUser.getfName());
        Attribute sn = new BasicAttribute("sn", newUser.getsName());
        Attribute uid = new BasicAttribute("uid", userName);
        Attribute password = new BasicAttribute("unicodePwd", createUnicodePassword(newPassword));
        Attribute logonscript = new BasicAttribute("scriptPath", "logon.bat"); //TODO work out where to get script from

        container.put(objClasses);
        container.put(sAMAccountName);
        container.put(principalName);
        container.put(cn);
        container.put(sn);
        container.put(givenName);
        container.put(uid);
        container.put(password);
        //container.put(logonscript); //TODO Login script not implemented yet

            LdapContext cnx = getADConnection();
            cnx.createSubcontext(userDN, container);

            String[] groupArray = newUser.getGroups(); // Get the array of group DN's from the JSon
            ModificationItem mod[] = new ModificationItem[1];
            mod[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
                    new BasicAttribute("member", userDN)); // Prepare an attribute for adding to a group
            for (String item: groupArray) {
                cnx.modifyAttributes(item, mod); // Add to group!
                System.out.println("Adding to the group: " + item);
            }
            return returnedDetails;

    }
    public static void enableUser(String samAccountName) throws NamingException {
        String dn = getDN(samAccountName);
        System.out.println(dn);

        LdapContext ctx = getADConnection();

        int userAccountControlOrig = Integer.parseInt(ctx.getAttributes(dn).get("userAccountControl").get().toString());
        int userAccountControlValue = userAccountControlOrig & ~0x2; // Add UF_ACCOUNTDISABLE bits
        ModificationItem[] mods = new ModificationItem[1];
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",""+userAccountControlValue));
        ctx.modifyAttributes(dn, mods);
    }
}