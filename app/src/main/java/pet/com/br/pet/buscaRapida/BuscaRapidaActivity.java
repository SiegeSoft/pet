/**
 * Created by iaco_ on 24/08/2016.
 */

/**
 * Edit by rafael on 30/08/2016.
 */

package pet.com.br.pet.buscaRapida;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import java.util.HashMap;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.autentica.Login;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.gps.LocationDirector;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.BuscaRapida;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.models.Usuario;
import pet.com.br.pet.tindercard.FlingCardListener;
import pet.com.br.pet.tindercard.SwipeFlingAdapterView;
import pet.com.br.pet.utils.TagUtils;
import pet.com.br.pet.utils.UrlUtils;


/**
 * Created by iaco_ on 24/08/2016.
 */

/**
 * Edit by rafael on 30/08/2016.
 */

public class BuscaRapidaActivity extends BaseMenu implements FlingCardListener.ActionDownInterface {


    public static MyAppAdapter _myAppAdapter;
    public static ViewHolder _viewHolder;

    //envia para o servidor a latitude e longitude
    private String _getLatitude, _getLongitude;

    private RequestQueue requestQueue;
    private SwipeFlingAdapterView _flingContainer;
    private List<BuscaRapida> _buscaRapidaLista;
    private Context _context;

    private HashMap<String, String> _userDetails;
    private static ArrayList<String> _arrayLikes = new ArrayList<>();
    private static ArrayList<String> _arrayDislikes = new ArrayList<>();
    private static ArrayList<String> _arrayIds = new ArrayList<>();

    private TextView _textOops, _textConfings;
    private ImageView _pets;
    private ImageButton _btnHeart, _btnShare, _btnNext;
    private RelativeLayout _rodape;

    private LoginManager _loginManager;

    private float _noRepeatTimes = 0;

    private long _distance = 10;

    public static void removeBackground() {
        _viewHolder._background.setVisibility(View.GONE);
        _myAppAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_rapida);

        _loginManager = new LoginManager(getApplicationContext());

        if (!_loginManager.checkLogin()) {
            Intent i = new Intent(this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        } else {
            _userDetails = _loginManager.getUserDetails();
            Usuario.setUserName(_userDetails.get(LoginManager.KEY_NAME));
            Usuario.setLikes(_userDetails.get(LoginManager.KEY_LIKE));
            Profile.setNomeExibicao(_userDetails.get(LoginManager.KEY_NOME_EXIBICAO));
            Usuario.setDislikes(_userDetails.get(LoginManager.KEY_DISLIKE));
            Profile.setUsername(_userDetails.get(LoginManager.KEY_NAME));
            _arrayLikes.addAll(Usuario.getLikes());
            _arrayDislikes.addAll(Usuario.getDislikes());
        }

        _pets = (ImageView) findViewById(R.id.pets);
        _textOops = (TextView) findViewById(R.id.txtViewOops);
        _textConfings = (TextView) findViewById(R.id.txtViewConfings);
        _btnHeart = (ImageButton) findViewById(R.id.btnHeart);
        _btnNext = (ImageButton) findViewById(R.id.btnNext);
        _btnShare = (ImageButton) findViewById(R.id.btnShare);
        _rodape = (RelativeLayout) findViewById(R.id.rodape);


        _context = this;
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

        LocationDirector myloc = new LocationDirector(this);
        if (myloc.canGetLocation) {
            double longitude = myloc.getLatitude();
            double latitude = myloc.getLongitude();
            _getLatitude = "" + latitude;
            _getLongitude = "" + longitude;
            Profile.setLatitude(""+latitude);
            Profile.setLongitude(""+longitude);
        }

        requestQueue = Volley.newRequestQueue(this);

        _myAppAdapter = new MyAppAdapter(_buscaRapidaLista, BuscaRapidaActivity.this);
        _flingContainer.setAdapter(_myAppAdapter);

        _flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                setDislike(_buscaRapidaLista.get(0).getId());
                _buscaRapidaLista.remove(0);
                _myAppAdapter.notifyDataSetChanged();
                if (_buscaRapidaLista.size() == 0) {
                    getData();
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                setLike(_buscaRapidaLista.get(0).getId());
                _buscaRapidaLista.remove(0);
                _myAppAdapter.notifyDataSetChanged();
                if (_buscaRapidaLista.size() == 0) {
                    getData();
                }

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (_noRepeatTimes <= 3) {
                    if (itemsInAdapter == 0) {
                        getData();
                    }
                } else{
                    setPetsVisibleImage();
                }
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
                showInfos(itemPosition);
            }
        });

    }

    private void setPetsVisibleImage(){
        _pets.setVisibility(View.VISIBLE);
        _textOops.setVisibility(View.VISIBLE);
        _textConfings.setVisibility(View.VISIBLE);
        _btnHeart.setVisibility(View.GONE);
        _btnNext.setVisibility(View.GONE);
        _btnShare.setVisibility(View.GONE);
        _rodape.setVisibility(View.GONE);
    }

    private void setAnunciosVisibleImage(){
        _pets.setVisibility(View.GONE);
        _textOops.setVisibility(View.GONE);
        _textConfings.setVisibility(View.GONE);
        _btnHeart.setVisibility(View.VISIBLE);
        _btnNext.setVisibility(View.VISIBLE);
        _btnShare.setVisibility(View.VISIBLE);
        _rodape.setVisibility(View.VISIBLE);
    }



    private void setLike(String id) {
        if (_arrayLikes.size() == 1) {
            if (_arrayLikes.get(0).equals("")) {
                _arrayLikes.remove(0);
            }
        }
        _arrayLikes.add(id);
        _arrayIds.add(id);
        for (int a = 0; a < _arrayLikes.size(); a++) {
            Log.v("Likes", _arrayLikes.get(a));
            Log.v("Array Ids", _arrayIds.get(a));
        }
    }

    private void setDislike(String id) {
        if (_arrayDislikes.size() == 1) {
            if (_arrayDislikes.get(0).equals("")) {
                _arrayDislikes.remove(0);
            }
        }
        _arrayDislikes.add(id);
        _arrayIds.add(id);
        for (int a = 0; a < _arrayDislikes.size(); a++) {
            Log.v("DisLikes", _arrayDislikes.get(a));
            Log.v("Array Ids", _arrayIds.get(a));
        }
    }


    private void showInfos(int position) {
        Intent intent = new Intent(_context, InfoBuscaRapidaActivity.class);
        intent.putExtra("codigo", _buscaRapidaLista.get(position).getCodigo());
        intent.putExtra("raca", _buscaRapidaLista.get(position).getRaca());
        intent.putExtra("idade", _buscaRapidaLista.get(position).getIdade());
        intent.putExtra("descricao", _buscaRapidaLista.get(position).getDescricao());
        intent.putExtra("categoria", _buscaRapidaLista.get(position).getCategoria());
        intent.putExtra("vendaoudoa", _buscaRapidaLista.get(position).getTipoVenda());
        intent.putExtra("dono", _buscaRapidaLista.get(position).getDono());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_confings).setVisible(true);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_confings) {
            dialogSettings(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void dialogSettings(final Activity activity){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.activity_settings_busca_rapida, null);
        final SeekBar seekBar = (SeekBar) alertLayout.findViewById(R.id.distanceBar);
        final TextView txtDistance = (TextView) alertLayout.findViewById(R.id.distance);
        final TextView txtCity = (TextView) alertLayout.findViewById(R.id.range);

        txtDistance.setText("10");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtDistance.setText(String.valueOf(progress));

                if(progress < 30){
                    txtCity.setText(R.string.neighborhood);

                } else if(progress >= 30 && progress < 50){
                    txtCity.setText(R.string.city);

                } else if(progress >= 50 && progress < 100){
                    txtCity.setText(R.string.more_city);

                } else if(progress >= 100 && progress < 200) {
                    txtCity.setText(R.string.state);

                } else if(progress >= 200 && progress <= 400) {
                    txtCity.setText(R.string.more_state);

                } else {
                    txtCity.setText(R.string.all_country);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(R.string.titleSettings);
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
                _distance = seekBar.getProgress();
                setAnunciosVisibleImage();
                _noRepeatTimes = 0;
                getData();
                Toast.makeText(activity, ""+_distance, Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }


    private void getData() {
        try {
            requestQueue.add(getDataFromServer(_getLatitude, _getLongitude, _distance));
        }catch(NullPointerException error){
            setPetsVisibleImage();
        }
    }


    private JsonArrayRequest getDataFromServer(String latitude, String longitude, long distance) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(UrlUtils.LOCALIZACAO_URL+latitude+"&LON="+longitude+"&DISTANCE="+String.valueOf(distance),
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
                        _noRepeatTimes++;
                    }
                });
        return jsonArrayRequest;
    }



    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            BuscaRapida buscaRapida = new BuscaRapida();
            boolean equals = false;
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                buscaRapida.setId(json.getString(TagUtils.TAG_ID));
                buscaRapida.setCodigo(json.getString(TagUtils.TAG_COD));
                buscaRapida.setRaca(json.getString(TagUtils.TAG_RACA));
                buscaRapida.setCategoria(json.getString(TagUtils.TAG_CATEGORIA));
                buscaRapida.setDescricao(json.getString(TagUtils.TAG_DESCRICAO));
                buscaRapida.setIdade(json.getString(TagUtils.TAG_IDADE));
                buscaRapida.setTipoVenda(json.getString(TagUtils.TAG_VALOR));
                buscaRapida.setDono(json.getString(TagUtils.TAG_DONO));
                buscaRapida.setImgid(json.getString(TagUtils.TAG_IMAGEMPATCH));

            } catch (JSONException e) {
                Toast.makeText(BuscaRapidaActivity.this, "Error ao consultar o banco de dados" + e, Toast.LENGTH_SHORT).show();
            }
            if (!buscaRapida.getDono().equals(Usuario.getUserName())) {
                if (_buscaRapidaLista.size() == 0) {
                    isFirstEquals(buscaRapida, equals);
                } else {
                    isEquals(buscaRapida, equals);
                }
            }
        }
        _myAppAdapter.notifyDataSetChanged();
    }

    private void isFirstEquals(BuscaRapida buscaRapida, boolean equals) {
        for (int x = 0; x < _arrayIds.size(); x++) {
            if (_arrayIds.get(x).equals(buscaRapida.getId())) {
                equals = true;
                _noRepeatTimes++;
                break;
            }
        }
        if (!equals) {
            _buscaRapidaLista.add(buscaRapida);
            _noRepeatTimes--;
        }
    }

    private void isEquals(BuscaRapida buscaRapida, boolean equals) {
        if (!_buscaRapidaLista.get(_buscaRapidaLista.size() - 1).getId().equals(buscaRapida.getId())) {
            for (int x = 0; x < _arrayIds.size(); x++) {
                if (_arrayIds.get(x).equals(buscaRapida.getId())) {
                    equals = true;
                    _noRepeatTimes++;
                    break;
                }
            }
            if (!equals) {
                _buscaRapidaLista.add(buscaRapida);
                _noRepeatTimes--;
            }
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
        public TextView _nome, _info;
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

                _viewHolder = new ViewHolder();
                _viewHolder._nome = (TextView) rowView.findViewById(R.id.raca);
                _viewHolder._info = (TextView) rowView.findViewById(R.id.infos);
                _viewHolder._background = (FrameLayout) rowView.findViewById(R.id.background);
                _viewHolder._foto = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(_viewHolder);
            } else {
                _viewHolder = (ViewHolder) convertView.getTag();
            }

            _viewHolder._nome.setText(_buscaRapidaLista.get(position).getRaca());
            _viewHolder._info.setText("Idade: " + _buscaRapidaLista.get(position).getIdade());
            _viewHolder._foto.setImageBitmap(_buscaRapidaLista.get(position).getImgid());
            return rowView;
        }
    }


}