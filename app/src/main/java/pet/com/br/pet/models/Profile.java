package pet.com.br.pet.models;

/**
 * Created by iaco_ on 14/09/2016.
 */
public class Profile {

    static String username;
    static String nomeexibicao;
    static String latitude;
    static String longitude;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }

    public static String getNomeExibicao() {return nomeexibicao; }

    public static void setNomeExibicao(String nomeexibicao) {
        Profile.nomeexibicao = nomeexibicao;
    }

    public static String getLatitude() {return latitude;}

    public static void setLatitude(String latitude) {Profile.latitude = latitude;}

    public static String getLongitude() {return longitude;}

    public static void setLongitude(String longitude) {Profile.longitude = longitude;}

}
