package pet.com.br.pet.models;

/**
 * Created by iaco_ on 14/09/2016.
 */
public class Profile {
    static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }
}
