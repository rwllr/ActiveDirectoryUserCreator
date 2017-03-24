package biz.waller.ad;

/**
 * Created by Raphael on 14/03/2017.
 */
public class ADUser {
    //@SerializedName("fName")
    private String fName;
    private String sName;
    private String[] groupID;
    private String descr;

    public ADUser(String[] groups, String fName, String sName, String descr) {
        this.groupID = groups;
        this.fName = fName;
        this.sName = sName;
        this.descr = descr;
        System.out.println(this.fName);

    }
    public String getfName() {
        System.out.println(fName);
        return fName;
    }
    public String getsName() {
        System.out.println(sName);
        return sName;
    }
    public String[] getGroups() {
        return groupID;
    }
}
