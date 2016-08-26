package pet.com.br.pet.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pet.com.br.pet.R;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.BuscaRapida;
import pet.com.br.pet.utils.BuscaRapidaUtils;

/**
 * Created by iaco_ on 24/08/2016.
 */
public class BuscaRapidaActivity extends BaseMenu {


    private String getLatitude, getLongitude;

    private TextView codigo;
    private TextView myLatitude;
    private TextView myLongitude;
    private TextView myatualLongitude;
    private TextView myatualLatitude;
    private RequestQueue requestQueue;
    ImageView img;


    //imageadapter

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscarapida);


        codigo = (TextView) findViewById(R.id.codigo);

        myLatitude = (TextView) findViewById(R.id.latitude);
        myLongitude = (TextView) findViewById(R.id.longitude);
        myatualLatitude = (TextView) findViewById(R.id.atualLatitude);
        myatualLongitude = (TextView) findViewById(R.id.atualLongitude);
        img = (ImageView) findViewById(R.id.imagebuscarapida);


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
        getLatitude = "" + latitude;
        getLongitude = "" + longitude;

        myatualLatitude.setText("Atual Latitude: " + getLatitude);
        myatualLongitude.setText("Atual Latitude: " + getLongitude);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getDataFromServer(getLatitude, getLongitude));

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

                        Toast.makeText(BuscaRapidaActivity.this, "Sem conexao com o servidor"+error, Toast.LENGTH_LONG).show();
                    }
                });

        return json;
    }


    private void parseData(JSONObject json) {
        BuscaRapida buscaRapida = new BuscaRapida();
        try {
            buscaRapida.setIdRandom(json.getString(BuscaRapidaUtils.TAG_ID));
            buscaRapida.setLatitudeRandom(json.getString(BuscaRapidaUtils.TAG_LAT));
            buscaRapida.setLongitudeRandom(json.getString(BuscaRapidaUtils.TAG_LON));
            buscaRapida.setCodigo(json.getString(BuscaRapidaUtils.TAG_COD));
            myLatitude.setText("LAT: " +buscaRapida.getLatitudeRandom());
            myLongitude.setText("LON: " +buscaRapida.getLongitudeRandom());
            codigo.setText("Codigo: "+ buscaRapida.getCodigo());


            Log.v("ImagePatch:",json.getString(BuscaRapidaUtils.TAG_IMAGEMPATCH));
            if (json.getString(BuscaRapidaUtils.TAG_IMAGEMPATCH).equals("")) {
                buscaRapida.setImgid(null);
            } else {
                buscaRapida.setImgid(decodeBase64(json.getString(BuscaRapidaUtils.TAG_IMAGEMPATCH)));
            }

        } catch (JSONException e) {
            Toast.makeText(BuscaRapidaActivity.this, "Error ao consultar o banco de dados"+e, Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap decodeBase64(String input) {

        if (input.equals("")) {
            return null;
        } else {
            byte[] decodedString = Base64.decode(input, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            /*Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;*/
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.RelativeImg);
            int width  = layout.getMeasuredWidth();
            int height = layout.getMeasuredHeight();
            img.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, width, height, true));
            return decodedByte;

        }
    }

}
