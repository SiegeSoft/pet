package pet.com.br.pet.autentica;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

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
    public static final String KEY_NOME_EXIBICAO = "NOMEEXIBICAO";
    public static final String KEY_DOG_COIN = "DOGCOIN";
    public static final String KEY_TERMO_CONTRATO = "TERMODECONTRATO";
    public static final String KEY_MEDAL = "MEDAL";
    public static final String KEY_PROFILE = "PROFILEIMG";


    public LoginManager(Context _context) {
        this._context = _context;
        _sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        _editor = _sharedPreferences.edit();
    }


    /**
     * Login session
     */
    public void createLoginSession(String name, String nomeExibicao, String like, String dislike, String dogCoint, String termoContrato, String medal, String profileImg){
         _editor.putBoolean(IS_LOGIN, true);
        _editor.putString(KEY_NAME, name);
        _editor.putString(KEY_NOME_EXIBICAO, nomeExibicao);
        _editor.putString(KEY_LIKE, like);
        _editor.putString(KEY_DISLIKE, dislike);
        _editor.putString(KEY_DOG_COIN, dogCoint);
        _editor.putString(KEY_TERMO_CONTRATO, termoContrato);
        _editor.putString(KEY_MEDAL, medal);
        _editor.putString(KEY_PROFILE, profileImg);
        _editor.commit();
    }


    /**
     * DOGCOIN SESSION
     * */
    public void dogCoinSession(String dogCoin){
        _editor.putString(KEY_DOG_COIN, dogCoin);
        _editor.apply();
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
        user.put(KEY_NOME_EXIBICAO, _sharedPreferences.getString(KEY_NOME_EXIBICAO, null));
        user.put(KEY_LIKE, _sharedPreferences.getString(KEY_LIKE, null));
        user.put(KEY_DISLIKE, _sharedPreferences.getString(KEY_DISLIKE, null));
        user.put(KEY_DOG_COIN, _sharedPreferences.getString(KEY_DOG_COIN, null));
        user.put(KEY_TERMO_CONTRATO, _sharedPreferences.getString(KEY_TERMO_CONTRATO, null));
        user.put(KEY_MEDAL, _sharedPreferences.getString(KEY_MEDAL, null));
        user.put(KEY_PROFILE, _sharedPreferences.getString(KEY_PROFILE, null));

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
