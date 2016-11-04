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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pet.com.br.pet.R;
import pet.com.br.pet.chat.ChatViewActivity;
import pet.com.br.pet.conta.Conta;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.ChatViewUtils;
import pet.com.br.pet.utils.ContaUtils;

/**
 * Created by iacocesar on 23/10/2016.
 */

public class ContaProfileConfig extends Fragment {
    private View rootView;
    private RelativeLayout relativeLayout;

    private RequestQueue requestQueue;

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
            requestQueue = Volley.newRequestQueue(this.getActivity());
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
            button_confirmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    alteraSenha();
                }
            });


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


    //ALTERA SENHA
    public void alteraSenha() {
        if (texto_senhaantiga.length() >= 6 && texto_novasenha.length() >= 6) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ContaUtils.ALTERASENHA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.trim().equals("success")){
                                    texto_senhaantiga.setText("");
                                    texto_novasenha.setText("");
                                    Toast.makeText(getActivity(), "Senha Alterada Com Sucesso", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Não foi possivel alterar a sua senha", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Senha Antiga Inválida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error ao conectar ao servidor", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    String username = Profile.getUsername();
                    String senhaantiga = texto_senhaantiga.getText().toString();
                    String novasenha = texto_novasenha.getText().toString();
                    map.put("USERNAME", username);
                    map.put("SENHAANTIGA", senhaantiga);
                    map.put("NOVASENHA", novasenha);
                    return map;
                }
            };
            RequestQueue requestQueueNew = Volley.newRequestQueue(this.getActivity());
            requestQueueNew.add(stringRequest);
        }else{
            Toast.makeText(getActivity(), "A sua nova senha deverá conter 6 ou mais caracteres.", Toast.LENGTH_SHORT).show();
        }

    }

}
