package biz.waller.ad;

/**
 * Created by Raphael on 09/03/2017.
 */
public class ADGroup {
    private String dn;
    private String name;
    private String description;

    public ADGroup(String dn, String name, String description) {
        this.dn = dn;
        this.name = name;
        this.description = description;
    }
}
