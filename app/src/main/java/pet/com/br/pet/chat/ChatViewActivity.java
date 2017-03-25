package pet.com.br.pet.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.ChatViewAdapter;
import pet.com.br.pet.database.ChatController;
import pet.com.br.pet.menus.BaseMenu;
import pet.com.br.pet.models.ChatView;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.textviewcustom.ScrollTextView;
import pet.com.br.pet.utils.ChatViewUtils;


/**
 * Created by iaco_ on 28/08/2016.
 */
public class ChatViewActivity extends BaseMenu {
    private TextView titleTV;

    private int scrollvalue = 0;

    //LISTVIEW
    private List<ChatView> chatview;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private ChatController chatController;

    // private Activity context;

    //INTENT VARS
    String user;
    String codigo;
    String descricao;
    String number;
    String profileimg;

    //SENDDATACHAT
    EditText mensagem;
    Button botaoEnviar;

    //horas
    String HoraH;
    String HoraM;
    String HoraS;


    ScrollTextView scrolltext;

    Drawable icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatview);

        mensagem = (EditText) findViewById(R.id.edit_escrevemensagem);
        botaoEnviar = (Button) findViewById(R.id.botaoenviar);

        //instancia o chat controller class
        chatController = new ChatController(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewchatView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatview = new ArrayList<>();
        Intent intent = getIntent();
        user = intent.getStringExtra("Userchat");
        codigo = intent.getStringExtra("Usercodigo");
        descricao = intent.getStringExtra("Userdesc");
        profileimg = intent.getStringExtra("UserProfileImg");
        //ACTION BAR PERSONALIZADA
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.menu_chatview_text, null);

        ab.setCustomView(customView);
        ab.setDisplayShowCustomEnabled(true);

        //SETA O NOME DO USUARIO/MARQUEE DA ULTIMA VISUALIZAÇÃO
        TextView texttitle = (TextView) findViewById(R.id.textview_tittle);
        scrolltext = (ScrollTextView) findViewById(R.id.scrolltext);

        requestQueue = Volley.newRequestQueue(this);

        if (ChatView.getUltimaHorahHorah() == null && ChatView.getUltimaHoram() == null) {
            texttitle.setText("" + user);
            getUltimoHorarioVisualisado();
        } else {
            texttitle.setText("" + user);
            scrolltext.initialScroll();
        }

        if(Integer.parseInt(profileimg) == 1){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.cachorro_icons);
        }else if(Integer.parseInt(profileimg) == 2){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.galinha_icons);
        }else if(Integer.parseInt(profileimg) == 3){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.sapo_icons);
        }else if(Integer.parseInt(profileimg) == 4){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.hamster_icons);
        }else if(Integer.parseInt(profileimg) == 5){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.macaco_icons);
        }else if(Integer.parseInt(profileimg) == 6){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.gato_icons);
        }else if(Integer.parseInt(profileimg) == 7){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.tigre_icons);
        }else if(Integer.parseInt(profileimg) == 8){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.coelho_icons);
        }else if(Integer.parseInt(profileimg) == 9){
            icon = ChatViewActivity.this.getResources().getDrawable(R.drawable.rato_icons);
        }

        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();

        Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(bitmap));
        getSupportActionBar().setIcon(drawable);
        //FIM

        setaAdaptadorUsuarioDestino(user);

        //INICIA O PROGRAMA NA ULTIMA ATUALIZAÇÃO DO RECYCLERVIEW
        int recyclerposition = recyclerView.getAdapter().getItemCount();
        recyclerView.scrollToPosition(recyclerposition-1);
        ChatView.setAdaptercontador(recyclerView.getAdapter().getItemCount());

        getDataUsuarioDestino();
        if (scrollvalue == 0) {
            ScrollTextView scrolltext = (ScrollTextView) findViewById(R.id.scrolltext);
            scrolltext.startScroll();
            scrollvalue = 1;
        }
    }


    public Bitmap createCircleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.parseColor("#AB47BC");
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth((float) 1);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.helpchat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent refresh = new Intent(ChatViewActivity.this, ChatActivity.class);
            startActivity(refresh);
            finish();
        }
        if (item.getItemId() == R.menu.helpchat) {

        }

        return super.onOptionsItemSelected(item);
    }


    //RECEBER MENSAGENS DOS OUTROS USUARIOS
    private JsonArrayRequest getUsuarioDestinoDataFromServer(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            parseDataUsuarioDestino(response);
                            getDataUsuarioDestino();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getDataUsuarioDestino();
                    }
                });
        return jsonArrayRequest;
    }

    //RECEBER ULTIMO HORARIO VISUALIZADO
    //RECEBER MENSAGENS DOS OUTROS USUARIOS
    private JsonArrayRequest getUltimoHorarioVisualisadoFromServer(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            parseDataUltimoHorarioVisualisado(response);
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        return jsonArrayRequest;
    }


    //INICIAR O RECEBIMENTO DAS MENSAGENS DOS UUARIOS
    private void getDataUsuarioDestino() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getUsuarioDestinoDataFromServer(ChatViewUtils.DATA_URL + "&CODIGO=" + codigo + "&USUARIODESTINO=" + user + "&USUARIO=" + Profile.getUsername()));
    }

    private void getUltimoHorarioVisualisado() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getUltimoHorarioVisualisadoFromServer(ChatViewUtils.DATA_ULTIMOHORARIO + "&codigo=" + codigo + "&myusername=" + Profile.getUsername() + "&usuariodestino=" + user));
        //Incrementing the request counter
    }


    //CONVERTE EM STRING AS SUAS MENSAGENS DOS OUTROS USUARIOS
    private void parseDataUsuarioDestino(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ChatView chatvieww = new ChatView();
            JSONObject json = null;
            try {

                //PUXA OS ITEMS DO BANCO DE DADOS
                json = array.getJSONObject(i);
                chatvieww.setId(json.getString(ChatViewUtils.TAG_ID));
                chatvieww.setCodigo(json.getString(ChatViewUtils.TAG_CODIGO));
                chatvieww.setUsername(json.getString(ChatViewUtils.TAG_USUARIODESTINO));
                chatvieww.setMensagem(json.getString(ChatViewUtils.TAG_MENSAGEM));

                chatvieww.setDia(json.getString(ChatViewUtils.TAG_DIA));
                chatvieww.setMes(json.getString(ChatViewUtils.TAG_MES));
                chatvieww.setAno(json.getString(ChatViewUtils.TAG_ANO));

                chatvieww.setHorah(json.getString(ChatViewUtils.TAG_HORAH));
                chatvieww.setHoram(json.getString(ChatViewUtils.TAG_HORAM));
                chatvieww.setHoras(json.getString(ChatViewUtils.TAG_HORAS));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Linear search, see if the id exists
            boolean flag = false;
            //VERIFICA SE EXISTEM IDS DUPLICADOS
            for (ChatView chat : chatview) {
                if (null != chat.getId() && null != chatvieww.getId()) {
                    if (chat.getId().equals(chatvieww.getId())) {
                        // Item exists
                        flag = true;
                        break;
                    }
                }
            }
            // if flag is true item exists, don't add.
            if (!flag) {
                String internal = "0";
                chatController.insereDado(chatvieww.getId(), codigo, Profile.getUsername(), chatvieww.getMensagem(), user, chatvieww.getDia(), chatvieww.getMes(), chatvieww.getAno(), chatvieww.getHorah(), chatvieww.getHoram(), chatvieww.getHoras(), internal);
                //chatview.add(chatvieww);
                setaAdaptadorUsuarioDestino(user);
            }
        }
    }

    //CONVERTE EM STRING AS SUAS MENSAGENS DOS OUTROS USUARIOS
    private void parseDataUltimoHorarioVisualisado(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ChatView chatvieww = new ChatView();
            JSONObject json = null;
            try {
                //PUXA OS ITEMS DO BANCO DE DADOS
                json = array.getJSONObject(i);

                chatvieww.setUltimaHorahHorah(json.getString(ChatViewUtils.TAG_ULTIMAHORAH));
                chatvieww.setUltimaHoramHoram(json.getString(ChatViewUtils.TAG_ULTIMAHORAM));
                chatvieww.setUltimaHorasHoras(json.getString(ChatViewUtils.TAG_ULTIMAHORAS));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!(ChatView.getUltimaHorahHorah() == null && ChatView.getUltimaHoram() == null)) {
                scrolltext.initialScroll();
            } else {
                Toast.makeText(ChatViewActivity.this, "Nao foi possivel obter a hora", Toast.LENGTH_LONG).show();
            }


        }
    }


    //ATUALIZA O ADAPTADOR DAS MENSAGENS DE OUTROS USUARIOS
    public void setaAdaptadorUsuarioDestino(String username){
        //Adding adapter to recyclerview
        try {
            adapter = new ChatViewAdapter(chatController.carregaTodosDadosUsuarioDestino(username, codigo), ChatViewActivity.this);
            recyclerView.setAdapter(adapter);
            //This method runs in the same thread as the UI.
            adapter.notifyDataSetChanged();
            int recyclerposition = recyclerView.getAdapter().getItemCount();
            recyclerView.scrollToPosition(recyclerposition - 1);
            ChatView.setAdaptercontador(recyclerView.getAdapter().getItemCount());
        } catch (Exception e) {
            Toast.makeText(ChatViewActivity.this, "Banco de dados error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //ATUALIZA O ADAPTADOR DAS MENSAGENS DE OUTROS USUARIOS
    public void setaAdaptadorUsuarioDestinoMsg(String username) {
        //Adding adapter to recyclerview
        try {
            adapter = new ChatViewAdapter(chatController.carregaTodosDadosUsuarioDestino(username, codigo), ChatViewActivity.this);
            recyclerView.setAdapter(adapter);
            //This method runs in the same thread as the UI.
            adapter.notifyDataSetChanged();
            int recyclerposition = recyclerView.getAdapter().getItemCount();
            recyclerView.scrollToPosition(recyclerposition - 1);
            ChatView.setAdaptercontador(recyclerView.getAdapter().getItemCount());
        } catch (Exception e) {
            Toast.makeText(ChatViewActivity.this, "Banco de dados error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    //INSERIR MENSAGENS
    public void botaoenviarmensagem(View v) {
        String mg = mensagem.getText().toString().trim();

        if (mg != "" && mg.length() > 0) {
            enviaMensagem(mg);
        } else {
            Toast.makeText(ChatViewActivity.this, "Campo mensagem vazio ;)", Toast.LENGTH_SHORT).show();
        }
    }

    public void enviaMensagem(final String msg) {
        msg.trim();

        //ENVIAR DATA E HORA PARA O SERVERIDOR
        Calendar calendario = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat datadia = new SimpleDateFormat("dd");
        final String DataDia = datadia.format(calendario.getTime());
        SimpleDateFormat datames = new SimpleDateFormat("MM");
        final String DataMes = datames.format(calendario.getTime());
        SimpleDateFormat dataano = new SimpleDateFormat("yyyy");
        final String DataAno = dataano.format(calendario.getTime());
        SimpleDateFormat horah = new SimpleDateFormat("HH");
        HoraH = horah.format(calendario.getTime());
        SimpleDateFormat horam = new SimpleDateFormat("mm");
        HoraM = horam.format(calendario.getTime());
        SimpleDateFormat horaS = new SimpleDateFormat("ss");
        HoraS = horaS.format(calendario.getTime());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ChatViewUtils.DATA_MENSAGEM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.trim().equals("success")) {
                                Toast.makeText(ChatViewActivity.this, "Mensagem enviada à " + user, Toast.LENGTH_SHORT).show();
                                String uniqueId = UUID.randomUUID().toString();

                                //ENVIAR PARA O BANCO DE DADOS
                                String internal = "1";
                                chatController.insereDado(uniqueId, codigo, Profile.getUsername(), msg, user, DataDia, DataMes, DataAno, HoraH, HoraM, HoraS, internal);
                                mensagem.setText("");

                                //ATUALIZA O ADAPTADOR
                                setaAdaptadorUsuarioDestinoMsg(user);
                            } else {
                                Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatViewActivity.this, "Não foi possivel cadastrar a mensagem", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String meunomee;
                meunomee = Profile.getUsername().toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("CODIGO", codigo);
                map.put("MENSAGEM", msg);
                map.put("MEUNOME", meunomee);
                map.put("NOMEOUTRO", user);
                map.put("DIA", DataDia);
                map.put("MES", DataMes);
                map.put("ANO", DataAno);
                map.put("HORAH", HoraH);
                map.put("HORAM", HoraM);
                map.put("HORAS", HoraS);
                return map;
            }
        };
        RequestQueue requestQueueNew = Volley.newRequestQueue(this);
        requestQueueNew.add(stringRequest);

    }

    /*public void getProfileImg(){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, ContaUtils.ALTERANOMEEXIBICAO_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (response.trim().equals("success")) {
                                    } else if (response.trim().equals("Error")) {
                                    } else if (response.trim().equals("Usuario Inexistente")) {
                                    }
                                } catch (Exception e) {
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("USERNAME", user);
                        return map;
                    }
                };
                RequestQueue requestQueueNew = Volley.newRequestQueue(ChatViewActivity.this);
                requestQueueNew.add(stringRequest);
    }*/


}