package pet.com.br.pet.conta;

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

import pet.com.br.pet.R;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Profile;

/**
 * Created by iacocesar on 31/10/2016.
 */

public class ContaStatus extends BaseMenu {

    TextView texto_nomeexibicao;
    EditText edit_nomeexibicao;

    int button_alteranomeexibicaovalue = 0;

    //butonsvisibility
    Button onclicknomeexibicao1, onclicknomeexibicao2;


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
        texttitle.setText("Perfil");
        Drawable icon = this.getResources().getDrawable(R.drawable.andy);

        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();

        Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(bitmap));

        ImageView imgview = (ImageView) findViewById(R.id.image_conta_perfil);

        imgview.setImageDrawable(drawable);


        //TEXTVIEWS NOMEEXIBICAO
        texto_nomeexibicao = (TextView) findViewById(R.id.text_conta_usuario_exibicao);
        edit_nomeexibicao = (EditText) findViewById(R.id.edit_conta_usuario_exibicao);

        texto_nomeexibicao.setText("" + Profile.getNomeExibicao());
        edit_nomeexibicao.setText("" + Profile.getNomeExibicao());

        onclicknomeexibicao1 = (Button) findViewById(R.id.OnClickNomeExibicao1);
        onclicknomeexibicao2 = (Button) findViewById(R.id.OnClickNomeExibicao2);

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

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClickNomeExibicao1(View v) {
        if (button_alteranomeexibicaovalue == 0) {
            texto_nomeexibicao.setVisibility(View.GONE);
            edit_nomeexibicao.setVisibility(View.VISIBLE);
            onclicknomeexibicao2.setVisibility(View.VISIBLE);
            onclicknomeexibicao1.setVisibility(View.GONE);
            button_alteranomeexibicaovalue = 1;
        }
    }

    public void OnClickNomeExibicao2(View v) {
        if (button_alteranomeexibicaovalue == 1) {
            texto_nomeexibicao.setVisibility(View.VISIBLE);
            edit_nomeexibicao.setVisibility(View.GONE);
            onclicknomeexibicao1.setVisibility(View.VISIBLE);
            onclicknomeexibicao2.setVisibility(View.GONE);
            button_alteranomeexibicaovalue = 0;
        }
    }


}
