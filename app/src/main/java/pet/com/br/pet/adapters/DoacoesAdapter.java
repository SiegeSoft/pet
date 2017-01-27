package pet.com.br.pet.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pet.com.br.pet.R;
import pet.com.br.pet.anuncio.CadastroAnuncioActivity;
import pet.com.br.pet.anuncio.InfoAnunciosActivity;
import pet.com.br.pet.autentica.LoginManager;
import pet.com.br.pet.models.Anuncios;
import pet.com.br.pet.models.Doacoes;
import pet.com.br.pet.models.Profile;
import pet.com.br.pet.models.Usuario;
import pet.com.br.pet.negociacoes.MinhasDoacoesActivity;
import pet.com.br.pet.utils.TagUtils;
import pet.com.br.pet.utils.UrlUtils;

import static pet.com.br.pet.utils.TagUtils.KEY_USERNAME;

/**
 * Created by rafaelmagalhaes on 22/01/17.
 */

public class DoacoesAdapter extends RecyclerView.Adapter<DoacoesAdapter.ViewHolder> {


    private Activity context;
    private List<Doacoes> doacoes;

    public DoacoesAdapter(List<Doacoes> doacoes, Activity context) {
        super();
        this.doacoes = doacoes;
        this.context = context;
    }

    @Override
    public DoacoesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_minhas_adoacoes, parent, false);
        DoacoesAdapter.ViewHolder viewHolder = new DoacoesAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DoacoesAdapter.ViewHolder holder, int position) {
        final Doacoes doacoes = this.doacoes.get(position);
        holder.txtRaca.setText(doacoes.getRaca());

        //TODO: BASE 64 image
        if (doacoes.getImgid() == null) {
            holder.imgAnimal.setImageResource(R.drawable.pata);
        } else {
            holder.imgAnimal.setImageBitmap(doacoes.getImgid());
        }

        holder.txtDono.setText("Dono: " + doacoes.getDono());
        holder.txtIdade.setText("Idade: " + doacoes.getIdade());
        holder.txtHora.setText(doacoes.getHora());
        holder.txtCod.setText(doacoes.getCodigo());
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setMessage("Você deseja deletar seu anuncio de doação?")
                        .setNegativeButton("Não!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Sim, desejo deletar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteAnuncio(doacoes.getCodigo());
                            }
                        })
                        .create()
                        .show();


            }
        });

    }


    private void deleteAnuncio(final String codigo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlUtils.DELETAR_MEU_ANUNCIO+codigo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Doação deletada!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(context, MinhasDoacoesActivity.class);
                                            context.startActivity(intent);
                                            context.finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        } catch (Exception exception) {
                            Toast.makeText(context, "Erro" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Erro na sincronização com servidor: " + error)
                                .setNegativeButton("Tentar novamente", null)
                                .create()
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(TagUtils.TAG_COD, codigo);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return doacoes.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCod;
        TextView txtRaca;
        ImageView imgAnimal;
        TextView txtDono;
        TextView txtIdade;
        ImageView deletar;
        TextView txtHora;


        public ViewHolder(View itemView) {
            super(itemView);
            txtRaca = (TextView) itemView.findViewById(R.id.raca);
            imgAnimal = (ImageView) itemView.findViewById(R.id.imgAnimal);
            txtDono = (TextView) itemView.findViewById(R.id.dono);
            txtIdade = (TextView) itemView.findViewById(R.id.idade);
            txtHora = (TextView) itemView.findViewById(R.id.hora);
            txtCod = (TextView) itemView.findViewById(R.id.codigo);
            deletar = (ImageView) itemView.findViewById(R.id.deletar);
        }
    }
}
