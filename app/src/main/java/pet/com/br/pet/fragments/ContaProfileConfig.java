package pet.com.br.pet.fragments;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pet.com.br.pet.R;
import pet.com.br.pet.conta.Conta;

/**
 * Created by iacocesar on 23/10/2016.
 */

public class ContaProfileConfig extends Fragment {
    private View rootView;
    private RelativeLayout relativeLayout;

    //LAYOUT ALTERAR SENHA.
    TextView texto_titulo;
    EditText texto_senhaantiga;
    EditText texto_novasenha;

    TextView invisible;
    Button button_confirmar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relative_layoutundefinned);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            /** Inflating the layout for this fragment **/
            rootView = inflater.inflate(R.layout.fragment_contaprofileconfig, container, false);
            return rootView;
        }

    public void addPlaces() {
        //relativeLayout.setBackgroundColor(Color.parseColor("#000000"));

            //config botao_confirmar
            button_confirmar = new Button(getActivity());
            button_confirmar.setText("Confirmar");
            button_confirmar.setId(R.id.text_botaoconfirmar);

            //confir textotitulo
            texto_titulo = new TextView(getActivity());
            texto_titulo.setText("Alterar Senha");
            texto_titulo.setId(R.id.text_alterarsenha);

            //confir textosenhaantiga
            texto_senhaantiga = new EditText(getActivity());
            texto_senhaantiga.setId(R.id.text_senhaantiga);

            //confir textonovasenha
            texto_novasenha = new EditText(getActivity());
            texto_novasenha.setId(R.id.text_novasenha);

            //config texto ajustelayout nao mecher!!!
            invisible = new TextView(getActivity());
            invisible.setText("");

        if(Conta.fadeout_value == 1) {
            //texto titulo
            RelativeLayout.LayoutParams layout_text_titulo = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_text_titulo.setMargins(15, 10, 0, 0);
            texto_titulo.setTextSize(25);
            texto_titulo.setLayoutParams(layout_text_titulo);

            //texto senha_antiga
            RelativeLayout.LayoutParams layout_text_senhaantiga = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_text_senhaantiga.setMargins(0, 10, 0, 0);
            layout_text_senhaantiga.addRule(RelativeLayout.BELOW, R.id.text_alterarsenha);
            layout_text_senhaantiga.addRule(RelativeLayout.CENTER_HORIZONTAL);

            texto_senhaantiga.setHint("Senha Antiga");
            texto_senhaantiga.setMaxLines(1);
            texto_senhaantiga.setTextSize(18);
            texto_senhaantiga.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_senhaantiga.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_senhaantiga.setLayoutParams(layout_text_senhaantiga);

            //texto senha_novasenha
            RelativeLayout.LayoutParams layout_text_novasenha = new RelativeLayout.LayoutParams(
                    240,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_text_novasenha.setMargins(0, 10, 0, 0);
            layout_text_novasenha.addRule(RelativeLayout.BELOW, R.id.text_senhaantiga);
            layout_text_novasenha.addRule(RelativeLayout.CENTER_HORIZONTAL);
            texto_novasenha.setHint("Nova Senha");
            texto_novasenha.setTextSize(18);
            texto_novasenha.setMaxLines(1);
            texto_novasenha.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            texto_novasenha.setBackgroundColor(Color.parseColor("#EEEEEE"));
            texto_novasenha.setLayoutParams(layout_text_novasenha);


            //texto botaoconfirmar
            RelativeLayout.LayoutParams layout_button = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_button.addRule(RelativeLayout.BELOW, R.id.text_novasenha);
            layout_button.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layout_button.setMargins(0, 10, 0, 0);
            button_confirmar.setLayoutParams(layout_button);


            //texto ajustelayout nao modificar!!
            RelativeLayout.LayoutParams layout_invisible = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_invisible.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layout_invisible.addRule(RelativeLayout.BELOW, R.id.text_botaoconfirmar);

            invisible.setLayoutParams(layout_invisible);

            // Setting the parameters on the TextView
            //relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            relativeLayout.setBackgroundResource(R.drawable.alterarsenha_background);
            relativeLayout.addView(button_confirmar);
            relativeLayout.addView(texto_titulo);
            relativeLayout.addView(invisible);
            relativeLayout.addView(texto_novasenha);
            relativeLayout.addView(texto_senhaantiga);
        }else{
            if(Conta.fadeout_value == 0) {
                relativeLayout.removeAllViews();
            }
        }
    }

}
