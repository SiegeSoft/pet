package pet.com.br.pet.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
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
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.ChatAdapter;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Chat;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.ChatUtils;
import pet.com.br.pet.utils.ContaUtils;

/**
 * Created by iaco_ on 26/08/2016.
 */

public class ChatActivity extends BaseMenu {
    private List<Chat> chat;
    private RecyclerView recyclerView, recyclerView_novas;
    private RecyclerView.LayoutManager layoutManager, layoutManager_novas;
    private RecyclerView.Adapter adapter_novos, adapter_antigos;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("NEGOCIAÇÕES");
        ab.setSubtitle("Inicie as suas negociações ;)");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewchat);
        recyclerView.setHasFixedSize(true);
        recyclerView_novas = (RecyclerView) findViewById(R.id.recyclerViewchat_novas);
        recyclerView_novas.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager_novas = new LinearLayoutManager(this);
        recyclerView_novas.setLayoutManager(layoutManager_novas);

        chat = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        getData();
        recyclerView.addOnScrollListener(rVOnScrollListener);

    }

    private RecyclerView.OnScrollListener rVOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    getData();
                    break;
            }
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, -dy);
            if (isLastItemDisplaying(recyclerView)) {
                getData();
            }
            if (isFirstItemDisplaying(recyclerView)) {

            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private JsonArrayRequest getDataFromServer(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Toast.makeText(ChatActivity.this, "Baixando lista de conversas", Toast.LENGTH_SHORT).show();
                            parseData(response);
                        } catch (Exception e) {
                            Toast.makeText(ChatActivity.this, "Erro: Sem comunicacao com o servidor" + e, Toast.LENGTH_SHORT).show();
                            getData();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, "Não existem outras mensagens", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                });
        return jsonArrayRequest;
    }

    private void getData() {
        Log.e("ALERTA", "BAIXANDO DADOS DO SERVIDOR");
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(ChatUtils.DATA_URL + "myusername=" + Profile.getUsername()));
        //Incrementing the request counter
    }


    private void parseData(JSONArray array) throws JSONException {
            for (int i = 0; i < array.length(); i++) {
            final Chat chat1 = new Chat();
            JSONObject json = null;

                json = array.getJSONObject(i);
                //chat1.setId(json.getString(ChatUtils.TAG_ID));
                chat1.setCodigo(json.getString(ChatUtils.TAG_CODIGO));
                chat1.setUsername(json.getString(ChatUtils.TAG_USUARIO));
                chat1.setUsernameDestino(json.getString(ChatUtils.TAG_USUARIODESTINO));
                chat1.setDescricao(json.getString(ChatUtils.TAG_DESCRICAO));
                chat1.setProfileImg(json.getString(ChatUtils.TAG_PROFILEIMG));

                Log.e("TESTANDO", "ENTROU NA FUNCAO");

                StringRequest stringRequest = new StringRequest(Request.Method.POST, ContaUtils.VERIFICACONTAARRAY_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (response.trim().equals("mensagem nova")) {
                                        chat1.setDestinoVisualisado("0");
                                        chat.add(chat1);
                                    } else if (response.trim().equals("nao existem mensagens")) {
                                        chat1.setDestinoVisualisado("1");
                                        chat.add(chat1);
                                    }
                                } catch (Exception e) {
                                    //retorno = false;
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        String usuariodestino = Profile.getUsername();
                        map.put("USUARIODESTINO", usuariodestino);
                        map.put("USUARIO", chat1.getUsername());
                        map.put("CODIGO", chat1.getCodigo());
                        return map;
                    }
                };
                RequestQueue requestQueueNew = Volley.newRequestQueue(this.getApplicationContext());
                requestQueueNew.add(stringRequest);
            }


//Notifying the adapter that data has been added or changed
        Log.e("ADAPITADOR", "ENTROU NA FUNCAO");

        setaAdaptador();
    }

    private Bitmap decodeBase64(String input) {
        if (input.equals("")) {
            return null;
        } else {
            byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        }
    }

    public void setaAdaptador() {
        ArrayList<Chat> novostemp = new ArrayList();
        ArrayList<Chat> antigostemp = new ArrayList();

        for (int i = 0; i < chat.size(); i++) {
            if (chat.get(i).getDestinoVisualisado().equals("0")) {
                novostemp.add(chat.get(i));
                Log.e("NOVO REPEAT", "" + chat.get(i).getCodigo() + "" + chat.get(i).getUsername());
            } else if (chat.get(i).getDestinoVisualisado().equals("1")) {
                antigostemp.add(chat.get(i));
                Log.e("ANTIGO REPEAT", "" + chat.get(i).getCodigo() + "" + chat.get(i).getUsername());
            }
        }
        //Adding adapter to recyclerview NOVOS
        adapter_novos = new ChatAdapter(novostemp, this);
        recyclerView_novas.setAdapter(adapter_novos);
        adapter_novos.notifyDataSetChanged();

        //Adding adapter to recyclerview ANTIGOS
        adapter_antigos = new ChatAdapter(antigostemp, this);
        recyclerView.setAdapter(adapter_antigos);
        adapter_antigos.notifyDataSetChanged();
        //getData();
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {

        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private boolean isFirstItemDisplaying(RecyclerView recyclerView) {

        if (recyclerView.getAdapter().getItemCount() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

            if (firstVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItemPosition == 0)
                return true;
        }
        return false;
    }

}