package pet.com.br.pet.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.models.Anuncios;

/**
 * Created by rafae on 20/08/2016.
 */
public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.ViewHolder> {

    private Activity context;
    private List<Anuncios> anuncios;


    public AnunciosAdapter(List<Anuncios> anuncios, Activity context) {
        super();
        this.anuncios = anuncios;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_anuncios, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anuncios anuncio = anuncios.get(position);

        holder.txtRaca.setText(anuncio.getRaca());

        //TODO: BASE 64 image
        if (anuncio.getImgid() == null) {
            holder.imgAnimal.setImageResource(R.drawable.pata);
        } else {
            holder.imgAnimal.setImageBitmap(anuncio.getImgid());
        }

        holder.txtDono.setText("Dono: " + anuncio.getDono());
        holder.txtIdade.setText("Idade: " + anuncio.getIdade());
        holder.txtTipoVenda.setText("Venda/Doação: " + anuncio.getTipoVenda());
        holder.txtHora.setText(anuncio.getHora());

    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtRaca;
        ImageView imgAnimal;
        TextView txtDono;
        TextView txtIdade;
        TextView txtTipoVenda;
        TextView txtHora;

        public ViewHolder(View itemView) {
            super(itemView);

            txtRaca = (TextView) itemView.findViewById(R.id.raca);
            imgAnimal = (ImageView) itemView.findViewById(R.id.imgAnimal);
            txtDono = (TextView) itemView.findViewById(R.id.dono);
            txtIdade = (TextView) itemView.findViewById(R.id.idade);
            txtTipoVenda = (TextView) itemView.findViewById(R.id.tipoVenda);
            txtHora = (TextView) itemView.findViewById(R.id.hora);

        }
    }
}
