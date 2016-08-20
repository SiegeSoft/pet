package pet.com.br.pet.utils;

/**
 * Created by rafae on 20/08/2016.
 */
public class UsuarioUtils {

    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UsuarioUtils.userName = userName;
    }
}
