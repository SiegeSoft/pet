package pet.com.br.pet.anuncios;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.adapters.AnunciosAdapter;
import pet.com.br.pet.menus.BaseMenu;


public class AnunciosActivity extends BaseMenu {

    AnunciosAdapter anunciosAdapter;
    ListView listView;
    private ArrayList<String> raca = new ArrayList<String>();
    private ArrayList<String> dono =  new ArrayList<String>();
    private ArrayList<String> idade  =  new ArrayList<String>();
    private ArrayList<String> tipoVenda =  new ArrayList<String>();
    private ArrayList<String> hora = new ArrayList<String>();
    private ArrayList<Integer> imgid = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        //TESTE
        raca.add("Vira Latas");
        dono.add("Iaco Cesar");
        idade.add("2 meses");
        tipoVenda.add("Doação");
        hora.add("17:50 - 20/08/2016");
        imgid.add(R.drawable.pata);

        raca.add("Dogão");
        dono.add("Rafael");
        idade.add("1 mês");
        tipoVenda.add("Venda");
        hora.add("15:50 - 20/08/2016");
        imgid.add(R.drawable.pata);
        //FIM TESTE

        listView = (ListView) findViewById(R.id.listaAnimais);
        anunciosAdapter = new AnunciosAdapter(this,raca,dono,idade,tipoVenda,hora,imgid);
        listView.setAdapter(anunciosAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}