package pet.com.br.pet.models;

/**
 * Created by iacocesar on 04/02/2017.
 */

public class ConfirmationCodeTemp {
    private static String username;
    private static String email;

    public static String getUsername() {return username;}

    public static void setUsername(String username) {
        ConfirmationCodeTemp.username = username;
    }

    public static String getEmail() {return email;}

    public static void setEmail(String email) {ConfirmationCodeTemp.email = email;}

}
