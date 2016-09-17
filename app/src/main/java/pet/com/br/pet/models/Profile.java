package pet.com.br.pet.models;

/**
 * Created by iaco_ on 14/09/2016.
 */
public class Profile {
    static String username;
    static String id;

    public String getId() {
        return id;
    }

    public static void setId(String id) {
        Profile.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }
}
