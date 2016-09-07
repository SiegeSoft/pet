package pet.com.br.pet.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.ChatViewAdapter;
import pet.com.br.pet.database.ChatController;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.ChatView;
import pet.com.br.pet.utils.ChatViewUtils;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by iaco_ on 28/08/2016.
 */
public class ChatViewActivity extends BaseMenu {
    private List<ChatView> chatview;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private int _requestCount = 1;
    private ChatController chatController;
    private List<ChatView> chatViewList;

    //TIMMER
    private Timer myTimer;


    // private Activity context;
    //EditText msgenv;

    String user;
    String cell;
    String codigo;
    String descricao;
    EditText msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatview);


        //instancia o chat controller class
        chatController = new ChatController(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewchatView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatview = new ArrayList<>();

        Intent intent = getIntent();
        cell = intent.getStringExtra("Usercelular");
        user = intent.getStringExtra("Userchat");
        codigo = intent.getStringExtra("Usercodigo");
        descricao = intent.getStringExtra("Userdesc");

        msg = (EditText) findViewById(R.id.edit_escrevemensagem);

        msg.setText(""+cell);
        //msg.setText(""+user + " "+ codigo + " " +descricao );
        //requestQueue = Volley.newRequestQueue(this);
        requestQueue = Volley.newRequestQueue(this);

        //getData();

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                getData();
                setaAdaptador();
                adapter.notifyDataSetChanged();
                handler.postDelayed( this, 2000 );
            }
        },  5000 );

       // recyclerView.addOnScrollListener(rVOnScrollListener);

        //setaAdaptador();



    }


    /*private RecyclerView.OnScrollListener rVOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
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
        }
    };*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private JsonArrayRequest getDataFromServer(String url) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarchatView);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            parseData(response);
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (Exception e){
                            Toast.makeText(ChatViewActivity.this, "Não existem outras mensagens "+ e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatViewActivity.this, "Não existem outras mensagens", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }





    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        //cell = cell.replace("+", "");
        requestQueue.add(getDataFromServer(ChatViewUtils.DATA_URL+"&CODIGO="+codigo+"&CELULARCLIENTE="+cell));
        //Incrementing the request counter
        _requestCount++;
    }


    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ChatView chatvieww = new ChatView();
            JSONObject json = null;
            try {

                json = array.getJSONObject(i);
                chatvieww.setId(json.getString(ChatViewUtils.TAG_ID));
                chatvieww.setCelular(json.getString(ChatViewUtils.TAG_CELULAR));
                chatvieww.setCodigo(json.getString(ChatViewUtils.TAG_CODIGO));
                chatvieww.setUsername(json.getString(ChatViewUtils.TAG_USERCHAT));
                chatvieww.setMensagem(json.getString(ChatViewUtils.TAG_MENSAGEM));
                cell = chatvieww.getCelular();
               // ChatController chatController = new ChatController(getBaseContext());


            } catch (JSONException e) {
                e.printStackTrace();
            }



            // Linear search, see if the id exists
            boolean flag = false;
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
                chatController.insereDado(chatvieww.getId(), chatvieww.getCelular() ,chatvieww.getCodigo(),chatvieww.getUsername(),chatvieww.getMensagem());
                chatview.add(chatvieww);
                adapter = new ChatViewAdapter(chatController.carregaTodosDados(chatvieww.getCelular()), this);
                //Adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }
        }
        // Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }



    public void setaAdaptador(){
        //setaAdaptador();
        adapter = new ChatViewAdapter(chatController.carregaTodosDados(cell), this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //This method runs in the same thread as the UI.

        //Do something to the UI thread here

    }

    /*private boolean isLastItemDisplaying(RecyclerView recyclerView) {

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
    }*/

}
