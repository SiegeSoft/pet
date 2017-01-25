package pet.com.br.pet.negociacoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import pet.com.br.pet.adapters.DoacoesAdapter;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.buscaRapida.BuscaRapidaActivity;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Doacoes;
import pet.com.br.pet.utils.AnunciosUtils;
import pet.com.br.pet.utils.TagUtils;
import pet.com.br.pet.utils.UrlUtils;

public class MinhasDoacoesActivity extends BaseMenu {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private List<Doacoes> _doacoes;
    private LoginManager _loginManager;
    private HashMap<String, String> _userDetails;
    private Paint p = new Paint();
    private DoacoesAdapter doacoesAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_adoacoes);
        context = getApplicationContext();
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
        swiipeDeleteRecycler();
    }

    private void swiipeDeleteRecycler(){

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    deleteSwipDoacao(_doacoes.get(position).getCodigo(), position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX < 0){
                        p.setColor(Color.parseColor("#f27979"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void deleteSwipDoacao(final String codigo, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Você deseja deletar seu anuncio de doação?")
                .setNegativeButton("Não!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _doacoes.clear();
                        adapter.notifyDataSetChanged();
                        getData(_userDetails.get(LoginManager.KEY_NAME));
                    }
                })
                .setPositiveButton("Sim, desejo deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAnuncio(codigo, position);
                    }
                })
                .create()
                .show();
    }



    private void deleteAnuncio(final String codigo, final int position){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtils.DELETAR_MEU_ANUNCIO+codigo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            setMessage(position);

                        } catch (Exception exception) {
                            Toast.makeText(context, "Erro ao deletar ", Toast.LENGTH_SHORT).show();
                            Log.e("Error: ", ""+exception.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Erro na sincronização com servidor ")
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(TagUtils.TAG_COD, codigo);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setMessage(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Doação deletada!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        _doacoes.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
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
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, BuscaRapidaActivity.class);
            startActivity(intent);
        }
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
