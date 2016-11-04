package pet.com.br.pet.models;

/**
 * Created by iaco_ on 14/09/2016.
 */
public class Profile {
    static String username;
    static String nomeexibicao;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }

    public static String getNomeExibicao() {
        return nomeexibicao;
    }

    public static void setNomeExibicao(String nomeexibicao) {
        Profile.nomeexibicao = nomeexibicao;
    }

}
