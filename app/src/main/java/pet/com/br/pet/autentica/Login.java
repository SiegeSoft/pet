package pet.com.br.pet.autentica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

/**
 * Created by iaco_ on 15/08/2016.
 */
public class Login  extends AppCompatActivity {

    private static final String LOGIN_URL = "http://kahvitech.com/pet/login.php";
    private static final String USER_DETAIL_URL = "http://kahvitech.com/pet/informacoesCurtidas.php?user=";

    private static final String KEY_USERNAME="USERNAME";
    private static final String KEY_PASSWORD="PASSWORD";
    private static final String KEY_LIKES="LIKES";
    private static final String KEY_DISLIKE="DISLIKE";
    private EditText editTextUsername;
    private EditText editTextPassword;

    private String username;
    private String password;
    private ProgressDialog loading;

    private LoginManager _session;
    private String _name, _likes, _dislikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editText_login);
        editTextPassword = (EditText) findViewById(R.id.editText_senha);
        _session = new LoginManager(getApplicationContext());

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
                                _likes = ""+json.getString(KEY_LIKES);
                                _dislikes =""+json.getString(KEY_DISLIKE);
                                _session.createLoginSession(_name,_likes,_dislikes);
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
}

