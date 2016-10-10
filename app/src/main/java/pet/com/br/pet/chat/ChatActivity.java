package pet.com.br.pet.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
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
import pet.com.br.pet.adapters.ChatAdapter;
import pet.com.br.pet.adapters.ChatViewAdapter;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Chat;
import pet.com.br.pet.models.ChatView;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.utils.AnunciosUtils;
import pet.com.br.pet.utils.ChatUtils;

/**
 * Created by iaco_ on 26/08/2016.
 */

public class ChatActivity extends BaseMenu {
    private List<Chat> chat;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chat = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        getData();
        recyclerView.addOnScrollListener(rVOnScrollListener);
        setaAdaptador();

    }

    private RecyclerView.OnScrollListener rVOnScrollListener = new RecyclerView.OnScrollListener() {

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
            if(isFirstItemDisplaying(recyclerView)){

            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private JsonArrayRequest getDataFromServer(String url) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarchat);
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
                            Toast.makeText(ChatActivity.this, "Não existem outras mensagens"+ e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, "Não existem outras mensagens", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }

    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(ChatUtils.DATA_URL+ "myusername="+ Profile.getUsername()));
        //Incrementing the request counter
    }


    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Chat chat1 = new Chat();
            JSONObject json = null;

            try {

                json = array.getJSONObject(i);
                chat1.setId(json.getString(ChatUtils.TAG_ID));
                chat1.setCodigo(json.getString(ChatUtils.TAG_CODIGO));
                chat1.setUsername(json.getString(ChatUtils.TAG_USUARIO));
                chat1.setUsernameDestino(json.getString(ChatUtils.TAG_USUARIODESTINO));
                chat1.setDescricao(json.getString(ChatUtils.TAG_DESCRICAO));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //this.chat.add(chat);

            boolean flag = false;
            for(Chat chaT : chat){
                if(null != chaT.getUsername() && null != chat1.getUsername()){
                    if(chaT.getUsername().equals(chat1.getUsername())){
                        // Item exists
                        flag = true;
                        break;
                    }
                }
            }
            // if flag is true item exists, don't add.
            if(!flag){
                chat.add(chat1);
            }
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
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

    public void setaAdaptador(){
        //initializing our adapter
        adapter = new ChatAdapter(chat, this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
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