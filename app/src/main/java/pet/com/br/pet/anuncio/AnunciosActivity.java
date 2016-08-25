package pet.com.br.pet.anuncio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Anuncios;
import pet.com.br.pet.utils.AnunciosUtils;


public class AnunciosActivity extends BaseMenu {


    private List<Anuncios> anuncios;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private RequestQueue requestQueue;
    private int requestCount = 1;

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
            super.onScrolled(recyclerView, dx, -dy);
            if (isLastItemDisplaying(recyclerView)) {
                getData();
            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AnunciosUtils.DATA_URL + String.valueOf(requestCount),
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


    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
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
                Log.v("ImagePatch:",json.getString(AnunciosUtils.TAG_IMAGEMPATCH));
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
            //Adding the superhero object to the list
            this.anuncios.add(anuncios);
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
}