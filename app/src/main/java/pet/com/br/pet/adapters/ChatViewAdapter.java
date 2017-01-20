package pet.com.br.pet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import pet.com.br.pet.R;
import pet.com.br.pet.models.ChatView;

/**
 * Created by iaco_ on 29/08/2016.
 */
public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private Activity context;
    private List<ChatView> chatView;
    private static int dataDia = 0;
    private static int dataMes = 0;
    private static int dataAno = 0;


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

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        //VERIFICAR DATA ATUAL
        Calendar calendario = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat datadia = new SimpleDateFormat("dd");
        final String DataDia = datadia.format(calendario.getTime());
        SimpleDateFormat datames = new SimpleDateFormat("MM");
        final String DataMes = datames.format(calendario.getTime());
        SimpleDateFormat dataano = new SimpleDateFormat("yyyy");
        final String DataAno = dataano.format(calendario.getTime());

        String iDataDia = DataDia;
        String iDataMes = DataMes;
        String iDataAno = DataAno;

        int currentdataDia = Integer.parseInt(chatView.get(position).getDia());
        int currentdataMes = Integer.parseInt(chatView.get(position).getMes());
        int currentdataAno = Integer.parseInt(chatView.get(position).getAno());

        if(!(position == 0)) {
            //int previousdataDia = Integer.parseInt(chatView.get(position-1).getDia());

            if (chatView.get(position).getDia().equals(chatView.get(position-1).getDia())
                    && chatView.get(position).getMes().equals(chatView.get(position-1).getMes())
                    && chatView.get(position).getAno().equals(chatView.get(position-1).getAno())) {
                holder.txtMsgDay.setVisibility(View.GONE);
                holder.cardView.setContentPadding(0,0,0,0);
            } else {
                if(currentdataDia == Integer.parseInt(iDataDia)
                        && currentdataMes == Integer.parseInt(iDataMes)
                        && currentdataAno == Integer.parseInt(iDataAno)){
                    holder.txtMsgDay.setVisibility(View.VISIBLE);
                    holder.txtMsgDay.setText("HOJE");
                    holder.cardView.setContentPadding(0,80,0,0);
                    //previousdataDia = currentdataDia;
                }else if(currentdataDia == Integer.parseInt(iDataDia)- 1
                        && currentdataMes == Integer.parseInt(iDataMes)- 1
                        && currentdataAno == Integer.parseInt(iDataAno)- 1){
                    holder.txtMsgDay.setVisibility(View.VISIBLE);
                    holder.txtMsgDay.setText("ONTEM");
                    holder.cardView.setContentPadding(0,80,0,0);
                    //previousdataDia = currentdataDia;
                }else if(!(currentdataDia == Integer.parseInt(iDataDia)- 1 && currentdataDia == Integer.parseInt(iDataDia)
                        && currentdataMes == Integer.parseInt(iDataMes)- 1 && currentdataMes == Integer.parseInt(iDataMes)
                        && currentdataAno == Integer.parseInt(iDataAno)- 1 && currentdataAno == Integer.parseInt(iDataAno))){
                    holder.txtMsgDay.setVisibility(View.VISIBLE);
                    holder.txtMsgDay.setText(""+ currentdataDia+ "/" + currentdataMes + "/" + currentdataAno);
                    holder.cardView.setContentPadding(0,80,0,0);
                }
                //previousdataDia = currentdataDia;
            }
        }else {
            ;
            if(currentdataDia == Integer.parseInt(iDataDia)
                    && currentdataMes == Integer.parseInt(iDataMes)
                    && currentdataAno == Integer.parseInt(iDataAno)){
                holder.txtMsgDay.setVisibility(View.VISIBLE);
                holder.txtMsgDay.setText("HOJE");
                holder.cardView.setContentPadding(0,80,0,0);
            }else if(currentdataDia == Integer.parseInt(iDataDia)- 1
                    && currentdataMes == Integer.parseInt(iDataMes)- 1
                    && currentdataAno == Integer.parseInt(iDataMes)- 1){
                holder.txtMsgDay.setVisibility(View.VISIBLE);
                holder.txtMsgDay.setText("ONTEM");
                holder.cardView.setContentPadding(0,80,0,0);
            }else if(!(currentdataDia == Integer.parseInt(iDataDia)- 1 && currentdataDia == Integer.parseInt(iDataDia)
                    && currentdataMes == Integer.parseInt(iDataMes)- 1 && currentdataMes == Integer.parseInt(iDataMes)
                    && currentdataAno == Integer.parseInt(iDataAno)- 1 && currentdataAno == Integer.parseInt(iDataAno))){
                holder.txtMsgDay.setVisibility(View.VISIBLE);
                holder.txtMsgDay.setText(""+ currentdataDia+ "/" + currentdataMes + "/" + currentdataAno);
                holder.cardView.setContentPadding(0,80,0,0);
            }
        }


        if (chatView.get(position).getMsgInternal().equals("1")) {
            //Exibe a data da mensagem caso o dia for atual

            //VERIFICA SE É O SEU USUARIO

            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.cardView.setLayoutParams(params);
            holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
            holder.relativechatView.setBackgroundResource(R.drawable.inmessage);

            //Atribuir aos textviews

            //holder.txtUserchat.setText(chatView.get(position).getUsername());
            holder.txtUsermsg.setText(chatView.get(position).getMensagem());
            holder.txthora.setText(""+chatView.get(position).getHorah()+":"+chatView.get(position).getHoram());

        }
        if(chatView.get(position).getMsgInternal().equals("0")){

            //VERIFICA SE ESSE É O OUTRO USUARIO

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.cardView.setLayoutParams(params);
            holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
            holder.relativechatView.setBackgroundResource(R.drawable.out_message_bg);

            //Atribuir aos textviews

            //holder.txtUserchat.setText(chatView.get(position).getUsername());
            holder.txtUsermsg.setText(chatView.get(position).getMensagem());
            holder.txthora.setText(""+chatView.get(position).getHorah()+":"+chatView.get(position).getHoram());
        }

    }


    @Override
    public int getItemCount() {
        return chatView.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserid;
        //TextView txtUserchat;
        TextView txtUsercodigo;
        TextView txtUsermsg;
        TextView txtMsgDay;
        TextView txthora;

        CardView cardView;
        Context context;
        RelativeLayout relativechatView;

        public ViewHolder(View itemView) {
            super(itemView);

            //txtUserid = (TextView) itemView.findViewById(R.id.chatid);
            //txtUserchat = (TextView) itemView.findViewById(R.id.userchatView);
            //txtUsercodigo = (TextView) itemView.findViewById(R.id.usercodigoView);
            txtUsermsg = (TextView) itemView.findViewById(R.id.usermsgView);
            txtMsgDay = (TextView) itemView.findViewById(R.id.txtMsgDay);
            txthora = (TextView) itemView.findViewById(R.id.hora);


            cardView = (CardView) itemView.findViewById(R.id.cardChatView);
            context = itemView.getContext();
            relativechatView = (RelativeLayout) itemView.findViewById(R.id.relative_chatview);


        }
    }

}