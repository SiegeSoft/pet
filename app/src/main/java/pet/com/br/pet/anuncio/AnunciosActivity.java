package pet.com.br.pet.anuncio;

import android.content.Context;
import android.content.Intent;
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
import pet.com.br.pet.adapters.AnunciosAdapter;
import pet.com.br.pet.buscaRapida.InfoBuscaRapidaActivity;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Anuncios;
import pet.com.br.pet.utils.AnunciosUtils;
import pet.com.br.pet.utils.UrlUtils;


public class AnunciosActivity extends BaseMenu {


    private List<Anuncios> anuncios;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Context _context;

    private RequestQueue requestQueue;
    private int requestCount = 1, requestCountInit = 2;

    //String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);
        _context = this;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        anuncios = new ArrayList<>();



        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        recyclerView.addOnScrollListener(rVOnScrollListener);

        //initializing our adapter
        adapter = new AnunciosAdapter(anuncios, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }

    private RecyclerView.OnScrollListener rVOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case RecyclerView.SCROLL_STATE_IDLE:
                    getNewData();
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
                getNewData();
            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private JsonArrayRequest getDataFromServer(String url) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AnunciosActivity.this, "Não existem outros anuncios", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }


    private JsonArrayRequest getNewDataFromServer(String url) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseNewData(response);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AnunciosActivity.this, "Não existem novos anuncios" +error, Toast.LENGTH_LONG).show();
                    }
                });
        return jsonArrayRequest;
    }


    private void getData() {

        requestQueue.add(getDataFromServer(UrlUtils.ANUNCIO_URL+String.valueOf(requestCount)));
        requestCount++;
    }

    private void getNewData() {

        requestQueue.add(getNewDataFromServer(UrlUtils.ANUNCIO_ATUALIZA_URL+String.valueOf(requestCountInit)));
        requestCountInit++;
    }


    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Anuncios anuncios = new Anuncios();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                anuncios.setRaca(json.getString(AnunciosUtils.TAG_RACA));
                anuncios.setDono(json.getString(AnunciosUtils.TAG_DONO));
                anuncios.setIdade(json.getString(AnunciosUtils.TAG_IDADE));
                anuncios.setTipoVenda(json.getString(AnunciosUtils.TAG_VALOR));
                anuncios.setHora(json.getString(AnunciosUtils.TAG_HORARIO));
                anuncios.setUsername(json.getString(AnunciosUtils.TAG_USERNAME));
                if (json.getString(AnunciosUtils.TAG_IMAGEMPATCH).equals("")) {
                    anuncios.setImgid(null);
                } else {
                    anuncios.setImgid(decodeBase64(json.getString(AnunciosUtils.TAG_IMAGEMPATCH)));
                }
                anuncios.setCodigo(json.getString(AnunciosUtils.TAG_CODIGO));
                anuncios.setDescricao(json.getString(AnunciosUtils.TAG_DESCRICAO));
                anuncios.setCategoria(json.getString(AnunciosUtils.TAG_CATEGORIA));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.anuncios.add(anuncios);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }



    private void parseNewData(JSONArray array) {

            Anuncios anuncios = new Anuncios();
            JSONObject json = null;

            try {

                json = array.getJSONObject(0);
                anuncios.setRaca(json.getString(AnunciosUtils.TAG_RACA));
                anuncios.setDono(json.getString(AnunciosUtils.TAG_DONO));
                anuncios.setIdade(json.getString(AnunciosUtils.TAG_IDADE));
                anuncios.setTipoVenda(json.getString(AnunciosUtils.TAG_VALOR));
                anuncios.setHora(json.getString(AnunciosUtils.TAG_HORARIO));

                if (json.getString(AnunciosUtils.TAG_IMAGEMPATCH).equals("")) {
                    anuncios.setImgid(null);
                } else {
                    anuncios.setImgid(decodeBase64(json.getString(AnunciosUtils.TAG_IMAGEMPATCH)));
                }
                anuncios.setCodigo(json.getString(AnunciosUtils.TAG_CODIGO));
                anuncios.setDescricao(json.getString(AnunciosUtils.TAG_DESCRICAO));
                anuncios.setCategoria(json.getString(AnunciosUtils.TAG_CATEGORIA));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(!anuncios.getCodigo().equals(this.anuncios.get(0).getCodigo())) {
                this.anuncios.add(0,anuncios);
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