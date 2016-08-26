package pet.com.br.pet.anuncios;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Anuncios;
import pet.com.br.pet.utils.AnunciosUtils;


public class AnunciosActivity extends BaseMenu {


    private List<Anuncios> anuncios;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private RequestQueue requestQueue;
    private int requestCount = 1, requestCountInit = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

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
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
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
                        Toast.makeText(AnunciosActivity.this, "Não existem novos anuncios", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }


    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(AnunciosUtils.DATA_URL+String.valueOf(requestCount)));
        //Incrementing the request counter
        requestCount++;
    }

    private void getNewData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getNewDataFromServer(AnunciosUtils.DATA_NEW_URL+String.valueOf(requestCountInit)));
        //Incrementing the request counter
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
            Log.v("normal:","Anuncio codigo" + this.anuncios.get(0).getCodigo() );
            Log.v("normal:","Anuncio codigo" + anuncios.getCodigo());
            if(!anuncios.getCodigo().equals(this.anuncios.get(0).getCodigo())) {
                Log.v("dentro do if:","Anuncio codigo" + this.anuncios.get(0).getCodigo() );
                this.anuncios.add(0,anuncios);
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

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        Log.v("normal:","NAO" + recyclerView.getAdapter().getItemCount() );
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            Log.v("normal:","NAO" + recyclerView.getAdapter().getItemCount() );
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private boolean isFirstItemDisplaying(RecyclerView recyclerView) {
        Log.v("firstVisible:","SIM" + recyclerView.getAdapter().getItemCount() );
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            Log.v("firstVisible:","SIM" + String.valueOf(firstVisibleItemPosition));
            if (firstVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItemPosition == 0)
                return true;
        }
        return false;
    }
}