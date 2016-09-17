package pet.com.br.pet.models;

import java.util.ArrayList;

/**
 * Created by rafae on 20/08/2016.
 */
public class Usuario {

    private static String userName;
    private static String likes, dislikes;


    public static String getUserName() {  return userName; }
    public static void setUserName(String userName) {
        Usuario.userName = userName;
    }

    public static ArrayList<String> getLikes() {
        String[] stringLikes = likes.split("@");
        ArrayList<String> arrayLikes = new ArrayList<>();
        for(String likes : stringLikes){
            arrayLikes.add(likes);
        }
        return arrayLikes;
    }

    public static void setLikes(String likes) {
        Usuario.likes = likes;
    }

    public static ArrayList<String> getDislikes() {
        String[] stringDislikes = dislikes.split("@");
        ArrayList<String> arrayDislikes = new ArrayList<>();
        for(String dislikes : stringDislikes){
            arrayDislikes.add(dislikes);
        }
        return arrayDislikes;
    }

    public static void setDislikes(String dislikes) {
        Usuario.dislikes = dislikes;
    }


}
