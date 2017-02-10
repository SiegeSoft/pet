package pet.com.br.pet.negociacoes;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.CurtidasAdapter;
import pet.com.br.pet.adapters.DoacoesAdapter;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Doacoes;
import pet.com.br.pet.models.Usuario;
import pet.com.br.pet.utils.AnunciosUtils;
import pet.com.br.pet.utils.TagUtils;
import pet.com.br.pet.utils.UrlUtils;

public class MinhasCurtidasActivity extends BaseMenu {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private List<Doacoes> _doacoes;
    private LoginManager _loginManager;
    private HashMap<String, String> _userDetails;
    private Paint p = new Paint();
    private Context context;
    private ArrayList<String> arrayLikes = new ArrayList<>();
    private ArrayList<String> arrayDislikes = new ArrayList<>();
    private Set<String> hashSetLikes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_curtidas);

        context = getApplicationContext();
        _loginManager = new LoginManager(this);
        _userDetails = _loginManager.getUserDetails();



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        _doacoes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        getLikes();


        //initializing our adapter
        adapter = new CurtidasAdapter(_doacoes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        swipeDeleteRecycler();

    }


    private void swipeDeleteRecycler(){

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
        builder.setMessage("Você deseja apagar esse like?")
                .setNegativeButton("Não!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        adapter.notifyDataSetChanged();
                        //getData(_userDetails.get(LoginManager.KEY_NAME));
                    }
                })
                .setPositiveButton("Sim, desejo deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //deleteAnuncio(codigo, position);
                    }
                })
                .create()
                .show();
    }

    private void getLikes(){
        Usuario.setDislikes(_userDetails.get(LoginManager.KEY_DISLIKE));
        Usuario.setLikes(_userDetails.get(LoginManager.KEY_LIKE));
        arrayLikes.addAll(Usuario.getLikes());
        arrayDislikes.addAll(Usuario.getDislikes());
        hashSetLikes = new HashSet<>();
        hashSetLikes.addAll(arrayLikes);
        arrayLikes.clear();
        arrayLikes.addAll(hashSetLikes);
        Log.e("Arrayx", ""+arrayLikes.size());
        for(int a=0; a<arrayLikes.size(); a++){
            getData(arrayLikes.get(a));
            Log.e("Minhas curtidas", ""+arrayLikes.get(a));
        }
    }

    private void getData(String id){
        requestQueue.add(getInfos(UrlUtils.ANUNCIO_ID +""+ id));
    }


    private JsonArrayRequest getInfos(final String url) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseData(response);
                        Log.e("Passou aqui", ""+url);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MinhasCurtidasActivity.this, "Você não possui curtidas", Toast.LENGTH_LONG).show();
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
