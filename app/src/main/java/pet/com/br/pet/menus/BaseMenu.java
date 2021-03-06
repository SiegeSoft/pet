package pet.com.br.pet.menus;

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
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import pet.com.br.pet.R;
import pet.com.br.pet.ajuda.AjudaActivity;
import pet.com.br.pet.anuncio.AnunciosActivity;
import pet.com.br.pet.anuncio.CadastroAnuncioActivity;
import pet.com.br.pet.autentica.Login;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.buscaRapida.BuscaRapidaActivity;
import pet.com.br.pet.chat.ChatActivity;
import pet.com.br.pet.conta.Conta;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.negociacoes.MinhasCurtidasActivity;
import pet.com.br.pet.negociacoes.MinhasDoacoesActivity;


/**
 * Created by rafae on 19/08/2016.
 */
public class BaseMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;
    private boolean isSelectedAnuncios = true, isSelectedNegociacoes = true;
    private LoginManager _loginManager;
    private HashMap<String, String> _userDetails;

    public static TextView _nomeDoUsuario;
    public static TextView _menudogCoin;
    public static ImageView drawable_image_usericon;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.menu_drawer, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        _loginManager = new LoginManager(this);
        _userDetails = _loginManager.getUserDetails();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_menu = navigationView.getMenu();

        nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(false);
        nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(false);
        nav_menu.findItem(R.id.sub_buscarapida).setVisible(false);
        nav_menu.findItem(R.id.sub_chat).setVisible(false);
        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
        Profile.setProfileImage(_userDetails.get(LoginManager.KEY_PROFILE));

        if(Profile.getProfileImage() == null || Profile.getProfileImage() == "" || Integer.parseInt(Profile
        .getProfileImage()) == 0) {
            Profile.setIcon(this.getResources().getDrawable(R.drawable.cachorro_icons));
        }else{
        if(Integer.parseInt(Profile.getProfileImage()) == 1){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.cachorro_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 2){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.galinha_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 3){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.sapo_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 4){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.hamster_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 5){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.macaco_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 6){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.gato_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 7){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.tigre_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 8){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.coelho_icons));
        }else if(Integer.parseInt(Profile.getProfileImage()) == 9){
            Profile.setIcon(BaseMenu.this.getResources().getDrawable(R.drawable.rato_icons));
        }}

        _nomeDoUsuario = (TextView) findViewById(R.id.Text_MenuUser);
        _menudogCoin = (TextView) findViewById(R.id.text_dogcoin);

        drawable_image_usericon = (ImageView) findViewById(R.id.image_usericon);
        Bitmap bitmap = ((BitmapDrawable) Profile.getIcon()).getBitmap();
        Drawable drawable = new BitmapDrawable(getResources(), createCircleBitmap(bitmap));
        drawable_image_usericon.setImageDrawable(drawable);
        _nomeDoUsuario.setText(_userDetails.get(LoginManager.KEY_NOME_EXIBICAO));
        _menudogCoin.setText(_userDetails.get(LoginManager.KEY_DOG_COIN));

        setUpNavView();
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

    protected boolean useToolbar() {
        return true;
    }

    protected boolean useDrawerToggle() {
        return true;
    }




    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);

        if (useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();

        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        selectedNavItemId = menuItem.getItemId();

        switch (selectedNavItemId) {
            case R.id.sub_ver_anuncio:
                if (isSelectedAnuncios) {
                    menuSelecionado(true, true, true, true, false);
                    Log.d("Valor:", String.valueOf(isSelectedAnuncios));
                } else {
                    menuSelecionado(false, false, false, false, false);
                }
                break;

            case R.id.sub_pesq_anuncio:
                iniciaOutraActivity(AnunciosActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_ins_anuncio:
                iniciaOutraActivity(CadastroAnuncioActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_buscarapida:
                iniciaOutraActivity(BuscaRapidaActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_ver_negociacoes:
                if (isSelectedNegociacoes) {
                    menuSelecionado(false, false, false, false, true);
                    Log.d("Valor:", String.valueOf(isSelectedNegociacoes));
                } else {
                    menuSelecionado(false, false, false, false, false);
                }
                break;

            case R.id.sub_chat:
                iniciaOutraActivity(ChatActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;


            case R.id.sub_curtidas:
                iniciaOutraActivity(MinhasCurtidasActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_conta:
                iniciaOutraActivity(Conta.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_minhas_doacoes:
                iniciaOutraActivity(MinhasDoacoesActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_ajuda:
                iniciaOutraActivity(AjudaActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                finish();
                break;

            case R.id.sub_sair:
                fullLayout.closeDrawer(GravityCompat.START);
                _loginManager = new LoginManager(this);
                if(_loginManager.logoutUser()){
                    logout();
                }
                break;
        }
        return onOptionsItemSelected(menuItem);
    }

    private void logout(){
        Intent i = new Intent(this, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void iniciaOutraActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    private void menuSelecionado(boolean pesquisar, boolean inserir, boolean buscaRapida, boolean verAnuncio, boolean verNegociacoes) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(pesquisar);
        nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(inserir);
        nav_menu.findItem(R.id.sub_buscarapida).setVisible(buscaRapida);
        nav_menu.findItem(R.id.sub_curtidas).setVisible(pesquisar);


        if (verAnuncio) {
            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            isSelectedAnuncios = false;
        }
        if (!verAnuncio) {
            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_add);
            isSelectedAnuncios = true;
        }
        if(verNegociacoes){
            nav_menu.findItem(R.id.sub_chat).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_minhas_doacoes).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            isSelectedNegociacoes = false;
        }
        if (!verNegociacoes) {
            nav_menu.findItem(R.id.sub_chat).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_minhas_doacoes).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_add);
            isSelectedNegociacoes = true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_confings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}