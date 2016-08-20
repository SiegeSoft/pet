package pet.com.br.pet.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pet.com.br.pet.R;

/**
 * Created by rafae on 20/08/2016.
 */
public class AnunciosAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> raca;
    private final ArrayList<String> dono;
    private final ArrayList<String> idade;
    private final ArrayList<String> tipoVenda;
    private final ArrayList<String> hora;
    private final ArrayList<Integer> imgid;


    public AnunciosAdapter(Activity context, ArrayList<String> raca, ArrayList<String> dono, ArrayList<String> idade, ArrayList<String> tipoVenda,
                           ArrayList<String> hora, ArrayList<Integer> imgid) {
        super(context, R.layout.item_lista_anuncios, raca);
        this.context = context;
        this.raca = raca;
        this.dono = dono;
        this.idade = idade;
        this.tipoVenda = tipoVenda;
        this.hora = hora;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item_lista_anuncios, null, true);

        TextView txtRaca = (TextView) rowView.findViewById(R.id.raca);
        ImageView imgAnimal = (ImageView) rowView.findViewById(R.id.imgAnimal);
        TextView txtDono = (TextView) rowView.findViewById(R.id.dono);
        TextView txtIdade = (TextView) rowView.findViewById(R.id.idade);
        TextView txtTipoVenda = (TextView) rowView.findViewById(R.id.tipoVenda);
        TextView txtHora = (TextView) rowView.findViewById(R.id.hora);

        txtRaca.setText(raca.get(position));
        imgAnimal.setImageResource(imgid.get(position));
        txtDono.setText("Dono: "+dono.get(position));
        txtIdade.setText("Idade: "+idade.get(position));
        txtTipoVenda.setText("Venda/Doação: "+tipoVenda.get(position));
        txtHora.setText(hora.get(position));

        return rowView;

    }

}
