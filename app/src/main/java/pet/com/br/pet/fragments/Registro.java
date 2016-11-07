package pet.com.br.pet.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import pet.com.br.pet.autentica.Login;
import pet.com.br.pet.conta.Conta;
import pet.com.br.pet.utils.ContaUtils;

/**
 * Created by iacocesar on 03/11/2016.
 */

public class Registro extends Fragment {

    private View rootView;
    private RelativeLayout relativeLayout;

    private RequestQueue requestQueue;

    //LAYOUT Registra SENHA.
    TextView texto_titulo;
    EditText texto_cadastra_username;
    EditText texto_cadastra_nomeexibicao;
    EditText texto_cadastra_senha;
    EditText texto_cadastra_email;
    EditText texto_cadastra_celular;


    TextView invisible;
    Button button_confirmar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_registro);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        rootView = inflater.inflate(R.layout.fragment_registro, container, false);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        return rootView;
    }

    public void addPlaces() {
        //relativeLayout.setBackgroundColor(Color.parseColor("#000000"));

        //FRAGMENT ESTRUTURAÇAO LAYOUT
        //config botao_confirmar
        button_confirmar = new Button(getActivity());
        button_confirmar.setText("Registrar");
        button_confirmar.setId(R.id.text_botaoregistrar);

        //config textotitulo
        texto_titulo = new TextView(getActivity());
        texto_titulo.setText("Registrar-se");
        texto_titulo.setId(R.id.text_registro);

        //config texto_user
        texto_cadastra_username = new EditText(getActivity());
        texto_cadastra_username.setId(R.id.text_username);

        //config texto_momeexibicao
        texto_cadastra_nomeexibicao = new EditText(getActivity());
        texto_cadastra_nomeexibicao.setId(R.id.text_nomeexibicao);

        //config cadastrarsenha
        texto_cadastra_senha = new EditText(getActivity());
        texto_cadastra_senha.setId(R.id.text_password);

        //config cadastraremail
        texto_cadastra_email = new EditText(getActivity());
        texto_cadastra_email.setId(R.id.text_email);

        //config cadastrarcelular
        texto_cadastra_celular = new EditText(getActivity());
        texto_cadastra_celular.setId(R.id.text_celular);

        //config texto ajustelayout nao mecher!!!
        invisible = new TextView(getActivity());
        invisible.setText("");


        //fade
        if(Login.fadeout_valueregistro == 1) {
            //texto titulo
            RelativeLayout.LayoutParams layout_text_titulo = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_text_titulo.setMargins(15, 10, 0, 0);
            texto_titulo.setTextSize(25);
            texto_titulo.setLayoutParams(layout_text_titulo);

            //texto nomeexibicao
            RelativeLayout.LayoutParams layout_texto_cadastra_nomeexibicao = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_texto_cadastra_nomeexibicao.setMargins(0, 10, 0, 0);
            layout_texto_cadastra_nomeexibicao.addRule(RelativeLayout.BELOW, R.id.text_registro);
            layout_texto_cadastra_nomeexibicao.addRule(RelativeLayout.CENTER_HORIZONTAL);
            texto_cadastra_nomeexibicao.setHint("Nome Exibicao");
            texto_cadastra_nomeexibicao.setMaxLines(1);
            texto_cadastra_nomeexibicao.setTextSize(18);
            texto_cadastra_nomeexibicao.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            texto_cadastra_nomeexibicao.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_cadastra_nomeexibicao.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_cadastra_nomeexibicao.setLayoutParams(layout_texto_cadastra_nomeexibicao);


            //texto user
            RelativeLayout.LayoutParams layout_texto_cadastra_username = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_texto_cadastra_username.setMargins(0, 10, 0, 0);
            layout_texto_cadastra_username.addRule(RelativeLayout.BELOW, R.id.text_nomeexibicao);
            layout_texto_cadastra_username.addRule(RelativeLayout.CENTER_HORIZONTAL);

            texto_cadastra_username.setHint("Usuario");
            texto_cadastra_username.setMaxLines(1);
            texto_cadastra_username.setTextSize(18);
            texto_cadastra_username.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            texto_cadastra_username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_cadastra_username.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_cadastra_username.setLayoutParams(layout_texto_cadastra_username);

            //texto user
            RelativeLayout.LayoutParams layout_texto_cadastra_email = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_texto_cadastra_email.setMargins(0, 10, 0, 0);
            layout_texto_cadastra_email.addRule(RelativeLayout.BELOW, R.id.text_username);
            layout_texto_cadastra_email.addRule(RelativeLayout.CENTER_HORIZONTAL);

            texto_cadastra_email.setHint("Email");
            texto_cadastra_email.setMaxLines(1);
            texto_cadastra_email.setTextSize(18);
            texto_cadastra_email.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            texto_cadastra_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_cadastra_email.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_cadastra_email.setLayoutParams(layout_texto_cadastra_email);


            //texto CADASTRAR SENHA
            RelativeLayout.LayoutParams layout_texto_cadastra_senha = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_texto_cadastra_senha.setMargins(0, 10, 0, 0);
            layout_texto_cadastra_senha.addRule(RelativeLayout.BELOW, R.id.text_email);
            layout_texto_cadastra_senha.addRule(RelativeLayout.CENTER_HORIZONTAL);
            texto_cadastra_senha.setHint("Senha");
            texto_cadastra_senha.setTextSize(18);
            texto_cadastra_senha.setMaxLines(1);
            texto_cadastra_senha.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            texto_cadastra_senha.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_cadastra_senha.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_cadastra_senha.setLayoutParams(layout_texto_cadastra_senha);

            //texto botaoconfirmar
            RelativeLayout.LayoutParams layout_button_confirmar = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_button_confirmar.addRule(RelativeLayout.BELOW, R.id.text_password);
            layout_button_confirmar.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout_button_confirmar.setMargins(0, 20, 0, 0);
            button_confirmar.setBackgroundResource(R.drawable.botaologin_background);
            button_confirmar.setPadding(15,0,15,0);
            button_confirmar.setLayoutParams(layout_button_confirmar);
            button_confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });


            //texto ajustelayout nao modificar!!
            RelativeLayout.LayoutParams layout_invisible = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_invisible.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layout_invisible.addRule(RelativeLayout.BELOW, R.id.text_botaoregistrar);

            invisible.setLayoutParams(layout_invisible);

            // Setting the parameters on the TextView
            //relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            relativeLayout.setBackgroundResource(R.drawable.alterarsenha_background);
            relativeLayout.addView(button_confirmar);
            relativeLayout.addView(texto_titulo);
            relativeLayout.addView(invisible);
            relativeLayout.addView(texto_cadastra_nomeexibicao);
            relativeLayout.addView(texto_cadastra_username);
            relativeLayout.addView(texto_cadastra_senha);
        }else{
            if(Login.fadeout_valueregistro == 0) {
                relativeLayout.removeAllViews();
            }
        }
    }

}

