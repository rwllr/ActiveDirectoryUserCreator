package biz.waller.ad;

import java.util.List;

/**
 * Created by Raphael on 21/02/2017.
 */
public class Runner {
/*    protected ADDetails myAD;
    public void Runner() {
        myAD.getPhone("raphael@waller.biz");
    }
*/

public static void main(String args[]) {
    String enteredNumber = "447858532173";
    List<String> phoneList = ADDetails.getPhone("raphael@waller.biz");
    for (int i=0; i<phoneList.size(); i++) {
        System.out.println(phoneList.get(i));
    }
    if (phoneList.contains(enteredNumber)) {
        MakeCall.dial(enteredNumber); }
        };
    }

