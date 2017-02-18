package pet.com.br.pet.conta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.ContaUtils;

/**
 * Created by iacocesar on 31/10/2016.
 */

public class ContaStatus extends BaseMenu {
    //imgview
    ImageView image_conta_perfil;
    //PROFILEIMGVALUE
    int value_profile_img = 1;
    //
    TextView texto_nomeexibicao;
    EditText edit_nomeexibicao;

    int button_alteranomeexibicaovalue = 0;

    //butonsvisibility
    Button onclicknomeexibicao1, onclicknomeexibicao2;

    //loginsession cache
    private LoginManager _session;
    private HashMap<String, String> _userDetails;

    private ProgressDialog loading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_status);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.menu_conta_perfil, null);

        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);

        //SETA O TITULO
        TextView texttitle = (TextView) findViewById(R.id.textview_tittle);
        texttitle.setText("PERFIL");

        _session = new LoginManager(getApplicationContext());
        _userDetails = _session.getUserDetails();

        //TEXTVIEWS NOMEEXIBICAO
        texto_nomeexibicao = (TextView) findViewById(R.id.text_conta_usuario_exibicao);
        edit_nomeexibicao = (EditText) findViewById(R.id.edit_conta_usuario_exibicao);

        texto_nomeexibicao.setText("" + _userDetails.get(LoginManager.KEY_NOME_EXIBICAO));
        edit_nomeexibicao.setText("" + _userDetails.get(LoginManager.KEY_NOME_EXIBICAO));

        onclicknomeexibicao1 = (Button) findViewById(R.id.OnClickNomeExibicao1);
        onclicknomeexibicao2 = (Button) findViewById(R.id.OnClickNomeExibicao2);

        Bitmap bitmap = ((BitmapDrawable) Profile.getIcon()).getBitmap();

        Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(bitmap));

        image_conta_perfil = (ImageView) findViewById(R.id.image_conta_perfil);

        image_conta_perfil.setImageDrawable(drawable);

        image_conta_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                LayoutInflater myLayout = LayoutInflater.from(ContaStatus.this);
                View dialogView = myLayout.inflate(R.layout.fragment_alertdialog_imageselector, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ContaStatus.this);
                builder.setTitle("Alterar imagem de perfil");
                builder.setMessage("Selecione uma das imagens abaixo e pressione ''Escolher''.");
                builder.setCancelable(false);
                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();

                final ImageView icone_1 = (ImageView) dialogView.findViewById(R.id.icon1);
                icone_1.setBackgroundResource(R.drawable.conta_border_pets_activated);
                final ImageView icone_2 = (ImageView) dialogView.findViewById(R.id.icon2);
                final ImageView icone_3 = (ImageView) dialogView.findViewById(R.id.icon3);
                final ImageView icone_4 = (ImageView) dialogView.findViewById(R.id.icon4);
                final ImageView icone_5 = (ImageView) dialogView.findViewById(R.id.icon5);
                final ImageView icone_6 = (ImageView) dialogView.findViewById(R.id.icon6);
                final ImageView icone_7 = (ImageView) dialogView.findViewById(R.id.icon7);
                final ImageView icone_8 = (ImageView) dialogView.findViewById(R.id.icon8);
                final ImageView icone_9 = (ImageView) dialogView.findViewById(R.id.icon9);
                final TextView texto_pet_names = (TextView) dialogView.findViewById(R.id.text_pet_names);
                texto_pet_names.setText("Escolheu: Freddy");

                icone_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Freddy");
                        value_profile_img = 1;

                    }
                });

                icone_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Martha");
                        value_profile_img = 2;
                    }
                });

                icone_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Jhonny");
                        value_profile_img = 3;
                    }
                });

                icone_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Laila");
                        value_profile_img = 4;
                    }
                });

                icone_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: George");
                        value_profile_img = 5;
                    }
                });

                icone_6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Simon");
                        value_profile_img = 6;
                    }
                });

                icone_7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Mason");
                        value_profile_img = 7;
                    }
                });

                icone_8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets);
                        texto_pet_names.setText("Escolheu: Ramon");
                        value_profile_img = 8;
                    }
                });

                icone_9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        icone_1.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_2.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_3.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_4.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_5.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_6.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_7.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_8.setBackgroundResource(R.drawable.conta_border_pets);
                        icone_9.setBackgroundResource(R.drawable.conta_border_pets_activated);
                        texto_pet_names.setText("Escolheu: Stuart");
                        value_profile_img = 9;
                    }
                });

                builder.setPositiveButton("Escolher", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        enviaProfileImg(""+value_profile_img);
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        value_profile_img = 1;
                        dialog.cancel();
                    }
                });
                builder.show();            }
        });
    }

    public Bitmap createCircleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.parseColor("#AB47BC");
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth((float) 1);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2f,
                bitmapimg.getHeight() / 2.1f, bitmapimg.getWidth() / 2.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //onBackPressed();
            Intent refresh = new Intent(ContaStatus.this, Conta.class);
            startActivity(refresh);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void BotaoIniciaNomeExibicao(View v){
        if (button_alteranomeexibicaovalue == 0) {
            texto_nomeexibicao.setVisibility(View.GONE);
            edit_nomeexibicao.setVisibility(View.VISIBLE);
            onclicknomeexibicao2.setVisibility(View.VISIBLE);
            onclicknomeexibicao1.setVisibility(View.GONE);
            button_alteranomeexibicaovalue = 1;
        }
    }
    public void BotaoCompletaNomeExibicao(View v){
        if (button_alteranomeexibicaovalue == 1) {
            alteraNomeExibicao();
        }
    }

    //ALTERA SENHA
    public void alteraNomeExibicao() {
        if (edit_nomeexibicao.length() >= 6) {
            loading = ProgressDialog.show(this, "Aguarde...", "Alterando Nome de Usuario...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ContaUtils.ALTERANOMEEXIBICAO_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.trim().equals("success")) {
                                    _session.userNomeExibicaoSession(String.valueOf(edit_nomeexibicao.getText().toString()));
                                    Profile.setNomeExibicao(_userDetails.get(LoginManager.KEY_NOME_EXIBICAO));
                                    edit_nomeexibicao.setText(_userDetails.get(LoginManager.KEY_NOME_EXIBICAO));
                                    texto_nomeexibicao.setVisibility(View.VISIBLE);
                                    texto_nomeexibicao.setText(edit_nomeexibicao.getText().toString());
                                    edit_nomeexibicao.setVisibility(View.GONE);
                                    onclicknomeexibicao1.setVisibility(View.VISIBLE);
                                    onclicknomeexibicao2.setVisibility(View.GONE);
                                    button_alteranomeexibicaovalue = 0;
                                    Toast.makeText(ContaStatus.this, "Nome Alterado Com Sucesso", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    RestartActivity();
                                } else if (response.trim().equals("Error")) {
                                    Toast.makeText(ContaStatus.this, "Não foi possivel alterar o seu nome", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                } else if (response.trim().equals("Usuario Inexistente")) {
                                    Toast.makeText(ContaStatus.this, "Erro durante a alteração do seu nome", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            } catch (Exception e) {
                                Toast.makeText(ContaStatus.this, "Erro interno ao alterar o seu nome", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ContaStatus.this, "Error ao conectar ao servidor", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    String username = Profile.getUsername();
                    String newusername = edit_nomeexibicao.getText().toString();
                    map.put("USERNAME", username);
                    map.put("NOMEEXIBICAO", newusername);
                    return map;
                }
            };
            RequestQueue requestQueueNew = Volley.newRequestQueue(ContaStatus.this);
            requestQueueNew.add(stringRequest);
        } else {
            Toast.makeText(ContaStatus.this, "O seu nome de exibição deve conter 6 ou mais digitos.", Toast.LENGTH_SHORT).show();
        }
    }


    public void enviaProfileImg(final String profileimg){
        loading = ProgressDialog.show(this, "Aguarde...", "Alterando Imagem...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ContaUtils.ALTERAPROFILEIMG_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.trim().equals("success")) {
                                    _session.userProfileSession(String.valueOf(profileimg));
                                    if(value_profile_img == 1){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.cachorro_icons));
                                    }else if(value_profile_img == 2){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.galinha_icons));
                                    }else if(value_profile_img == 3){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.sapo_icons));
                                    }else if(value_profile_img == 4){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.hamster_icons));
                                    }else if(value_profile_img == 5){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.macaco_icons));
                                    }else if(value_profile_img == 6){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.gato_icons));
                                    }else if(value_profile_img == 7){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.tigre_icons));
                                    }else if(value_profile_img == 8){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.coelho_icons));
                                    }else if(value_profile_img == 9){
                                        Profile.setIcon(ContaStatus.this.getResources().getDrawable(R.drawable.rato_icons));
                                    }
                                    Profile.setProfileImage(profileimg);
                                    Toast.makeText(ContaStatus.this, "Imagem de perfil Alterada com sucesso", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                    RestartActivity();
                                } else if (response.trim().equals("Error")) {
                                    Toast.makeText(ContaStatus.this, "Não foi possivel alterar a sua imagem de perfil", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                } else if (response.trim().equals("Usuario Inexistente")) {
                                    Toast.makeText(ContaStatus.this, "Erro durante a alteração da sua imagem", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            } catch (Exception e) {
                                Toast.makeText(ContaStatus.this, "Erro interno ao alterar a sua imagem", Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ContaStatus.this, "Error ao conectar ao servidor", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    String username = Profile.getUsername();
                    map.put("USERNAME", username);
                    map.put("PROFILEIMG", profileimg);
                    return map;
                }
            };
            RequestQueue requestQueueNew = Volley.newRequestQueue(ContaStatus.this);
            requestQueueNew.add(stringRequest);

    }


    public void RestartActivity(){
        Intent refresh = new Intent(ContaStatus.this, ContaStatus.class);
        startActivity(refresh);
        finish();
    }




}
