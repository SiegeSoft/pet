package pet.com.br.pet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class AnunciosActivity extends AppCompatActivity {

    int negociacoes_isdown = 0, anuncios_isdown = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anuncios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_menu = navigationView.getMenu();


        nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(false);
        nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(false);
        nav_menu.findItem(R.id.nav_buscarapida).setVisible(false);

        nav_menu.findItem(R.id.sub_chat).setVisible(false);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();


                switch (anuncios_isdown) {
                    case 0:
                        if (id == R.id.sub_ver_anuncio) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(true);
                            nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(true);
                            nav_menu.findItem(R.id.nav_buscarapida).setVisible(true);
                            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_info_details);
                            anuncios_isdown = 1;

                        }
                        break;

                    case 1:
                        if (id == R.id.sub_ver_anuncio) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_pesq_anuncio).setVisible(false);
                            nav_menu.findItem(R.id.sub_ins_anuncio).setVisible(false);
                            nav_menu.findItem(R.id.nav_buscarapida).setVisible(false);
                            nav_menu.findItem(R.id.sub_ver_anuncio).setIcon(android.R.drawable.ic_menu_add);
                            anuncios_isdown = 0;
                        }
                        break;
                }

                if (anuncios_isdown == 1) {
                    if (id == R.id.sub_pesq_anuncio) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }

                    if (id == R.id.sub_ins_anuncio) {
                        Intent intencao = new Intent(AnunciosActivity.this, CadastroAnuncioActivity.class);
                        startActivity(intencao);
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        finish();
                    }
                    if (id == R.id.nav_buscarapida) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }

                switch (negociacoes_isdown) {
                    case 0:
                        if (id == R.id.sub_ver_negociacoes) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_chat).setVisible(true);
                            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_info_details);
                            negociacoes_isdown = 1;
                        }

                        break;

                    case 1:
                        if (id == R.id.sub_ver_negociacoes) {
                            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_menu = navigationView.getMenu();
                            nav_menu.findItem(R.id.sub_chat).setVisible(false);
                            nav_menu.findItem(R.id.sub_ver_negociacoes).setIcon(android.R.drawable.ic_menu_add);
                            negociacoes_isdown = 0;
                        }
                        break;
                }

                if (negociacoes_isdown == 1) {

                    if (id == R.id.sub_chat) {
                        // Handle the About action
                    } else {
                    }
                }


                if (id == R.id.nav_conta) {
                    // Handle the About action
                }
                if (id == R.id.nav_sair) {
                    // Handle the About action
                }


                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                //drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
