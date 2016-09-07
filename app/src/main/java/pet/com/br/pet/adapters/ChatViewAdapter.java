package pet.com.br.pet.adapters;

import android.app.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.database.ChatController;
import pet.com.br.pet.database.ChatData;
import pet.com.br.pet.models.ChatView;

/**
 * Created by iaco_ on 29/08/2016.
 */
public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private Activity context;
    private List<ChatView> chatView;


    public ChatViewAdapter(List<ChatView> chatView, Activity context) {
        super();
        this.chatView = chatView;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_chatview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        //ChatView userChatView = chatView.get(position);




        holder.txtUserid.setText(chatView.get(position).getId());
        holder.txtUserchat.setText(chatView.get(position).getCodigo());
        holder.txtUsercodigo.setText(chatView.get(position).getUsername());
        holder.txtUsermsg.setText(chatView.get(position).getMensagem());


//        holder.txtUserid.setText(userChatView.getId());
//        holder.txtUserchat.setText(userChatView.getUsername());
//        holder.txtUsercodigo.setText(userChatView.getCodigo());
//        holder.txtUsermsg.setText(userChatView.getMensagem());

        //holder.txtUsercodigo.setText();

        /*holder.itemView.setOnClickListener(new View.OnClickListener(){



            @Override

            public void onClick(View v) {

                //Intent intent = new Intent(v.getContext(), ChatViewActivity.class);
                //intent.putExtra("Userchat", userChat.getUsername());
                //intent.putExtra("Usercodigo", userChat.getCodigo());
                //intent.putExtra("Userdesc", userChat.getDescricao());
                //v.getContext().startActivity(intent);
                //context.finish();
                //context.finish();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return chatView.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserid;
        TextView txtUserchat;
        TextView txtUsercodigo;
        TextView txtUsermsg;


        public ViewHolder(View itemView) {
            super(itemView);
            txtUserid = (TextView) itemView.findViewById(R.id.chatid);
            txtUserchat = (TextView) itemView.findViewById(R.id.userchatView);
            txtUsercodigo = (TextView) itemView.findViewById(R.id.usercodigoView);
            txtUsermsg = (TextView) itemView.findViewById(R.id.usermsgView);


        }
    }

}