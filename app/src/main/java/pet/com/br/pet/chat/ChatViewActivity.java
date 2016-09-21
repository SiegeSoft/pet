package pet.com.br.pet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.ChatViewAdapter;
import pet.com.br.pet.database.ChatController;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.ChatView;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.ChatViewUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


/**
 * Created by iaco_ on 28/08/2016.
 */
public class ChatViewActivity extends BaseMenu {


    //LISTVIEW
    private List<ChatView> chatview;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private int _requestCount = 1;
    private ChatController chatController;

    // private Activity context;

    //INTENT VARS
    String user;
    String codigo;
    String descricao;
    String number;

    //SENDDATACHAT
    EditText mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatview);

        mensagem = (EditText) findViewById(R.id.edit_escrevemensagem);

        //instancia o chat controller class
        chatController = new ChatController(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewchatView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatview = new ArrayList<>();
        Intent intent = getIntent();
        user = intent.getStringExtra("Userchat");
        codigo = intent.getStringExtra("Usercodigo");
        descricao = intent.getStringExtra("Userdesc");

        requestQueue = Volley.newRequestQueue(this);

        setaAdaptadorUsuarioDestino(user);

        //INICIA O PROGRAMA NA ULTIMA ATUALIZAÇÃO DO RECYCLERVIEW
        int recyclerposition = recyclerView.getAdapter().getItemCount();
        recyclerView.scrollToPosition(recyclerposition-1);

        //ATUALIZA AS INFORMAÇÕES A CADA 3 SEGUNDOS
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                getDataUsuarioDestino();
                //adapter.notifyDataSetChanged();
                handler.postDelayed( this, 3000 );
            }
        },  3000 );

    }


    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    //RECEBER MENSAGENS DOS OUTROS USUARIOS
    private JsonArrayRequest getUsuarioDestinoDataFromServer(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            parseDataUsuarioDestino(response);
                        }
                        catch (Exception e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        return jsonArrayRequest;
    }

    //INICIAR O RECEBIMENTO DAS MENSAGENS DOS UUARIOS
    private void getDataUsuarioDestino() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getUsuarioDestinoDataFromServer(ChatViewUtils.DATA_URL+"&CODIGO="+codigo+"&USUARIODESTINO="+user+"&USUARIO="+Profile.getUsername()));
        //Incrementing the request counter
    }

    //CONVERTE EM STRING AS SUAS MENSAGENS DOS OUTROS USUARIOS
    private void parseDataUsuarioDestino(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ChatView chatvieww = new ChatView();
            JSONObject json = null;
            try {

                json = array.getJSONObject(i);
                chatvieww.setId(json.getString(ChatViewUtils.TAG_ID));
                chatvieww.setCodigo(json.getString(ChatViewUtils.TAG_CODIGO));
                chatvieww.setUsername(json.getString(ChatViewUtils.TAG_USUARIODESTINO));
                chatvieww.setMensagem(json.getString(ChatViewUtils.TAG_MENSAGEM));
                //ChatView.setMeunome(Profile.getUsername().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Linear search, see if the id exists
            boolean flag = false;

            //VERIFICA SE EXISTEM IDS DUPLICADOS
            for(ChatView chat : chatview){
                if(null != chat.getId() && null != chatvieww.getId()){
                    if(chat.getId().equals(chatvieww.getId())){
                        // Item exists
                        flag = true;
                        break;
                    }
                }
            }

            // if flag is true item exists, don't add.
            if(!flag){
                chatController.insereDado(chatvieww.getId(),chatvieww.getCodigo(),chatvieww.getUsername(),chatvieww.getMensagem(), chatvieww.getUsername());
                //chatview.add(chatvieww);
                setaAdaptadorUsuarioDestino(chatvieww.getUsername());
            }
        }
        // Notifying the adapter that data has been added or changed
    }
    //ATUALIZA O ADAPTADOR DAS MENSAGENS DE OUTROS USUARIOS
    public void setaAdaptadorUsuarioDestino(String username){
        //Adding adapter to recyclerview
        try {
            //ChatView chat = new ChatView();
            //chat.setUsername(user);
            //chatview.add(chat);
            adapter = new ChatViewAdapter(chatController.carregaTodosDadosUsuarioDestino(username, codigo), this);
            recyclerView.setAdapter(adapter);

            //This method runs in the same thread as the UI.
            adapter.notifyDataSetChanged();
            int recyclerposition = recyclerView.getAdapter().getItemCount();
            recyclerView.scrollToPosition(recyclerposition-1);

            //recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            //Do something to the UI thread here
        }
        catch (Exception e){
            Toast.makeText(ChatViewActivity.this, "Banco de dados error: "+ e, Toast.LENGTH_SHORT).show();
        }
    }


    //INSERIR MENSAGENS
    public void botaoenviarmensagem(View v){
            String mg = mensagem.getText().toString().trim();

            if(mg!= "" && mg.length() > 0){enviaMensagem(mg);}else{
                Toast.makeText(ChatViewActivity.this, "Campo mensagem vazio ;)", Toast.LENGTH_SHORT).show();
            }
    }

    public void enviaMensagem(final String msg){
        msg.trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChatViewUtils.DATA_MENSAGEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            if(response.trim().equals("success")){
                                Toast.makeText(ChatViewActivity.this, "Mensagem enviada à "+user, Toast.LENGTH_SHORT).show();
                                String uniqueId = UUID.randomUUID().toString();
                                chatController.insereDado(uniqueId, codigo, user, msg, Profile.getUsername());
                                mensagem.setText("");
                                setaAdaptadorUsuarioDestino(user);
                            }else{
                                Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String meunomee;
                meunomee = Profile.getUsername().toString();
                Map<String,String> map = new HashMap<String,String>();
                map.put("CODIGO",codigo);
                map.put("MENSAGEM" ,msg);
                map.put("MEUNOME", meunomee);
                map.put("NOMEOUTRO",user);
                return map;
            }
        };
        RequestQueue requestQueueNew = Volley.newRequestQueue(this);
        requestQueueNew.add(stringRequest);

    }


}
