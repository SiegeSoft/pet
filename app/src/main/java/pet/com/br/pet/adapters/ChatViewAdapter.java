package pet.com.br.pet.adapters;

import android.annotation.TargetApi;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.w3c.dom.Text;

import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.chat.ChatViewActivity;
import pet.com.br.pet.database.ChatController;
import pet.com.br.pet.database.ChatData;
import pet.com.br.pet.models.ChatView;
import pet.com.br.pet.models.Profile;

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


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.txtUserid.setText(chatView.get(position).getId());
        holder.txtUsercodigo.setText(chatView.get(position).getCodigo());
        //holder.txtUserchat.setText(chatView.get(position).getUsername());
        holder.txtUsermsg.setText(chatView.get(position).getMensagem());

        String meunome;
        String nomeoutro;

        nomeoutro = chatView.get(position).getUsername();
        meunome = Profile.getUsername();
        if(nomeoutro != meunome){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            holder.cardView.setLayoutParams(params);
            holder.txtUserchat.setText(nomeoutro);
        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            holder.cardView.setLayoutParams(params);
            holder.txtUserchat.setText(meunome);
        }

        //holder.cardView.setparent
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
        CardView cardView;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            txtUserid = (TextView) itemView.findViewById(R.id.chatid);
            txtUserchat = (TextView) itemView.findViewById(R.id.userchatView);
            txtUsercodigo = (TextView) itemView.findViewById(R.id.usercodigoView);
            txtUsermsg = (TextView) itemView.findViewById(R.id.usermsgView);
            cardView = (CardView) itemView.findViewById(R.id.cardChatView);
            context = itemView.getContext();
        }
    }

}
