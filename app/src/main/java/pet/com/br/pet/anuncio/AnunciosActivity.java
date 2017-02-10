package pet.com.br.pet.anuncio;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
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
    private int _opcoes = 0;

    private RequestQueue requestQueue;
    private int requestCount = 1, requestCountInit = 2;
    private String _categoria = "";

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            dialogSettings(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogSettings(final Activity activity){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.activity_search_anuncio, null);
        final Spinner spinnerCategoria = (Spinner) alertLayout.findViewById(R.id.spinnerCategorias);

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.refinarPesquisa);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (spinnerCategoria.getSelectedItem().toString().equals("Todos os pets") || spinnerCategoria.getSelectedItem().toString().equals("Selecione a categoria")) {
                    _opcoes = 0;
                    anuncios.clear();
                    adapter.notifyDataSetChanged();
                    requestCount = 1;
                    requestCountInit = 2;
                    getData();

                } else {
                    _opcoes = 1;
                    anuncios.clear();
                    requestCount = 1;
                    requestCountInit = 2;
                    _categoria = spinnerCategoria.getSelectedItem().toString();
                    adapter.notifyDataSetChanged();
                    getData();
                }
                if(!(spinnerCategoria.getSelectedItem().toString().equals("Selecione a categoria"))) {
                    Toast.makeText(activity, "Vocẽ selecionou: " + spinnerCategoria.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
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
        if(_opcoes==0) {
            requestQueue.add(getDataFromServer(UrlUtils.ANUNCIO_URL + String.valueOf(requestCount)));
            requestCount++;
        } else{
            requestQueue.add(getDataFromServer(UrlUtils.ANUNCIO_CATEGORIA__URL + String.valueOf(requestCount) +"&categoria="+_categoria));
            requestCount++;
        }

    }



    private void getNewData() {
        if(_opcoes==0) {
            requestQueue.add(getNewDataFromServer(UrlUtils.ANUNCIO_ATUALIZA_URL + String.valueOf(requestCountInit)));
            requestCountInit++;
        } else{
            requestQueue.add(getDataFromServer(UrlUtils.ANUNCIO_CATEGORIA_ATUALIZA_URL + String.valueOf(requestCountInit) +"&categoria="+_categoria));
            requestCountInit++;
        }
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

        try {
            if (!anuncios.getCodigo().equals(this.anuncios.get(0).getCodigo())) {
                this.anuncios.add(0, anuncios);
            }
        }catch(ArrayIndexOutOfBoundsException error){
            getNewData();
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