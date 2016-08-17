package pet.com.br.pet.Anuncios;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import pet.com.br.pet.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
//endregion

public class CadastroAnuncioActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://thebossgamestudio.xyz/pet/inserirAnuncio.php";
    public static final String KEY_CODIGO="CODIGO";
    public static final String KEY_RACA="RACA";
    public static final String KEY_IDADE="IDADE";
    public static final String KEY_VALOR="VALOR";
    public static final String KEY_DESCRICAO="DESCRICAO";

    private TextView txtcodigo;
    private EditText editTextraca;
    private EditText editTextidade;
    private EditText editTextvalor;
    private EditText editTextdescricao;

    private String strcodigo;
    private String strraca;
    private String stridade;
    private String strvalor;
    private String strdescricao;

    private String codigogen;

    ProgressDialog loading;

    int negociacoes_isdown = 0, anuncios_isdown = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        final DateFormat dateFormat = new SimpleDateFormat("HHmmddMMssyyyy");
        final Date data = new Date();
        double gen = Math.random() * 140;
        double getgen = Math.round(gen);
        codigogen = (dateFormat.format(data) + getgen).replace(".","");

        txtcodigo = (TextView) findViewById(R.id.text_codigo);

        txtcodigo.setText("Codigo: "+codigogen);

        editTextraca = (EditText) findViewById(R.id.editText_raca);
        editTextidade = (EditText) findViewById(R.id.editText_idade);
        editTextvalor = (EditText) findViewById(R.id.editText_valor);
        editTextdescricao = (EditText) findViewById(R.id.editText_descricao);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();


        nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(false);
        nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(false);
        nav_menu.findItem(R.id.nav_buscarapida).setVisible(false);

        nav_menu.findItem(R.id.sub_chat).setVisible(false);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();


                switch (anuncios_isdown) {
                    case 0:
                        if (id == R.id.sub_ver_anuncio) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(true);
                            nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(true);
                            nav_menu.findItem(R.id.nav_buscarapida).setVisible(true);
                            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_info_details);
                            anuncios_isdown = 1;

                        }
                        break;

                    case 1:
                        if (id == R.id.sub_ver_anuncio) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(false);
                            nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(false);
                            nav_menu.findItem(R.id.nav_buscarapida).setVisible(false);
                            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_add);
                            anuncios_isdown = 0;
                        }
                        break;
                }

                if (anuncios_isdown == 1) {
                    if (id == R.id.sub_pesq_anuncio) {
                        Intent intencao = new Intent(CadastroAnuncioActivity.this, AnunciosActivity.class);
                        startActivity(intencao);
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        finish();
                    }

                    if (id == R.id.sub_ins_anuncio) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    if (id == R.id.nav_buscarapida) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }

                switch (negociacoes_isdown) {
                    case 0:
                        if (id == R.id.sub_ver_negociacoes) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_chat).setVisible(true);
                            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_info_details);
                            negociacoes_isdown = 1;
                        }

                        break;

                    case 1:
                        if (id == R.id.sub_ver_negociacoes) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_chat).setVisible(false);
                            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_add);
                            negociacoes_isdown = 0;
                        }
                        break;
                }

                if (negociacoes_isdown == 1) {

                    if (id == R.id.sub_chat) {
                        // Handle the About action
                    } else {
                    }
                }


                if (id == R.id.nav_conta) {
                    // Handle the About action
                }
                if (id == R.id.nav_sair) {
                    // Handle the About action
                }


                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                //drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    private void iniciaAnuncio() {
        strcodigo = codigogen.trim();
        strraca = editTextraca.getText().toString().trim();
        stridade = editTextidade.getText().toString().trim();
        strvalor = editTextvalor.getText().toString().trim();
        strdescricao = editTextdescricao.getText().toString().trim();

        loading = ProgressDialog.show(this, "Aguarde...", "Carregando...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.trim().equals("success")){
                                loading.dismiss();
                                openProfile();
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
                                builder.setMessage("ERRO AO CADASTRAR O ANUNCIO")
                                        .setNegativeButton("Tentar novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();
                            }
                        }catch(Exception e){
                            loading.dismiss();
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
                            builder.setMessage("Erro na sincronização com servidor: "+e)
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
                        builder.setMessage("Erro na sincronização com servidor: "+error)
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                        // Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();

                        //
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_CODIGO,strcodigo);
                map.put(KEY_RACA,strraca);
                map.put(KEY_IDADE,stridade);
                map.put(KEY_VALOR,strvalor);
                map.put(KEY_DESCRICAO,strdescricao);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void openProfile(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
        builder.setMessage("Anuncio Cadastrado com sucesso")
                .setNegativeButton("Prosseguir", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent refresh = new Intent(CadastroAnuncioActivity.this, CadastroAnuncioActivity.class);
                        startActivity(refresh);
                        finish();
                    }
                })
                .create()
                .show();

    }
    public void inicia_anuncio(View v) {
        iniciaAnuncio();
    }
}
