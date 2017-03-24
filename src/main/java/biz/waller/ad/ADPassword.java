package biz.waller.ad;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Raphael on 21/02/2017.
 */
public class ADPassword {
    public static String generate() {
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
        return password;
}
}