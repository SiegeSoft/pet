package pet.com.br.pet.models;

import android.graphics.drawable.Drawable;

/**
 * Created by iaco_ on 14/09/2016.
 */
public class Profile {

    static String username;
    static String nomeexibicao;
    static String latitude;
    static String longitude;
    static Drawable icon;
    static String dogCoin;
    static String medal;
    static String termoDeContrato;
    static String profileImage;

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

    public static Drawable getIcon() {
        return icon;
    }

    public static void setIcon(Drawable icon) {
        Profile.icon = icon;
    }


    public static String getDogCoin() {
        return dogCoin;
    }

    public static void setDogCoin(String dogCoin) {
        Profile.dogCoin = dogCoin;
    }

    public static String getMedal() {
        return medal;
    }

    public static void setMedal(String medal) {
        Profile.medal = medal;
    }

    public static String getTermoDeContrato() {
        return termoDeContrato;
    }

    public static void setTermoDeContrato(String termoDeContrato) {
        Profile.termoDeContrato = termoDeContrato;
    }

    public static String getProfileImage() {
        return profileImage;
    }

    public static void setProfileImage(String profileImage) {
        Profile.profileImage = profileImage;
    }
}
