package pet.com.br.pet.menus;

import android.content.Intent;
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

import pet.com.br.pet.R;
import pet.com.br.pet.anuncios.CadastroAnuncioActivity;

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

        setUpNavView();
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
                fullLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.sub_ins_anuncio:
                iniciaOutraActivity(CadastroAnuncioActivity.class);
                fullLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.sub_buscarapida:
                fullLayout.closeDrawer(GravityCompat.START);
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
                fullLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.sub_conta:
                fullLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.sub_sair:
                fullLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return onOptionsItemSelected(menuItem);
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

        if (verAnuncio) {
            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_info_details);
            isSelectedAnuncios = false;
        }
        if (!verAnuncio) {
            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_add);
            isSelectedAnuncios = true;
        }
        if(verNegociacoes){
            nav_menu.findItem(R.id.sub_chat).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_info_details);
            isSelectedNegociacoes = false;
        }
        if (!verNegociacoes) {
            nav_menu.findItem(R.id.sub_chat).setVisible(verNegociacoes);
            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_add);
            isSelectedNegociacoes = true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
