package pet.com.br.pet.conta;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import pet.com.br.pet.R;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.fragments.ContaProfileConfig;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Profile;

/**
 * Created by iacocesar on 22/10/2016.
 */

public class Conta extends BaseMenu {
    ImageView imagem_conta_perfil;
    ContaProfileConfig contaProfileConfig;
    int value = 0;
    public static int fadeout_value = 0;
    TextView nomeexibicao;
    //loginsession cache
    private LoginManager _session;
    private HashMap<String, String> _userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        _session = new LoginManager(getApplicationContext());
        _userDetails = _session.getUserDetails();
        imagem_conta_perfil = (ImageView) findViewById(R.id.image_conta_perfil);
        nomeexibicao = (TextView) findViewById(R.id.text_conta_usuario_exibicao);

        Conta.fadeout_value = 0;

        Bitmap bitmap = ((BitmapDrawable) Profile.getIcon()).getBitmap();

        Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(bitmap));

        imagem_conta_perfil.setImageDrawable(drawable);
        nomeexibicao.setText("" + _userDetails.get(LoginManager.KEY_NOME_EXIBICAO));

        //FRAGMENT TROCA DE SENHA
        TextView textconta = (TextView) findViewById(R.id.text_conta_alterarSenha);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        contaProfileConfig = new ContaProfileConfig();
        fragmentTransaction.add(R.id.fragment_senha, contaProfileConfig);
        fragmentTransaction.commit();
        textconta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (fadeout_value == 0) {
                    RelativeLayout relative_fadeout = (RelativeLayout) findViewById(R.id.relative_conta_fadeout);
                    relative_fadeout.setVisibility(View.VISIBLE);
                    // Creating a new RelativeLayout
                    fadeout_value = 1;
                    contaProfileConfig.addPlaces();
                }
            }
        });

        RelativeLayout relative_conta_fragment_view = (RelativeLayout) findViewById(R.id.relative_conta_fragment_view);

        if (!relative_conta_fragment_view.hasOnClickListeners()) {
                        RelativeLayout relative_fadeout = (RelativeLayout) findViewById(R.id.relative_conta_fadeout);
                        relative_fadeout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fadeout_value == 1) {
                                    RelativeLayout relative_fade = (RelativeLayout) findViewById(R.id.relative_conta_fadeout);
                                    relative_fade.setVisibility(View.GONE);
                                    Conta.fadeout_value = 0;
                                    contaProfileConfig.addPlaces();
                    }
                }
            });
        }
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
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    //ALTERAR IMAGEM DE PERFIL.
    public void OnClickPerfil(View v){
        if (fadeout_value == 0) {
            Intent intent = new Intent(this ,ContaStatus.class);
            startActivity(intent);
            finish();
        }
    }

}
