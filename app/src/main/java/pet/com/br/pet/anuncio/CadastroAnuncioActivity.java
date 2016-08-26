package pet.com.br.pet.anuncio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import pet.com.br.pet.R;
import pet.com.br.pet.menus.BaseMenu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//endregion

public class CadastroAnuncioActivity extends BaseMenu {

    private static final String LOGIN_URL = "http://thebossgamestudio.xyz/pet/inserirAnuncio.php";
    private static final String KEY_CODIGO="CODIGO";
    private static final String KEY_RACA="RACA";
    private static final String KEY_IDADE="IDADE";
    private static final String KEY_VALOR="VALOR";
    private static final String KEY_DESCRICAO="DESCRICAO";
    private static final String KEY_LAT="LAT";
    private static final String KEY_LON="LON";


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


    private String getLatitude;
    private String getLongitude;

    private String _codigo;

    ProgressDialog loading;


    //seleciona imagem

    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE2 = 1;

    Uri selectedImage;
    Uri selectedImage2;

    static String encoded_string;
    private String image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        txtcodigo = (TextView) findViewById(R.id.text_codigo);
        txtcodigo.setText("Codigo: "+criarCodigo());

        editTextraca = (EditText) findViewById(R.id.editText_raca);
        editTextidade = (EditText) findViewById(R.id.editText_idade);
        editTextvalor = (EditText) findViewById(R.id.editText_valor);
        editTextdescricao = (EditText) findViewById(R.id.editText_descricao);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        getLatitude = "" + latitude;
        getLongitude = "" + longitude;


    }

    private String criarCodigo(){
        String codigo;
        final DateFormat dateFormat = new SimpleDateFormat("HHmmddMMssyyyy");
        final Date data = new Date();
        double gen = Math.random() * 140;
        double getgen = Math.round(gen);
        codigo = (dateFormat.format(data) + getgen).replace(".","");
        _codigo = codigo;
        return codigo;
    }


    private void iniciaAnuncio() {
        strcodigo = _codigo.trim();
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
                map.put(KEY_LAT,getLatitude);
                map.put(KEY_LON,getLongitude);
                map.put("IMAGEM",image_name);
                map.put("IMAGEMPATCH",encoded_string);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void inicia_img1(View v) {
        // TODO Auto-generated method stub
        pickImage();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                image_name = "teste123.jpg";
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView my_img_view = (ImageView) findViewById (R.id.imageAnuncio);
                my_img_view.setImageBitmap(bitmap);
                encodeTobase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        encoded_string = imageEncoded;
        Log.e("LOOK", imageEncoded);
        return imageEncoded;
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
