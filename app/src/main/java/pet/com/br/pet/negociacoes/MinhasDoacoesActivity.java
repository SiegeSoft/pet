package pet.com.br.pet.negociacoes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.DoacoesAdapter;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Doacoes;
import pet.com.br.pet.utils.AnunciosUtils;
import pet.com.br.pet.utils.UrlUtils;

public class MinhasDoacoesActivity extends BaseMenu {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private List<Doacoes> _doacoes;
    private LoginManager _loginManager;
    private HashMap<String, String> _userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_adoacoes);

        _loginManager = new LoginManager(this);
        _userDetails = _loginManager.getUserDetails();




        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        _doacoes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getData(_userDetails.get(LoginManager.KEY_NAME));

        //initializing our adapter
        adapter = new DoacoesAdapter(_doacoes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void getData(String username){
        requestQueue.add(getInfos(UrlUtils.ANUNCIO_MINHAS_DOACOES + username));
    }


    private JsonArrayRequest getInfos(String url) {
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
                        Toast.makeText(MinhasDoacoesActivity.this, "Você não possui doações anunciadas", Toast.LENGTH_LONG).show();
                    }
                });
        return jsonArrayRequest;
    }


    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            Doacoes doacoes = new Doacoes();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                doacoes.setRaca(json.getString(AnunciosUtils.TAG_RACA));
                doacoes.setDono(json.getString(AnunciosUtils.TAG_DONO));
                doacoes.setIdade(json.getString(AnunciosUtils.TAG_IDADE));
                doacoes.setTipoVenda(json.getString(AnunciosUtils.TAG_VALOR));
                doacoes.setHora(json.getString(AnunciosUtils.TAG_HORARIO));
                doacoes.setUsername(json.getString(AnunciosUtils.TAG_USERNAME));
                if (json.getString(AnunciosUtils.TAG_IMAGEMPATCH).equals("")) {
                    doacoes.setImgid(null);
                } else {
                    doacoes.setImgid(decodeBase64(json.getString(AnunciosUtils.TAG_IMAGEMPATCH)));
                }
                doacoes.setCodigo(json.getString(AnunciosUtils.TAG_CODIGO));
                doacoes.setDescricao(json.getString(AnunciosUtils.TAG_DESCRICAO));
                doacoes.setCategoria(json.getString(AnunciosUtils.TAG_CATEGORIA));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            _doacoes.add(doacoes);
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

}
