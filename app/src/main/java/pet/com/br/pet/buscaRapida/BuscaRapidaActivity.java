package pet.com.br.pet.buscaRapida;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.models.BuscaRapida;
import pet.com.br.pet.tindercard.FlingCardListener;
import pet.com.br.pet.tindercard.SwipeFlingAdapterView;
import pet.com.br.pet.utils.BuscaRapidaUtils;

/**
 * Created by iaco_ on 24/08/2016.
 */

/**
 * Edit by rafael on 30/08/2016.
 */

public class BuscaRapidaActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {


    public static MyAppAdapter _myAppAdapter;
    private String _getLatitude, _getLongitude;
    private TextView myatualLongitude;
    private TextView myatualLatitude;
    private RequestQueue requestQueue;
    public static ViewHolder _viewHolder;
    private SwipeFlingAdapterView _flingContainer;
    private List<BuscaRapida> _buscaRapidaLista;

    public static void removeBackground() {
        _viewHolder._background.setVisibility(View.GONE);
        _myAppAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_rapida);

        _flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        _buscaRapidaLista = new ArrayList<BuscaRapida>();


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        _getLatitude = "" + latitude;
        _getLongitude = "" + longitude;

        requestQueue = Volley.newRequestQueue(this);
        getData();


        _myAppAdapter = new MyAppAdapter(_buscaRapidaLista, BuscaRapidaActivity.this);
        _flingContainer.setAdapter(_myAppAdapter);


        _flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                _buscaRapidaLista.remove(0);
                _myAppAdapter.notifyDataSetChanged();
                getData();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                _buscaRapidaLista.remove(0);
                _myAppAdapter.notifyDataSetChanged();
                getData();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = _flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        _flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = _flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                _myAppAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void getData() {
        requestQueue.add(getDataFromServer(_getLatitude, _getLongitude));
    }


    private JsonObjectRequest getDataFromServer(String latitude, String longitude) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        JsonObjectRequest json = new JsonObjectRequest(BuscaRapidaUtils.DATA_GPS_URL + latitude + "&LON=" + longitude,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseData(response);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BuscaRapidaActivity.this, "Sem conexao com o servidor", Toast.LENGTH_LONG).show();
                    }
                });
        return json;
    }


    private void parseData(JSONObject json) {
        BuscaRapida buscaRapida;
        try {
            buscaRapida = new BuscaRapida();
            buscaRapida.setId(json.getString(BuscaRapidaUtils.TAG_ID));
            buscaRapida.setCodigo(json.getString(BuscaRapidaUtils.TAG_COD));
            buscaRapida.setRaca(json.getString(BuscaRapidaUtils.TAG_RACA));
            buscaRapida.setCategoria(json.getString(BuscaRapidaUtils.TAG_CATEGORIA));
            buscaRapida.setDescricao(json.getString(BuscaRapidaUtils.TAG_DESCRICAO));
            buscaRapida.setIdade(json.getString(BuscaRapidaUtils.TAG_IDADE));
            buscaRapida.setTipoVenda(json.getString(BuscaRapidaUtils.TAG_VALOR));
            buscaRapida.setDono(json.getString(BuscaRapidaUtils.TAG_DONO));
            buscaRapida.setImgid(json.getString(BuscaRapidaUtils.TAG_IMAGEMPATCH));

            _buscaRapidaLista.add(buscaRapida);
            _myAppAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(BuscaRapidaActivity.this, "Error ao consultar o banco de dados" + e, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }


    /**
     * Adapter
     */

    public static class ViewHolder {
        public static FrameLayout _background;
        public TextView _nome, _info, _descrcao;
        public ImageView _foto;

    }

    public class MyAppAdapter extends BaseAdapter {


        public List<BuscaRapida> _buscaRapidaLista;
        public Context context;

        private MyAppAdapter(List<BuscaRapida> apps, Context context) {
            this._buscaRapidaLista = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return _buscaRapidaLista.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item_busca_rapida, parent, false);

                // configure view holder
                _viewHolder = new ViewHolder();
                _viewHolder._nome = (TextView) rowView.findViewById(R.id.raca);
                _viewHolder._info = (TextView) rowView.findViewById(R.id.infos);
                _viewHolder._descrcao = (TextView) rowView.findViewById(R.id.descricao);
                _viewHolder._background = (FrameLayout) rowView.findViewById(R.id.background);
                _viewHolder._foto = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(_viewHolder);
            } else {
                _viewHolder = (ViewHolder) convertView.getTag();
            }
            _viewHolder._nome.setText(_buscaRapidaLista.get(position).getRaca());
            _viewHolder._info.setText("Idade: " + _buscaRapidaLista.get(position).getIdade() + " Doação/Venda: " + _buscaRapidaLista.get(position).getTipoVenda()
                    + " Dono: " + _buscaRapidaLista.get(position).getDono());
            _viewHolder._descrcao.setText(_buscaRapidaLista.get(position).getDescricao());
            _viewHolder._foto.setImageBitmap(_buscaRapidaLista.get(position).getImgid());
            return rowView;
        }
    }


}
