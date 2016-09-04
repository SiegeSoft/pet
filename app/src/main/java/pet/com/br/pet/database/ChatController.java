package pet.com.br.pet.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pet.com.br.pet.models.ChatView;

/**
 * Created by iaco_ on 03/09/2016.
 */
public class ChatController {

    private SQLiteDatabase db;
    private ChatData banco;

    public ChatController(Context context) {
        banco = new ChatData(context);
    }

    public String insereDado(String id, String codigo, String username, String mensagem) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(ChatData.C_ID_1, id);
        valores.put(ChatData.C_CODIGO_1, codigo);
        valores.put(ChatData.C_USERNAME_1, username);
        valores.put(ChatData.C_MSG_1, mensagem);

        resultado = db.insert(ChatData.TABLE_1, null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";

    }

    public List<ChatView> carregaTodosDados() {
        ChatView chatview;
        List<ChatView> chatViewList = new ArrayList<ChatView>();

        String[] campos = {banco.C_ID_1, banco.C_CODIGO_1, banco.C_USERNAME_1, banco.C_MSG_1};
        db = banco.getWritableDatabase();
        Cursor cursor = db.query(banco.TABLE_1, campos, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    chatview = new ChatView();
                    chatview.setId(cursor.getString(0));
                    chatview.setCodigo(cursor.getString(1));
                    chatview.setUsername(cursor.getString(2));
                    chatview.setMensagem(cursor.getString(3));

                    boolean flag = false;
                    for(ChatView chat : chatViewList){
                        if(null != chat.getId() && null != chatview.getId()){
                            if(chat.getId().equals(chatview.getId())){
                                // Item exists
                                flag = true;
                                break;
                            }
                        }
                    }

                    if(!flag){
                        chatViewList.add(chatview);
                    }

                } while (cursor.moveToNext());
            }
        }
        db.close();
        return chatViewList;
    }

}
