package pet.com.br.pet.autentica;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.UrlUtils;

/**
 * Created by iaco_ on 15/08/2016.
 */
public class Login extends AppCompatActivity {
    Context context;
    private static final String LOGIN_URL = "http://kahvitech.com/pet/login.php";
    private static final String USER_DETAIL_URL = "http://kahvitech.com/pet/informacoesCurtidas.php?user=";

    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_NOME_EXIBICAO = "NOMEEXIBICAO";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_LIKES = "LIKES";
    private static final String KEY_DISLIKE = "DISLIKE";
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
    private String _name, _nomeExibicao, _likes, _dislikes, _medal, _dogCoin, _termoContrato, _profileImg;

    //REGISTRAR
    public static int fadeout_valueregistro = 0;

    int termo_uso_value = 0;

    private final int REQUEST_CODE_SMS_PERMISSIONS = 0x2;
    private final int REQUEST_PHONE_STATE_CODE = 0x3;
    //RELATIVES
    RelativeLayout relative_termo_contrato, relative_login_form;

    RelativeLayout relative_fade;

    //registro
    EditText texto_cadastra_username;
    EditText texto_cadastra_nomeexibicao;
    EditText texto_cadastra_senha;
    EditText texto_cadastra_email;
    //EditText texto_cadastra_celular;
    TextView registro_username_alerta, registro_password_alerta, registro_nomeexibicao_alerta, registro_email_alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //VALUES
        termo_uso_value = 0;
        fadeout_valueregistro = 0;


        //IDS
        relative_termo_contrato = (RelativeLayout) findViewById(R.id.relative_termo_contrato);
        relative_login_form = (RelativeLayout) findViewById(R.id.relative_loginform);
        relative_fade = (RelativeLayout) findViewById(R.id.relative_registro_fadeout);
        editTextUsername = (EditText) findViewById(R.id.editText_login);
        editTextPassword = (EditText) findViewById(R.id.editText_senha);
        texto_cadastra_username = (EditText) findViewById(R.id.editText_registrar_login);
        texto_cadastra_senha = (EditText) findViewById(R.id.editText_registrar_senha);
        texto_cadastra_nomeexibicao = (EditText) findViewById(R.id.editText_registrar_nomeusuario);
        texto_cadastra_email = (EditText) findViewById(R.id.editText_registrar_email);
        registro_username_alerta = (TextView) findViewById(R.id.registro_username_alert);
        registro_password_alerta = (TextView) findViewById(R.id.registro_password_alert);
        registro_nomeexibicao_alerta = (TextView) findViewById(R.id.registro_nomeexibicao_alert);
        registro_email_alerta = (TextView) findViewById(R.id.registro_email_alert);

        //TELEPHONEMANAGER
        askForPermission(Manifest.permission.READ_SMS, REQUEST_CODE_SMS_PERMISSIONS);


        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = mTelephonyMgr.getLine1Number();
            Profile.setPhoneNumber(mPhoneNumber);
            Log.e("Telefone: ", "" + mPhoneNumber);

        } catch (Exception e) {

            Log.e("Exception: ", "" + e.getMessage());
        }


        //USERCACHE
        _session = new LoginManager(getApplicationContext());


        //RELATIVE
        RelativeLayout layoutregistro = (RelativeLayout) findViewById(R.id.relative_autenticacao_registro);
        if (!layoutregistro.hasOnClickListeners()) {
            relative_fade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fadeout_valueregistro == 1) {
                        relative_fade.setVisibility(View.GONE);
                        relative_login_form.setVisibility(View.VISIBLE);
                        fadeout_valueregistro = 0;
                    }
                }
            });
        }


    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " permissão já garantida.", Toast.LENGTH_SHORT).show();
            statusCheck();
        }
    }

    public void statusCheck() {
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = mTelephonyMgr.getLine1Number();
            Profile.setPhoneNumber(mPhoneNumber);
            Log.e("Telefone: ", "" + mPhoneNumber);

        } catch (Exception e) {
            askForPermission(Manifest.permission.READ_SMS, REQUEST_CODE_SMS_PERMISSIONS);
            askForPermission(Manifest.permission.READ_PHONE_STATE, REQUEST_PHONE_STATE_CODE);
            Log.e("Exception: ", "" + e.getMessage());
        }
    }


    public void OnClickRegistrar(View v) {
        OpenTermoContrato();
    }


    public void OpenTermoContrato() {
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

                        try {
                            if (response.trim().equals("success")) {
                                getInfos();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("ERRO AO REALIZAR O LOGIN SENHA OU USUARIO INVALIDO")
                                        .setNegativeButton("Tentar novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();

                            }
                        } catch (Exception e) {
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
                        builder.setMessage("Erro na sincronização com servidor: " + error)
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME, username);
                map.put(KEY_PASSWORD, password);
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getInfos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(USER_DETAIL_URL + username,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json = response.getJSONObject(i);
                                _name = "" + json.getString(KEY_USERNAME);
                                _nomeExibicao = "" + json.getString(KEY_NOME_EXIBICAO);
                                _likes = "" + json.getString(KEY_LIKES);
                                _dislikes = "" + json.getString(KEY_DISLIKE);
                                _medal = "" + json.getString(KEY_MEDAL);
                                _dogCoin = "" + json.getString(KEY_DOG_COIN);
                                _profileImg = "" + json.getString(KEY_PROFILE);
                                _termoContrato = "" + json.getString(KEY_TERMO_CONTRATO);

                                _session.createLoginSession(_name, _nomeExibicao, _likes, _dislikes, _dogCoin, _termoContrato, _medal, _profileImg);
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
                        Toast.makeText(Login.this, "Sem conexao com o servidor" + error, Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void abrirConta() {
        Intent intent = new Intent(this, BuscaRapidaActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View v) {
        solicitaLogin();
    }


    //TERMO DE CONTRATO

    public void AceitarTermoContrato(View v) {
        if (termo_uso_value == 0) {
            relative_termo_contrato.setVisibility(View.VISIBLE);
            if (fadeout_valueregistro == 0) {
                relative_fade.setVisibility(View.VISIBLE);
                // Creating a new RelativeLayout
                fadeout_valueregistro = 1;
            }
            relative_login_form.setVisibility(View.GONE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 1;
        }
    }

    public void RejeitarTermoContrato(View v) {
        if (termo_uso_value == 1) {
            relative_login_form.setVisibility(View.VISIBLE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 0;

        } else {
            relative_login_form.setVisibility(View.VISIBLE);
            relative_termo_contrato.setVisibility(View.GONE);
            termo_uso_value = 0;
        }
    }

    public void BotaoaceitarRegistro(View v) {
        if(texto_cadastra_username.length() == 0 && texto_cadastra_nomeexibicao.length() == 0 &&
                texto_cadastra_senha.length() == 0 && texto_cadastra_email.length() == 0){
            registro_username_alerta.setText("(Preencha o campo corretamente)");
            registro_username_alerta.setVisibility(View.VISIBLE);
            registro_password_alerta.setText("(Preencha o campo corretamente)");
            registro_password_alerta.setVisibility(View.VISIBLE);
            registro_nomeexibicao_alerta.setText("(Preencha o campo corretamente)");
            registro_nomeexibicao_alerta.setVisibility(View.VISIBLE);
            registro_email_alerta.setText("(Preencha o campo corretamente)");
            registro_email_alerta.setVisibility(View.VISIBLE);
        }else{
            validateTextRegistro();
        }
    }

    private void validateTextRegistro() {
        if (!validateTextUsername()) {
            return;
        }

        if (!validateTextPassword()) {
            return;
        }

        if (!validateTextNomeExibicao()) {
            return;
        }
        if (!validateTextEmail()) {
            return;
        }

        enviaRegistro();
    }

    public void ConfirmaCodigoEmail(){
        final EditText edittext = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Ativação de conta!");
        builder.setTitle("Enviamos uma mensagem para o seu email, copie e cole aqui o código enviado.");

        builder.setView(edittext);

        builder.setPositiveButton("Validar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                edittext.getText().toString();
            }
        });

        builder.setNegativeButton("Reenviar Código", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        builder.show();
    }

    private boolean validateTextUsername() {
        if (texto_cadastra_username.length() == 0) {
            registro_username_alerta.setText("(Preencha o campo corretamente)");
            registro_username_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if(texto_cadastra_username.length() > 0 && texto_cadastra_username.length() < 7) {
            registro_username_alerta.setText("(O campo deve conter 7 ou mais digitos.)");
            registro_username_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if(texto_cadastra_username.length() >= 7){
            registro_username_alerta.setVisibility(View.GONE);
        }
        return true;
    }

    private boolean validateTextPassword() {
        if (texto_cadastra_senha.length() == 0) {
            registro_password_alerta.setText("(Preencha o campo corretamente)");
            registro_password_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if (texto_cadastra_senha.length() > 0 && texto_cadastra_senha.length() < 7) {
            registro_password_alerta.setText("(O campo deve conter 7 ou mais digitos.)");
            registro_password_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if(texto_cadastra_senha.length() >=7){
            registro_password_alerta.setVisibility(View.GONE);
        }
        return true;
    }

    private boolean validateTextNomeExibicao() {
        if (texto_cadastra_nomeexibicao.length() == 0) {
            registro_nomeexibicao_alerta.setText("(Preencha o campo corretamente)");
            registro_nomeexibicao_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if (texto_cadastra_nomeexibicao.length() > 0 && texto_cadastra_nomeexibicao.length() < 7) {
            registro_nomeexibicao_alerta.setText("(O campo deve conter 7 ou mais digitos.)");
            registro_nomeexibicao_alerta.setVisibility(View.VISIBLE);
            return false;
        } else if(texto_cadastra_nomeexibicao.length() >= 7){
            registro_nomeexibicao_alerta.setVisibility(View.GONE);
        }
        return true;
    }

    private boolean validateTextEmail() {
        if (texto_cadastra_email.length() == 0) {
            registro_email_alerta.setText("(Preencha o campo corretamente)");
            registro_email_alerta.setVisibility(View.VISIBLE);
            return false;
        } else {
            registro_email_alerta.setVisibility(View.GONE);
        }
        return true;
    }


    //ENVIAREGISTRO
    public void enviaRegistro() {
        registro_username_alerta.setVisibility(View.GONE);
        registro_password_alerta.setVisibility(View.GONE);
        registro_nomeexibicao_alerta.setVisibility(View.GONE);
        registro_email_alerta.setVisibility(View.GONE);
        loading = ProgressDialog.show(Login.this, "Aguarde...", "Carregando...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtils.REGISTRO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.trim().equals("Usuario ja existente")){
                                //Toast.makeText(Login.this, "Usuario já existente, tente outro", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Nome de usuario já existente, tente outro.")
                                        .setNegativeButton("Tentar Novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();
                            }else if (response.trim().equals("Email ja existente")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Email já existente, tente outro.")
                                        .setNegativeButton("Tentar Novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();
                            }else if (response.trim().equals("sucesso")) {
                                //Toast.makeText(Login.this, "Usuario Cadastrado Com Sucesso", Toast.LENGTH_SHORT).show();
                                texto_cadastra_username.setText("");
                                texto_cadastra_senha.setText("");
                                texto_cadastra_nomeexibicao.setText("");
                                texto_cadastra_email.setText("");
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Usuario Cadastrado Com Sucesso.")
                                        .setNegativeButton("Prosseguir", null)
                                        .create()
                                        .show();
                                ConfirmaCodigoEmail();
                                loading.dismiss();
                                fadeout_valueregistro = 0;
                                relative_fade.setVisibility(View.GONE);
                                relative_login_form.setVisibility(View.VISIBLE);
                            }else if(response.trim().equals("erro")) {
                                //Toast.makeText(Login.this, "Não foi possivel cadastrar o seu usuário", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("Não foi possivel cadastrar o seu usuário.")
                                        .setNegativeButton("Tentar Novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();
                            }
                        } catch (Exception e) {
                            //Toast.makeText(Login.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setMessage("Erro ao cadastrar usuário.")
                                    .setNegativeButton("Tentar Novamente", null)
                                    .create()
                                    .show();
                            loading.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String username = texto_cadastra_username.getText().toString();
                String password = texto_cadastra_senha.getText().toString();
                String nomeexibicao = texto_cadastra_nomeexibicao.getText().toString();
                String email = texto_cadastra_email.getText().toString();
                String telefone = "5513991571171";

                map.put("USERNAME",String.valueOf(username));
                map.put("PASSWORD", String.valueOf(password));
                map.put("NOMEEXIBICAO", String.valueOf(nomeexibicao));
                map.put("EMAIL", String.valueOf(email));
                map.put("CELULAR", String.valueOf(telefone));
                return map;
            }
        };
        RequestQueue requestQueueNew = Volley.newRequestQueue(Login.this);
        requestQueueNew.add(stringRequest);
    }
}

