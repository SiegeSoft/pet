package pet.com.br.pet.autentica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.buscaRapida.BuscaRapidaActivity;
import pet.com.br.pet.fragments.Registro;
import pet.com.br.pet.models.Profile;

/**
 * Created by iaco_ on 15/08/2016.
 */
public class Login  extends AppCompatActivity {

    private static final String LOGIN_URL = "http://kahvitech.com/pet/login.php";
    private static final String USER_DETAIL_URL = "http://kahvitech.com/pet/informacoesCurtidas.php?user=";

    private static final String KEY_USERNAME="USERNAME";
    private static final String KEY_NOME_EXIBICAO ="NOMEEXIBICAO";
    private static final String KEY_PASSWORD="PASSWORD";
    private static final String KEY_LIKES="LIKES";
    private static final String KEY_DISLIKE="DISLIKE";
    private static final String KEY_DOG_COIN = "DOGCOIN";
    private static final String KEY_TERMO_CONTRATO = "TERMODECONTRATO";
    private static final String KEY_MEDAL = "MEDAL";
    private static final String KEY_PROFILE = "PROFILEIMG";

    private EditText editTextUsername;
    private EditText editTextPassword;

    private String username;
    private String password;
    private ProgressDialog loading;

    private LoginManager _session;
    private String _name, _nomeExibicao, _likes, _dislikes, _medal, _dogCoin, _termoContrato, _profileImg ;

    //REGISTRAR
    public static int fadeout_valueregistro = 0;

    int termo_uso_value = 0;
    Registro registro;

    //RELATIVES
    RelativeLayout relative_termo_contrato, relative_login_form;

    public static RelativeLayout relative_fade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        termo_uso_value = 0;
        fadeout_valueregistro = 0;

        relative_termo_contrato = (RelativeLayout) findViewById(R.id.relative_termo_contrato);
        relative_login_form = (RelativeLayout) findViewById(R.id.relative_loginform);

        relative_fade = (RelativeLayout) findViewById(R.id.relative_registro_fadeout);
        editTextUsername = (EditText) findViewById(R.id.editText_login);
        editTextPassword = (EditText) findViewById(R.id.editText_senha);

        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = mTelephonyMgr.getLine1Number();
        Profile.setPhoneNumber(mPhoneNumber);

        _session = new LoginManager(getApplicationContext());

        RelativeLayout relative_registro_fragment_view = (RelativeLayout) findViewById(R.id.relative_autenticacao_registro);
        if (!relative_registro_fragment_view.hasOnClickListeners()) {
            RelativeLayout relative_fadeout = (RelativeLayout) findViewById(R.id.relative_registro_fadeout);
            relative_fadeout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fadeout_valueregistro == 1) {
                        RelativeLayout relative_fade = (RelativeLayout) findViewById(R.id.relative_registro_fadeout);
                        relative_fade.setVisibility(View.GONE);
                        relative_login_form.setVisibility(View.VISIBLE);
                        fadeout_valueregistro = 0;
                    }
                }
            });
        }


    }

    public void OnClickRegistrar(View v){
        OpenTermoContrato();
    }



    public void OpenTermoContrato(){
        termo_uso_value = 0;
        relative_termo_contrato.setVisibility(View.VISIBLE);
        relative_login_form.setVisibility(View.GONE);
    }

    private void solicitaLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        loading = ProgressDialog.show(this, "Aguarde...", "Carregando...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            if(response.trim().equals("success")){
                                getInfos();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("ERRO AO REALIZAR O LOGIN SENHA OU USUARIO INVALIDO")
                                        .setNegativeButton("Tentar novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();

                            }
                        }catch(Exception e){
                            loading.dismiss();
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setMessage("Erro na sincronização com servidor")
                                    .setNegativeButton("Tentar novamente", null)
                                    .create()
                                    .show();
                            loading.dismiss();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        error.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("Erro na sincronização com servidor: "+error)
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,username);
                map.put(KEY_PASSWORD,password);
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getInfos(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(USER_DETAIL_URL+username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json = response.getJSONObject(i);
                                _name = ""+json.getString(KEY_USERNAME);
                                _nomeExibicao = ""+json.getString(KEY_NOME_EXIBICAO);
                                _likes = ""+json.getString(KEY_LIKES);
                                _dislikes =""+json.getString(KEY_DISLIKE);
                                _medal = ""+json.getString(KEY_MEDAL);
                                _dogCoin = ""+json.getString(KEY_DOG_COIN);
                                _profileImg = ""+json.getString(KEY_PROFILE);
                                _termoContrato = ""+json.getString(KEY_TERMO_CONTRATO);

                                _session.createLoginSession(_name, _nomeExibicao, _likes,_dislikes, _dogCoin, _termoContrato, _medal, _profileImg);
                                abrirConta();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                loading.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(Login.this, "Sem conexao com o servidor"+error, Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void abrirConta(){
        Intent intent = new Intent(this, BuscaRapidaActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View v) {
        solicitaLogin();
    }


    //TERMO DE CONTRATO

    public void AceitarTermoContrato(View v){
        if(termo_uso_value == 0) {
            relative_termo_contrato.setVisibility(View.VISIBLE);
            if (fadeout_valueregistro == 0) {
                RelativeLayout relative_fadeout = (RelativeLayout) findViewById(R.id.relative_registro_fadeout);
                relative_fadeout.setVisibility(View.VISIBLE);
                // Creating a new RelativeLayout
                fadeout_valueregistro = 1;
            }
            relative_login_form.setVisibility(View.GONE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 1;
        }
    }

    public void RejeitarTermoContrato(View v){
        if(termo_uso_value == 1) {
            relative_login_form.setVisibility(View.VISIBLE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 0;

        }else{
            relative_login_form.setVisibility(View.VISIBLE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 0;
        }
    }

}

