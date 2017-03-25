package pet.com.br.pet.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.chat.ChatViewActivity;
import pet.com.br.pet.models.Chat;

/**
 * Created by iaco_ on 27/08/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Activity context;
    private List<Chat> chat;
    private String usuario_nome, usuario_codigo;

    public ChatAdapter(List<Chat> chat, Activity context) {
        super();
        this.chat = chat;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Chat userChat = chat.get(position);

        //holder.txtUserid.setText(userChat.getId());
        holder.txtUserchat.setText("Usuario: " + chat.get(position).getUsername());
        holder.txtUsercodigo.setText("Cod: " + chat.get(position).getCodigo());
        holder.txtUserdesc.setText("Anuncio: " + chat.get(position).getDescricao());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ChatViewActivity.class);
                intent.putExtra("Userchat", userChat.getUsername());
                intent.putExtra("Usercodigo", userChat.getCodigo());
                intent.putExtra("Userdesc", userChat.getDescricao());
                intent.putExtra("UserProfileImg", userChat.getProfileImg());
                v.getContext().startActivity(intent);
            }
        });

        if (chat.get(position).getDestinoVisualisado().equals("0")) {
            holder.relativechat.setBackgroundResource(R.drawable.out_message_bg);
        }
        if (chat.get(position).getDestinoVisualisado().equals("1")) {
            holder.relativechat.setBackgroundResource(R.drawable.chatbaloon);
        }
    }


    @Override
    public int getItemCount() {
        return chat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //final TextView txtUserid;
        TextView txtUserchat;
        TextView txtUsercodigo;
        TextView txtUserdesc;

        RelativeLayout relativechat;

        public ViewHolder(View itemView) {
            super(itemView);

            //txtUserid = (TextView) itemView.findViewById(R.id.userid);
            txtUserchat = (TextView) itemView.findViewById(R.id.userchat);
            txtUsercodigo = (TextView) itemView.findViewById(R.id.usercodigo);
            txtUserdesc = (TextView) itemView.findViewById(R.id.userdesc);
            relativechat = (RelativeLayout) itemView.findViewById(R.id.relative_chat);


        }
    }
}