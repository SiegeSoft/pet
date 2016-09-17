package pet.com.br.pet.autentica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import pet.com.br.pet.anuncio.AnunciosActivity;
import pet.com.br.pet.buscaRapida.BuscaRapidaActivity;
import pet.com.br.pet.models.BuscaRapida;

/**
 * Created by rafae on 15/09/2016.
 */
public class LoginManager {

    private SharedPreferences _sharedPreferences;
    private SharedPreferences.Editor _editor;

    private Context _context;

    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LoginPetPref";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "username";
    public static final String KEY_LIKE = "like";
    public static final String KEY_DISLIKE = "dislike";


    public LoginManager(Context _context) {
        this._context = _context;
        _sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        _editor = _sharedPreferences.edit();
    }


    /**
     * Login session
     */
    public void createLoginSession(String name, String like, String dislike){
         _editor.putBoolean(IS_LOGIN, true);


        _editor.putString(KEY_NAME, name);
        _editor.putString(KEY_LIKE, like);
        _editor.putString(KEY_DISLIKE, dislike);


        _editor.commit();
    }


    /**
     * Check if is logged
     */
    public boolean checkLogin(){
        if(!this.isLoggedIn()){
            return false;
        }
        return true;
    }

    public boolean isLoggedIn(){
        return _sharedPreferences.getBoolean(IS_LOGIN, false);
    }


    /**
     * Hash Map com os dados dos usuarios
     * @return
     */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, _sharedPreferences.getString(KEY_NAME, null));
        user.put(KEY_LIKE, _sharedPreferences.getString(KEY_LIKE, null));
        user.put(KEY_DISLIKE, _sharedPreferences.getString(KEY_DISLIKE, null));

        return user;
    }



    /**
     * Clear session details
     * */
    public boolean logoutUser(){
        _editor.clear();
        _editor.commit();
        return true;

    }


}
