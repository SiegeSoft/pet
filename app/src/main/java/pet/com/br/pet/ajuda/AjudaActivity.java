package pet.com.br.pet.ajuda;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pet.com.br.pet.R;
import pet.com.br.pet.menus.BaseMenu;

/**
 * Created by iacocesar on 13/02/2017.
 */

public class AjudaActivity extends BaseMenu {

    TextView text_dogcoin, text_buscarapida, text_buscarapida_distancia, text_conta_alterasenha,
            text_conta_alteranomeexibicao, text_conta_imagem;
    ImageView image_dogcoin, image_buscarapida, image_buscarapida_distancia, image_conta_alterasenha,
            image_conta_alteranomeexibicao, image_conta_imagem;
    int value_dogcoin, value_buscarapida, value_buscarapida_distancia, value_conta_alterarsenha,
            value_conta_alterarnomeexibicao, value_conta_imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        value_dogcoin = 0;value_buscarapida = 0;value_buscarapida_distancia = 0;value_conta_alterarsenha = 0;value_conta_alterarnomeexibicao = 0;value_conta_imagem = 0;

        text_dogcoin = (TextView) findViewById(R.id.textview_topic_dogcoin);
        text_buscarapida = (TextView) findViewById(R.id.textview_topic_buscarapida);
        text_buscarapida_distancia = (TextView) findViewById(R.id.textview_topic_buscarapida_distancia);
        text_conta_alterasenha = (TextView) findViewById(R.id.textview_topic_conta_alterarsenha);
        text_conta_alteranomeexibicao = (TextView) findViewById(R.id.textview_topic_conta_alterarnomeexibicao);
        text_conta_imagem = (TextView) findViewById(R.id.textview_topic_conta_imagem);

        image_dogcoin = (ImageView) findViewById(R.id.image_open_topic_dogcoin);
        image_buscarapida = (ImageView) findViewById(R.id.image_open_topic_buscarapida);
        image_buscarapida_distancia = (ImageView) findViewById(R.id.image_open_topic_buscarapida_distancia);
        image_conta_alterasenha = (ImageView) findViewById(R.id.image_open_topic_conta_alterarsenha);
        image_conta_alteranomeexibicao = (ImageView) findViewById(R.id.image_open_topic_conta_alterarnomeexibicao);
        image_conta_imagem = (ImageView) findViewById(R.id.image_open_topic_conta_imagem);


        RelativeLayout relative_dogcoin = (RelativeLayout) findViewById(R.id.relative_topic_dogcoin);
        relative_dogcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_dogcoin == 0) {
                    image_dogcoin.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_dogcoin.setVisibility(View.VISIBLE);
                    ChangeStates("dogcoin", 1);
                }else{
                    image_dogcoin.setImageResource(android.R.drawable.ic_menu_add);
                    text_dogcoin.setVisibility(View.GONE);
                    ChangeStates("dogcoin", 0);
                }
            }
        });

        RelativeLayout relative_buscarapida = (RelativeLayout) findViewById(R.id.relative_topic_buscarapida);
        relative_buscarapida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_buscarapida == 0) {
                    image_buscarapida.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_buscarapida.setVisibility(View.VISIBLE);
                    ChangeStates("buscarapida", 1);
                }else{
                    image_buscarapida.setImageResource(android.R.drawable.ic_menu_add);
                    text_buscarapida.setVisibility(View.GONE);
                    ChangeStates("buscarapida", 0);
                }
            }
        });

        RelativeLayout relative_buscarapida_distancia = (RelativeLayout) findViewById(R.id.relative_topic_buscarapida_distancia);
        relative_buscarapida_distancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_buscarapida_distancia == 0) {
                    image_buscarapida_distancia.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_buscarapida_distancia.setVisibility(View.VISIBLE);
                    ChangeStates("buscarapida_distancia", 1);
                }else{
                    image_buscarapida_distancia.setImageResource(android.R.drawable.ic_menu_add);
                    text_buscarapida_distancia.setVisibility(View.GONE);
                    ChangeStates("buscarapida_distancia", 0);
                }
            }
        });


        RelativeLayout relative_conta_alterasenha = (RelativeLayout) findViewById(R.id.relative_topic_conta_alterarsenha);
        relative_conta_alterasenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_conta_alterarsenha == 0) {
                    image_conta_alterasenha.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_conta_alterasenha.setVisibility(View.VISIBLE);
                    ChangeStates("conta_alterasenha", 1);
                }else{
                    image_conta_alterasenha.setImageResource(android.R.drawable.ic_menu_add);
                    text_conta_alterasenha.setVisibility(View.GONE);
                    ChangeStates("conta_alterasenha", 0);
                }

            }
        });

        RelativeLayout relative_conta_alteranomeexibicao = (RelativeLayout) findViewById(R.id.relative_topic_conta_alterarnomeexibicao);
        relative_conta_alteranomeexibicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_conta_alterarnomeexibicao == 0) {
                    image_conta_alteranomeexibicao.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_conta_alteranomeexibicao.setVisibility(View.VISIBLE);
                    ChangeStates("conta_alteranomeexibicao", 1);
                }else{
                    image_conta_alteranomeexibicao.setImageResource(android.R.drawable.ic_menu_add);
                    text_conta_alteranomeexibicao.setVisibility(View.GONE);
                    ChangeStates("conta_alteranomeexibicao", 0);
                }
            }
        });

        RelativeLayout relative_conta_imagem = (RelativeLayout) findViewById(R.id.relative_topic_conta_imagem);
        relative_conta_imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_conta_imagem == 0) {
                    image_conta_imagem.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    text_conta_imagem.setVisibility(View.VISIBLE);
                    ChangeStates("conta_imagem", 1);
                }else{
                    image_conta_imagem.setImageResource(android.R.drawable.ic_menu_add);
                    text_conta_imagem.setVisibility(View.GONE);
                    ChangeStates("conta_imagem", 0);
                }

            }
        });

    }

    public void ChangeStates(String name, int value){
        if(name.equals("dogcoin")){
            value_dogcoin = value;

        }else if(name.equals("buscarapida")){
            value_buscarapida = value;

        }else if(name.equals("buscarapida_distancia")){
            value_buscarapida_distancia = value;

        }else if(name.equals("conta_alterasenha")){
            value_conta_alterarsenha = value;

        }else if(name.equals("conta_alteranomeexibicao")){
            value_conta_alterarnomeexibicao = value;

        }else if(name.equals("conta_imagem")){
            value_conta_imagem = value;

        }

    }


}
