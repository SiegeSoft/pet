package pet.com.br.pet.anuncio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.HashMap;

import pet.com.br.pet.R;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.chat.ChatViewActivity;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.Usuario;

import static android.view.View.GONE;
import static pet.com.br.pet.utils.TagUtils.TAG_IMAGEMPATCH;
import static pet.com.br.pet.utils.UrlUtils.ANUNCIO_UNICO_URL;

public class InfoAnunciosActivity extends BaseMenu {

    private String _raca;
    private String _dono;
    private String _idade;
    private String _tipoVenda;
    private String _codigo;
    private String _descricao;
    private String _categoria;
    private TextView _textViewRaca, _textViewIdade, _textViewDescricao, _textViewCategoria, _textViewDono, _textViewTipoVenda;
    private ImageView _imageViewFoto;
    private RequestQueue _requestQueue;
    private String _username;
    private HashMap<String, String> _userDetails;
    private LoginManager _loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_anuncios);
        _loginManager = new LoginManager(getApplicationContext());
        _userDetails = _loginManager.getUserDetails();
        getInfos();
        setIds();
        setInfos();
        _requestQueue = Volley.newRequestQueue(this);
        _requestQueue.add(getImage(_codigo));
        Button chat = (Button) findViewById(R.id.chatButton);
        Button reportar = (Button) findViewById(R.id.reportarButton);

        Log.e("Username ", ""+_username);
        Log.e("Usuario.getUsername ", ""+_userDetails.get(LoginManager.KEY_NAME));
        if(_username.equals(_userDetails.get(LoginManager.KEY_NAME))){
           chat.setVisibility(GONE);
            reportar.setVisibility(GONE);
        }

    }

    private void setIds() {
        _imageViewFoto = (ImageView) findViewById(R.id.cardImage);
        _textViewRaca = (TextView) findViewById(R.id.raca);
        _textViewIdade = (TextView) findViewById(R.id.idade);
        _textViewDescricao = (TextView) findViewById(R.id.textInfo);
        _textViewCategoria = (TextView) findViewById(R.id.textCategoria);
        _textViewTipoVenda = (TextView) findViewById(R.id.textVendaOuDoa);
        _textViewDono = (TextView) findViewById(R.id.textNome);

    }

    public void abrirChat(View v){
        Intent intent = new Intent(this, ChatViewActivity.class);
        intent.putExtra("Userchat", _username);
        intent.putExtra("Usercodigo", _codigo);
        intent.putExtra("Userdesc", "");
        this.startActivity(intent);
    }

    private Bitmap decodeStringImg(String imgid) {
        byte[] decodedString = Base64.decode(imgid, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void getInfos() {
        Intent intent = getIntent();
        _codigo = intent.getStringExtra("codigo");
        _username = intent.getStringExtra("username");
        _raca = intent.getStringExtra("raca");
        _idade = intent.getStringExtra("idade");
        _descricao = intent.getStringExtra("descricao");
        _categoria = intent.getStringExtra("categoria");
        _dono = intent.getStringExtra("dono");
        _tipoVenda = intent.getStringExtra("vendaoudoa");
    }

    private void setInfos() {

        _textViewRaca.setText(_raca);
        _textViewIdade.setText(_idade);
        _textViewDescricao.setText(_descricao);
        _textViewCategoria.setText(_categoria);
        _textViewTipoVenda.setText(_tipoVenda);
        _textViewDono.setText(_dono);
    }

    private JsonArrayRequest getImage(String codigo) {
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ANUNCIO_UNICO_URL+codigo,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        parseData(response);
                        progressBar.setVisibility(GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(GONE);
                        Toast.makeText(InfoAnunciosActivity.this, "Sem imagem", Toast.LENGTH_LONG).show();
                    }
                });
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {

        JSONObject json = null;
        try {
            json = array.getJSONObject(0);

            _imageViewFoto.setImageBitmap(decodeStringImg(json.getString(TAG_IMAGEMPATCH)));

        } catch (JSONException e) {
            Toast.makeText(InfoAnunciosActivity.this, "Sem imagem" + e, Toast.LENGTH_SHORT).show();
        }
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


}
