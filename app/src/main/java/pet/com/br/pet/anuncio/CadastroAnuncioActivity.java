package pet.com.br.pet.anuncio;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.gps.LocationDirector;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Profile;
//endregion

public class CadastroAnuncioActivity extends BaseMenu {

    private static final String LOGIN_URL = "http://kahvitech.com/pet/cadastraAnuncio.php";
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
    private static final int SELECT_PICTURE2 = 2;

    Uri selectedImage;
    Uri selectedImage2;

    static String encoded_string;
    private String image_name;
    private Bitmap bitmap;
    private File file;
    private Uri file_uri;

    //PERMISSION
    private static final int GPS_ENABLE_REQUEST = 0x1001;
    private static final int WIFI_ENABLE_REQUEST = 0x1002;
    private static final int ROAMING_ENABLE_REQUEST = 0x1003;

    //LOCATION
    static final Integer LOCATION = 0x1;

    //ALERT DIALOG
    private android.support.v7.app.AlertDialog mGPSDialog;


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
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE);
    }
    public void inicia_img2(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE2);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded=null;
        try {
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            encoded_string = imageEncoded;
            Log.e("LOOK", imageEncoded);
        } catch (Exception e){
            e.printStackTrace();
        }catch(OutOfMemoryError e){
        baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,50, baos);
        b=baos.toByteArray();
            imageEncoded=Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("EWN", "Out of memory error catched");
    }
            return imageEncoded;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        if(!(Profile.getLatitude() == null && Profile.getLongitude() == null)) {
            iniciaAnuncio();
        }else {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
            Toast.makeText(this, "Reenvie novamente o seu anuncio", Toast.LENGTH_SHORT).show();
        }
    }

    //PERMISSÃO HABILITAR GPS

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
            Toast.makeText(this, "" + permission + " permissão já garantida.", Toast.LENGTH_SHORT).show();
            statusCheck();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    try {
                        statusCheck();
                    }catch(Exception e){
                        statusCheck();
                    }
                    break;
            }
        }
    }

    public void statusCheck() {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDiabledDialog();
            }
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                LocationDirector myloc = new LocationDirector(this);

                if (myloc.canGetLocation) {
                    double latitude = myloc.getLatitude();
                    double longitude = myloc.getLongitude();
                    getLatitude = "" + latitude;
                    getLongitude = "" + longitude;
                    Profile.setLatitude(Double.toString(latitude));
                    Profile.setLongitude(Double.toString(longitude));

                    Log.v("get location values", Double.toString(latitude)
                            + "     " + Double.toString(longitude));
                }
                //checkInternetConnection();
                //Intent i = new Intent(SettingsConfiguration.this, MainActivity.class);
                //startActivity(i);
                //finish();
            }

        }catch (Exception e){
            //statusCheck();
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        }
    }

    //VERIFICA SE O GPS ESTÁ DESATIVADO.
    private void showGPSDiabledDialog() {
        try{
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("OoOps GPS Desativado!");
            builder.setMessage("Ative o seu GPS para utilizar o aplicativo");
            builder.setPositiveButton("Ativar GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
                }
            });
                /*.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //dialog.cancel();
                        finish();
                    }
                });*/
            mGPSDialog = builder.create();
            mGPSDialog.show();
        }catch (Exception e){
            showGPSDiabledDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LocationDirector myloc = new LocationDirector(this);

            if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
                try {
                    image_name = "pic1.jpg";
                    Uri selectedImage = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ImageView my_img_view = (ImageView) findViewById (R.id.imageAnuncio);
                    Bitmap bitajustado = Bitmap.createScaledBitmap(bitmap,300, 300, true);
                    my_img_view.setImageBitmap(bitajustado);
                    encodeTobase64(bitajustado);
                } catch (Exception e) {
                    Log.e("Error na captura","CODE: "+e);
                }catch(OutOfMemoryError e){
                    Log.e("Error na captura","CODE: "+e);
                }
        }

        if (requestCode == SELECT_PICTURE2 && resultCode == RESULT_OK) {
            try {
                image_name = "pic2.jpg";
                Uri selectedImage = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView my_img_view = (ImageView) findViewById (R.id.imageAnuncio2);
                Bitmap bitajustado = Bitmap.createScaledBitmap(bitmap,300, 300, true);
                my_img_view.setImageBitmap(bitajustado);
                encodeTobase64(bitajustado);
            } catch (Exception e) {
                Log.e("Error na captura","CODE: "+e);
            }catch(OutOfMemoryError e){
                Log.e("Error na captura","CODE: "+e);
            }
        }

        try {

            if (requestCode == GPS_ENABLE_REQUEST) {

                if (myloc.canGetLocation) {
                    double myLat = 0;
                    double myLong = 0;

                    myLat = myloc.getLatitude();
                    myLong = myloc.getLongitude();

                    Profile.setLatitude(""+myLat);
                    Profile.setLongitude(""+myLong);

                    Log.v("get location values", Double.toString(myLat)
                            + "     " + Double.toString(myLong));
                }
                statusCheck();
            }
            //else {
            //  super.onActivityResult(requestCode, resultCode, data);
            //}
        }catch (Exception e){
            Log.e("ALERTA DE ERROR A.R:", "REFAZNEDO LOGICA... ");
            statusCheck();
        }
    }


}
