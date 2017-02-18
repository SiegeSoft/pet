package pet.com.br.pet.anuncio;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.gps.LocationDirector;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.models.Usuario;

import static pet.com.br.pet.utils.TagUtils.KEY_USERNAME;
//endregion

public class CadastroAnuncioActivity extends BaseMenu {

    private static final String CADASTRARANUNCIO_URL = "http://kahvitech.com/pet/cadastraAnuncio.php";
    private static final String KEY_CODIGO = "CODIGO";
    private static final String KEY_RACA = "RACA";
    private static final String KEY_IDADE = "IDADE";
    private static final String KEY_VALOR = "VALOR";
    private static final String KEY_DESCRICAO = "DESCRICAO";
    private static final String KEY_LAT = "LAT";
    private static final String KEY_LON = "LON";
    private static final String KEY_HORARIO = "HORARIO";
    private static final String KEY_CATEOGRIA = "CATEGORIA";
    private static final String KEY_DONO = "DONO";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private Date date = new Date();

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
    private String strdogcoin;


    private String getLatitude;
    private String getLongitude;

    private String _codigo;

    ProgressDialog loading;


    //seleciona imagem

    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_PICTURE2 = 2;

    Uri selectedImage;
    Uri selectedImage2;

    static String encoded_string, encoded_string2;
    private String image_name, image_name2;
    private Bitmap bitmap, bitmap2;
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
    private android.support.v7.app.AlertDialog imagedialog;
    private android.support.v7.app.AlertDialog dogcoindialog;


    //IMAGESVIEWS
    ImageView my_img_view, my_img_view2, img_view;
    static int valor_img_view = 0;

    static TextView tamanhoimagememmega;

    boolean showimagewidthvalue = false;

    //image dimens
    int newWidth = 0;
    int newHeight = 0;

    //SPINNERS

    Spinner spinner_categorias;

    //testandoooooo
    private TextInputLayout inputLayoutRaca, inputLayoutIdade, inputLayoutDescricao;

    //dogcoindiscount

    public int dogcondiscount = 0;

    //loginsession cache
    private LoginManager _session;
    private HashMap<String, String> _userDetails;


    //checkbox
    CheckBox checkboxbuscarapida;

    //VALUE BUSCARAPIDA
    int value_buscarapida=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);
        _session = new LoginManager(getApplicationContext());
        _userDetails = _session.getUserDetails();

        value_buscarapida=0;
        txtcodigo = (TextView) findViewById(R.id.text_codigo);
        txtcodigo.setText("Codigo: " + criarCodigo());
        tamanhoimagememmega = (TextView) findViewById(R.id.ImageSize);

        editTextraca = (EditText) findViewById(R.id.editText_raca);
        editTextidade = (EditText) findViewById(R.id.editText_idade);
        //editTextvalor = (EditText) findViewById(R.id.editText_valor);
        editTextdescricao = (EditText) findViewById(R.id.editText_descricao);
        editTextdescricao.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextdescricao.setRawInputType(InputType.TYPE_CLASS_TEXT);

        editTextraca.addTextChangedListener(new MyTextWatcher(editTextraca));
        editTextidade.addTextChangedListener(new MyTextWatcher(editTextidade));
        editTextdescricao.addTextChangedListener(new MyTextWatcher(editTextdescricao));

        inputLayoutRaca = (TextInputLayout) findViewById(R.id.input_layout_raca);
        inputLayoutIdade = (TextInputLayout) findViewById(R.id.input_layout_idade);
        inputLayoutDescricao = (TextInputLayout) findViewById(R.id.input_layout_descricao);
        ///
        img_view = (ImageView) findViewById(R.id.imageAnuncio);
        //checkbox
        checkboxbuscarapida = (CheckBox) findViewById(R.id.checkbox_buscarapida);

        spinner_categorias = (Spinner) findViewById(R.id.spinnercategorias);
        spinner_categorias.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager in = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        }) ;

        valor_img_view = 0;
    }



    private String criarCodigo() {
        String codigo;
        final DateFormat dateFormat = new SimpleDateFormat("HHmmddMMssyyyy");
        final Date data = new Date();
        double gen = Math.random() * 140;
        double getgen = Math.round(gen);
        codigo = (dateFormat.format(data) + getgen).replace(".", "");
        _codigo = codigo;
        return codigo;
    }


    private void iniciaAnuncio() {
        //dogcondiscount = Integer.parseInt(Profile.getDogCoin()) - 90;
        if(valor_img_view == 2) {
            dogcondiscount = Integer.parseInt(Profile.getDogCoin()) - 90;
          /**  _session.dogCoinSession(String.valueOf(dogcondiscount));
            Profile.setDogCoin(_userDetails.get(LoginManager.KEY_DOG_COIN));
            Log.e("DOGCOINVALUE",""+dogcondiscount);**/
        }

        strcodigo = _codigo.trim();
        strraca = editTextraca.getText().toString().trim();
        stridade = editTextidade.getText().toString().trim();
        //strvalor = editTextvalor.getText().toString().trim();
        strdescricao = editTextdescricao.getText().toString().trim();
        loading = ProgressDialog.show(this, "Aguarde...", "Carregando...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CADASTRARANUNCIO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.trim().equals("success")) {
                                loading.dismiss();
                                if(valor_img_view == 2) {
                                    _session.dogCoinSession(String.valueOf(dogcondiscount));
                                    Profile.setDogCoin(_userDetails.get(LoginManager.KEY_DOG_COIN));
                                    Log.e("DOGCOINVALUE",""+dogcondiscount);
                                }
                                openProfile();


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
                                builder.setMessage("ERRO AO CADASTRAR O ANUNCIO")
                                        .setNegativeButton("Tentar novamente", null)
                                        .create()
                                        .show();
                                loading.dismiss();
                            }
                        } catch (Exception e) {
                            loading.dismiss();
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
                            builder.setMessage("Erro na sincronização com servidor: " + e)
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
                        builder.setMessage("Erro na sincronização com servidor: " + error)
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                        // Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();

                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_CODIGO, strcodigo);
                map.put(KEY_RACA, strraca);
                map.put(KEY_IDADE, stridade);
                map.put(KEY_VALOR, "Doação");
                map.put(KEY_DESCRICAO, strdescricao);
                map.put(KEY_LAT, getLatitude);
                map.put(KEY_LON, getLongitude);
                map.put(KEY_DONO, Profile.getNomeExibicao());
                map.put(KEY_USERNAME, Usuario.getUserName());
                map.put(KEY_CATEOGRIA, spinner_categorias.getSelectedItem().toString());
                map.put(KEY_HORARIO, simpleDateFormat.format(date));
                map.put("IMAGEM", image_name);
                map.put("IMAGEMPATCH", encoded_string);
                if(valor_img_view == 2) {
                map.put("DOGCOIN", String.valueOf(dogcondiscount));
                }else{
                    map.put("DOGCOIN", "0");
                }
                map.put("BUSCARAPIDA", String.valueOf(value_buscarapida));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void inicia_img1(View v) {
        // TODO Auto-generated method stub
        if (valor_img_view == 0) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, SELECT_PICTURE);
        } else if (valor_img_view == 1 && Integer.parseInt(Profile.getDogCoin()) >= 90) {
            showDogCoinImage();
        }else{

        }
    }

    public void ImageException(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("OoOps sem DogCoin's =/");
        builder.setMessage("Deseja adiquirir mais moedas?");
        builder.setPositiveButton("Mais Moedas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                dialog.cancel();
            }
        });

    }

    public void showDogCoinImage(){
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Adiquirir a segunda imagem?");
            builder.setMessage("Utilizar DogCoin's para liberar a segunda imagem?");
            builder.setPositiveButton("Usar DogCoin", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which){
                    Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_PICTURE2);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {
                    dialog.cancel();
                }
            });
            dogcoindialog = builder.create();
            dogcoindialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void showImageWidthException() {
        if(valor_img_view == 0){
            int size = bitmap.getByteCount();
            double final_bitmapmegabytes = size / 1024.0;
            DecimalFormat dec = new DecimalFormat("0.00");
            String s = String.format(dec.format(final_bitmapmegabytes / 1024).concat(" MB de 5.7 MB"));
            tamanhoimagememmega.setTextColor(Color.parseColor("#FFFF0000"));
            tamanhoimagememmega.setText("" + s.replace(",", "."));
        }else if(valor_img_view == 1){
            int size = bitmap2.getByteCount();
            double final_bitmapmegabytes = size / 1024.0;
            DecimalFormat dec = new DecimalFormat("0.00");
            String s = String.format(dec.format(final_bitmapmegabytes / 1024).concat(" MB de 5.7 MB"));
            tamanhoimagememmega.setTextColor(Color.parseColor("#FFFF0000"));
            tamanhoimagememmega.setText("" + s.replace(",", "."));
        }
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Oops Tamanho da imagem excedeu 5.7 MB!");
            builder.setMessage("Deseja utilizar o nosso ajuste rápido?");
            builder.setPositiveButton("Usar Ajuste", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (valor_img_view == 0) {
                        Bitmap bitajustado = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                        encodeTobase64(bitajustado);
                    }else if (valor_img_view == 1) {
                        Bitmap bitajustado = Bitmap.createScaledBitmap(bitmap2, newWidth, newHeight, true);
                        encodeTobase64(bitajustado);
                    }

                    }
            });
            builder.setNegativeButton("Editarei a imagem", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {
                    tamanhoimagememmega.setTextColor(Color.parseColor("#000000"));
                    tamanhoimagememmega.setText("0.0 MB de 5.7 MB");
                    dialog.cancel();
                    //finish();
                    //startActivity(getIntent());
                }
            });
            imagedialog = builder.create();
            imagedialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


        public String encodeTobase64(Bitmap image) {

        String imageEncoded = null;
            Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (valor_img_view == 0) {
                immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                byte[] b = baos.toByteArray();
                try {
                    imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                    encoded_string = imageEncoded;
                    Log.e("LOOK", imageEncoded);


                    int size = immagex.getByteCount();
                    double final_bitmapmegabytes = size / 1024.0;

                    DecimalFormat dec = new DecimalFormat("0.00");

                    String s = String.format(dec.format(final_bitmapmegabytes / 1024).concat(" MB de 5.7 MB"));
                    tamanhoimagememmega.setTextColor(Color.parseColor("#000000"));
                    tamanhoimagememmega.setText("" + s.replace(",", "."));
                    my_img_view = (ImageView) findViewById(R.id.image_list_Anuncio2);
                    my_img_view.setImageBitmap(immagex);

                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(1000);
                    rotate.setInterpolator(new LinearInterpolator());
                    ObjectAnimator.ofFloat(my_img_view, View.ALPHA, 0.2f, 1.0f).setDuration(2400).start();
                    my_img_view.startAnimation(rotate);

                    img_view.setImageResource(R.drawable.pegarfotonegative);

                    valor_img_view = 1;

                } catch (Exception e) {

                    e.printStackTrace();

                } catch (OutOfMemoryError e) {
                    baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 50, baos);
                    b = baos.toByteArray();
                    imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.e("WAR: ", "Out of memory error catched");
                    valor_img_view = 1;
                }
        } else if (valor_img_view == 1) {
            immagex.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] b = baos.toByteArray();
            try {
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                encoded_string2 = imageEncoded;
                //Log.e("LOOK", imageEncoded);

                int size = immagex.getByteCount();
                double final_bitmapmegabytes = size / 1024.0;

                DecimalFormat dec = new DecimalFormat("0.00");

                String s = String.format(dec.format(final_bitmapmegabytes / 1024).concat(" MB de 1.3 MB"));
                tamanhoimagememmega.setTextColor(Color.parseColor("#000000"));
                tamanhoimagememmega.setText("" + s.replace(",", "."));

                my_img_view2 = (ImageView) findViewById(R.id.image_list_Anuncio3);
                my_img_view2.setImageBitmap(immagex);

                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setInterpolator(new LinearInterpolator());
                ObjectAnimator.ofFloat(my_img_view2, View.ALPHA, 0.2f, 1.0f).setDuration(2400).start();
                my_img_view2.startAnimation(rotate);

                img_view.setImageResource(R.drawable.pegarfotonegative);

                valor_img_view = 2;
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 50, baos);
                b = baos.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                //Log.e("EWN", "Out of memory error catched");
            }
            valor_img_view = 2;
        }
        return imageEncoded;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void openProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroAnuncioActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Anuncio Cadastrado com sucesso")
                .setNegativeButton("Finalizar", new DialogInterface.OnClickListener() {
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
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        if (!(Profile.getLatitude() == null && Profile.getLongitude() == null)) {
            submitForm();
        } else {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
            Toast.makeText(this, "Reenvie novamente o seu anuncio", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitForm() {
        if (!validateRaca()) {
            return;
        }

        if (!validateIdade()) {
            return;
        }

        if (!validateDescricao()) {
            return;
        }

        if(!spinner_categorias.getSelectedItem().toString().equals("Selecionar Categoria") && valor_img_view != 0){
            iniciaAnuncio();
        }else if(spinner_categorias.getSelectedItem().toString().equals("Selecionar Categoria") && valor_img_view != 0){
            Toast.makeText(this, "Você deve selecionar uma categoria", Toast.LENGTH_SHORT).show();
        }else if(spinner_categorias.getSelectedItem().toString().equals("Selecionar Categoria") && valor_img_view == 0){
            Toast.makeText(this, "Insira uma imagem e selecione uma Categoria", Toast.LENGTH_SHORT).show();
        }else if(!spinner_categorias.getSelectedItem().toString().equals("Selecionar Categoria") && valor_img_view == 0){
            Toast.makeText(this, "Insira uma Imagem", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(this, "" + permission + " permissão já garantida.", Toast.LENGTH_SHORT).show();
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
                    } catch (Exception e) {
                        statusCheck();
                    }
                    break;
            }
        }
    }

    public void statusCheck() {

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDiabledDialog();
            }
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

        } catch (Exception e) {
            //statusCheck();
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
        }
    }

    //VERIFICA SE O GPS ESTÁ DESATIVADO.
    private void showGPSDiabledDialog() {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("GPS Desativado!");
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
        } catch (Exception e) {
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
                if (!(bitmap.getWidth() > 1000 || bitmap.getHeight() > 1000)) {
                    //my_img_view = (ImageView) findViewById(R.id.image_list_Anuncio2);
                //Bitmap bitajustado = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                encodeTobase64(bitmap);
                //my_img_view.setImageBitmap(bitmap);
                //encodeTobase64(bitajustado);
                }else{
                    if(bitmap.getWidth()>1000 && bitmap.getHeight() < 1000) {
                        newWidth = 1000;
                        newHeight = bitmap.getHeight();
                        showImageWidthException();
                    }else if(bitmap.getWidth()<1000 && bitmap.getHeight() > 1000){
                        newWidth = bitmap.getWidth();
                        newHeight = 1000;
                        showImageWidthException();
                    }else if(bitmap.getWidth()>1000 && bitmap.getHeight() > 1000){
                        newWidth = 1000;
                        newHeight = 1000;
                        showImageWidthException();
                    }
                }
            } catch(NullPointerException e)
            {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap.getWidth()>1000 && bitmap.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap.getHeight();
                    showImageWidthException();
                }else if(bitmap.getWidth()<1000 && bitmap.getHeight() > 1000){
                    newWidth = bitmap.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap.getWidth()>1000 && bitmap.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            } catch (Exception e) {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap.getWidth()>1000 && bitmap.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap.getHeight();
                    showImageWidthException();
                }else if(bitmap.getWidth()<1000 && bitmap.getHeight() > 1000){
                    newWidth = bitmap.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap.getWidth()>1000 && bitmap.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            } catch (OutOfMemoryError e) {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap.getWidth()>1000 && bitmap.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap.getHeight();
                    showImageWidthException();
                }else if(bitmap.getWidth()<1000 && bitmap.getHeight() > 1000){
                    newWidth = bitmap.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap.getWidth()>1000 && bitmap.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            }
        }

        if (requestCode == SELECT_PICTURE2 && resultCode == RESULT_OK) {
            try {
                image_name2 = "pic2.jpg";
                Uri selectedImage = data.getData();
                bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                if (!(bitmap2.getWidth() > 1000 || bitmap2.getHeight() > 1000)) {
                    encodeTobase64(bitmap2);
                }else{
                    if(bitmap2.getWidth()>1000 && bitmap2.getHeight() < 1000) {
                        newWidth = 1000;
                        newHeight = bitmap2.getHeight();
                        showImageWidthException();
                    }else if(bitmap2.getWidth()<1000 && bitmap2.getHeight() > 1000){
                        newWidth = bitmap2.getWidth();
                        newHeight = 1000;
                        showImageWidthException();
                    }else if(bitmap2.getWidth()>1000 && bitmap2.getHeight() > 1000){
                        newWidth = 1000;
                        newHeight = 1000;
                        showImageWidthException();
                    }
                }
            } catch(NullPointerException e)
            {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap2.getWidth()>1000 && bitmap2.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap2.getHeight();
                    showImageWidthException();
                }else if(bitmap2.getWidth()<1000 && bitmap2.getHeight() > 1000){
                    newWidth = bitmap2.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap2.getWidth()>1000 && bitmap2.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            } catch (Exception e) {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap2.getWidth()>1000 && bitmap2.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap2.getHeight();
                    showImageWidthException();
                }else if(bitmap2.getWidth()<1000 && bitmap2.getHeight() > 1000){
                    newWidth = bitmap2.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap2.getWidth()>1000 && bitmap2.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            } catch (OutOfMemoryError e) {
                Log.e("Error na captura", "CODE: " + e);
                if(bitmap2.getWidth()>1000 && bitmap2.getHeight() < 1000) {
                    newWidth = 1000;
                    newHeight = bitmap2.getHeight();
                    showImageWidthException();
                }else if(bitmap2.getWidth()<1000 && bitmap2.getHeight() > 1000){
                    newWidth = bitmap2.getWidth();
                    newHeight = 1000;
                    showImageWidthException();
                }else if(bitmap2.getWidth()>1000 && bitmap2.getHeight() > 1000){
                    newWidth = 1000;
                    newHeight = 1000;
                    showImageWidthException();
                }
            }
        }

        try {

            if (requestCode == GPS_ENABLE_REQUEST) {

                if (myloc.canGetLocation) {
                    double myLat = 0;
                    double myLong = 0;

                    myLat = myloc.getLatitude();
                    myLong = myloc.getLongitude();

                    Profile.setLatitude("" + myLat);
                    Profile.setLongitude("" + myLong);

                    Log.v("get location values", Double.toString(myLat)
                            + "     " + Double.toString(myLong));
                }
                statusCheck();
            }
            //else {
            //  super.onActivityResult(requestCode, resultCode, data);
            //}
        } catch (Exception e) {
            Log.e("ALERTA DE ERROR A.R:", "REFAZNEDO LOGICA... ");
            statusCheck();
        }
    }


    ///EDITTEXTS ANIMATION

    private boolean validateRaca() {
        if (editTextraca.getText().toString().trim().isEmpty()) {
            inputLayoutRaca.setError(getString(R.string.err_msg_raca));
            requestFocus(editTextraca);
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editTextraca.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
            }
            inputLayoutRaca.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateIdade() {
        if (editTextidade.getText().toString().trim().isEmpty()) {
            inputLayoutIdade.setError(getString(R.string.err_msg_idade));
            requestFocus(editTextidade);
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editTextidade.setBackgroundTintList( ColorStateList.valueOf(Color.parseColor("#999999")) );
            }
            inputLayoutIdade.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDescricao() {
        if (editTextdescricao.getText().toString().trim().isEmpty()) {
            inputLayoutDescricao.setError(getString(R.string.err_msg_descricao));
            requestFocus(editTextdescricao);
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                editTextdescricao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
                editTextdescricao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
            }
            inputLayoutDescricao.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

        private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_raca:
                    validateRaca();
                    break;
                case R.id.editText_idade:
                    validateIdade();
                    break;
                case R.id.editText_descricao:
                    validateDescricao();
                    break;

            }
        }
    }

    public void OnClickCheckBuscaRapida(View v){
        if(value_buscarapida == 0){
            int total_debito_dogcoin = 0;
            int total_value_dogcoin = 0;
            if (my_img_view2 != null) {
                total_debito_dogcoin = 0;
                total_value_dogcoin = 0;
                total_debito_dogcoin = 90 + 10;
                total_value_dogcoin = Integer.parseInt(Profile.getDogCoin()) - total_debito_dogcoin;
                if (total_value_dogcoin < 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("OoOps, DogCoin's insuficientes");
                    builder.setMessage("Você não pode impulsionar o seu anuncio, custo total: " + total_debito_dogcoin + " DogCoin's");
                    builder.setNegativeButton("Entendido", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            checkboxbuscarapida.setChecked(false);
                            dialog.cancel();
                        }
                    });
                    builder.create();
                    builder.show();
                } else {
                    if (checkboxbuscarapida.isChecked() == true) {
                        checkboxbuscarapida.setChecked(true);
                        AlertaCheckBoxBuscaRapida();
                    }
                }
            } else {
                total_debito_dogcoin = 0;
                total_value_dogcoin = 0;
                total_debito_dogcoin = 10;
                total_value_dogcoin = Integer.parseInt(Profile.getDogCoin()) - total_debito_dogcoin;
                if (total_value_dogcoin < 0) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("OoOps, DogCoin's insuficientes");
                    builder.setMessage("Você não pode impulsionar o seu anuncio, custo total: " + total_debito_dogcoin + " DogCoin's");
                    builder.setNegativeButton("Entendido", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            checkboxbuscarapida.setChecked(false);
                            dialog.cancel();
                        }
                    });
                    builder.create();
                    builder.show();
                } else {
                    if (checkboxbuscarapida.isChecked() == true) {
                        checkboxbuscarapida.setChecked(true);
                        AlertaCheckBoxBuscaRapida();
                    }
                }
            }
        }
    }

    public void AlertaCheckBoxBuscaRapida(){
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Impulsionar Busca Rápida");
            builder.setMessage("Deseja realmente impulsionar o seu anuncio? taxa de: 10 DogCoin's");
            builder.setPositiveButton("Impulsionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    value_buscarapida = 1;
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        checkboxbuscarapida.setChecked(false);
                        dialog.cancel();
                    }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            AlertaCheckBoxBuscaRapida();
        }

    }


}
